package dev.qrowned.punish.api.user;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface PunishUserHandler {

    @Nullable PunishUser getUser(@NotNull UUID uuid);

    @Nullable PunishUser getUser(@NotNull String name);

    @NotNull CompletableFuture<@Nullable PunishUser> fetchUser(@NotNull UUID uuid);

    @NotNull CompletableFuture<@Nullable PunishUser> fetchUser(@NotNull String name);

    void updateName(@NotNull UUID uuid, @NotNull String name);

    @NotNull PunishUser createUser(@NotNull UUID uuid, @NotNull String name);

}
