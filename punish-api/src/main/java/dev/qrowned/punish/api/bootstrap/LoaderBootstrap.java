package dev.qrowned.punish.api.bootstrap;

public interface LoaderBootstrap {

    void onLoad();

    default void onEnable() {
    }

    default void onDisable() {
    }

}
