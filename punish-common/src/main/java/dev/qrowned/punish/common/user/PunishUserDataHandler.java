package dev.qrowned.punish.common.user;

import com.github.benmanes.caffeine.cache.Caffeine;
import dev.qrowned.punish.api.database.AbstractDataHandler;
import dev.qrowned.punish.api.database.AbstractDataSource;
import dev.qrowned.punish.api.database.DataTransformer;
import dev.qrowned.punish.api.user.AbstractPunishUser;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public final class PunishUserDataHandler extends AbstractDataHandler<UUID, AbstractPunishUser> {

    private static final String FETCH_STATEMENT = "select * from punish_user where uuid = ?;";
    private static final String FETCH_NAME_STATEMENT = "select * from punish_user where name = ?;";
    private static final String INSERT_STATEMENT = "insert into punish_user(uuid, name, createdAt) values (?, ?, ?);";
    private static final String UPDATE_STATEMENT = "update punish_user set name = ? where uuid = ?;";

    private final DataTransformer<AbstractPunishUser> dataTransformer;

    public PunishUserDataHandler(@NotNull AbstractDataSource abstractDataSource,
                                 @NotNull DataTransformer<AbstractPunishUser> dataTransformer) {
        super(abstractDataSource, Caffeine.newBuilder()
                .expireAfterAccess(3, TimeUnit.MINUTES)
                .buildAsync((uuid, executor) -> {
                    PreparedStatement preparedStatement = abstractDataSource.prepare(FETCH_STATEMENT);
                    preparedStatement.setString(1, uuid.toString());
                    return abstractDataSource.query(preparedStatement, dataTransformer);
                })
        );

        this.dataTransformer = dataTransformer;
    }

    public CompletableFuture<AbstractPunishUser> getData(@NotNull String name) {
        Optional<AbstractPunishUser> cachedUser = super.asyncLoadingCache.synchronous().asMap().values().stream()
                .filter(punishUser -> punishUser.getName().equals(name))
                .findFirst();
        if (cachedUser.isPresent()) return CompletableFuture.completedFuture(cachedUser.get());

        PreparedStatement preparedStatement = super.abstractDataSource.prepare(FETCH_NAME_STATEMENT);
        try {
            preparedStatement.setString(1, name);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return super.abstractDataSource.query(preparedStatement, this.dataTransformer);
    }

    @Override
    @SneakyThrows
    public void updateData(@NotNull UUID uuid, @NotNull AbstractPunishUser abstractPunishUser) {
        this.invalidate(uuid);

        PreparedStatement preparedStatement = super.abstractDataSource.prepare(UPDATE_STATEMENT);
        preparedStatement.setString(1, abstractPunishUser.getName());
        preparedStatement.setString(2, uuid.toString());
        super.abstractDataSource.update(preparedStatement);
    }

    @Override
    @SneakyThrows
    public void insertData(@NotNull UUID uuid, @NotNull AbstractPunishUser abstractPunishUser) {
        this.invalidate(uuid);

        PreparedStatement preparedStatement = super.abstractDataSource.prepare(INSERT_STATEMENT);
        preparedStatement.setString(1, uuid.toString());
        preparedStatement.setString(2, abstractPunishUser.getName());
        preparedStatement.setTimestamp(3, Timestamp.from(abstractPunishUser.getCreatedAt()));
        super.abstractDataSource.update(preparedStatement);
    }
}
