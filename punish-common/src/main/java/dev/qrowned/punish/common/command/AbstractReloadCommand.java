package dev.qrowned.punish.common.command;

import dev.qrowned.punish.api.command.AbstractPunishCommand;
import dev.qrowned.punish.api.config.ConfigProvider;
import dev.qrowned.punish.api.message.MessageHandler;
import dev.qrowned.punish.common.punish.PunishmentDataHandler;
import dev.qrowned.punish.common.user.PunishUserDataHandler;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class AbstractReloadCommand<P> extends AbstractPunishCommand<P> {

    private final ConfigProvider configProvider;
    private final MessageHandler<P> messageHandler;
    private final PunishUserDataHandler punishUserDataHandler;
    private final PunishmentDataHandler punishmentDataHandler;

    @Override
    public void execute(P sender, String[] args) {
        this.configProvider.reloadConfigs();
        this.punishUserDataHandler.invalidateAll();
        this.punishmentDataHandler.invalidateAll();

        this.messageHandler.getMessage("reload.successful").send(sender);
    }

}
