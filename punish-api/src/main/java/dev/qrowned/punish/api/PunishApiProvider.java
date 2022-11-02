package dev.qrowned.punish.api;

import dev.qrowned.punish.api.exception.NotLoadedException;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

public final class PunishApiProvider {

    private static PunishApi instance = null;

    public static @NotNull PunishApi get() {
        PunishApi instance = PunishApiProvider.instance;
        if (instance == null)
            throw new NotLoadedException();
        return instance;
    }

    @ApiStatus.Internal
    public static void register(PunishApi instance) {
        PunishApiProvider.instance = instance;
    }

    @ApiStatus.Internal
    public static void unregister() {
        PunishApiProvider.instance = null;
    }

    @ApiStatus.Internal
    private PunishApiProvider() {
        throw new UnsupportedOperationException("This class cannot be instantiated.");
    }

}
