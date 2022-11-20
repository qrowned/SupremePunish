package dev.qrowned.punish.api.command.sub;

import dev.qrowned.punish.api.command.annotation.SubCommand;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PunishSubCommandMapping<P> {

    private final PunishSubCommand<P> executor;
    private final SubCommand subCommandAnnotation;

}
