package dev.qrowned.punish.common.util;

import dev.qrowned.punish.api.database.AbstractDataSource;
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

    public static void createTables(@NotNull AbstractDataSource abstractDataSource) {
        PreparedStatement punishUserStatement = abstractDataSource.prepare(PUNISH_USER_TABLE);
        abstractDataSource.update(punishUserStatement);
    }

}
