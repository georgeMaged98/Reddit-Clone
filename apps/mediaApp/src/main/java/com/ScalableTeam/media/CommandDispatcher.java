package com.ScalableTeam.media;

import com.ScalableTeam.media.commands.ICommand;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@AllArgsConstructor
public class CommandDispatcher {
    private final Map<String, ICommand> commandMap;

    @RabbitListener(queues = MessageConfig.QUEUE, returnExceptions = "true")
    public Object receiveMessage(Message message, @Header("command") String command) {
        ICommand iCommand = commandMap.get(command);
        return iCommand.execute(message.getPayload());
    }
}
