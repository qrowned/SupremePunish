package dev.qrowned.punish.common;

import dev.qrowned.config.api.ConfigService;
import dev.qrowned.config.message.api.MessageService;
import dev.qrowned.punish.api.PunishApi;
import dev.qrowned.punish.api.PunishPlugin;
import dev.qrowned.punish.api.amqp.PubSubProvider;
import dev.qrowned.punish.api.command.CommandHandler;
import dev.qrowned.punish.api.event.EventHandler;
import dev.qrowned.punish.api.platform.Platform;
import dev.qrowned.punish.api.punish.PunishmentHandler;
import dev.qrowned.punish.api.user.PunishUserHandler;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@Getter
public class SupremePunishApi implements PunishApi {

    private final PunishPlugin plugin;

    private final PunishUserHandler userHandler;
    private final EventHandler eventHandler;
    private final PubSubProvider pubSubProvider;
    private final PunishmentHandler punishmentHandler;
    private final CommandHandler<?> commandHandler;
    private final MessageService<?> messageService;

    public SupremePunishApi(@NotNull PunishPlugin plugin) {
        this.plugin = plugin;

        this.userHandler = plugin.getUserHandler();
        this.eventHandler = plugin.getEventHandler();
        this.pubSubProvider = plugin.getPubSubProvider();
        this.punishmentHandler = plugin.getPunishmentHandler();
        this.commandHandler = plugin.getCommandHandler();
        this.messageService = plugin.getMessageService();
    }

    @Override
    public @NotNull Platform getPlatform() {
        return this.plugin.getPlatform();
    }

    @Override
    public @NotNull String getServerName() {
        return Objects.requireNonNull(this.plugin.getBootstrap().getServerName());
    }

    @Override
    public @NotNull ConfigService getConfigService() {
        return this.plugin.getConfigService();
    }

}
