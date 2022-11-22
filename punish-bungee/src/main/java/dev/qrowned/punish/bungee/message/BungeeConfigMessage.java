package dev.qrowned.punish.bungee.message;

import dev.qrowned.punish.api.message.AbstractConfigMessage;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class BungeeConfigMessage extends AbstractConfigMessage<CommandSender> {

    @Override
    public void send(@NotNull CommandSender receiver, @NotNull List<String> messages) {
        BaseComponent[] baseComponents = new BaseComponent[messages.size()];
        for (int i = 0; i < messages.size(); i++) {
            baseComponents[i] = new TextComponent(messages.get(i) + (i + 1 >= messages.size() ? "" : "\n"));
        }
        receiver.sendMessage(baseComponents);
    }

}
