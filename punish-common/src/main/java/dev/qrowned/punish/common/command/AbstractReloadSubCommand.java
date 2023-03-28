package dev.qrowned.punish.common.command;

import dev.qrowned.config.api.ConfigService;
import dev.qrowned.config.message.api.MessageService;
import dev.qrowned.punish.api.command.sub.AbstractPunishSubCommand;
import dev.qrowned.punish.common.punish.PunishmentDataHandler;
import dev.qrowned.punish.common.user.PunishUserDataHandler;

public abstract class AbstractReloadSubCommand<P> extends AbstractPunishSubCommand<P> {

    private final ConfigService configProvider;
    private final MessageService<P> messageService;
    private final PunishUserDataHandler punishUserDataHandler;
    private final PunishmentDataHandler punishmentDataHandler;

    public AbstractReloadSubCommand(ConfigService configProvider,
                                    MessageService<P> messageService,
                                    PunishUserDataHandler punishUserDataHandler,
                                    PunishmentDataHandler punishmentDataHandler) {
        super("/punish reload", "Reloads the cache and configs.");
        this.configProvider = configProvider;
        this.messageService = messageService;
        this.punishUserDataHandler = punishUserDataHandler;
        this.punishmentDataHandler = punishmentDataHandler;
    }

    @Override
    public void execute(P sender, String[] args) {
        this.configProvider.reloadConfigs();
        this.punishUserDataHandler.invalidateAll();
        this.punishmentDataHandler.invalidateAll();

        this.messageService.getMessage("reload.successful").send(sender);
    }

}
