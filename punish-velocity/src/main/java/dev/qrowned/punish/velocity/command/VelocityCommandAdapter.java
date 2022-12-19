package dev.qrowned.punish.velocity.command;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.RawCommand;
import dev.qrowned.punish.api.command.PunishCommand;
import dev.qrowned.punish.api.command.annotation.SubCommand;
import dev.qrowned.punish.api.command.sub.AbstractPunishSubCommand;
import dev.qrowned.punish.api.command.sub.PunishSubCommandMapping;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import org.apache.commons.lang3.ArrayUtils;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public final class VelocityCommandAdapter implements RawCommand {

    private final Component NO_PERMISSION = Component.text("NO PERMISSION MESSAGE (CHANGE ME!!!)");

    private final PunishCommand<CommandSource> command;

    @Override
    public void execute(Invocation invocation) {
        CommandSource commandSender = invocation.source();
        String[] args = invocation.arguments().split(" ");
        if (!this.command.hasPermission(commandSender)) {
            commandSender.sendMessage(NO_PERMISSION);
            return;
        }

        final List<PunishSubCommandMapping<CommandSource>> subCommandMappings = this.command.getAnnotationMappings();

        final List<PunishSubCommandMapping<CommandSource>> subCommands = subCommandMappings.stream()
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
                if (subCommandAnnotationMapping.getExecutor() instanceof AbstractPunishSubCommand<CommandSource>) {
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
    public List<String> suggest(Invocation invocation) {
        String[] args = invocation.arguments().split(" ");
        final boolean emptyEnd = (args[args.length - 1].endsWith(" "));
        final int argsLength = args.length - (emptyEnd ? 0 : 1);

        return this.command.handleTabCompletion(invocation.source(), null, argsLength, args);
    }

}
