package dev.qrowned.punish.api.punish;

import dev.qrowned.punish.api.result.PunishResult;
import org.apache.commons.lang3.SerializationUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface PunishmentHandler {

    default CompletableFuture<PunishResult<Punishment>> punish(@NotNull UUID target, @NotNull UUID executor, @NotNull String reason) {
        PunishmentReason punishmentReason = this.getPunishmentReason(reason);
        if (punishmentReason == null)
            return CompletableFuture.supplyAsync(() -> new PunishResult<>("punish.reason.notFound"));

        return this.punish(target, executor, punishmentReason);
    }

    default CompletableFuture<PunishResult<Punishment>> ban(@NotNull UUID target, @NotNull UUID executor, @NotNull String reason) {
        PunishmentReason punishmentReason = this.getPunishmentReason(reason, Punishment.Type.BAN);
        if (punishmentReason == null)
            return CompletableFuture.supplyAsync(() -> new PunishResult<>("punish.reason.notFound"));

        return this.punish(target, executor, punishmentReason);
    }

    default CompletableFuture<PunishResult<Punishment>> mute(@NotNull UUID target, @NotNull UUID executor, @NotNull String reason) {
        PunishmentReason punishmentReason = this.getPunishmentReason(reason, Punishment.Type.MUTE);
        if (punishmentReason == null)
            return CompletableFuture.supplyAsync(() -> new PunishResult<>("punish.reason.notFound"));

        return this.punish(target, executor, punishmentReason);
    }

    CompletableFuture<PunishResult<Punishment>> punish(@NotNull UUID target, @NotNull UUID executor, @NotNull Punishment.Type type, @NotNull String reason, long duration);

    CompletableFuture<PunishResult<Punishment>> punish(@NotNull UUID target, @NotNull UUID executor, @NotNull PunishmentReason punishmentReason);

    default CompletableFuture<PunishResult<Punishment>> ban(@NotNull UUID target, @NotNull UUID executor, @NotNull String reason, long duration) {
        return this.punish(target, executor, Punishment.Type.BAN, reason, duration);
    }

    default CompletableFuture<PunishResult<Punishment>> mute(@NotNull UUID target, @NotNull UUID executor, @NotNull String reason, long duration) {
        return this.punish(target, executor, Punishment.Type.MUTE, reason, duration);
    }

    default CompletableFuture<PunishResult<Punishment>> kick(@NotNull UUID target, @NotNull UUID executor, @NotNull String reason) {
        return this.punish(target, executor, Punishment.Type.KICK, reason, 0);
    }

    CompletableFuture<PunishResult<Punishment>> pardon(@NotNull UUID target, @NotNull UUID executor, @NotNull Punishment.Type type, @NotNull String reason);

    default CompletableFuture<PunishResult<Punishment>> unban(@NotNull UUID target, @NotNull UUID executor, @NotNull String reason) {
        return this.pardon(target, executor, Punishment.Type.BAN, reason);
    }

    default CompletableFuture<PunishResult<Punishment>> unmute(@NotNull UUID target, @NotNull UUID executor, @NotNull String reason) {
        return this.pardon(target, executor, Punishment.Type.MUTE, reason);
    }

    CompletableFuture<Optional<Punishment>> getActivePunishment(@NotNull UUID uuid, @NotNull Punishment.Type type);

    CompletableFuture<List<Punishment>> getPunishments(@NotNull UUID uuid);

    PunishmentReason getPunishmentReason(@NotNull String id);

    PunishmentReason getPunishmentReason(@NotNull String id, @NotNull Punishment.Type type);

    List<PunishmentReason> getPunishmentReasons();

}
