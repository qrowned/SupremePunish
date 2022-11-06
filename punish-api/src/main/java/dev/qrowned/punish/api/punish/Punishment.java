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
        this.target = target;
        this.executor = executor;
        this.type = punishmentReason.getType();
        this.executionTime = System.currentTimeMillis();
        this.duration = punishmentReason.getDuration();
        this.reason = punishmentReason.getId();
    }

    public boolean isActive() {
        if (this.isPardon()) return false;
        return System.currentTimeMillis() <= (this.executionTime + this.duration);
    }

    public boolean isPardon() {
        return this.pardonExecutor != null;
    }

    public enum Type {
        BAN, MUTE, KICK
    }

}
