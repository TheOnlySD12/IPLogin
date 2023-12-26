package com.theonlysd12.iplogin;

import com.theonlysd12.iplogin.commands.CommandManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class IPLogin extends JavaPlugin {

    File nameToIPFile = new File(getDataFolder(), "name-to-ip.yml");
    FileConfiguration nameToIPConfig = YamlConfiguration.loadConfiguration(nameToIPFile);
    File logFile = new File(getDataFolder(), "log.yml");
    FileConfiguration logConfig  = YamlConfiguration.loadConfiguration(logFile);

    private static IPLogin plugin;
    public static boolean usernameFilter;
    public static boolean advancedUsernameFilter;
    public static List<String> blockedUsernames;
    public static boolean banPlayers;
    public static boolean basicLogger;
    public static boolean fileLogger;
    public static final String pluginChatName = "[IPLogin] ";

    @Override
    public void onEnable() {
        plugin = this;
        getCommand("iplogin").setExecutor(new CommandManager());
        getServer().getPluginManager().registerEvents(new IPChecker(), this);

        usernameFilter = getConfig().getBoolean("username-filter.enabled");
        if(usernameFilter){
            blockedUsernames = getConfig().getStringList("username-filter.filter");
        }



        saveDefaultConfig();
        basicLogger = getConfig().getBoolean("logger.basic");
        fileLogger = getConfig().getBoolean("logger.custom");


        try {
            nameToIPConfig.save(nameToIPFile);
            if (fileLogger) {
                logConfig.save(logFile);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onDisable() {
        saveConfig();
        try {
            nameToIPConfig.save(nameToIPFile);
            if (fileLogger) {
                logConfig.save(logFile);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static IPLogin getPlugin(){
        return plugin;
    }
}