package com.ScalableTeam.httpServer.controllers;

import com.ScalableTeam.amqp.RabbitMQProducer;
import com.ScalableTeam.httpServer.utils.CommandsMapper;
import com.ScalableTeam.models.notifications.requests.DeviceTokenRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Controller
public class DeviceTokensController {

    @Autowired
    private CommandsMapper commandsMapper;

    @Autowired
    private RabbitMQProducer rabbitMQProducer;

    @RequestMapping(method = RequestMethod.PUT, value = "/registerDeviceToken")
    private void registerDeviceToken(@RequestBody DeviceTokenRequest deviceTokenRequest) {
        String commandName = commandsMapper.getNotifications().get("registerDeviceToken");
        log.info("Controller - Command: {}, Payload: {}", commandName, deviceTokenRequest);
        rabbitMQProducer.publishAsynchronous(commandName, deviceTokenRequest);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/unregisterDeviceToken")
    private void unregisterDeviceToken(@RequestBody DeviceTokenRequest deviceTokenRequest) {
        String commandName = commandsMapper.getNotifications().get("unregisterDeviceToken");
        log.info("Controller - Command: {}, Payload: {}", commandName, deviceTokenRequest);
        rabbitMQProducer.publishAsynchronous(commandName, deviceTokenRequest);
    }
}
