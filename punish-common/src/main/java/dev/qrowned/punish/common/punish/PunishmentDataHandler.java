package dev.qrowned.punish.common.punish;

import com.github.benmanes.caffeine.cache.Caffeine;
import dev.qrowned.punish.api.database.AbstractAllDataHandler;
import dev.qrowned.punish.api.database.AbstractDataSource;
import dev.qrowned.punish.api.punish.Punishment;
import dev.qrowned.punish.common.punish.transformer.PunishmentTransformer;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.*;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public final class PunishmentDataHandler extends AbstractAllDataHandler<UUID, Punishment> {

    private static final PunishmentTransformer PUNISHMENT_TRANSFORMER = new PunishmentTransformer();

    private static final String FETCH_STATEMENT = "select * from punishments where target = ?;";
    private static final String FETCH_ALL_STATEMENT = "select * from punishments;";
    private static final String INSERT_STATEMENT = "insert into punishments(type, target, executor, reason, evidence, executionTime, duration) values(?, ?, ?, ?, ?, ?, ?);";
    private static final String UPDATE_STATEMENT = "update punishments set pardonExecutionTime = current_timestamp, pardonExecutor = ?, pardonReason = ? where id = ?;";

    public PunishmentDataHandler(@NotNull AbstractDataSource abstractDataSource) {
        super(abstractDataSource, Caffeine.newBuilder()
                .expireAfterAccess(2, TimeUnit.MINUTES)
                .buildAsync((uuid, executor) -> {
                    PreparedStatement preparedStatement = abstractDataSource.prepare(FETCH_STATEMENT);
                    preparedStatement.setString(1, uuid.toString());
                    return abstractDataSource.queryAll(preparedStatement, PUNISHMENT_TRANSFORMER);
                }));
    }

    @Override
    @SneakyThrows
    public void updateData(@NotNull UUID uuid, @NotNull Punishment punishment) {
        PreparedStatement preparedStatement = super.abstractDataSource.prepare(UPDATE_STATEMENT);
        preparedStatement.setString(1, this.convertUUID(punishment.getPardonExecutor()));
        preparedStatement.setString(2, punishment.getPardonReason());
        preparedStatement.setInt(3, punishment.getId());
        super.abstractDataSource.update(preparedStatement);
    }

    @Override
    public void insertData(@NotNull UUID uuid, @NotNull Punishment punishment) {
        this.insertDataWithReturn(punishment);
    }

    public CompletableFuture<List<Punishment>> fetchAllPunishments() {
        PreparedStatement preparedStatement = super.abstractDataSource.prepare(FETCH_ALL_STATEMENT);
        return super.abstractDataSource.queryAll(preparedStatement, PUNISHMENT_TRANSFORMER);
    }

    @SneakyThrows
    public CompletableFuture<Punishment> insertDataWithReturn(@NotNull Punishment punishment) {
        PreparedStatement preparedStatement = super.abstractDataSource.getDataSource().getConnection().prepareStatement(INSERT_STATEMENT, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, punishment.getType().name());
        preparedStatement.setString(2, punishment.getTarget().toString());
        preparedStatement.setString(3, punishment.getExecutor().toString());
        preparedStatement.setString(4, punishment.getReason());
        preparedStatement.setString(5, punishment.getEvidence().orElse(null));
        preparedStatement.setTimestamp(6, Timestamp.from(Instant.ofEpochMilli(punishment.getExecutionTime())));
        preparedStatement.setLong(7, punishment.getDuration());

        return CompletableFuture.supplyAsync(() -> {
            try {
                preparedStatement.executeUpdate();

                try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                    if (resultSet.next()) {
                        punishment.setId(resultSet.getInt(1));
                        return punishment;
                    } else
                        throw new RuntimeException("Punishment creation failed. No punishment id found!");
                }

            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    preparedStatement.getConnection().close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            return null;
        });
    }

    private String convertUUID(@Nullable UUID uuid) {
        return uuid == null ? null : uuid.toString();
    }

}
