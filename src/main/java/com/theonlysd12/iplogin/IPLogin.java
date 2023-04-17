package com.theonlysd12.iplogin;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class IPLogin extends JavaPlugin implements Listener {
    
    private Map<String, String> NameToIPMap;
    FileConfiguration fileConfig = getConfig();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, Command command, @NotNull String label, String[] args) {
        if (command.getName().equalsIgnoreCase("setip")) {
            if (args.length == 1 && sender instanceof Player player) {
                NameToIPMap.put(player.getName(), args[0]);
                player.sendMessage(Component.text("Your IP has been set to ", NamedTextColor.YELLOW)
                        .append(Component.text(args[0], NamedTextColor.WHITE)));
                return true;
            } else if (args.length == 1 && sender instanceof ConsoleCommandSender || args.length == 1 && sender instanceof BlockCommandSender) {
                getLogger().info("You must be a player to use this command.");
                return true;
            }
            if (args.length == 2) {
                if (sender instanceof ConsoleCommandSender || sender instanceof BlockCommandSender) {
                    NameToIPMap.put(args[0], args[1]);
                    getLogger().info(args[0] + "'s IP has been set to: " + args[1]);
                    return true;
                } else if (sender instanceof Player player && sender.isOp()) {
                    NameToIPMap.put(args[0], args[1]);
                    player.sendMessage(Component.text(args[0] + "'s IP has been set to ", NamedTextColor.YELLOW)
                            .append(Component.text(args[1], NamedTextColor.WHITE)));
                    return true;
                } else if (sender instanceof Player player) {
                    if (Objects.equals(args[0], player.getName())) {
                        NameToIPMap.put(args[0], args[1]);
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
                    fileConfig.set("allow-alts", Boolean.parseBoolean(args[0]));
                    getLogger().info("Allow-Alts is now set to " + args[0]);
                    return true;
                } else if (sender instanceof Player player && sender.isOp()) {
                    fileConfig.set("allow-alts", Boolean.parseBoolean(args[0]));
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
                getLogger().info("Allow-Alts is currently set to " + fileConfig.getBoolean("allow-alts"));
                return true;
            } else if (sender instanceof Player player) {
                player.sendMessage(Component.text("Allow-Alts is currently set to " + fileConfig.getBoolean("allow-alts"), NamedTextColor.YELLOW));
                return true;
            }
        }
        return false;
    }

    @Override
    public void onEnable() {
        fileConfig.addDefault("allow-alts", false);
        fileConfig.addDefault("ban-alts", false);
        fileConfig.addDefault("alts-message", "Alts are not allowed.");
        fileConfig.addDefault("ban-impersonators", false);
        fileConfig.addDefault("impersonators-message", "Impersonators are not allowed.");
        fileConfig.addDefault("welcome-back-message", "Welcome back, ");
        fileConfig.addDefault("welcome-message","Welcome, ");
        fileConfig.options().copyDefaults(true);
        saveConfig();
        File dataFolder = getDataFolder();
        if (!dataFolder.exists()) {
            if (!dataFolder.mkdirs()) {
                getLogger().warning("Failed to create data folder at " + dataFolder.getAbsolutePath());
                return;
            }
        }
        this.saveDefaultConfig();
        NameToIPMap = new HashMap<>();
        loadMapFromFile();
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        saveMapToFile();
        saveConfig();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        String name = event.getPlayer().getName();
        String ip = Objects.requireNonNull(event.getPlayer().getAddress()).getAddress().getHostAddress();
        if (NameToIPMap.containsKey(name)) {
            if (NameToIPMap.get(name).equals("*") || NameToIPMap.get(name).equals(ip)) {
                event.joinMessage(Component.text(Objects.requireNonNull(fileConfig.getString("welcome-back-message")), NamedTextColor.YELLOW)
                        .append(Component.text(name, NamedTextColor.YELLOW)));
            } else if (fileConfig.getBoolean("ban-impersonators")) {
                event.getPlayer().banPlayerIP(fileConfig.getString("impersonators-message"));
            } else {
                event.getPlayer().kick(Component.text(Objects.requireNonNull(fileConfig.getString("impersonators-message"))));
            }
        } else {
            if(NameToIPMap.containsValue(ip) && !fileConfig.getBoolean("allow-alts")) {
                if (fileConfig.getBoolean("ban-alts")) {
                    event.getPlayer().banPlayerIP(fileConfig.getString("alts-message"));
                } else {
                    event.getPlayer().kick(Component.text(Objects.requireNonNull(fileConfig.getString("alts-message"))));
                }
            } else {
                NameToIPMap.put(name, ip);
                event.joinMessage(Component.text(Objects.requireNonNull(fileConfig.getString("welcome-message")), NamedTextColor.YELLOW)
                        .append(Component.text(name, NamedTextColor.YELLOW)));
            }
        }
    }

    private void loadMapFromFile() {
        File file = new File(getDataFolder(), "name-to-ip-map.txt");
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(":");
                    NameToIPMap.put(parts[0], parts[1]);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveMapToFile() {
        File file = new File(getDataFolder(), "name-to-ip-map.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Map.Entry<String, String> entry : NameToIPMap.entrySet()) {
                writer.write(entry.getKey() + ":" + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}