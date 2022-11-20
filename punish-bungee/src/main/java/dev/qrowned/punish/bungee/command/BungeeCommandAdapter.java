package dev.qrowned.punish.bungee.command;

import dev.qrowned.punish.api.command.PunishCommand;
import dev.qrowned.punish.api.command.annotation.SubCommand;
import dev.qrowned.punish.api.command.sub.AbstractPunishSubCommand;
import dev.qrowned.punish.api.command.sub.PunishSubCommandMapping;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public final class BungeeCommandAdapter extends Command implements TabExecutor {

    private static final BaseComponent[] NO_PERMISSION = TextComponent.fromLegacyText("NO PERMISSION MESSAGE (CHANGE ME!!!)");

    private final PunishCommand<CommandSender> command;

    public BungeeCommandAdapter(@NotNull PunishCommand<CommandSender> punishCommand) {
        super(punishCommand.getName(), punishCommand.getPermission(), punishCommand.getAliases());
        this.command = punishCommand;
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (!this.command.hasPermission(commandSender)) {
            commandSender.sendMessage(NO_PERMISSION);
            return;
        }

        final List<PunishSubCommandMapping<CommandSender>> subCommandMappings = this.command.getAnnotationMappings();

        final List<PunishSubCommandMapping<CommandSender>> subCommands = subCommandMappings.stream()
                .filter(subCommandAnnotationMapping -> {
                    final SubCommand subCommandAnnotation = subCommandAnnotationMapping.getSubCommandAnnotation();
                    String[] shortenedArgs = ArrayUtils.subarray(args,
                            0,
                            subCommandAnnotation.name().split(" ").length);

                    if (!subCommandAnnotation.ignoreMaxArgs() && (args.length < subCommandAnnotation.minArgs() || args.length > subCommandAnnotation.maxArgs())) {
                        return false;
                    }

                    return subCommandAnnotationMapping.getExecutor().checkName(String.join(" ", shortenedArgs));
                }).toList();

        if (subCommands.isEmpty()) {
            try {
                this.command.handleNoSubCommandFound(commandSender, args);
            } catch (Throwable throwable) {
                this.command.handleException(commandSender, throwable, Optional.empty());
            }
        }

        subCommands.forEach(subCommandAnnotationMapping -> {
            try {
                final String subCommandPermission = subCommandAnnotationMapping.getExecutor().getPermission();
                if (subCommandAnnotationMapping.getExecutor() instanceof AbstractPunishSubCommand<CommandSender>) {
                    if (!commandSender.hasPermission(subCommandPermission)) {
                        if (!this.command.handleNoPermission(commandSender,
                                subCommandPermission,
                                Optional.ofNullable(subCommandAnnotationMapping.getExecutor()))) {
                            commandSender.sendMessage(NO_PERMISSION);
                        }
                        return;
                    }
                }
                subCommandAnnotationMapping.getExecutor().execute(commandSender, args);
            } catch (Throwable throwable) {
                this.command.handleException(commandSender, throwable, Optional.ofNullable(subCommandAnnotationMapping.getExecutor()));
            }
        });

        this.command.execute(commandSender, args);
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender commandSender, String[] args) {
        final boolean emptyEnd = (args[args.length - 1].endsWith(" "));
        final int argsLength = args.length - (emptyEnd ? 0 : 1);

        return this.command.handleTabCompletion(commandSender, null, argsLength, args);
    }
}
