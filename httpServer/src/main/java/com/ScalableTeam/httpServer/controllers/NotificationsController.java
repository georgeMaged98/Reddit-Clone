package com.ScalableTeam.httpServer.controllers;

import com.ScalableTeam.amqp.Config;
import com.ScalableTeam.amqp.RabbitMQProducer;
import com.ScalableTeam.models.notifications.requests.NotificationDeleteRequest;
import com.ScalableTeam.models.notifications.requests.NotificationReadRequest;
import com.ScalableTeam.models.notifications.requests.NotificationSendRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static com.ScalableTeam.amqp.MessagePublisher.getMessageHeaders;

@Slf4j
@RestController
@Controller
public class NotificationsController {

    @Autowired
    private Config config;

    @Autowired
    private RabbitMQProducer rabbitMQProducer;

    @RequestMapping(method = RequestMethod.POST, value = "/sendNotification")
    private void sendNotification(@RequestBody NotificationSendRequest notificationSendRequest) {
        String commandName = "sendNotification";

        log.info(commandName + "::Controller, Body: {}", notificationSendRequest);

        MessagePostProcessor messagePostProcessor = getMessageHeaders(
                config.getQueues().getResponse().getNotifications().get(commandName));

        rabbitMQProducer.publishAsynchronous(notificationSendRequest,
                config.getExchange(),
                config.getQueues().getRequest().getNotifications().get(commandName),
                messagePostProcessor);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getNotifications/{userId}")
    private Object getNotifications(@PathVariable String userId) {
        String commandName = "getNotifications";

        log.info(commandName + "::Controller, Body: {}", userId);

        return rabbitMQProducer.publishSynchronous(userId,
                config.getExchange(),
                config.getQueues().getRequest().getReddit().get(commandName));
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/markNotificationAsRead")
    private void markNotificationAsRead(@RequestBody NotificationReadRequest notificationReadRequest) {
        String commandName = "markNotificationAsRead";

        log.info(commandName + "::Controller, Body: {}", notificationReadRequest);

        MessagePostProcessor messagePostProcessor = getMessageHeaders(
                config.getQueues().getResponse().getNotifications().get(commandName));

        rabbitMQProducer.publishAsynchronous(notificationReadRequest,
                config.getExchange(),
                config.getQueues().getRequest().getNotifications().get(commandName),
                messagePostProcessor);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/deleteNotification")
    private void deleteNotification(@RequestBody NotificationDeleteRequest notificationDeleteRequest) {
        String commandName = "deleteNotification";

        log.info(commandName + "::Controller, Body: {}", notificationDeleteRequest);

        MessagePostProcessor messagePostProcessor = getMessageHeaders(
                config.getQueues().getResponse().getNotifications().get(commandName));

        rabbitMQProducer.publishAsynchronous(notificationDeleteRequest,
                config.getExchange(),
                config.getQueues().getRequest().getNotifications().get(commandName),
                messagePostProcessor);
    }
}