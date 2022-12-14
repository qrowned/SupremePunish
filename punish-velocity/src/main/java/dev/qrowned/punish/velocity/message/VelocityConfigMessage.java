package dev.qrowned.punish.velocity.message;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.qrowned.punish.api.message.AbstractConfigMessage;
import dev.qrowned.punish.velocity.PunishVelocityPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class VelocityConfigMessage extends AbstractConfigMessage<CommandSource> {

    @Override
    protected void send(@NotNull CommandSource receiver, @NotNull List<String> messages) {
        receiver.sendMessage(this.parseComponent(messages));
    }

    @Override
    public void broadcast(@NotNull String permission, @NotNull String... format) {
        try {
            PunishVelocityPlugin.getServer().getAllPlayers().stream()
                    .filter(player -> player.hasPermission(permission))
                    .forEach(player -> this.send(player, format));
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    public Component parseComponent(String... format) {
        return this.parseComponent(super.formatMessage(format));
    }

    public Component parseComponent(List<String> formattedMessages) {
        if (formattedMessages.size() == 0) return Component.empty();
        TextComponent component = Component.text(formattedMessages.get(0));
        for (int i = 1; i < formattedMessages.size(); i++) {
            component = component.append(Component.text("\n" + formattedMessages.get(i)));
        }
        return component;
    }

}
