package com.ScalableTeam.amqp;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Component;

import static com.ScalableTeam.amqp.MessagePublisher.processMessage;

@Component
@Slf4j
@AllArgsConstructor
public class RabbitMQProducer {

    private final AmqpTemplate amqpTemplate;

    public void publishAsynchronous(String commandName, Object payload) {
        log.info("Publishing. Command: {}, Payload: {}", commandName, payload);
        amqpTemplate.convertAndSend(commandName, payload, processMessage(commandName));
        log.info("Published. Command: {}, Payload: {}", commandName, payload);
    }

    public Object publishSynchronous(String commandName, Object payload) {
        log.info("Publishing. Command: {}, Payload: {}", commandName, payload);
        Object result = amqpTemplate.convertSendAndReceive(commandName, payload, processMessage(commandName));
        log.info("Published. Command: {}, Payload: {}", commandName, payload);
        return result;
    }
}
