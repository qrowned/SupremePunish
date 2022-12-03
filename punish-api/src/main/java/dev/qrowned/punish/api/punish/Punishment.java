package dev.qrowned.punish.api.punish;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class Punishment implements Serializable {

    private Integer id;
    private final UUID target;
    private final UUID executor;

    private final Type type;
    private final Long executionTime;
    private final Long duration;
    private final String reason;

    private UUID pardonExecutor;
    private Long pardonExecutionTime;
    private String pardonReason;

    public Punishment(@NotNull UUID target,
                      @NotNull UUID executor,
                      @NotNull PunishmentReason punishmentReason) {
        this(target, executor, punishmentReason.getType(), punishmentReason.getDuration(), punishmentReason.getId());
    }

    public Punishment(@NotNull UUID target,
                      @NotNull UUID executor,
                      @NotNull Type type,
                      @NotNull Long duration,
                      @NotNull String reason) {
        this.target = target;
        this.executor = executor;
        this.type = type;
        this.duration = duration;
        this.reason = reason;
        this.executionTime = System.currentTimeMillis();
    }

    public boolean isActive() {
        if (this.isPardon()) return false;
        return this.getRemainingDuration() == -1 || this.getRemainingDuration() > 0;
    }

    public long getRemainingDuration() {
        if (this.duration == -1) return -1;
        return (this.executionTime + this.duration) - System.currentTimeMillis();
    }

    public boolean isPardon() {
        return this.pardonExecutor != null;
    }

    public enum Type {
        BAN, MUTE, KICK
    }

}
