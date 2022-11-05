package dev.qrowned.punish.common.util;

import dev.qrowned.punish.api.PunishPlugin;
import dev.qrowned.punish.api.logger.PluginLogger;
import dev.qrowned.punish.api.user.PunishUser;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
public abstract class AbstractConnectionListener {

    protected final PunishPlugin plugin;

    public CompletableFuture<PunishUser> loadUser(@NotNull UUID uuid, @NotNull String name) {
        PluginLogger logger = this.plugin.getLogger();

        return this.plugin.getUserHandler().fetchUser(uuid).thenApplyAsync(punishUser -> {
            if (punishUser == null) {
                PunishUser createdUser = this.plugin.getUserHandler().createUser(uuid, name);
                logger.info("Created new user for UUID " + uuid + " with the name " + name + ".");
                return createdUser;
            }

            if (!punishUser.getName().equals(name)) {
                logger.info("The player with the UUID " + uuid + " changed his name. Changing it in the database...");
                this.plugin.getUserHandler().updateName(uuid, name);
            }

            return punishUser;
        });
    }

}
