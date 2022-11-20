package dev.qrowned.punish.bungee.user;

import dev.qrowned.punish.api.user.AbstractPunishUser;
import dev.qrowned.punish.common.user.AbstractPunishUserHandler;
import dev.qrowned.punish.common.user.PunishUserDataHandler;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class BungeePunishUserHandler extends AbstractPunishUserHandler {

    public BungeePunishUserHandler(@NotNull PunishUserDataHandler punishUserDataHandler) {
        super(punishUserDataHandler);
    }

    @Override
    public @NotNull AbstractPunishUser createUser(@NotNull UUID uuid, @NotNull String name) {
        BungeePunishUser punishUser = new BungeePunishUser(uuid, name);
        super.punishUserDataHandler.insertData(uuid, punishUser);
        return punishUser;
    }

}
