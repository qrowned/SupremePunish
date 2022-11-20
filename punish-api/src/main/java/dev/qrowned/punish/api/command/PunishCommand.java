package dev.qrowned.punish.api.command;

import dev.qrowned.punish.api.command.sub.PunishSubCommand;
import dev.qrowned.punish.api.command.sub.PunishSubCommandMapping;

import java.util.*;

public interface PunishCommand<P> {

    void handleNoSubCommandFound(P sender, String[] args);

    List<String> handleTabCompletion(P sender, String cursor, int currentArg, String[] args);

    default void handleException(P sender, Throwable throwable, Optional<PunishSubCommand<P>> subCommandExecutor) {
        throwable.printStackTrace();
    }

    default boolean handleNoPermission(P sender, String permission, Optional<PunishSubCommand<P>> subCommandExecutor) {
        return false;
    }

    void registerSubCommands(PunishSubCommand<P>... subCommandExecutor);

    List<PunishSubCommandMapping<P>> getAnnotationMappings();

    List<PunishSubCommand<P>> getSubCommandExecutors();

    default List<String> getSubCommandsAsList() {
        List<String> suggestions = new ArrayList<>();

        this.getSubCommandExecutors().forEach(proxiedPlayerISubCommandExecutor -> {
            suggestions.add(proxiedPlayerISubCommandExecutor.getName());
            suggestions.addAll(Arrays.asList(proxiedPlayerISubCommandExecutor.getAliases()));
        });

        return suggestions;
    }

    void execute(P sender, String[] args);

    String getName();

    String getPermission();

    boolean hasPermission(P player);

    UUID getUUID(P player);

    default String[] getAliases() {
        return new String[0];
    }

}
