package dev.qrowned.punish.common;

import dev.qrowned.punish.api.PunishApiProvider;
import dev.qrowned.punish.api.PunishPlugin;
import dev.qrowned.punish.api.config.ConfigProvider;
import dev.qrowned.punish.api.database.AbstractDataSource;
import dev.qrowned.punish.api.logger.PluginLogger;
import dev.qrowned.punish.api.user.PunishUserHandler;
import dev.qrowned.punish.common.config.CommonConfigProvider;
import dev.qrowned.punish.common.config.impl.LicenseConfig;
import dev.qrowned.punish.common.config.impl.MySqlConfig;
import dev.qrowned.punish.common.datasource.JsonConfigDataSource;
import dev.qrowned.punish.common.user.CommonPunishUserHandler;
import dev.qrowned.punish.common.user.PunishUserDataHandler;
import dev.qrowned.punish.common.util.DataTableCreationUtil;
import lombok.Getter;

import java.io.File;

@Getter
public abstract class AbstractPunishPlugin implements PunishPlugin {

    private static final String PUNISH_FOLDER_PATH = "./plugins/SupremePunish/";

    private AbstractDataSource dataSource;
    private SupremePunishApi supremePunishApi;
    private PunishUserHandler userHandler;

    private final ConfigProvider configProvider = new CommonConfigProvider();

    public void load() {
        // register configs
        this.configProvider.registerConfig("license", new File(PUNISH_FOLDER_PATH + "license.json"), LicenseConfig.class);
        this.configProvider.registerConfig("mysql", new File(PUNISH_FOLDER_PATH + "mysql.json"), MySqlConfig.class);
    }

    public void enable() {
        // register with punish api
        this.supremePunishApi = new SupremePunishApi(this);
        PunishApiProvider.register(this.supremePunishApi);

        // register datasource
        this.dataSource = new JsonConfigDataSource(this.configProvider.getConfig("mysql", MySqlConfig.class));

        // create database tables
        DataTableCreationUtil.createTables(this.dataSource);

        // initialize handler
        this.userHandler = new CommonPunishUserHandler(new PunishUserDataHandler(this.dataSource));

        this.registerListener();
        this.registerCommands();
    }

    @Override
    public PluginLogger getLogger() {
        return this.getBootstrap().getPluginLogger();
    }

    protected abstract void registerListener();
    protected abstract void registerCommands();

}
