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

public class ChangeIPCommand extends SubCommand {
    private static Component getSuccessMessage(String oldIP, String newIP){
        return Component.text()
                .content(IPLogin.pluginChatName)
                .color(NamedTextColor.GREEN)
                .append(Component.text()
                        .content("Successfully changed IP: ")
                        .color(NamedTextColor.WHITE)
                        .append(Component.text()
                                .content(oldIP)
                                .color(NamedTextColor.DARK_GRAY)
                                .build())
                        .append(Component.text(" to "))
                        .append(Component.text()
                                .content(newIP)
                                .color(NamedTextColor.GRAY)
                                .build())
                        .build())
                .build();
    }

    @Override
    public String getName() {
        return "change-ip";
    }

    @Override
    public String getDescription() {
        return "Changes an IP";
    }

    @Override
    public String getSyntax() {
        return "Type /iplogin change-ip <old ip> <new ip> to change one of your IPs or /iplogin change-ip <old ip> <new ip> <player name> to change a player's IP";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if(args.length == 2){
            if(isSenderPlayer(sender) && doesPlayerHaveIP(sender, args[1])){
                ConversationFactory factory = new ConversationFactory(IPLogin.getPlugin());
                Conversation conversation = factory.withFirstPrompt(new WhatIPPrompt(args[1])).buildConversation((Conversable) sender);
                conversation.begin();
            }
        } else if(args.length == 3){
            if(isSenderPlayer(sender) && isPlayerValid(sender, args[2]) && doesPlayerHaveIP(sender, args[1])){
                IPStorer.changeIPFromPlayer(sender.getName(), args[1], args[2]);
                sender.sendMessage(getSuccessMessage(args[1], args[2]));
            }
        } else if(args.length == 4){
            if(sender instanceof Player){
                if(isSenderOP(sender) && isPlayerValid(sender, args[3]) && doesPlayerHaveIP(sender, args[2], args[1]) && isIPValid(sender, args[2])){
                    IPStorer.changeIPFromPlayer(args[3], args[1], args[2]);
                    sender.sendMessage(getSuccessMessage(args[1], args[2]));
                }
            } else {
                if(isPlayerValid(sender, args[2]) && doesPlayerHaveIP(sender, args[2], args[1]) && isIPValid(sender, args[3])){
                    IPStorer.changeIPFromPlayer(args[3], args[1], args[2]);
                    sender.sendMessage(getSuccessMessage(args[1], args[2]));
                }
            }
        } else {sender.sendMessage(incorrectUseMessage);}
    }

    @Override
    public List<String> getArguments(CommandSender sender, String[] args) {
        if(args.length == 2 && sender instanceof Player){
            return IPStorer.getIPsFromPlayer(sender.getName());
            // ad
        }

        return null;
    }

    private static class WhatIPPrompt extends RegexPrompt {
        private final String oldIP;

        public WhatIPPrompt(String oldIP) {
            super("^[0-9.]*$");
            this.oldIP = oldIP;
        }

        @Override
        public @NotNull String getPromptText(@NotNull ConversationContext context) {
            return ChatColor.YELLOW + "[IPLogin] " + ChatColor.WHITE + "Please enter the new IP. Only digits and dots are allowed:";
        }

        @Override
        protected @NotNull Prompt acceptValidatedInput(@NotNull ConversationContext context, @NotNull String input) {
            Player player = (Player) context.getForWhom();
            IPStorer.changeIPFromPlayer(player.getName(), oldIP, input);
            player.sendMessage(getSuccessMessage(oldIP, input));
            player.performCommand("iplogin get-ips");
            return Prompt.END_OF_CONVERSATION;
        }
    }
}
