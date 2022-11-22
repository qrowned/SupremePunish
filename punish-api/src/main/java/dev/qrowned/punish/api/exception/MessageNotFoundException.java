package dev.qrowned.punish.api.exception;

public class MessageNotFoundException extends IllegalArgumentException {

    public MessageNotFoundException(String id) {
        super("Cannot find message with the ID " + id + ". Please configure it in messages.json.");
    }
}
