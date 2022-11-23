package dev.qrowned.punish.common.user;

import dev.qrowned.punish.api.user.AbstractPunishUser;
import dev.qrowned.punish.api.user.PunishUserHandler;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public abstract class AbstractPunishUserHandler implements PunishUserHandler {

    protected final PunishUserDataHandler punishUserDataHandler;

    @SneakyThrows
    @Override
    public @Nullable AbstractPunishUser getUser(@NotNull UUID uuid) {
        return this.punishUserDataHandler.getData(uuid).get(5, TimeUnit.SECONDS);
    }

    @SneakyThrows
    @Override
    public @Nullable AbstractPunishUser getUser(@NotNull String name) {
        return this.punishUserDataHandler.getData(name).get(5, TimeUnit.SECONDS);
    }

    @Override
    public @NotNull CompletableFuture<@Nullable AbstractPunishUser> fetchUser(@NotNull UUID uuid) {
        return this.punishUserDataHandler.getData(uuid);
    }

    @Override
    public @NotNull CompletableFuture<@Nullable AbstractPunishUser> fetchUser(@NotNull String name) {
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

}
