package dev.qrowned.punish.api.amqp.exception;

@FunctionalInterface
public interface ReturningExceptionRunnable<T> {

    T run() throws Throwable;

}
