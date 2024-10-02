package org.pj.rabbitmqspring.service.rabbitMq;

import org.pj.rabbitmqspring.model.request.ExchangeRequest;
import org.pj.rabbitmqspring.model.response.ApiResponse;

public interface IMessageService {
    ApiResponse sendMessageWithRoutingKey(ExchangeRequest request);
    ApiResponse sendMessageWithHeaders(ExchangeRequest request);
}
