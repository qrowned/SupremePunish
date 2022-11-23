package dev.qrowned.punish.bungee.user;

import dev.qrowned.punish.api.user.AbstractPunishUser;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.UUID;

public final class BungeePunishUser extends AbstractPunishUser {

    public BungeePunishUser(UUID uuid, String name, Instant createdAt) {
        super(uuid, name, createdAt);
    }

    public BungeePunishUser(@NotNull UUID uuid, @NotNull String name) {
        super(uuid, name);
    }

    /**
     * Check if a @{@link AbstractPunishUser} has a certain permission.
     *
     * @param permission the permission to check.
     * @return whether the player has the permission.
     * @apiNote Only works if the player is online.
     */
    @Override
    public boolean hasPermission(@NotNull String permission) {
        if (super.uuid.equals(CONSOLE_UUID)) return true;

        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(super.uuid);
        if (player == null) return false;
        return player.hasPermission(permission);
    }

}
