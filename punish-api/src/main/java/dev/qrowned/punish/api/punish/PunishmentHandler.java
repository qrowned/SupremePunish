package dev.qrowned.punish.api.punish;

import dev.qrowned.punish.api.result.PunishResult;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface PunishmentHandler {

    CompletableFuture<PunishResult<Punishment>> punish(@NotNull UUID target, @NotNull UUID executor, @NotNull String reason);

    CompletableFuture<PunishResult<Punishment>> punish(@NotNull UUID target, @NotNull UUID executor, @NotNull Punishment.Type type, @NotNull String reason, long duration);

    default CompletableFuture<PunishResult<Punishment>> ban(@NotNull UUID target, @NotNull UUID executor, @NotNull String reason, long duration) {
        return this.punish(target, executor, Punishment.Type.BAN, reason, duration);
    }

    default CompletableFuture<PunishResult<Punishment>> mute(@NotNull UUID target, @NotNull UUID executor, @NotNull String reason, long duration) {
        return this.punish(target, executor, Punishment.Type.MUTE, reason, duration);
    }

    default CompletableFuture<PunishResult<Punishment>> kick(@NotNull UUID target, @NotNull UUID executor, @NotNull String reason) {
        return this.punish(target, executor, Punishment.Type.KICK, reason, -1);
    }

    CompletableFuture<PunishResult<Punishment>> pardon(@NotNull UUID target, @NotNull UUID executor, @NotNull Punishment.Type type, @NotNull String reason);

    CompletableFuture<Optional<Punishment>> getActivePunishment(@NotNull UUID uuid, @NotNull Punishment.Type type);

    PunishmentReason getPunishmentReason(@NotNull String id);

    List<PunishmentReason> getPunishmentReasons();

}
