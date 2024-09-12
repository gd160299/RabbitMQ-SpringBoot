package org.pj.rabbitmqspring.RestController;


import org.pj.rabbitmqspring.RestController.Response.ApiResponse;
import org.pj.rabbitmqspring.Service.Receive.MessageHandler;
import org.pj.rabbitmqspring.RestController.Request.ExchangeRequest;
import org.pj.rabbitmqspring.Service.Receive.Consumer;
import org.pj.rabbitmqspring.Service.Send.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rabbitmq")
public class MessageRestController {

    @Autowired
    private Producer producer;

    @Autowired
    private Consumer consumer;

    // API cho Direct, Fanout, Topic Exchange
    @PostMapping("/send")
    public ResponseEntity<ApiResponse> sendMessage(@RequestBody ExchangeRequest request) {
        try {
            producer.sendMessageWithRoutingKey(request.getExchangeName(), request.getExchangeType(), request.getQueueName(), request.getRoutingKey(), request.getMessage());
            consumer.receiveMessage(request.getQueueName(), new MessageHandler());
            return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), "Message sent successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(500, "Error occurred: " + e.getMessage()));
        }
    }

    // API cho Headers Exchange
    @PostMapping("/send/headers")
    public ResponseEntity<ApiResponse> sendMessageWithHeaders(@RequestBody ExchangeRequest request) {
        try {
            producer.sendMessageWithHeaders(request.getExchangeName(), request.getQueueName(), request.getHeaders(), request.getMessage());
            consumer.receiveMessage(request.getQueueName(), new MessageHandler());
            return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), "Message sent successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(500, "Error occurred: " + e.getMessage()));
        }
    }
}
