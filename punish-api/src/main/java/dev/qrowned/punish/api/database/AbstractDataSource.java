package dev.qrowned.punish.api.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Getter
public abstract class AbstractDataSource {

    private final HikariDataSource dataSource;

    @SneakyThrows
    public AbstractDataSource(@NotNull String address,
                              int port,
                              @NotNull String database,
                              @NotNull String username,
                              @NotNull String password,
                              int minIdle,
                              int maxPoolSize) {
        Class.forName("com.mysql.cj.jdbc.Driver");

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://" + address + ":" + port + "/" + database);
        config.setUsername(username);
        config.setPassword(password);
        config.setMinimumIdle(minIdle);
        config.setMaximumPoolSize(maxPoolSize);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.addDataSourceProperty("allowMultiQueries", "true");
        this.dataSource = new HikariDataSource(config);
    }

    @SneakyThrows
    public PreparedStatement prepare(@NotNull String statement) {
        return this.dataSource.getConnection().prepareStatement(statement);
    }

    public void update(@NotNull PreparedStatement preparedStatement) {
        CompletableFuture.runAsync(() -> {
            try {
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    preparedStatement.getConnection().close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public CompletableFuture<ResultSet> query(@NotNull PreparedStatement preparedStatement) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return preparedStatement.executeQuery();
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
        });
    }

    public <T> CompletableFuture<T> query(@NotNull PreparedStatement preparedStatement, @NotNull DataTransformer<T> dataTransformer) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) return dataTransformer.transform(resultSet);
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

    public <T> CompletableFuture<List<T>> queryAll(@NotNull PreparedStatement preparedStatement, @NotNull DataTransformer<T> dataTransformer) {
        return CompletableFuture.supplyAsync(() -> {
            List<T> list = new ArrayList<>();
            try {
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next())
                    list.add(dataTransformer.transform(resultSet));
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    preparedStatement.getConnection().close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            return list;
        });
    }

    public <T> T execute(ConnectionCallback<T> callback) {
        try (Connection conn = this.dataSource.getConnection()) {
            return callback.doInConnection(conn);
        } catch (SQLException e) {
            throw new IllegalStateException("Error during execution.", e);
        }
    }

    public interface ConnectionCallback<T> {
        @SneakyThrows
        T doInConnection(Connection conn) throws SQLException;
    }

}
