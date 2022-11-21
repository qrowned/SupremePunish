package dev.qrowned.punish.common.command;

import dev.qrowned.punish.api.command.AbstractPunishCommand;
import dev.qrowned.punish.api.config.ConfigProvider;
import dev.qrowned.punish.common.config.impl.LicenseConfig;
import dev.qrowned.punish.common.punish.PunishmentDataHandler;
import dev.qrowned.punish.common.user.PunishUserDataHandler;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public abstract class AbstractReloadCommand<P> extends AbstractPunishCommand<P> {

    private final ConfigProvider configProvider;
    private final PunishUserDataHandler punishUserDataHandler;
    private final PunishmentDataHandler punishmentDataHandler;

    @Override
    public void execute(P sender, String[] args) {
        this.configProvider.reloadConfigs();
        this.punishUserDataHandler.invalidateAll();
        this.punishmentDataHandler.invalidateAll();

        this.sendMessage(sender, "Successfully reloaded configs and cleared all data caches.");
    }

    protected abstract void sendMessage(@NotNull P player, @NotNull String message);

}
