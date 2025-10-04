package com.miniBili.web.component;

import com.miniBili.component.RedisComponent;
import com.miniBili.entity.constants.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RedisKeyExpireationListener extends KeyExpirationEventMessageListener {
    @Autowired
    private RedisComponent redisComponent;


    public RedisKeyExpireationListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String key = message.toString();
        if(!key.contains(Constants.REDIS_KEY_VIDEO_PLAY_COUNT_ONLINE_PREIFX + Constants.REDIS_KEY_VIDEO_PLAY_COUNT_USER_PREIFX)){
            return;
        }
        Integer userKeyIndex = key.indexOf(Constants.REDIS_KEY_VIDEO_PLAY_COUNT_USER_PREIFX) + Constants.REDIS_KEY_VIDEO_PLAY_COUNT_USER_PREIFX.length();
        String fileId = key.substring(userKeyIndex,userKeyIndex+20);
        redisComponent.decreamentPlayOnlineCount(String.format(Constants.REDIS_KEY_VIDEO_PLAY_COUNT_ONLINE,fileId));
    }
}
