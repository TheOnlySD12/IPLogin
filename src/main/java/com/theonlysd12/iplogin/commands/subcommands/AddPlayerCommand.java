package com.theonlysd12.iplogin.commands.subcommands;

import com.theonlysd12.iplogin.IPStorer;
import com.theonlysd12.iplogin.commands.SubCommand;
import org.bukkit.command.CommandSender;

import java.util.List;

public class AddPlayerCommand extends SubCommand {
    @Override
    public String getName() {
        return "t-add";
    }

    @Override
    public String getDescription() {
        return "t-add";
    }

    @Override
    public String getSyntax() {
        return "broski";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        IPStorer.addPlayer(args[1], args[2]);
    }

    @Override
    public List<String> getArguments(CommandSender sender, String[] args) {
        return List.of("big", "small", "extra smarty boi");
    }
}
