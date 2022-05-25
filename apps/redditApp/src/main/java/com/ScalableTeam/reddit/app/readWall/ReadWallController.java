package com.ScalableTeam.reddit.app.readWall;

import com.ScalableTeam.amqp.Config;
import com.ScalableTeam.amqp.RabbitMQProducer;
import com.ScalableTeam.reddit.app.MessagePublisher;
import com.ScalableTeam.reddit.app.entity.Post;
import com.ScalableTeam.reddit.config.GeneralConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@Slf4j
public class ReadWallController extends MessagePublisher {
    @Autowired
    private ReadWallService readWallService;
    @Autowired
    private GeneralConfig generalConfig;
    @Autowired
    private RabbitMQProducer rabbitMQProducer;
    @Autowired
    private Config config;

    @RequestMapping("/wall")
    private String readWall(@RequestParam String userNameId) throws Exception {
        log.info(generalConfig.getCommands().get("readWall") + "Controller", userNameId);
        String commandName="readWall";
        return (String) rabbitMQProducer.publishSynchronous(userNameId,
                config.getExchange(),
                config.getQueues().getRequest().getReddit().get(commandName));
        //return readWallService.execute(userNameId);
    }
}
