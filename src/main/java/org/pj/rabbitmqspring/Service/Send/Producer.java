package org.pj.rabbitmqspring.Service.Send;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class Producer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitAdmin rabbitAdmin;

    private static final Logger logger = LoggerFactory.getLogger(Producer.class);

    public void sendMessageWithRoutingKey(String exchangeName, String exchangeType, String queueName, String routingKey, String message) {
        logger.info("Begin: Preparing to send message to queue: {} with routing key: {}", queueName, routingKey);
        Queue queue = new Queue(queueName, false);
        Exchange exchange = createExchange(exchangeName, exchangeType);

        rabbitAdmin.declareQueue(queue);
        rabbitAdmin.declareExchange(exchange);
        rabbitAdmin.declareBinding(BindingBuilder.bind(queue).to(exchange).with(routingKey).noargs());

        rabbitTemplate.convertAndSend(exchangeName, routingKey, message);
        logger.info("End: Message sent to exchange: {} with routing key: {}", exchangeName, routingKey);
    }

    public void sendMessageWithHeaders(String exchangeName, String queueName, Map<String, Object> headers, String message) {
        logger.info("Begin: Preparing to send message to queue: {} with headers: {}", queueName, headers.size());
        Queue queue = new Queue(queueName, false);
        HeadersExchange exchange = new HeadersExchange(exchangeName);

        rabbitAdmin.declareQueue(queue);
        rabbitAdmin.declareExchange(exchange);
        rabbitAdmin.declareBinding(BindingBuilder.bind(queue).to(exchange).whereAny(headers).match());

        MessageProperties properties = new MessageProperties();
        properties.setHeaders(headers);
        Message msg = new Message(message.getBytes(), properties);
        rabbitTemplate.convertAndSend(exchangeName, "", msg);
        logger.info("End: Message sent to exchange: {} with headers: {}", exchangeName, headers.size());
    }

    private Exchange createExchange(String exchangeName, String exchangeType) {
        return switch (exchangeType.toLowerCase()) {
            case "direct" -> new DirectExchange(exchangeName);
            case "fanout" -> new FanoutExchange(exchangeName);
            case "topic" -> new TopicExchange(exchangeName);
            case "headers" -> new HeadersExchange(exchangeName);
            default -> throw new IllegalArgumentException("Unsupported exchange type: " + exchangeType);
        };
    }
}

