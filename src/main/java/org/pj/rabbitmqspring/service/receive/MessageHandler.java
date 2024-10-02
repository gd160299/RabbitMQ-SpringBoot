package org.pj.rabbitmqspring.service.receive;

import org.springframework.amqp.core.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageHandler {

    private static final Logger logger = LoggerFactory.getLogger(MessageHandler.class);

    public void handleMessage(Message message) {
        byte[] body = message.getBody();
        String messageBody = new String(body);
        logger.info("Begin: Handling received message");
        logger.info("Received message: {}", messageBody);
        logger.info("End: Handling message completed");
    }
}
