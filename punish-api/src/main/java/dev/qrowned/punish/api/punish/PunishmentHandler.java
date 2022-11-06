package dev.qrowned.punish.api.punish;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface PunishmentHandler {

    CompletableFuture<Punishment> punish(@NotNull UUID target, @NotNull UUID executor, @NotNull PunishmentReason punishmentReason);

    Punishment pardon(@NotNull UUID target, @NotNull UUID executor, @NotNull String reason, @NotNull Punishment punishment);

    default CompletableFuture<Punishment> pardon(@NotNull UUID target, @NotNull UUID executor, @NotNull String reason) {
        return this.getActivePunishment(target).thenApplyAsync(punishment -> this.pardon(target, executor, reason, punishment));
    }

    CompletableFuture<Punishment> getActivePunishment(@NotNull UUID uuid);

    PunishmentReason getPunishmentReason(@NotNull String id);

}
