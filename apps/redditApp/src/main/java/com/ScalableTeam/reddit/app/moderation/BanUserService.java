
package com.ScalableTeam.reddit.app.moderation;

import com.ScalableTeam.reddit.MyCommand;
import com.ScalableTeam.reddit.app.entity.Channel;
import com.ScalableTeam.reddit.app.entity.User;
import com.ScalableTeam.reddit.app.repository.ChannelRepository;
import com.ScalableTeam.reddit.app.repository.UserRepository;
import com.ScalableTeam.reddit.app.requestForms.AssignModeratorsForm;
import com.ScalableTeam.reddit.app.requestForms.BanUserForm;
import com.ScalableTeam.reddit.config.GeneralConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;

@ComponentScan("com.ScalableTeam.reddit")
@Service
@Slf4j
public class BanUserService implements MyCommand {
    @Autowired
private ChannelRepository channelRepository;
@Autowired
private UserRepository userRepository;
@Autowired
private GeneralConfig generalConfig;
private final String serviceName = "banUser";
    @RabbitListener(queues = "${mq.queues.request.reddit."+serviceName+"}")
    public String listenToRequestQueue(BanUserForm banUserForm, Message message) throws Exception {
        String correlationId = message.getMessageProperties().getCorrelationId();
        String indicator = generalConfig.getCommands().get(serviceName);
        log.info(indicator + "Service::"+serviceName+", CorrelationId={}", correlationId);
        return execute(banUserForm);
    }

    @Override
    public String execute(Object body) throws Exception {

try {

    BanUserForm request = (BanUserForm) body;
    String modId = request.getModId();
    String requestedBanUserId =request.getRequestedBanUserId();
    String redditId = request.getRedditId();

    Optional<Channel> reddit = channelRepository.findById(redditId);
    if (!reddit.isPresent()) {
        throw new IllegalStateException("Reddit not found in DB!");
    }

    Optional<User> user = userRepository.findById(requestedBanUserId);
    if (!user.isPresent()) {
        throw new IllegalStateException("User not found in DB!");
    }

    if (!reddit.get().getModerators().containsKey(modId)) {
        return "User " + modId + " is not a mod for channel " + redditId;
    }

    User toban = user.get();
    if(toban.getFollowedChannels().isEmpty() || !toban.getFollowedChannels().containsKey(redditId)){
        return "User " + requestedBanUserId + " is not following channel " + redditId;
    }

    HashMap<String, Boolean> ban = new HashMap<String, Boolean>();
    ban.put(requestedBanUserId, true);
    channelRepository.updateBannedUsersWithID(redditId, ban);

    return "user " + requestedBanUserId + " banned successfully from channel " + redditId;
}
catch(Exception e){
    return e.getMessage();
}
    }

    @RabbitListener(queues = "${mq.queues.response.reddit."+serviceName+"}")
    public void receive(String response, Message message) {
        String indicator = generalConfig.getCommands().get(serviceName);
        String correlationId = message.getMessageProperties().getCorrelationId();
        log.info(indicator + "Service::CorrelationId: {}, message: {}", correlationId, response);
    }
}
