package dev.qrowned.punish.api.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
public class PunishUser {

    private final UUID uuid;
    private String name;

    private final Instant createdAt;

    public PunishUser(@NotNull UUID uuid, @NotNull String name) {
        this.uuid = uuid;
        this.name = name;
        this.createdAt = Instant.now();
    }

}
