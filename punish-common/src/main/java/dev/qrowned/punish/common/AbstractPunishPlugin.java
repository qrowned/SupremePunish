package dev.qrowned.punish.common;

import dev.qrowned.punish.api.PunishApiProvider;
import dev.qrowned.punish.api.PunishPlugin;
import dev.qrowned.punish.api.config.ConfigProvider;
import dev.qrowned.punish.api.logger.PluginLogger;
import dev.qrowned.punish.common.amqp.CommonPubSubProvider;
import dev.qrowned.punish.common.config.CommonConfigProvider;
import dev.qrowned.punish.common.config.impl.LicenseConfig;
import dev.qrowned.punish.common.config.impl.MySqlConfig;
import dev.qrowned.punish.common.config.impl.PunishmentsConfig;
import dev.qrowned.punish.common.config.impl.RabbitMqConfig;
import dev.qrowned.punish.common.datasource.JsonConfigDataSource;
import dev.qrowned.punish.common.event.CommonEventHandler;
import dev.qrowned.punish.common.event.listener.AbstractPunishListener;
import dev.qrowned.punish.common.punish.CommonPunishmentHandler;
import dev.qrowned.punish.common.punish.PunishmentDataHandler;
import dev.qrowned.punish.common.user.CommonPunishUserHandler;
import dev.qrowned.punish.common.user.PunishUserDataHandler;
import dev.qrowned.punish.common.util.DataTableCreationUtil;
import lombok.Getter;

import java.io.File;

@Getter
public abstract class AbstractPunishPlugin implements PunishPlugin {

    protected static final String PUNISH_FOLDER_PATH = "./plugins/SupremePunish/";

    protected JsonConfigDataSource dataSource;
    protected CommonPubSubProvider pubSubProvider;
    protected CommonEventHandler eventHandler;
    protected SupremePunishApi supremePunishApi;
    protected CommonPunishUserHandler userHandler;
    protected CommonPunishmentHandler punishmentHandler;

    protected PunishUserDataHandler punishUserDataHandler;
    protected PunishmentDataHandler punishmentDataHandler;

    protected final ConfigProvider configProvider = new CommonConfigProvider();

    public void load() {
        // register configs
        this.configProvider.registerConfig("license", new File(PUNISH_FOLDER_PATH + "license.json"), LicenseConfig.class);
        this.configProvider.registerConfig("mysql", new File(PUNISH_FOLDER_PATH + "mysql.json"), MySqlConfig.class);
        this.configProvider.registerConfig("rabbitMq", new File(PUNISH_FOLDER_PATH + "rabbitMq.json"), RabbitMqConfig.class);
        this.configProvider.registerConfig("punishments", new File(PUNISH_FOLDER_PATH + "punishments.json"), PunishmentsConfig.class);
    }

    public void enable() {
        // register with punish api
        this.supremePunishApi = new SupremePunishApi(this);
        PunishApiProvider.register(this.supremePunishApi);

        // initialize datasource and create tables
        this.dataSource = new JsonConfigDataSource(this.configProvider.getConfig("mysql", MySqlConfig.class));
        DataTableCreationUtil.createTables(this.dataSource);

        // initialize amqp and register listener
        this.pubSubProvider = new CommonPubSubProvider(this.configProvider.getConfig("rabbitMq", RabbitMqConfig.class), this.getLogger());

        // initialize handler
        this.punishUserDataHandler = new PunishUserDataHandler(this.dataSource);
        this.userHandler = new CommonPunishUserHandler(this.punishUserDataHandler);

        this.eventHandler = new CommonEventHandler(this.getLogger(), this.pubSubProvider);

        this.punishUserDataHandler = new PunishUserDataHandler(this.dataSource);
        this.punishmentHandler = new CommonPunishmentHandler(
                this.eventHandler,
                this.configProvider.getConfig("punishments", PunishmentsConfig.class),
                new PunishmentDataHandler(this.dataSource)
        );

        this.registerPubSubListener();
        this.registerPluginListener();

        this.registerPlatformListener();
        this.registerCommands();
    }

    @Override
    public PluginLogger getLogger() {
        return this.getBootstrap().getPluginLogger();
    }

    protected abstract void registerPlatformListener();

    protected abstract void registerCommands();

    public void registerPubSubListener() {
        this.pubSubProvider.registerListener(this.eventHandler);
    }

    public void registerPluginListener() {
    }

}
