package dev.qrowned.punish.common.config;

import dev.qrowned.config.api.ConfigAdapter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Getter
@NoArgsConstructor
public final class LicenseConfig implements ConfigAdapter<LicenseConfig> {

    private UUID license;

    @Override
    public void reload(@NotNull LicenseConfig config) {
        this.license = config.getLicense();
    }

}
