package org.pj.rabbitmqspring.service.receive;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Service;

@Service
public class Consumer {

    private final RabbitAdmin rabbitAdmin;

    private final ConnectionFactory connectionFactory;

    private static final Logger logger = LoggerFactory.getLogger(Consumer.class);

    public Consumer(RabbitAdmin rabbitAdmin, ConnectionFactory connectionFactory) {
        this.rabbitAdmin = rabbitAdmin;
        this.connectionFactory = connectionFactory;
    }

    public void receiveMessage(String queueName, Object messageListener) {
        logger.info("Begin: Preparing to receive message from queue: {}", queueName);
        Queue queue = new Queue(queueName, true);
        rabbitAdmin.declareQueue(queue);
        // Tạo SimpleMessageListenerContainer để lắng nghe từ queue
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
        container.setQueueNames(queueName);

        MessageListenerAdapter adapter = new MessageListenerAdapter(messageListener);
        adapter.setDefaultListenerMethod("handleMessage");
        adapter.setMessageConverter(null);
        container.setMessageListener(adapter);
        container.setErrorHandler(error -> logger.error("Error occurred in listener"));
        container.start();
        logger.info("End: Message listener started for queue: {}", queueName);
    }
}
