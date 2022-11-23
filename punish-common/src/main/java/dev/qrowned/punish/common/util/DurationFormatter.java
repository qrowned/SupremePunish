package dev.qrowned.punish.common.util;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.jetbrains.annotations.NotNull;

public final class DurationFormatter {

    public static final String DHM_FORMAT = "dd' days' HH 'hours' mm 'minutes'";

    public static String format(long duration, @NotNull String format) {
        return DurationFormatUtils.formatDuration(duration, format);
    }

    public static String formatPunishDuration(long duration) {
        if (duration == -1) return "§4permanently";
        return format(duration, DHM_FORMAT);
    }

}
