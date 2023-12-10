package com.theonlysd12.iplogin;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

// Test 234

public class IPLogin extends JavaPlugin implements Listener {

    File nameToIPFile = new File(getDataFolder(), "name-to-ip.yml");
    FileConfiguration nameToIPConfig = YamlConfiguration.loadConfiguration(nameToIPFile);
    File logFile = new File(getDataFolder(), "log.yml");
    FileConfiguration logConfig  = YamlConfiguration.loadConfiguration(logFile);
    Map<String, Object> nameToIPConfigValues;
    private boolean log_basic;
    private boolean log_custom;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, Command command, @NotNull String label, String[] args) {
        if (command.getName().equalsIgnoreCase("setip")) {
            if (args.length == 1 && sender instanceof Player player) {
                nameToIPConfig.set(player.getName(), args[0]);
                player.sendMessage(Component.text("Your IP has been set to ", NamedTextColor.YELLOW)
                        .append(Component.text(args[0], NamedTextColor.WHITE)));
                return true;
            } else if (args.length == 1 && sender instanceof ConsoleCommandSender || args.length == 1 && sender instanceof BlockCommandSender) {
                getLogger().info("You must be a player to use this command.");
                return true;
            }
            if (args.length == 2) {
                if (sender instanceof ConsoleCommandSender || sender instanceof BlockCommandSender) {
                    nameToIPConfig.set(args[0], args[1]);
                    getLogger().info(args[0] + "'s IP has been set to: " + args[1]);
                    return true;
                } else if (sender instanceof Player player && sender.isOp()) {
                    nameToIPConfig.set(args[0], args[1]);
                    player.sendMessage(Component.text(args[0] + "'s IP has been set to ", NamedTextColor.YELLOW)
                            .append(Component.text(args[1], NamedTextColor.WHITE)));
                    return true;
                } else if (sender instanceof Player player) {
                    if (Objects.equals(args[0], player.getName())) {
                        nameToIPConfig.set(args[0], args[1]);
                        player.sendMessage(Component.text("Your IP has been set to ", NamedTextColor.YELLOW)
                                .append(Component.text(args[1], NamedTextColor.WHITE)));
                        return true;
                    }
                    player.sendMessage(Component.text("You do not have permission to do that.", NamedTextColor.RED));
                    return true;
                }
            }
        }
        if (command.getName().equalsIgnoreCase("allowalts") && args.length == 1) {
            if (args[0].equalsIgnoreCase("true") || args[0].equalsIgnoreCase("false")) {
                if (sender instanceof ConsoleCommandSender || sender instanceof BlockCommandSender) {
                    getConfig().set("allow-alts", Boolean.parseBoolean(args[0]));
                    getLogger().info("Allow-Alts is now set to " + args[0]);
                    return true;
                } else if (sender instanceof Player player && sender.isOp()) {
                    getConfig().set("allow-alts", Boolean.parseBoolean(args[0]));
                    player.sendMessage(Component.text("Allow-Alts is now set to ", NamedTextColor.YELLOW)
                            .append(Component.text(args[0], NamedTextColor.WHITE)));
                    return true;
                } else if (sender instanceof Player player) {
                    player.sendMessage(Component.text("You do not have permission to do that.", NamedTextColor.RED));
                    return true;
                }
            }
        } else if (command.getName().equalsIgnoreCase("allowalts") && args.length == 0) {
            if (sender instanceof ConsoleCommandSender || sender instanceof BlockCommandSender) {
                getLogger().info("Allow-Alts is currently set to " + getConfig().getBoolean("allow-alts"));
                return true;
            } else if (sender instanceof Player player) {
                player.sendMessage(Component.text("Allow-Alts is currently set to " + getConfig().getBoolean("allow-alts"), NamedTextColor.YELLOW));
                return true;
            }
        }
        return false;
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        log_basic = getConfig().getBoolean("logger.basic");
        log_custom = getConfig().getBoolean("logger.custom");
        try {
            nameToIPConfig.save(nameToIPFile);
            if (log_custom) {
                logConfig.save(logFile);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        saveConfig();
        try {
            nameToIPConfig.save(nameToIPFile);
            if (log_custom) {
                logConfig.save(logFile);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        String name = event.getPlayer().getName();
        String ip = Objects.requireNonNull(event.getPlayer().getAddress()).getAddress().getHostAddress();
        if (getConfig().getBoolean("username-filter.enabled")){
            List<String> filterList = getConfig().getStringList("username-filter.filter");
            if (!getConfig().getStringList("username-filter.exceptions").contains(name)) {
                if (getConfig().getInt("username-filter.mode") ==  1){
                    for (String filter : filterList) {
                        if (name.contains(filter)) {
                            if (getConfig().getBoolean("username-filter.ban")) {
                                event.getPlayer().banPlayerIP(getConfig().getString("username-filter.message"));
                                LogPlayer(name, ip, "Username filter", "Ban");
                            } else {
                                event.getPlayer().kick(Component.text(Objects.requireNonNull(getConfig().getString("username-filter.message"))));
                                LogPlayer(name, ip, "Username filter", "Kick");
                            }}}
                } else if (getConfig().getInt("username-filter.mode") == 2){
                    String lowercase_name = name.toLowerCase();
                    for (String filter : filterList) {
                        if (lowercase_name.contains(filter)) {
                            if (getConfig().getBoolean("username-filter.ban")) {
                                event.getPlayer().banPlayerIP(getConfig().getString("username-filter.message"));
                                LogPlayer(name, ip, "Username filter", "Ban");
                            } else {
                                event.getPlayer().kick(Component.text(Objects.requireNonNull(getConfig().getString("username-filter.message"))));
                                LogPlayer(name, ip, "Username filter", "Kick");
                            }}}}}}
        if (nameToIPConfig.contains(name)) {
            if (Objects.equals(nameToIPConfig.get(name), "*") || Objects.equals(nameToIPConfig.get(name), ip)) {
                event.joinMessage(Component.text(Objects.requireNonNull(getConfig().getString("welcome-back-message")), NamedTextColor.YELLOW)
                        .append(Component.text(name, NamedTextColor.YELLOW)));
            } else if (getConfig().getBoolean("ban-impersonators")) {
                event.getPlayer().banPlayerIP(getConfig().getString("impersonators-message"));
                LogPlayer(name, ip, "Impersonator", "Ban");
            } else {
                event.getPlayer().kick(Component.text(Objects.requireNonNull(getConfig().getString("impersonators-message"))));
                LogPlayer(name, ip, "Impersonator", "Kick");
            }
        } else {
            nameToIPConfigValues = nameToIPConfig.getValues(true);
            if(nameToIPConfigValues.containsValue(ip) && !getConfig().getBoolean("allow-alts")) {
                if (getConfig().getBoolean("ban-alts")) {
                    event.getPlayer().banPlayerIP(getConfig().getString("alts-message"));
                    LogPlayer(name, ip, "Alt", "Ban");
                } else {
                    event.getPlayer().kick(Component.text(Objects.requireNonNull(getConfig().getString("alts-message"))));
                    LogPlayer(name, ip, "Alt", "Kick");
                }
            } else {
                nameToIPConfig.set(name, ip);
                event.joinMessage(Component.text(Objects.requireNonNull(getConfig().getString("welcome-message")), NamedTextColor.YELLOW)
                        .append(Component.text(name, NamedTextColor.YELLOW)));
            }
        }
    }

    public void LogPlayer(String name, String ip, String reason, String type) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        if (log_basic) {
            if (type.equals("Ban")) {
                getLogger().info(name + " has been " + type.toLowerCase() + "ned from the server for \"" + reason + "\".");
            } else {
                getLogger().info(name + " has been " + type.toLowerCase() + "ed from the server for \"" + reason + "\".");
            }

        }
        if (log_custom) {
            logConfig.set(name + "." + formatter.format(System.currentTimeMillis()) + ".IP", ip);
            logConfig.set(name + "." + formatter.format(System.currentTimeMillis()) + ".Reason", reason);
            logConfig.set(name + "." + formatter.format(System.currentTimeMillis()) + ".Type", type);
            if (reason.equals("Alt")) {
                List<String> ownerList = new ArrayList<>();
                nameToIPConfigValues.forEach((key, value) -> {
                    if (Objects.equals(value.toString(), ip)) {
                        ownerList.add(key);
                    }});
                logConfig.set(name + "." + formatter.format(System.currentTimeMillis()) + ".Owner(s)", ownerList);
            }
        }
    }
}