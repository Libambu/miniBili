package com.miniBili.component;

import com.miniBili.entity.dto.VideoPlayDto;
import com.miniBili.entity.enums.SearchOrderTypeEnum;
import com.miniBili.service.VideoInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RocketMQMessageListener(topic = "Video-Count-Topic",
        consumerGroup = "videoCount-consumer-group",
        maxReconsumeTimes = 4,
        consumeThreadMax = 2)
public class VideoCountConsumer implements RocketMQListener<VideoPlayDto> {

    @Autowired
    private VideoInfoService videoInfoService;
    @Autowired
    private RedisComponent redisComponent;
    @Autowired
    private ESsearchComponent esSearchComponent;

    @Override
    public void onMessage(VideoPlayDto videoPlayDto) {
        try {
            //增加视频播放量，更新最新播放时间
            videoInfoService.addReadCount(videoPlayDto.getVideoId());
            //记录播放历史
            if(videoPlayDto.getUserId()!=null){

            }
            //更新日播放量，在数据图表中统计用到
            redisComponent.recordVideoPlayCount(videoPlayDto.getVideoId());
            //更新es播放数量
            esSearchComponent.updateDocCount(videoPlayDto.getVideoId(), SearchOrderTypeEnum.VIDEO_PLAY.getField(),1);
        }catch (Exception e){
            log.error(videoPlayDto.getVideoId() + ":获取视频文件队列失败:"+e.getMessage());
            throw  e;
        }
    }
}
