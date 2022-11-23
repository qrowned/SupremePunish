package dev.qrowned.punish.api.message;

import dev.qrowned.punish.api.PunishApiProvider;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents an abstract model for a config message on a certain platform.
 *
 * @param <P> Model for sending messages to.
 */
@Getter
public abstract class AbstractConfigMessage<P> implements Serializable {

    protected String id;
    protected String[] message;

    protected abstract void send(@NotNull P receiver, @NotNull List<String> messages);

    public void send(@NotNull P receiver, String... format) {
        this.send(receiver, this.formatMessage(format));
    }

    public abstract void broadcast(@NotNull String permission, @NotNull String... format);

    public List<String> formatMessage(String... format) {
        List<String> strings = new ArrayList<>(Arrays.asList(format));
        strings.addAll(Arrays.asList(
                "%prefix%",
                PunishApiProvider.get().getMessageHandler().getPrefix()
        ));

        if (strings.size() % 2 != 0)
            throw new UnsupportedOperationException("String[] for formatting needs an even length!");

        List<String> messages = new ArrayList<>();
        for (String content : this.message) {
            for (int i = 0; i < strings.size(); i++) {
                content = content.replace(strings.get(i), strings.get(i + 1));
                i++;
            }
            messages.add(content);
        }
        return messages;
    }

}
