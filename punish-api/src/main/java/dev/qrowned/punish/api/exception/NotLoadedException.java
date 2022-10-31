package dev.qrowned.punish.api.exception;

public class NotLoadedException extends IllegalStateException {

    private static final String MESSAGE = "The Punish API isn't loaded yet!\n" +
            "This could be because:\n" +
            "  a) the SupremePunish plugin is not installed or it failed to enable\n" +
            "  b) the plugin in the stacktrace does not declare a dependency on SupremePunish\n" +
            "  c) the plugin in the stacktrace is retrieving the API before the plugin 'enable' phase\n" +
            "     (call the #get method in onEnable, not the constructor!)\n";

    public NotLoadedException() {
        super(MESSAGE);
    }

}
