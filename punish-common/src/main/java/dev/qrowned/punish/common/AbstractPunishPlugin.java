package dev.qrowned.punish.common;

import dev.qrowned.chatlog.api.ChatLogApi;
import dev.qrowned.chatlog.api.ChatLogApiProvider;
import dev.qrowned.chatlog.api.log.ChatLog;
import dev.qrowned.punish.api.PunishApiProvider;
import dev.qrowned.punish.api.PunishPlugin;
import dev.qrowned.punish.api.config.ConfigProvider;
import dev.qrowned.punish.api.logger.PluginLogger;
import dev.qrowned.punish.api.platform.Platform;
import dev.qrowned.punish.common.amqp.CommonPubSubProvider;
import dev.qrowned.punish.common.config.CommonConfigProvider;
import dev.qrowned.punish.common.config.impl.LicenseConfig;
import dev.qrowned.punish.common.config.impl.MySqlConfig;
import dev.qrowned.punish.common.config.impl.PunishmentsConfig;
import dev.qrowned.punish.common.config.impl.RabbitMqConfig;
import dev.qrowned.punish.common.datasource.JsonConfigDataSource;
import dev.qrowned.punish.common.event.CommonEventHandler;
import dev.qrowned.punish.common.event.listener.NetworkPlayerJoinListener;
import dev.qrowned.punish.common.event.listener.NetworkPlayerQuitListener;
import dev.qrowned.punish.common.punish.CommonPunishmentHandler;
import dev.qrowned.punish.common.punish.PunishmentDataHandler;
import dev.qrowned.punish.common.user.AbstractPunishUserHandler;
import dev.qrowned.punish.common.user.PunishUserDataHandler;
import dev.qrowned.punish.common.util.DataTableCreationUtil;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Optional;

@Getter
public abstract class AbstractPunishPlugin implements PunishPlugin {

    protected static final String PUNISH_FOLDER_PATH = "./plugins/SupremePunish/";

    protected Platform platform;

    protected JsonConfigDataSource dataSource;
    protected CommonPubSubProvider pubSubProvider;
    protected CommonEventHandler eventHandler;
    protected SupremePunishApi supremePunishApi;
    protected AbstractPunishUserHandler userHandler;
    protected CommonPunishmentHandler punishmentHandler;

    protected PunishUserDataHandler punishUserDataHandler;
    protected PunishmentDataHandler punishmentDataHandler;

    protected ChatLogApi chatLogApi = null;

    protected final ConfigProvider configProvider = new CommonConfigProvider();

    public AbstractPunishPlugin(@NotNull Platform platform) {
        this.platform = platform;
    }

    public void load() {
        // register configs
        this.configProvider.registerConfig("license", new File(PUNISH_FOLDER_PATH + "license.json"), LicenseConfig.class);
        this.configProvider.registerConfig("mysql", new File(PUNISH_FOLDER_PATH + "mysql.json"), MySqlConfig.class);
        this.configProvider.registerConfig("rabbitMq", new File(PUNISH_FOLDER_PATH + "rabbitMq.json"), RabbitMqConfig.class);
        this.configProvider.registerConfig("punishments", new File(PUNISH_FOLDER_PATH + "punishments.json"), PunishmentsConfig.class);
    }

    public void enable() {
        // initialize chatlog addon
        if (this.isChatLogAvailable()) {
            this.getLogger().info("Recognized ChatLog Plugin is loaded! Activating chatlog addon...");
            this.chatLogApi = ChatLogApiProvider.get();
        }

        // initialize datasource and create tables
        this.dataSource = new JsonConfigDataSource(this.configProvider.getConfig("mysql", MySqlConfig.class));
        DataTableCreationUtil.createTables(this.dataSource);

        // initialize amqp and register listener
        this.pubSubProvider = new CommonPubSubProvider(this.configProvider.getConfig("rabbitMq", RabbitMqConfig.class), this.getLogger());

        this.registerHandler();

        // register with punish api
        this.supremePunishApi = new SupremePunishApi(this);
        PunishApiProvider.register(this.supremePunishApi);

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
        this.eventHandler.registerEventAdapter(
                new NetworkPlayerQuitListener(this, this.punishUserDataHandler),
                new NetworkPlayerJoinListener(this)
        );
    }

    public void registerHandler() {
        this.eventHandler = new CommonEventHandler(this.getLogger(), this.pubSubProvider);

        this.punishmentDataHandler = new PunishmentDataHandler(this.dataSource);
        this.punishmentHandler = new CommonPunishmentHandler(
                this.eventHandler,
                this,
                this.configProvider.getConfig("punishments", PunishmentsConfig.class),
                this.userHandler,
                this.getMetricsBase(),
                this.punishmentDataHandler
        );
    }

    public Optional<ChatLogApi> getChatLogApi() {
        return Optional.ofNullable(this.chatLogApi);
    }

}
