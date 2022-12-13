package dev.qrowned.punish.velocity;

import dev.qrowned.punish.api.command.CommandHandler;
import dev.qrowned.punish.api.message.MessageHandler;
import dev.qrowned.punish.common.AbstractPunishPlugin;
import dev.qrowned.punish.velocity.bootstrap.PunishVelocityBootstrap;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
public final class PunishVelocityPlugin extends AbstractPunishPlugin {

    private final PunishVelocityBootstrap bootstrap;

    public PunishVelocityPlugin(@NotNull PunishVelocityBootstrap bootstrap) {
        super(new VelocityPlatform(bootstrap.getStartupTime()));
        this.bootstrap = bootstrap;
    }

    @Override
    public @NotNull CommandHandler<?> getCommandHandler() {
        return null;
    }

    @Override
    public @NotNull MessageHandler<?> getMessageHandler() {
        return null;
    }

    @Override
    protected void registerPlatformListener() {

    }

    @Override
    protected void registerCommands() {

    }
}
