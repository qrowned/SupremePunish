package dev.qrowned.punish.api.punish;

import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@NoArgsConstructor
public class PunishmentReason implements Serializable {

    private String id;
    private String displayName;

    private Long minDuration;
    private Long maxDuration = -1L;
    private TimeUnit durationTimeUnit;
    private int durationMultiplier = 2;
    private Punishment.Type type;

    private String permission;
    private String[] aliases;

    public PunishmentReason(@NotNull String id,
                            @Nullable String displayName,
                            @Nullable Long minDuration,
                            @Nullable Long maxDuration,
                            @Nullable TimeUnit durationTimeUnit,
                            @NotNull Punishment.Type type,
                            @Nullable String permission,
                            @NotNull String... aliases) {
        this.id = id;
        this.displayName = displayName;
        this.minDuration = minDuration;
        this.maxDuration = maxDuration;
        this.durationTimeUnit = durationTimeUnit;
        this.type = type;
        this.permission = permission;
        this.aliases = aliases;
    }

    public PunishmentReason(@NotNull String id,
                            long duration,
                            @NotNull TimeUnit durationTimeUnit,
                            @NotNull Punishment.Type type) {
        this.id = id;
        this.minDuration = duration;
        this.durationTimeUnit = durationTimeUnit;
        this.type = type;
    }

    public String getId() {
        return this.id;
    }

    public String getDisplayName() {
        return this.displayName == null ? this.id : this.displayName;
    }

    public Punishment.Type getType() {
        return this.type;
    }

    public String getPermission() {
        return this.permission == null ? "" : this.permission;
    }

    public long getDuration() {
        return this.durationTimeUnit.toMillis(this.minDuration);
    }

    public long getMaxDuration() {
        return this.durationTimeUnit.toMillis(this.maxDuration);
    }

    public long getDuration(long previousPunishments) {
        if (this.minDuration == -1) return -1;
        long calculatedDuration = ((previousPunishments + 1) * this.durationMultiplier) * this.getDuration();
        return this.maxDuration == -1 || calculatedDuration < this.getMaxDuration() ? calculatedDuration : -1;
    }

    public boolean isConfigured() {
        return this.minDuration != null && this.durationTimeUnit != null && this.displayName != null;
    }

    public boolean checkAlias(@NotNull String input) {
        return this.id.equalsIgnoreCase(input) || Arrays.stream(this.aliases).anyMatch(s -> s.equalsIgnoreCase(input));
    }

    public List<String> getUsableIds() {
        ArrayList<String> strings = new ArrayList<>(Arrays.asList(this.aliases));
        strings.add(this.id);
        return strings;
    }

}
