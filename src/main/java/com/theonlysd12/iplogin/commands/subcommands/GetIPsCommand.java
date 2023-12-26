package com.theonlysd12.iplogin.commands.subcommands;

import com.theonlysd12.iplogin.IPStorer;
import com.theonlysd12.iplogin.commands.SubCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class GetIPsCommand extends SubCommand {
    private final Component playerOnlyCommandMessage = Component.text("Only a player can run this command").color(NamedTextColor.RED);

    @Override
    public String getName() {
        return "get-ips";
    }

    @Override
    public String getDescription() {
        return "Show your IPs";
    }

    @Override
    public String getSyntax() {
        return "Type /iplogin get-ips to see your IPs";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(playerOnlyCommandMessage);
        } else {
            List<String> IPs = IPStorer.getIPsFromPlayer(sender.getName());

            Component message = Component.text()
                    .content("Hi ")
                    .append(Component.text().content(sender.getName()).color(NamedTextColor.GREEN).decorate(TextDecoration.BOLD).build())
                    .append(Component.text(", your saved IPs are:"))
                    .append(Component.newline())
                    .append(Component.text("=============="))
                    .build();

            for (String ip : IPs) {
                Component removeIPComponent = Component.text()
                        .content(" [-]")
                        .color(NamedTextColor.RED)
                        .hoverEvent(HoverEvent.showText(Component.text("Remove IP")))
                        .clickEvent(ClickEvent.runCommand("/iplogin remove-ip " + ip))
                        .build();

                Component changeIPComponent = Component.text()
                        .content(" [/]")
                        .color(NamedTextColor.BLUE)
                        .hoverEvent(HoverEvent.showText(Component.text("Change IP")))
                        .clickEvent(ClickEvent.runCommand("/iplogin change-ip " + ip))
                        .build();

                message = message.append(Component.newline()).append(Component.text(ip)).append(removeIPComponent).append(changeIPComponent);
            }

            message = message.append(Component.newline()).append(Component.text()
                    .content("[+]")
                    .color(NamedTextColor.GREEN)
                    .hoverEvent(HoverEvent.showText(Component.text("Add IP")))
                    .clickEvent(ClickEvent.runCommand("/iplogin add-ip"))
                    .build());

            sender.sendMessage(message);
        }
    }

    @Override
    public List<String> getArguments(CommandSender sender, String[] args) {
        return null;
    }
}
