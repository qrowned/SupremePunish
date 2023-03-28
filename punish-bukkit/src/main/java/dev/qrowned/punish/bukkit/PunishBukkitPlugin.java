package dev.qrowned.punish.bukkit;

import dev.qrowned.punish.bukkit.bootstrap.PunishBukkitBootstrap;
import org.jetbrains.annotations.NotNull;

public final class PunishBukkitPlugin {

    private final PunishBukkitBootstrap bootstrap;

    public PunishBukkitPlugin(@NotNull PunishBukkitBootstrap bootstrap) {
        this.bootstrap = bootstrap;
    }

}
