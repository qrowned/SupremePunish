package dev.qrowned.punish.api.user;

import dev.qrowned.punish.api.PunishApiProvider;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
public abstract class AbstractPunishUser {

    public static UUID CONSOLE_UUID = new UUID(0, 0);

    protected final UUID uuid;
    protected String name;

    protected final Instant createdAt;

    public AbstractPunishUser(@NotNull UUID uuid,
                              @NotNull String name) {
        this.uuid = uuid;
        this.name = name;
        this.createdAt = Instant.now();
    }

    public abstract boolean hasPermission(@NotNull String permission);

    public boolean isOnline() {
        return PunishApiProvider.get().getPlatform().getUniqueConnections().contains(this.uuid);
    }

}
