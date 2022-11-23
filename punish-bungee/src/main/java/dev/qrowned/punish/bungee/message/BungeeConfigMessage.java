package dev.qrowned.punish.bungee.message;

import dev.qrowned.punish.api.message.AbstractConfigMessage;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class BungeeConfigMessage extends AbstractConfigMessage<CommandSender> {

    @Override
    protected void send(@NotNull CommandSender receiver, @NotNull List<String> messages) {
        receiver.sendMessage(this.parseBaseComponent(messages));
    }

    @Override
    public void broadcast(@NotNull String permission, @NotNull String... format) {
        try {
            ProxyServer.getInstance().getPlayers().stream()
                    .filter(proxiedPlayer -> proxiedPlayer.hasPermission(permission))
                    .forEach(proxiedPlayer -> this.send(proxiedPlayer, format));
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    public BaseComponent[] parseBaseComponent(String... format) {
        return this.parseBaseComponent(super.formatMessage(format));
    }

    public BaseComponent[] parseBaseComponent(List<String> formattedMessages) {
        BaseComponent[] baseComponents = new BaseComponent[formattedMessages.size()];
        for (int i = 0; i < formattedMessages.size(); i++) {
            baseComponents[i] = new TextComponent(formattedMessages.get(i) + (i + 1 >= formattedMessages.size() ? "" : "\n"));
        }
        return baseComponents;
    }

}
