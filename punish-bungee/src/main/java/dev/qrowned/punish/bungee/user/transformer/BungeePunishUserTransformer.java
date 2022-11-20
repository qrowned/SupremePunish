package dev.qrowned.punish.bungee.user.transformer;

import dev.qrowned.punish.api.database.DataTransformer;
import dev.qrowned.punish.api.user.AbstractPunishUser;
import dev.qrowned.punish.bungee.user.BungeePunishUser;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.util.UUID;

public final class BungeePunishUserTransformer implements DataTransformer<AbstractPunishUser> {

    @SneakyThrows
    @Override
    public BungeePunishUser transform(@NotNull ResultSet resultSet) {
        return new BungeePunishUser(
                UUID.fromString(resultSet.getString("uuid")),
                resultSet.getString("name"),
                resultSet.getTimestamp("createdAt").toInstant()
        );
    }

}
