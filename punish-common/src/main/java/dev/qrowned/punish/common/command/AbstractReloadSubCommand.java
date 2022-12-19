package dev.qrowned.punish.common.command;

import dev.qrowned.punish.api.command.sub.AbstractPunishSubCommand;
import dev.qrowned.punish.api.config.ConfigProvider;
import dev.qrowned.punish.api.message.MessageHandler;
import dev.qrowned.punish.common.punish.PunishmentDataHandler;
import dev.qrowned.punish.common.user.PunishUserDataHandler;

public abstract class AbstractReloadSubCommand<P> extends AbstractPunishSubCommand<P> {

    private final ConfigProvider configProvider;
    private final MessageHandler<P> messageHandler;
    private final PunishUserDataHandler punishUserDataHandler;
    private final PunishmentDataHandler punishmentDataHandler;

    public AbstractReloadSubCommand(ConfigProvider configProvider,
                                    MessageHandler<P> messageHandler,
                                    PunishUserDataHandler punishUserDataHandler,
                                    PunishmentDataHandler punishmentDataHandler) {
        super("/punish reload", "Reloads the cache and configs.");
        this.configProvider = configProvider;
        this.messageHandler = messageHandler;
        this.punishUserDataHandler = punishUserDataHandler;
        this.punishmentDataHandler = punishmentDataHandler;
    }

    @Override
    public void execute(P sender, String[] args) {
        this.configProvider.reloadConfigs();
        this.punishUserDataHandler.invalidateAll();
        this.punishmentDataHandler.invalidateAll();

        this.messageHandler.getMessage("reload.successful").send(sender);
    }

}
