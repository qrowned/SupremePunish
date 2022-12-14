package dev.qrowned.punish.velocity.metrics;

import com.velocitypowered.api.plugin.PluginContainer;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.qrowned.punish.api.logger.PluginLogger;
import dev.qrowned.punish.api.metrics.MetricsCompact;
import lombok.Getter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public final class VelocityMetrics {

    private final PluginContainer pluginContainer;

    private final ProxyServer server;

    @Getter
    private MetricsCompact.MetricsBase metricsBase;

    public VelocityMetrics(
            Object plugin, ProxyServer server, PluginLogger logger, Path dataDirectory, int serviceId) {
        pluginContainer =
                server
                        .getPluginManager()
                        .fromInstance(plugin)
                        .orElseThrow(
                                () -> new IllegalArgumentException("The provided instance is not a plugin"));
        this.server = server;
        File configFile = dataDirectory.getParent().resolve("bStats").resolve("config.txt").toFile();
        MetricsCompact.MetricsConfig config;
        try {
            config = new MetricsCompact.MetricsConfig(configFile, true);
        } catch (IOException e) {
            logger.severe("Failed to create bStats config", e);
            return;
        }
        metricsBase =
                new MetricsCompact.MetricsBase(
                        "velocity",
                        config.getServerUUID(),
                        serviceId,
                        config.isEnabled(),
                        this::appendPlatformData,
                        this::appendServiceData,
                        task -> server.getScheduler().buildTask(plugin, task).schedule(),
                        () -> true,
                        logger::warn,
                        logger::info,
                        config.isLogErrorsEnabled(),
                        config.isLogSentDataEnabled(),
                        config.isLogResponseStatusTextEnabled());
        if (!config.didExistBefore()) {
            // Send an info message when the bStats config file gets created for the first time
            logger.info(
                    "Velocity and some of its plugins collect metrics and send them to bStats (https://bStats.org).");
            logger.info(
                    "bStats collects some basic information for plugin authors, like how many people use");
            logger.info(
                    "their plugin and their total player count. It's recommend to keep bStats enabled, but");
            logger.info(
                    "if you're not comfortable with this, you can opt-out by editing the config.txt file in");
            logger.info("the '/plugins/bStats/' folder and setting enabled to false.");
        }
    }

    /**
     * Adds a custom chart.
     *
     * @param chart The chart to add.
     */
    public void addCustomChart(MetricsCompact.CustomChart chart) {
        if (metricsBase != null) {
            metricsBase.addCustomChart(chart);
        }
    }

    private void appendPlatformData(MetricsCompact.JsonObjectBuilder builder) {
        builder.appendField("playerAmount", server.getPlayerCount());
        builder.appendField("managedServers", server.getAllServers().size());
        builder.appendField("onlineMode", server.getConfiguration().isOnlineMode() ? 1 : 0);
        builder.appendField("velocityVersionVersion", server.getVersion().getVersion());
        builder.appendField("velocityVersionName", server.getVersion().getName());
        builder.appendField("velocityVersionVendor", server.getVersion().getVendor());
        builder.appendField("javaVersion", System.getProperty("java.version"));
        builder.appendField("osName", System.getProperty("os.name"));
        builder.appendField("osArch", System.getProperty("os.arch"));
        builder.appendField("osVersion", System.getProperty("os.version"));
        builder.appendField("coreCount", Runtime.getRuntime().availableProcessors());
    }

    private void appendServiceData(MetricsCompact.JsonObjectBuilder builder) {
        builder.appendField(
                "pluginVersion", pluginContainer.getDescription().getVersion().orElse("unknown"));
    }

}
