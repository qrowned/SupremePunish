package dev.qrowned.punish.common.user.transformer;

import dev.qrowned.punish.api.database.DataTransformer;
import dev.qrowned.punish.api.user.PunishUser;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.util.UUID;

public final class PunishUserTransformer implements DataTransformer<PunishUser> {

    @Override
    @SneakyThrows
    public PunishUser transform(@NotNull ResultSet resultSet) {
        return new PunishUser(
                UUID.fromString(resultSet.getString("uuid")),
                resultSet.getString("name"),
                resultSet.getTimestamp("createdAt").toInstant()
        );
    }

}
