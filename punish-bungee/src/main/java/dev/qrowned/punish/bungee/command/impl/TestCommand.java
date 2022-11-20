package dev.qrowned.punish.bungee.command.impl;

import dev.qrowned.punish.api.command.annotation.Command;
import dev.qrowned.punish.api.command.annotation.SubCommand;
import dev.qrowned.punish.api.command.sub.AbstractPunishSubCommand;
import dev.qrowned.punish.bungee.command.BungeePunishCommand;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.UUID;

@Command(name = "test",
        aliases = "knecht")
public final class TestCommand extends BungeePunishCommand {

    public TestCommand() {
        this.registerSubCommands(new TestSubCommand());
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        UUID uuid = this.getUUID(sender);
        sender.sendMessage(TextComponent.fromLegacyText(uuid + " executed the command."));
    }

    @SubCommand(name = "chef",
            aliases = "chefo",
            boundToClass = TestCommand.class)
    public static class TestSubCommand extends AbstractPunishSubCommand<CommandSender> {

        public TestSubCommand() {
            super("baum", "giftig");
        }

        @Override
        public void execute(CommandSender sender, String[] args) {
            UUID uuid = this.getUUID(sender);
            sender.sendMessage(TextComponent.fromLegacyText(uuid + " executed the subcommand."));
        }

        @Override
        public UUID getUUID(CommandSender player) {
            return player instanceof ProxiedPlayer proxiedPlayer ? proxiedPlayer.getUniqueId() : new UUID(0, 0);
        }

    }

}
