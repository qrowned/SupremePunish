package dev.qrowned.punish.api.util;

import dev.qrowned.license.api.data.LicenseData;
import dev.qrowned.license.client.LicenseHandler;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.UUID;

public final class LicenseCheckerUtil {

    private static final LicenseHandler LICENSE_HANDLER = new LicenseHandler(UUID.fromString("41adc24e-e5db-4357-8fe5-79bb7f50e69a"));

    public static LicenseData checkLicense(@NotNull UUID key) throws IOException {
        LicenseData licenseData = LICENSE_HANDLER.authenticate(key);
        return licenseData;
    }

}
