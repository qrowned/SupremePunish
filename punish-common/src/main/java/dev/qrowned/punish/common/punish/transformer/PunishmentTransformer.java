package dev.qrowned.punish.common.punish.transformer;

import dev.qrowned.punish.api.database.DataTransformer;
import dev.qrowned.punish.api.punish.Punishment;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.util.UUID;

@RequiredArgsConstructor
public final class PunishmentTransformer implements DataTransformer<Punishment> {

    @Override
    @SneakyThrows
    public Punishment transform(@NotNull ResultSet resultSet) {
        return new Punishment(
                resultSet.getInt("id"),
                UUID.fromString(resultSet.getString("target")),
                UUID.fromString(resultSet.getString("executor")),
                Punishment.Type.valueOf(resultSet.getString("type")),
                resultSet.getTimestamp("executionTime").getTime(),
                resultSet.getTimestamp("duration").getTime(),
                resultSet.getString("reason"),
                UUID.fromString(resultSet.getString("pardonExecutor")),
                resultSet.getTimestamp("pardonExecutionTime").getTime(),
                resultSet.getString("pardonReason")
        );
    }

}
