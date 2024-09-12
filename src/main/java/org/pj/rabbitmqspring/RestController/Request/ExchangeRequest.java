package org.pj.rabbitmqspring.RestController.Request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Map;

@Getter
@Setter
public class ExchangeRequest {
    @NotNull
    private String exchangeType;
    @NotNull
    private String exchangeName;
    @NotNull
    private String queueName;
    @NotBlank
    private String message;

    private String routingKey;
    private Map<String, Object> headers;
}
