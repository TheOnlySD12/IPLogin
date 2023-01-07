package com.theonlysd12.iplogin;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
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

    @Override
    public boolean onCommand(@NotNull CommandSender sender, Command command, @NotNull String label, String[] args) {
        if (command.getName().equalsIgnoreCase("setip") && args.length == 1) {
            if (sender instanceof Player player) {
               NameToIPMap.put(player.getName(), args[0]);
               player.sendMessage(Component.text("Your IP has been set to: ", NamedTextColor.YELLOW)
                       .append(Component.text(args[0], NamedTextColor.WHITE)));
                saveMapToFile();
               return true;
            }
        }
        return false;
    }

    @Override
    public void onEnable() {
        File dataFolder = getDataFolder();
        if (!dataFolder.exists()) {
            if (!dataFolder.mkdirs()) {
                getLogger().warning("Failed to create data folder at " + dataFolder.getAbsolutePath());
                return;
            }
        }
        NameToIPMap = new HashMap<>();
        loadMapFromFile();
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        String name = event.getPlayer().getName();
        String ip = Objects.requireNonNull(event.getPlayer().getAddress()).getAddress().getHostAddress();
        if (NameToIPMap.containsKey(name)) {
            if (!NameToIPMap.get(name).equals(ip)) {
                event.getPlayer().banPlayerIP("You tried to enter with someone else's username");
            } else {
                event.joinMessage(Component.text("Welcome back, ", NamedTextColor.YELLOW)
                        .append(Component.text(name, NamedTextColor.YELLOW)));
            }
        } else {
            if(NameToIPMap.containsValue(ip)) {
                event.getPlayer().banPlayerIP("Alts are not allowed");
            } else {
                NameToIPMap.put(name, ip);
                saveMapToFile();
                event.joinMessage(Component.text("Welcome, ", NamedTextColor.YELLOW)
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