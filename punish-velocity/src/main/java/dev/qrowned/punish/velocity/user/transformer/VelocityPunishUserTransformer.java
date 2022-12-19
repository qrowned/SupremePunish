package dev.qrowned.punish.velocity.user.transformer;

import dev.qrowned.punish.api.database.DataTransformer;
import dev.qrowned.punish.api.user.AbstractPunishUser;
import dev.qrowned.punish.velocity.user.VelocityPunishUser;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.util.UUID;

public final class VelocityPunishUserTransformer implements DataTransformer<AbstractPunishUser> {

    @Override
    @SneakyThrows
    public VelocityPunishUser transform(@NotNull ResultSet resultSet) {
        return new VelocityPunishUser(
                UUID.fromString(resultSet.getString("uuid")),
                resultSet.getString("name"),
                resultSet.getTimestamp("createdAt").toInstant()
        );
    }

}
