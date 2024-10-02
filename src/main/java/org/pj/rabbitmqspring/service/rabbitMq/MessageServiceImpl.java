package org.pj.rabbitmqspring.service.rabbitMq;

import org.pj.rabbitmqspring.model.request.ExchangeRequest;
import org.pj.rabbitmqspring.model.response.ApiResponse;
import org.pj.rabbitmqspring.service.rabbitMq.receive.Consumer;
import org.pj.rabbitmqspring.service.rabbitMq.receive.MessageHandler;
import org.pj.rabbitmqspring.service.rabbitMq.send.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class MessageServiceImpl implements IMessageService {

    private final Producer producer;
    private final Consumer consumer;

    @Autowired
    public MessageServiceImpl(Producer producer, Consumer consumer) {
        this.producer = producer;
        this.consumer = consumer;
    }

    @Override
    public ApiResponse sendMessageWithRoutingKey(ExchangeRequest request) {
        try {
            producer.sendMessageWithRoutingKey(request.getExchangeName(), request.getExchangeType(), request.getQueueName(), request.getRoutingKey(), request.getMessage());
            consumer.receiveMessage(request.getQueueName(), new MessageHandler());
            return new ApiResponse(HttpStatus.OK.value(), "Message sent successfully");
        } catch (Exception exception) {
            return new ApiResponse(500, new StringBuilder("Error occurred: ").append(exception).toString());
        }
    }

    @Override
    public ApiResponse sendMessageWithHeaders(ExchangeRequest request) {
        try {
            producer.sendMessageWithHeaders(request.getExchangeName(), request.getQueueName(), request.getHeaders(), request.getMessage());
            consumer.receiveMessage(request.getQueueName(), new MessageHandler());
            return new ApiResponse(HttpStatus.OK.value(), "Message sent successfully");
        } catch (Exception exception) {
            return new ApiResponse(500, new StringBuilder("Error occurred: ").append(exception).toString());
        }
    }
}

