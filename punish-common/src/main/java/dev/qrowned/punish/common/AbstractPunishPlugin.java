package dev.qrowned.punish.common;

import dev.qrowned.punish.api.PunishApiProvider;
import dev.qrowned.punish.api.PunishPlugin;
import dev.qrowned.punish.api.database.AbstractDataSource;
import lombok.Getter;

@Getter
public abstract class AbstractPunishPlugin implements PunishPlugin {

    private AbstractDataSource dataSource;
    private SupremePunishApi supremePunishApi;

    public void load() {
        // TODO: 01.11.2022 Implement data source
    }

    public void enable() {
        // register with punish api
        this.supremePunishApi = new SupremePunishApi(this);
        PunishApiProvider.register(this.supremePunishApi);
    }

}
