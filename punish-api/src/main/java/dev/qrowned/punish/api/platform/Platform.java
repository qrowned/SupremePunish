package dev.qrowned.punish.api.platform;

import lombok.NonNull;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface Platform {

    @NotNull Platform.Type getType();

    @NonNull List<UUID> getUniqueConnections();

    @NonNull Instant getStartTime();

    enum Type {
        BUKKIT("Bukkit"),
        BUNGEECORD("BungeeCord"),
        VELOCITY("Velocity");

        private final String friendlyName;

        Type(String friendlyName) {
            this.friendlyName = friendlyName;
        }

        public @NonNull String getFriendlyName() {
            return this.friendlyName;
        }
    }

}
