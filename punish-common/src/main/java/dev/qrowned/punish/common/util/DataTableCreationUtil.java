package dev.qrowned.punish.common.util;

import dev.qrowned.punish.api.database.AbstractDataSource;
import dev.qrowned.punish.api.user.AbstractPunishUser;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;

public final class DataTableCreationUtil {

    private static final String PUNISH_USER_TABLE = "create table if not exists punish_user " +
            "( " +
            "    id        int auto_increment " +
            "        primary key, " +
            "    uuid      varchar(36)                           not null, " +
            "    name      varchar(16)                           not null, " +
            "    createdAt timestamp default current_timestamp() not null, " +
            "    constraint punish_user_name_uindex " +
            "        unique (name), " +
            "    constraint punish_user_uuid_uindex " +
            "        unique (uuid) " +
            "); ";
    private static final String PUNISHMENTS_TABLE = "create table if not exists punishments " +
            "( " +
            "    id                  int auto_increment " +
            "        primary key, " +
            "    type                varchar(16)                           not null, " +
            "    target              varchar(36)                           not null, " +
            "    executor            varchar(36)                           not null, " +
            "    reason              varchar(50)                           not null, " +
            "    executionTime       timestamp default current_timestamp() not null, " +
            "    duration            bigint                                not null, " +
            "    pardonExecutor      varchar(36)                           null, " +
            "    pardonReason        varchar(50)                           null, " +
            "    pardonExecutionTime timestamp                             null " +
            ");";

    @SneakyThrows
    public static void createTables(@NotNull AbstractDataSource abstractDataSource) {
        PreparedStatement punishUserStatement = abstractDataSource.prepare(PUNISH_USER_TABLE);
        abstractDataSource.update(punishUserStatement);

        PreparedStatement consoleUserStatement = abstractDataSource.prepare("insert into punish_user(uuid, name) values (?, ?) on duplicate key update name = name;");
        consoleUserStatement.setString(1, AbstractPunishUser.CONSOLE_UUID.toString());
        consoleUserStatement.setString(2, "ConsoleAdmin");
        abstractDataSource.update(consoleUserStatement);

        PreparedStatement punishmentsStatement = abstractDataSource.prepare(PUNISHMENTS_TABLE);
        abstractDataSource.update(punishmentsStatement);
    }

}
