package com.heima.xxljob.job;

import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class HelloJob {
//    @Value("${server.port}")
//    public String port;
//    @XxlJob("demoJobHandler")
//    public void helloJob(){
//        System.out.println("任务执行了。。。。。。。。。"+port);
//    }
    @XxlJob("shardingJobHandler")
    public void shardingJobHandler(){
        int shardIndex = XxlJobHelper.getShardIndex();
        int shardTotal = XxlJobHelper.getShardTotal();
        for (int i = 0; i < 10000; i++) {
            if(i%shardTotal==shardIndex){
                System.out.println("当前第"+shardIndex+"分片执行了，任务项为"+i);
            }
        }
    }
}
