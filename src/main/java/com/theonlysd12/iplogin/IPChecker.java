package com.theonlysd12.iplogin;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.text.SimpleDateFormat;
import java.util.Objects;

public class IPChecker implements Listener {
    private final TextComponent newPlayerJoinMessage = Component.text("trebe din setari")
            .color(NamedTextColor.GREEN);

    private final TextComponent oldPlayerJoinMessage = Component.text("trebe din setari")
            .color(NamedTextColor.GREEN);

    private final TextComponent kickPlayerMessage = Component.text("trebe din setari")
            .color(NamedTextColor.WHITE);

    private final TextComponent banPlayerMessage = Component.text("trebe din setari")
            .color(NamedTextColor.WHITE);

    private final TextComponent nameKickPlayerMessage = Component.text("trebe din setari")
            .color(NamedTextColor.WHITE);


    @EventHandler(priority = EventPriority.LOWEST)
    public void onPLayerJoin(PlayerJoinEvent event) {
        String username = event.getPlayer().getName();
        String ip = Objects.requireNonNull(event.getPlayer().getAddress()).getHostName();

        if (IPLogin.usernameFilter) {
            if (IPLogin.advancedUsernameFilter) {
                String lowercaseUsername = username.toLowerCase();
                for (String filter : IPLogin.blockedUsernames) {
                    if (lowercaseUsername.contains(filter)) {
                        LogPlayer(username, ip, "inappropriate username");
                        event.getPlayer().kick(nameKickPlayerMessage);
                    }
                }
            } else {
                for (String filter : IPLogin.blockedUsernames) {
                    if (username.contains(filter)) {
                        LogPlayer(username, ip, "inappropriate username");
                        event.getPlayer().kick(nameKickPlayerMessage);
                    }
                }
            }
        }

        if (IPStorer.containsPlayer(username)) {
            if (IPStorer.getIPsFromPlayer(username).contains("*") || IPStorer.getIPsFromPlayer(username).contains(ip)) {
                // Normal Player
                event.joinMessage(oldPlayerJoinMessage);
            } else {
                // Impersonator
                if (IPLogin.banPlayers) {
                    event.getPlayer().banPlayerIP(banPlayerMessage.toString());
                } else {
                    event.getPlayer().kick(kickPlayerMessage);
                }
                LogPlayer(username, ip, "impersonating");
            }
        } else if (IPStorer.containsIP(ip)) {
            // Alt
            if (IPLogin.banPlayers) {
                event.getPlayer().banPlayerIP(banPlayerMessage.toString());
            } else {
                event.getPlayer().kick(kickPlayerMessage);
            }
            LogPlayer(username, ip, "using an alt");
        } else {
            // New Player
            IPStorer.addPlayer(username, ip);
            event.joinMessage(newPlayerJoinMessage);
        }

    }

    public void LogPlayer(String name, String ip, String reason) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        if (IPLogin.basicLogger) {
            if (IPLogin.banPlayers) {
                Bukkit.getLogger().info(name + " has been banned from the server for \"" + reason + "\".");
            } else {
                Bukkit.getLogger().info(name + " has been kicked from the server for \"" + reason + "\".");
            }

        }/*
        if (IPLogin.fileLogger) {
            logConfig.set(name + "." + formatter.format(System.currentTimeMillis()) + ".IP", ip);
            logConfig.set(name + "." + formatter.format(System.currentTimeMillis()) + ".Reason", reason);
            logConfig.set(name + "." + formatter.format(System.currentTimeMillis()) + ".Type", IPLogin.banPlayers ? "Ban" : "Kick");
            if (reason.equals("using an alt")) {
                List<String> ownerList = IPStorer.getPlayersFromIP(ip);
                logConfig.set(name + "." + formatter.format(System.currentTimeMillis()) + ".Owner(s)", ownerList);
            }
        }*/
    }
}
