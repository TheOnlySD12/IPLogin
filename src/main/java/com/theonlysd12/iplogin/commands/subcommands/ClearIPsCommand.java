package com.theonlysd12.iplogin.commands.subcommands;

import com.theonlysd12.iplogin.IPLogin;
import com.theonlysd12.iplogin.IPStorer;
import com.theonlysd12.iplogin.commands.SubCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class ClearIPsCommand extends SubCommand {
    private Component getSuccessMessage(String username){
        return Component.text()
                .content(IPLogin.pluginChatName)
                .color(NamedTextColor.GREEN)
                .append(Component.text()
                        .content("Successfully removed all IPs for ")
                        .color(NamedTextColor.WHITE)
                        .append(Component.text()
                                .content(username)
                                .color(NamedTextColor.GRAY)
                                .build())
                        .build())
                .build();
    }

    @Override
    public String getName() {
        return "clear-ips";
    }

    @Override
    public String getDescription() {
        return "Removes all your saved IPs";
    }

    @Override
    public String getSyntax() {
        return "Type /iplogin clear-ips to remove all your saved IPs or /iplogin clear-ips <player name> to remove all of a player's saved IPs";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if(args.length == 1){
            if(isSenderPlayer(sender)){
                IPStorer.clearIPsFromPlayer(sender.getName());
                sender.sendMessage(getSuccessMessage(sender.getName()));
            }
        } else if(args.length == 2){
            if(sender instanceof Player){
                if(isSenderOP(sender) && isPlayerValid(sender, args[1])){
                    IPStorer.clearIPsFromPlayer(args[1]);
                    sender.sendMessage(getSuccessMessage(args[1]));
                }
            } else {
                if(isPlayerValid(sender, args[1])){
                    IPStorer.clearIPsFromPlayer(args[1]);
                    sender.sendMessage(getSuccessMessage(args[1]));
                }
            }
        }
    }

    @Override
    public List<String> getArguments(CommandSender sender, String[] args) {
        if(args.length == 2){
            if(sender instanceof Player){
                if(sender.isOp()){
                    return IPStorer.getPlayers();
                }
            } else {
                return IPStorer.getPlayers();
            }
        }

        return null;
    }
}
