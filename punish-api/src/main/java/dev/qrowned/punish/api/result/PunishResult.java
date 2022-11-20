package dev.qrowned.punish.api.result;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

@Getter
@AllArgsConstructor
public final class PunishResult<T> implements Serializable {

    private final T result;
    private final boolean success;
    private final String message;

    public PunishResult(@NotNull T result) {
        this.result = result;
        this.success = true;
        this.message = null;
    }

    public PunishResult(@NotNull String message) {
        this.result = null;
        this.success = false;
        this.message = message;
    }

}
