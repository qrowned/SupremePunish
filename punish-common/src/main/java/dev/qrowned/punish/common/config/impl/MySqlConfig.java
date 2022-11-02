package dev.qrowned.punish.common.config.impl;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@NoArgsConstructor
public final class MySqlConfig {

    private String hostname;
    private int port;
    private String database;
    private String username;
    private String password;

    private int minIdle;
    private int maxPoolSize;

}
