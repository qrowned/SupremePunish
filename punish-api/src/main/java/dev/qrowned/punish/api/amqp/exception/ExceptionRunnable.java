package dev.qrowned.punish.api.amqp.exception;

@FunctionalInterface
public interface ExceptionRunnable {

    void run() throws Throwable;

}
