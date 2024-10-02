package org.pj.rabbitmqspring.restController;


import org.pj.rabbitmqspring.model.request.ExchangeRequest;
import org.pj.rabbitmqspring.model.response.ApiResponse;
import org.pj.rabbitmqspring.service.rabbitMq.IMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rabbitmq")
public class MessageRestController {

    private final IMessageService messageService;

    @Autowired
    public MessageRestController(IMessageService messageService) {
        this.messageService = messageService;
    }

    // API cho Direct, Fanout, Topic Exchange
    @PostMapping("/send")
    public ResponseEntity<ApiResponse> sendMessage(@RequestBody ExchangeRequest request) {
        ApiResponse response = messageService.sendMessageWithRoutingKey(request);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    // API cho Headers Exchange
    @PostMapping("/send/headers")
    public ResponseEntity<ApiResponse> sendMessageWithHeaders(@RequestBody ExchangeRequest request) {
        ApiResponse response = messageService.sendMessageWithHeaders(request);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
