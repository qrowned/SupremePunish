package dev.qrowned.punish.common.config.impl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public final class MySqlConfig {

    private final String hostname;
    private final int port;
    private final String database;
    private final String username;
    private final String password;

    private final int minIdle;
    private final int maxPoolSize;

}
