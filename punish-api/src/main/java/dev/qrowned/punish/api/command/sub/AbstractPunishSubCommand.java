package dev.qrowned.punish.api.command.sub;

import dev.qrowned.punish.api.command.annotation.SubCommand;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collections;
import java.util.List;

@Getter
@AllArgsConstructor
public abstract class AbstractPunishSubCommand<P> implements PunishSubCommand<P> {

    protected String subCommandUsage;
    protected String subCommandDescription;

    @Override
    public List<String> handleTabCompletion(P player, String cursor, int currentArg, String[] args) {
        return Collections.emptyList();
    }

    @Override
    public String getName() {
        SubCommand annotation = this.getClass().getAnnotation(SubCommand.class);
        if (annotation == null) {
            throw new UnsupportedOperationException("Can't fetch the name of the command because no @SubCommand annotation exists.");
        }
        return annotation.name();
    }

    @Override
    public String getPermission() {
        SubCommand annotation = this.getClass().getAnnotation(SubCommand.class);
        if (annotation == null) {
            throw new UnsupportedOperationException("Can't fetch the name of the command because no @SubCommand annotation exists.");
        }
        return annotation.permission();
    }

    @Override
    public String[] getAliases() {
        SubCommand annotation = this.getClass().getAnnotation(SubCommand.class);
        if (annotation == null) {
            throw new UnsupportedOperationException("Can't fetch the name of the command because no @SubCommand annotation exists.");
        }
        return annotation.aliases();
    }

}
