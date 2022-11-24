package dev.qrowned.punish.common.util;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public final class DurationFormatter {

    public static final String DHM_FORMAT = "dd' days' HH 'hours' mm 'minutes'";

    public static String format(long duration, @NotNull String format) {
        return DurationFormatUtils.formatDuration(duration, format);
    }

    public static String formatPunishDuration(long duration) {
        if (duration == -1) return "§4permanently";
        return format(duration, DHM_FORMAT);
    }

    public static long parseDuration(@NotNull String s) {
        try {
            if (s.equalsIgnoreCase("perma")) return -1;

            String timeUnit = Character.toString(s.charAt(s.length() - 1));
            int timeValue = Integer.parseInt(s.substring(0, s.length() - 1));

            return switch (timeUnit.toLowerCase()) {
                case "y" -> TimeUnit.DAYS.toMillis(365) * timeValue;
                case "d" -> TimeUnit.DAYS.toMillis(timeValue);
                case "h" -> TimeUnit.HOURS.toMillis(timeValue);
                case "m" -> TimeUnit.MINUTES.toMillis(timeValue);
                case "s" -> TimeUnit.SECONDS.toMillis(timeValue);
                default -> 0;
            };
        } catch (NumberFormatException exception) {
            return 0;
        }
    }

}
