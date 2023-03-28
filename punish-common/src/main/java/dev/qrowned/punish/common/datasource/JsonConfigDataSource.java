package dev.qrowned.punish.common.datasource;

import dev.qrowned.punish.api.database.AbstractDataSource;
import dev.qrowned.punish.common.config.MySqlConfig;
import org.jetbrains.annotations.NotNull;

public class JsonConfigDataSource extends AbstractDataSource {

    public JsonConfigDataSource(@NotNull MySqlConfig mySqlConfig) {
        super(mySqlConfig.getHostname(),
                mySqlConfig.getPort(),
                mySqlConfig.getDatabase(),
                mySqlConfig.getUsername(),
                mySqlConfig.getPassword(),
                mySqlConfig.getMinIdle(),
                mySqlConfig.getMaxPoolSize());
    }

}
