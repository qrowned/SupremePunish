package dev.qrowned.punish.api.amqp.listener;

import dev.qrowned.punish.api.amqp.io.BinaryReadBuffer;

public interface PubSubListener {

    void receive(BinaryReadBuffer binaryReadBuffer);

}
