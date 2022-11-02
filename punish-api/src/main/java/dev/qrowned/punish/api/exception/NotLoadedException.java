package dev.qrowned.punish.api.exception;

public class NotLoadedException extends IllegalStateException {

    private static final String MESSAGE = """
            The Punish API isn't loaded yet!
            This could be because:
              a) the SupremePunish plugin is not installed or it failed to enable
              b) the plugin in the stacktrace does not declare a dependency on SupremePunish
              c) the plugin in the stacktrace is retrieving the API before the plugin 'enable' phase
                 (call the #get method in onEnable, not the constructor!)
            """;

    public NotLoadedException() {
        super(MESSAGE);
    }

}
