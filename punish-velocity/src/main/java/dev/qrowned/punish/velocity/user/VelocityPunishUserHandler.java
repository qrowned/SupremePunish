package dev.qrowned.punish.velocity.user;

import dev.qrowned.punish.api.user.AbstractPunishUser;
import dev.qrowned.punish.common.user.AbstractPunishUserHandler;
import dev.qrowned.punish.common.user.PunishUserDataHandler;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public final class VelocityPunishUserHandler extends AbstractPunishUserHandler {

    public VelocityPunishUserHandler(PunishUserDataHandler punishUserDataHandler) {
        super(punishUserDataHandler);
    }

    @Override
    public @NotNull AbstractPunishUser createUser(@NotNull UUID uuid, @NotNull String name) {
        VelocityPunishUser punishUser = new VelocityPunishUser(uuid, name);
        super.punishUserDataHandler.insertData(uuid, punishUser);
        return punishUser;
    }
}
