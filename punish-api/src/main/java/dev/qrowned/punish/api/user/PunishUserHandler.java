package dev.qrowned.punish.api.user;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface PunishUserHandler {

    @Nullable AbstractPunishUser getUser(@NotNull UUID uuid);

    @Nullable AbstractPunishUser getUser(@NotNull String name);

    @NotNull CompletableFuture<@Nullable AbstractPunishUser> fetchUser(@NotNull UUID uuid);

    @NotNull CompletableFuture<@Nullable AbstractPunishUser> fetchUser(@NotNull String name);

    void updateName(@NotNull UUID uuid, @NotNull String name);

    @NotNull AbstractPunishUser createUser(@NotNull UUID uuid, @NotNull String name);

}
