package dev.qrowned.punish.common.config.impl;

import dev.qrowned.punish.api.config.ConfigAdapter;
import dev.qrowned.punish.api.message.AbstractConfigMessage;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.List;

/**
 * Represents an abstract model for configured messages used in {@link AbstractMessageConfig}.
 *
 * @param <T> Model of the current class for {@link ConfigAdapter}.
 * @param <V> Model of the {@link AbstractMessageConfig} to use.
 */
@Getter
public abstract class AbstractMessageConfig<T extends AbstractMessageConfig<T, V>, V extends AbstractConfigMessage<?>> implements ConfigAdapter<T>, Serializable {

    protected String prefix;
    protected List<V> messages;

    @Override
    public void reload(@NotNull T config) {
        this.prefix = config.prefix;
        this.messages = config.messages;
    }

}
