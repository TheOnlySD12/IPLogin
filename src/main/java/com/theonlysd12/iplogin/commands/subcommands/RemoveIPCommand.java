package com.theonlysd12.iplogin.commands.subcommands;

import com.theonlysd12.iplogin.IPLogin;
import com.theonlysd12.iplogin.IPStorer;
import com.theonlysd12.iplogin.commands.SubCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class RemoveIPCommand extends SubCommand {
    private static Component getSuccessMessage(String username, String ip) {
        return Component.text()
                .content(IPLogin.pluginChatName)
                .color(NamedTextColor.GREEN)
                .append(Component.text()
                        .content("Successfully removed IP: ")
                        .color(NamedTextColor.WHITE)
                        .append(Component.text()
                                .content(ip)
                                .color(NamedTextColor.GRAY)
                                .build())
                        .append(Component.text(" from "))
                        .append(Component.text()
                                .content(username)
                                .color(NamedTextColor.AQUA)
                                .build())
                        .build())
                .build();
    }

    @Override
    public String getName() {
        return "remove-ip";
    }

    @Override
    public String getDescription() {
        return "Remove IPs from players";
    }

    @Override
    public String getSyntax() {
        return "Type /iplogin remove-ip <player name> <ip> to remove a player's IP";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if(args.length == 3){
            if(sender instanceof Player){
                if(args[1].equals(sender.getName())){
                    if(doesPlayerHaveIP(sender, args[2])){
                        IPStorer.removeIPFromPlayer(sender.getName(), args[2]);
                        sender.sendMessage(getSuccessMessage(args[1], args[2]));
                    }
                } else if(isSenderOP(sender) && isPlayerValid(sender, args[1]) && doesPlayerHaveIP(sender, args[2])){
                    IPStorer.removeIPFromPlayer(args[1], args[2]);
                    sender.sendMessage(getSuccessMessage(args[1], args[2]));
                }
            } else {
                if(isPlayerValid(sender, args[1]) && doesPlayerHaveIP(sender, args[2])){
                    IPStorer.removeIPFromPlayer(args[1], args[2]);
                    sender.sendMessage(getSuccessMessage(args[1], args[2]));
                }
            }
        } else {sender.sendMessage(incorrectUseMessage);}
    }

    @Override
    public List<String> getArguments(CommandSender sender, String[] args) {
        if(args.length == 2){
            if(sender instanceof Player){
                if(sender.isOp()){
                    return IPStorer.getPlayers();
                } else {
                    return List.of(sender.getName());
                }
            } else {
                return IPStorer.getPlayers();
            }
        } else if(args.length == 3 && IPStorer.containsPlayer(args[1])){
            if(sender instanceof Player){
                if(sender.isOp()){
                    return IPStorer.getIPsFromPlayer(args[1]);
                } else {
                    return IPStorer.getIPsFromPlayer(sender.getName());
                }
            } else {
                return IPStorer.getPlayers();
            }
        }

        return List.of();
    }
}
