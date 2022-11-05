package dev.qrowned.punish.common.user;

import com.github.benmanes.caffeine.cache.Caffeine;
import dev.qrowned.punish.api.database.AbstractDataHandler;
import dev.qrowned.punish.api.database.AbstractDataSource;
import dev.qrowned.punish.api.user.PunishUser;
import dev.qrowned.punish.common.user.transformer.PunishUserTransformer;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public final class PunishUserDataHandler extends AbstractDataHandler<UUID, PunishUser> {

    private static final PunishUserTransformer PUNISH_USER_TRANSFORMER = new PunishUserTransformer();

    private static final String FETCH_STATEMENT = "select * from punish_user where uuid = ?;";
    private static final String FETCH_NAME_STATEMENT = "select * from punish_user where name = ?;";
    private static final String INSERT_STATEMENT = "insert into punish_user(uuid, name, createdAt) values (?, ?, ?);";
    private static final String UPDATE_STATEMENT = "update punish_user set name = ? where uuid = ?;";

    public PunishUserDataHandler(@NotNull AbstractDataSource abstractDataSource) {
        super(abstractDataSource, Caffeine.newBuilder()
                .expireAfterAccess(3, TimeUnit.MINUTES)
                .buildAsync((uuid, executor) -> {
                    PreparedStatement preparedStatement = abstractDataSource.prepare(FETCH_STATEMENT);
                    preparedStatement.setString(1, uuid.toString());
                    return abstractDataSource.query(preparedStatement, PUNISH_USER_TRANSFORMER);
                })
        );
    }

    public CompletableFuture<PunishUser> getData(@NotNull String name) {
        Optional<PunishUser> cachedUser = super.asyncLoadingCache.synchronous().asMap().values().stream()
                .filter(punishUser -> punishUser.getName().equals(name))
                .findFirst();
        if (cachedUser.isPresent()) return CompletableFuture.completedFuture(cachedUser.get());

        PreparedStatement preparedStatement = super.abstractDataSource.prepare(FETCH_NAME_STATEMENT);
        try {
            preparedStatement.setString(1, name);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return super.abstractDataSource.query(preparedStatement, PUNISH_USER_TRANSFORMER);
    }

    @Override
    @SneakyThrows
    public void updateData(@NotNull UUID uuid, @NotNull PunishUser punishUser) {
        this.invalidate(uuid);

        PreparedStatement preparedStatement = super.abstractDataSource.prepare(UPDATE_STATEMENT);
        preparedStatement.setString(1, punishUser.getName());
        preparedStatement.setString(2, uuid.toString());
        super.abstractDataSource.update(preparedStatement);
    }

    @Override
    @SneakyThrows
    protected void insertData(@NotNull UUID uuid, @NotNull PunishUser punishUser) {
        this.invalidate(uuid);

        PreparedStatement preparedStatement = super.abstractDataSource.prepare(INSERT_STATEMENT);
        preparedStatement.setString(1, uuid.toString());
        preparedStatement.setString(2, punishUser.getName());
        preparedStatement.setTimestamp(3, Timestamp.from(punishUser.getCreatedAt()));
        super.abstractDataSource.update(preparedStatement);
    }
}
