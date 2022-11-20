package dev.qrowned.punish.api.command;

import java.util.List;

public interface CommandHandler<P> {

    void registerCommands(PunishCommand<P>... commands);

    List<PunishCommand<P>> getRegisteredCommands();

}
