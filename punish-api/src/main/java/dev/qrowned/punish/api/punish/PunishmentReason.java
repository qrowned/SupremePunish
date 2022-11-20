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

    private Long duration;
    private TimeUnit durationTimeUnit;
    private Punishment.Type type;

    private String permission;
    private String[] aliases;

    public PunishmentReason(@NotNull String id,
                            @Nullable String displayName,
                            @Nullable Long duration,
                            @Nullable TimeUnit durationTimeUnit,
                            @NotNull Punishment.Type type,
                            @Nullable String permission,
                            @NotNull String... aliases) {
        this.id = id;
        this.displayName = displayName;
        this.duration = duration;
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
        this.duration = duration;
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
        return this.durationTimeUnit.toMillis(this.duration);
    }

    public boolean isConfigured() {
        return this.duration != null && this.durationTimeUnit != null && this.displayName != null;
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
