package dev.qrowned.punish.common.user;

import dev.qrowned.punish.api.user.PunishUser;
import dev.qrowned.punish.api.user.PunishUserHandler;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
public final class CommonPunishUserHandler implements PunishUserHandler {

    private final PunishUserDataHandler punishUserDataHandler;

    @Override
    public @Nullable PunishUser getUser(@NotNull UUID uuid) {
        return this.punishUserDataHandler.getData(uuid).join();
    }

    @Override
    public @Nullable PunishUser getUser(@NotNull String name) {
        return this.punishUserDataHandler.getData(name).join();
    }

    @Override
    public @NotNull CompletableFuture<@Nullable PunishUser> fetchUser(@NotNull UUID uuid) {
        return this.punishUserDataHandler.getData(uuid);
    }

    @Override
    public @NotNull CompletableFuture<@Nullable PunishUser> fetchUser(@NotNull String name) {
        return this.punishUserDataHandler.getData(name);
    }

    @Override
    public void updateName(@NotNull UUID uuid, @NotNull String name) {
        this.fetchUser(uuid).thenAcceptAsync(punishUser -> {
            if (punishUser == null) return;

            punishUser.setName(name);
            this.punishUserDataHandler.updateData(uuid, punishUser);
        });
    }

    @Override
    public @NotNull PunishUser createUser(@NotNull UUID uuid, @NotNull String name) {
        PunishUser punishUser = new PunishUser(uuid, name);
        this.punishUserDataHandler.insertData(uuid, punishUser);
        return punishUser;
    }

}
