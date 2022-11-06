package dev.qrowned.punish.common.punish;

import dev.qrowned.punish.api.event.EventHandler;
import dev.qrowned.punish.api.event.impl.PlayerPardonEvent;
import dev.qrowned.punish.api.event.impl.PlayerPunishEvent;
import dev.qrowned.punish.api.punish.Punishment;
import dev.qrowned.punish.api.punish.PunishmentHandler;
import dev.qrowned.punish.api.punish.PunishmentReason;
import dev.qrowned.punish.common.config.impl.PunishmentsConfig;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
public final class CommonPunishmentHandler implements PunishmentHandler {

    private final EventHandler eventHandler;
    private final PunishmentsConfig punishmentsConfig;
    private final PunishmentDataHandler punishmentDataHandler;

    @Override
    public CompletableFuture<Punishment> punish(@NotNull UUID target, @NotNull UUID executor, @NotNull PunishmentReason punishmentReason) {
        return this.punishmentDataHandler.insertDataWithReturn(new Punishment(target, executor, punishmentReason)).thenApplyAsync(punishment -> {
            this.eventHandler.call(new PlayerPunishEvent(target, executor, punishment));
            return punishment;
        });
    }

    @Override
    public Punishment pardon(@NotNull UUID target, @NotNull UUID executor, @NotNull String reason, @NotNull Punishment punishment) {
        punishment.setPardonExecutor(executor);
        punishment.setPardonReason(reason);
        punishment.setPardonExecutionTime(System.currentTimeMillis());
        this.punishmentDataHandler.updateData(target, punishment);
        this.eventHandler.call(new PlayerPardonEvent(target, executor, punishment));
        return punishment;
    }

    @Override
    public CompletableFuture<Punishment> getActivePunishment(@NotNull UUID uuid) {
        return this.punishmentDataHandler.getData(uuid)
                .thenApplyAsync(punishments -> punishments.stream().filter(Punishment::isActive).findFirst().orElse(null));
    }

    @Override
    public PunishmentReason getPunishmentReason(@NotNull String id) {
        return this.punishmentsConfig.getPunishmentReasons().stream().filter(punishmentReason -> punishmentReason.checkAlias(id))
                .findFirst().orElse(null);
    }

}
