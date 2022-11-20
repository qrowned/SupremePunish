package dev.qrowned.punish.api.command.sub;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public interface PunishSubCommand<P> {

    void execute(P sender, String[] args);

    List<String> handleTabCompletion(P player, String cursor, int currentArg, String[] args);

    String getName();

    String getPermission();

    String getSubCommandUsage();

    String getSubCommandDescription();

    default String[] getAliases() {
        return new String[0];
    }

    default boolean checkName(@NotNull String name) {
        return this.getName().equalsIgnoreCase(name) || Arrays.stream(this.getAliases()).anyMatch(s -> s.equalsIgnoreCase(name));
    }

    UUID getUUID(P player);

}
