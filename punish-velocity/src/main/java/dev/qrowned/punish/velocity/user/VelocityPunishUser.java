package dev.qrowned.punish.velocity.user;

import com.velocitypowered.api.proxy.Player;
import dev.qrowned.punish.api.user.AbstractPunishUser;
import dev.qrowned.punish.velocity.PunishVelocityPlugin;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public final class VelocityPunishUser extends AbstractPunishUser {

    public VelocityPunishUser(UUID uuid, String name, Instant createdAt) {
        super(uuid, name, createdAt);
    }

    public VelocityPunishUser(@NotNull UUID uuid, @NotNull String name) {
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

        Optional<Player> player = PunishVelocityPlugin.getServer().getPlayer(super.uuid);
        if (player.isEmpty()) return false;
        return player.get().hasPermission(permission);
    }

}
