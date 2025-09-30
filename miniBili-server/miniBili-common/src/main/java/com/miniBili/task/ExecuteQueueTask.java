package com.miniBili.task;


import com.miniBili.component.RedisComponent;
import com.miniBili.entity.constants.Constants;
import com.miniBili.entity.po.VideoInfoFilePost;
import com.miniBili.service.VideoInfoPostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@Slf4j
public class ExecuteQueueTask {
    //项目启动后，自动开一个（只有 2 个线程的）线程池，并在其中再开一个死循环线程，用来一直“消费” Redis 里的某个队列。
    private ExecutorService executorService = Executors.newFixedThreadPool(Constants.LENGTH_2);

    @Autowired
    private RedisComponent redisComponent;

    @Autowired
    private VideoInfoPostService videoInfoPostService;

    @PostConstruct
    //当 整个 Spring 容器启动完成 后，自动执行它标记的方法。
    public void consumTranferFileQueue() {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        VideoInfoFilePost videoInfoFilePost = redisComponent.getFileFromTransFerQueue();
                        if (videoInfoFilePost == null) {
                            Thread.sleep(15000);
                            continue;
                        }
                        // 进行转码
                        videoInfoPostService.transferVideoFile(videoInfoFilePost);
                    } catch (Exception e) {
                        // 防止死循环里抛出异常导致线程死掉；一旦异常被吞掉，线程会继续下一次循环。
                        e.printStackTrace();
                        log.error("获取转码信息失败，请重新获取");
                    }
                }
            }
        });
    }

    //这里为什么不直接删除文件呢，因为有一个问题，必须在审核通过之后才可以删，不然万一有其他东西比如视频名称也改了，
    // 这样的话就可能会不通过，但是视频已经删了的话，之前发布的已经审核就也不能看了
}
