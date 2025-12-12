package com.miniBili.component;


import com.miniBili.entity.po.VideoInfoFilePost;
import com.miniBili.exception.BusinessException;
import com.miniBili.service.VideoInfoPostService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RocketMQMessageListener(topic = "video-add-topic",
        consumerGroup = "videoAdd-consume-group",
        maxReconsumeTimes = 4,
        consumeThreadMax = 2)
public class VideoAddConsumer implements RocketMQListener<VideoInfoFilePost> {

    @Autowired
    private VideoInfoPostService videoInfoPostService;

    @Override
    public void onMessage(VideoInfoFilePost videoInfoFilePost) {
        try {
            videoInfoPostService.transferVideoFile(videoInfoFilePost);
        }catch (Exception e){
            log.error(videoInfoFilePost.getFileName()+"转码失败");
            e.printStackTrace();
            // 必须往外抛，RocketMQ 才会重试
            throw  new BusinessException("获取转码信息失败，请重新获取",e);
        }
    }
}
