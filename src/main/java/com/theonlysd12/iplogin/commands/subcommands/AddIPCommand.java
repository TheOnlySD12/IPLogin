package com.theonlysd12.iplogin.commands.subcommands;

import com.theonlysd12.iplogin.IPLogin;
import com.theonlysd12.iplogin.IPStorer;
import com.theonlysd12.iplogin.commands.SubCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.conversations.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AddIPCommand extends SubCommand {
    private static Component getSuccessMessage(String username, String ip) {
        return Component.text()
                .content(IPLogin.pluginChatName)
                .color(NamedTextColor.GREEN)
                .append(Component.text()
                        .content("Successfully added IP: ")
                        .color(NamedTextColor.WHITE)
                        .append(Component.text()
                                .content(ip)
                                .color(NamedTextColor.GRAY)
                                .build())
                        .append(Component.text(" to "))
                        .append(Component.text()
                                .content(username)
                                .color(NamedTextColor.AQUA)
                                .build())
                        .build())
                .build();
    }

    @Override
    public String getName() {
        return "add-ip";
    }

    @Override
    public String getDescription() {
        return "Add IPs to players";
    }

    @Override
    public String getSyntax() {
        return "Add-IP command can be used in multiple ways: \n 1. /iplogin add-ip - this will start a dialog to add a new IP \n 2. /iplogin add-ip <player name> <ip>";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if(args.length == 1){
            if(isSenderPlayer(sender)){
                ConversationFactory factory = new ConversationFactory(IPLogin.getPlugin());
                Conversation conversation = factory.withFirstPrompt(new WhatIPPrompt()).buildConversation((Conversable) sender);
                conversation.begin();
            }
        } else if(args.length == 3){
             if(sender instanceof Player){
                 if(args[1].equals(sender.getName())){
                     if(isIPValid(sender, args[2]) && isIPNew(sender, args[2])){
                         IPStorer.addPlayerIP(args[1], args[2]);
                         sender.sendMessage(getSuccessMessage(args[1], args[2]));
                     }
                 } else if(isSenderOP(sender) && isPlayerValid(sender, args[1]) && isIPValid(sender, args[2]) && isIPNew(sender, args[2])){
                     IPStorer.addPlayerIP(args[1], args[2]);
                     sender.sendMessage(getSuccessMessage(args[1], args[2]));
                 }
             } else {
                 if(isPlayerValid(sender, args[1]) && isIPValid(sender, args[2]) && isIPNew(sender, args[2])){
                     IPStorer.addPlayerIP(args[1], args[2]);
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
                return IPStorer.getIPsFromPlayer(sender.getName());
            }
        }

        return List.of();
    }

    private static class WhatIPPrompt extends RegexPrompt {
        public WhatIPPrompt() {
            super("^[0-9.]*$");
        }

        @Override
        public @NotNull String getPromptText(@NotNull ConversationContext context) {
            return ChatColor.YELLOW + "[IPLogin] " + ChatColor.WHITE + "Please enter the IP. Only digits and dots are allowed:";
        }

        @Override
        protected @NotNull Prompt acceptValidatedInput(@NotNull ConversationContext context, @NotNull String input) {
            Player player = (Player) context.getForWhom();
            IPStorer.addPlayerIP(player.getName(), input);
            player.sendMessage(getSuccessMessage(player.getName(), input));
            player.performCommand("iplogin get-ips");
            return Prompt.END_OF_CONVERSATION;
        }
    }
}
