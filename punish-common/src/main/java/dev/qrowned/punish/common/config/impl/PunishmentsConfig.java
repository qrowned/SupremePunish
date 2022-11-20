package dev.qrowned.punish.common.config.impl;

import dev.qrowned.punish.api.punish.PunishmentReason;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

@Getter
@NoArgsConstructor
public final class PunishmentsConfig {

    private List<PunishmentReason> punishmentReasons;

    public Optional<PunishmentReason> getPunishmentReason(@NotNull String id) {
        return this.punishmentReasons.stream().filter(punishmentReason -> punishmentReason.getId().equals(id))
                .findFirst();
    }

}
