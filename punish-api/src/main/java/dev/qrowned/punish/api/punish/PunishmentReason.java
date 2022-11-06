package dev.qrowned.punish.api.punish;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@NoArgsConstructor
public class PunishmentReason implements Serializable {

    private String id;
    private String displayName;

    private Long duration;
    private TimeUnit durationTimeUnit;
    private Punishment.Type type;

    private String[] aliases;

    public PunishmentReason(@NotNull String id,
                            @Nullable String displayName,
                            @Nullable Long duration,
                            @Nullable TimeUnit durationTimeUnit,
                            @NotNull Punishment.Type type,
                            @NotNull String... aliases) {
        this.id = id;
        this.displayName = displayName;
        this.duration = duration;
        this.durationTimeUnit = durationTimeUnit;
        this.type = type;
        this.aliases = aliases;
    }

    public String getId() {
        return this.id;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public Punishment.Type getType() {
        return this.type;
    }

    public long getDuration() {
        if (!this.isConfigured()) return -1;
        return this.durationTimeUnit.toMillis(this.duration);
    }

    public boolean isConfigured() {
        return this.duration != null && this.durationTimeUnit != null && this.displayName != null;
    }

    public boolean checkAlias(@NotNull String input) {
        return this.id.equalsIgnoreCase(input) || Arrays.asList(this.aliases).contains(input);
    }

}
