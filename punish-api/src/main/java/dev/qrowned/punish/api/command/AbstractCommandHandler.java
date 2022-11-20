package dev.qrowned.punish.api.command;

import dev.qrowned.punish.api.command.annotation.Command;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public abstract class AbstractCommandHandler<P> implements CommandHandler<P> {

    protected final List<PunishCommand<P>> punishCommands = new ArrayList<>();

    @Override
    @SafeVarargs
    public final void registerCommands(PunishCommand<P>... commands) {
        for (PunishCommand<P> command : commands) {
            Command commandAnnotation = command.getClass().getAnnotation(Command.class);

            if (commandAnnotation == null) {
                throw new UnsupportedOperationException("Can't register a command without @Command annotation.");
            }

            this.punishCommands.add(command);
            this.registerPlatformInternalCommand(command);
        }
    }

    @Override
    public List<PunishCommand<P>> getRegisteredCommands() {
        return this.punishCommands;
    }

    protected abstract void registerPlatformInternalCommand(@NotNull PunishCommand<P> punishCommand);

}
