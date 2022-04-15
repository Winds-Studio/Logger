package me.prism3.loggervelocity.Commands;

import me.prism3.loggervelocity.Utils.Messages;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;

import static me.prism3.loggervelocity.Utils.Data.loggerReload;

public class Reload implements SimpleCommand {

    private final Messages messages = new Messages();

    @Override
    public void execute(Invocation invocation) {

        final CommandSource sender = invocation.source();
        final String[] args = invocation.arguments();

        if (sender.hasPermission(loggerReload)) {

            if (args.length == 0) {

                sender.sendMessage(Identity.nil(), Component.text("Thank you for using the Logger Plugin!"));

            } else if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {

                new Messages().reload();
                messages.reload();
                sender.sendMessage(Identity.nil(), Component.text(this.messages.getString("General.Reload")));

            } else {

                sender.sendMessage(Identity.nil(), Component.text(this.messages.getString("General.Invalid-Syntax")));

            }
        } else {

            sender.sendMessage(Identity.nil(), Component.text(this.messages.getString("General.No-Permission")));

        }
    }
}