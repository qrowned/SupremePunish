package dev.qrowned.punish.api.command;

import dev.qrowned.punish.api.command.annotation.Command;
import dev.qrowned.punish.api.command.annotation.SubCommand;
import dev.qrowned.punish.api.command.sub.PunishSubCommand;
import dev.qrowned.punish.api.command.sub.PunishSubCommandMapping;

import java.util.*;
import java.util.stream.Collectors;

public abstract class AbstractPunishCommand<P> implements PunishCommand<P> {

    protected final List<PunishSubCommandMapping<P>> subCommandMappingList = new ArrayList<>();

    @Override
    @SafeVarargs
    public final void registerSubCommands(PunishSubCommand<P>... subCommandExecutor) {
        for (PunishSubCommand<P> subCommand : subCommandExecutor) {
            SubCommand subCommandAnnotation = subCommand.getClass().getAnnotation(SubCommand.class);

            if (subCommandAnnotation == null) {
                throw new UnsupportedOperationException("Can't register a sub command without @SubCommand annotation!");
            }

            this.subCommandMappingList.add(new PunishSubCommandMapping<>(subCommand, subCommandAnnotation));
        }
    }

    @Override
    public List<PunishSubCommand<P>> getSubCommandExecutors() {
        return this.subCommandMappingList.stream().map(PunishSubCommandMapping::getExecutor)
                .collect(Collectors.toList());
    }

    @Override
    public List<PunishSubCommandMapping<P>> getAnnotationMappings() {
        return this.subCommandMappingList.stream()
                .sorted(Comparator.comparingInt(value -> value.getSubCommandAnnotation().sortingId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<String> handleTabCompletion(P sender, String cursor, int currentArg, String[] args) {
        if (currentArg == 1) {
            Optional<PunishSubCommand<P>> subCommand = this.getSubCommandExecutors().stream()
                    .filter(pPunishSubCommand -> pPunishSubCommand.checkName(args[currentArg]))
                    .findFirst();
            return subCommand.isEmpty() ? Collections.emptyList() : subCommand.get().handleTabCompletion(sender, cursor, currentArg, args);
        }
        return currentArg == 0 ? this.getSubCommandsAsList() : Collections.emptyList();
    }

    @Override
    public String getName() {
        Command annotation = this.getClass().getAnnotation(Command.class);
        if (annotation == null) {
            throw new UnsupportedOperationException("Can't fetch the name of the command because no @Command annotation exists.");
        }
        return annotation.name();
    }

    @Override
    public String getPermission() {
        Command annotation = this.getClass().getAnnotation(Command.class);
        if (annotation == null) {
            throw new UnsupportedOperationException("Can't fetch the name of the command because no @Command annotation exists.");
        }
        return annotation.permission();
    }

    @Override
    public String[] getAliases() {
        Command annotation = this.getClass().getAnnotation(Command.class);
        if (annotation == null) {
            throw new UnsupportedOperationException("Can't fetch the name of the command because no @Command annotation exists.");
        }
        return annotation.aliases();
    }
}
