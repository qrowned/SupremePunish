package dev.qrowned.punish.common.punish;

import dev.qrowned.punish.api.event.EventHandler;
import dev.qrowned.punish.api.event.impl.PlayerPardonEvent;
import dev.qrowned.punish.api.event.impl.PlayerPunishEvent;
import dev.qrowned.punish.api.punish.Punishment;
import dev.qrowned.punish.api.punish.PunishmentHandler;
import dev.qrowned.punish.api.punish.PunishmentReason;
import dev.qrowned.punish.api.result.PunishResult;
import dev.qrowned.punish.api.user.PunishUserHandler;
import dev.qrowned.punish.common.config.impl.PunishmentsConfig;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public final class CommonPunishmentHandler implements PunishmentHandler {

    private final EventHandler eventHandler;
    private final PunishmentsConfig punishmentsConfig;
    private final PunishUserHandler punishUserHandler;
    private final PunishmentDataHandler punishmentDataHandler;

    @Override
    public CompletableFuture<PunishResult<Punishment>> punish(@NotNull UUID target, @NotNull UUID executor, @NotNull Punishment.Type type, @NotNull String reason, long duration) {
        return this.punish(target, executor, new PunishmentReason(reason, duration, TimeUnit.MILLISECONDS, type));
    }

    @Override
    public CompletableFuture<PunishResult<Punishment>> punish(@NotNull UUID target, @NotNull UUID executor, @NotNull PunishmentReason punishmentReason) {
        return this.getActivePunishment(target, punishmentReason.getType()).thenComposeAsync(punishment -> {
                    if (punishment.isPresent())
                        return CompletableFuture.supplyAsync(() -> new PunishResult<>("punish.existing"));

                    return this.punishUserHandler.fetchUser(executor).thenComposeAsync(executorUser -> {
                        if (executorUser == null || !executorUser.hasPermission(punishmentReason.getPermission()))
                            return CompletableFuture.supplyAsync(() -> new PunishResult<>("punish.reason.noPermission"));

                        return this.punishUserHandler.fetchUser(target).thenComposeAsync(targetUser -> {
                            if (targetUser == null || targetUser.hasPermission("supremepunish.bypass"))
                                return CompletableFuture.supplyAsync(() -> new PunishResult<>("punish.reason.noPermission"));

                            return this.punishmentDataHandler.insertDataWithReturn(new Punishment(target, executor, punishmentReason)).thenApplyAsync(createdPunishment -> {
                                if (createdPunishment == null) return new PunishResult<>("internalError");

                                this.eventHandler.call(new PlayerPunishEvent(target, executor, createdPunishment, punishmentReason));
                                return new PunishResult<>(createdPunishment);
                            });
                        });
                    });
                }
        );
    }

    @Override
    public CompletableFuture<PunishResult<Punishment>> pardon(@NotNull UUID target, @NotNull UUID executor, @NotNull Punishment.Type type, @NotNull String reason) {
        return this.getActivePunishment(target, type).thenApplyAsync(punishmentOptional -> {
            if (punishmentOptional.isEmpty() || punishmentOptional.get().getType().equals(Punishment.Type.KICK))
                return new PunishResult<>("punish.notExisting");

            Punishment punishment = punishmentOptional.get();
            punishment.setPardonExecutor(executor);
            punishment.setPardonReason(reason);
            punishment.setPardonExecutionTime(System.currentTimeMillis());

            this.punishmentDataHandler.updateData(target, punishment);
            this.eventHandler.call(new PlayerPardonEvent(target, executor, punishment));
            return new PunishResult<>(punishment);
        });
    }

    @Override
    public CompletableFuture<Optional<Punishment>> getActivePunishment(@NotNull UUID uuid, @NotNull Punishment.Type type) {
        return this.punishmentDataHandler.getData(uuid)
                .thenApplyAsync(punishments -> punishments.stream()
                        .filter(Punishment::isActive)
                        .filter(punishment -> punishment.getType().equals(type))
                        .findFirst());
    }

    @Override
    public PunishmentReason getPunishmentReason(@NotNull String id) {
        return this.punishmentsConfig.getPunishmentReasons().stream().filter(punishmentReason -> punishmentReason.checkAlias(id))
                .findFirst().orElse(null);
    }

    @Override
    public PunishmentReason getPunishmentReason(@NotNull String id, @NotNull Punishment.Type type) {
        return this.punishmentsConfig.getPunishmentReasons().stream()
                .filter(punishmentReason -> punishmentReason.getType().equals(type))
                .filter(punishmentReason -> punishmentReason.checkAlias(id))
                .findFirst().orElse(null);
    }

    @Override
    public List<PunishmentReason> getPunishmentReasons() {
        return this.punishmentsConfig.getPunishmentReasons();
    }
}
