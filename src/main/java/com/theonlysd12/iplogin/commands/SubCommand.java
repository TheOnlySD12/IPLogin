package com.theonlysd12.iplogin.commands;

import com.theonlysd12.iplogin.IPLogin;
import com.theonlysd12.iplogin.IPStorer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public abstract class SubCommand {

    public abstract String getName();

    public abstract String getDescription();

    public abstract String getSyntax();

    public abstract void perform(CommandSender sender, String[] args);

    public abstract List<String> getArguments(CommandSender sender, String[] args);

    public boolean isIPValid(CommandSender sender, String ip){
        if (!ip.matches("^[0-9.]*$")) {
            sender.sendMessage(incorrectIPMessage);
            return false;
        }
        return true;
    }

    public boolean isPlayerValid(CommandSender sender,String username){
        if (!IPStorer.containsPlayer(username)) {
            sender.sendMessage(nonExistentPlayerMessage);
            return false;
        }
        return true;
    }

    public boolean doesPlayerHaveIP(CommandSender sender,String username, String ip){
        if (!IPStorer.getIPsFromPlayer(username).contains(ip)) {
            sender.sendMessage(nonExistentIPMessage);
            return false;
        }
        return true;
    }

    public boolean doesPlayerHaveIP(CommandSender sender, String ip){
        if (!IPStorer.getIPsFromPlayer(sender.getName()).contains(ip)) {
            sender.sendMessage(nonExistentIPMessage);
            return false;
        }
        return true;
    }

    public boolean isSenderOP(CommandSender sender){
        if (!sender.isOp()) {
            sender.sendMessage(noPermissionMessage);
            return false;
        }
        return true;
    }

    public boolean isSenderPlayer(CommandSender sender){
        if (!(sender instanceof Player)) {
            sender.sendMessage(incorrectUseMessage);
            return false;
        }
        return true;
    }

    public boolean isIPNew(CommandSender sender, String ip){
        if (IPStorer.getIPsFromPlayer(sender.getName()).contains(ip)) {
            sender.sendMessage(alreadyHaveIPMessage);
            return false;
        }
        return true;
    }

    public boolean isIPNew(CommandSender sender, String username, String ip){
        if (IPStorer.getIPsFromPlayer(username).contains(ip)) {
            sender.sendMessage(alreadyHaveIPMessage);
            return false;
        }
        return true;
    }

    public final Component incorrectUseMessage = Component.text()
            .content(IPLogin.pluginChatName)
            .color(NamedTextColor.RED)
            .append(Component.text()
                    .content(getSyntax())
                    .color(NamedTextColor.WHITE)
                    .build())
            .build();

    private final Component incorrectIPMessage = Component.text()
            .content(IPLogin.pluginChatName)
            .color(NamedTextColor.RED)
            .append(Component.text()
                    .content("IP may only contain digits and dots.")
                    .color(NamedTextColor.WHITE)
                    .build())
            .build();

    private final Component nonExistentIPMessage = Component.text()
            .content(IPLogin.pluginChatName)
            .color(NamedTextColor.RED)
            .append(Component.text()
                    .content("Provided IP doesn't exist.")
                    .color(NamedTextColor.WHITE)
                    .build())
            .build();

    private final Component noPermissionMessage = Component.text()
            .content(IPLogin.pluginChatName)
            .color(NamedTextColor.RED)
            .append(Component.text()
                    .content("You do not have permission.")
                    .color(NamedTextColor.WHITE)
                    .build())
            .build();

    private final Component nonExistentPlayerMessage = Component.text()
            .content(IPLogin.pluginChatName)
            .color(NamedTextColor.RED)
            .append(Component.text()
                    .content("Provided Player doesn't exist.")
                    .color(NamedTextColor.WHITE)
                    .build())
            .build();

    private final Component alreadyHaveIPMessage = Component.text()
            .content(IPLogin.pluginChatName)
            .color(NamedTextColor.RED)
            .append(Component.text()
                    .content("Player already has that IP.")
                    .color(NamedTextColor.WHITE)
                    .build())
            .build();
}
