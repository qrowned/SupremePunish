package dev.qrowned.punish.bungee;

import dev.qrowned.punish.api.platform.Platform;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public final class BungeeCordPlatform implements Platform {

    private final Instant startTime;

    private final List<UUID> uniqueConnections = new ArrayList<>();

    public BungeeCordPlatform(@NotNull Instant startTime) {
        this.startTime = startTime;
    }

    @Override
    public @NotNull Platform.Type getType() {
        return Type.BUNGEECORD;
    }

}
