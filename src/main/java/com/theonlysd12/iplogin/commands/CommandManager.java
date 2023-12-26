package com.theonlysd12.iplogin.commands;

import com.theonlysd12.iplogin.IPLogin;
import com.theonlysd12.iplogin.commands.subcommands.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CommandManager implements TabExecutor {

    private final ArrayList<SubCommand> subCommands = new ArrayList<>();

    private final Component noSubCommandMessage = Component.text()
            .content(IPLogin.pluginChatName)
            .color(NamedTextColor.RED)
            .append(Component.text()
                    .content("Command does not exist.")
                    .color(NamedTextColor.WHITE))
            .build();

    public CommandManager(){
        subCommands.add(new RemoveIPCommand());
        subCommands.add(new ClearIPsCommand());
        subCommands.add(new ChangeIPCommand());
        subCommands.add(new AddIPCommand());
        subCommands.add(new GetIPsCommand());
        subCommands.add(new AddPlayerCommand());
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length > 0){
            boolean isNotSubCommand = true;
            for (SubCommand subCommand : subCommands) {
                if (args[0].equals(subCommand.getName())) {
                    subCommand.perform(sender, args);
                    isNotSubCommand = false;
                }
            }
            if(isNotSubCommand){
                sender.sendMessage(noSubCommandMessage);
            }
        } else {
            sender.sendMessage(Component.text("================================"));
            for(SubCommand subCommand : subCommands){
                sender.sendMessage(Component.text()
                        .content(subCommand.getSyntax())
                        .append(Component.newline())
                        .append(Component.text(subCommand.getDescription()))
                        .append(Component.newline())
                        .build());
            }
            sender.sendMessage(Component.text("================================"));
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if(args.length == 1){
            return subCommands.stream().map(SubCommand::getName).toList();
        } else if(args.length >= 2){
            for(SubCommand s : subCommands){
                if(args[0].equals(s.getName())){
                    return s.getArguments(sender, args);
                }
            }
        }

        return null;
    }
}

/*
  get-ips:
    description: Shows your saved IPs
    usage: Type /get-ips to get your ips
  add-ip:
    description: Add IPs to your player
    usage: Type /addip <new ip> to set your new IP. If you want to be able to log in from any IP type /addip *
*/

