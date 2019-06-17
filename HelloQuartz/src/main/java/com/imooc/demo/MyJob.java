package com.imooc.demo;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MyJob implements Job{
    private static final Logger log= LoggerFactory.getLogger(MyJob.class);


    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("MyJob is start.......");
        log.info("Hello quartz" +new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        log.info("MyJob is end .......");
        System.out.println("jobDetail--"+jobExecutionContext.getJobDetail().getKey());
        System.out.println("真的启动后就自己执行定时任务了.......");
        System.out.println("Hello quartz" +new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        System.out.println("MyJob is end .......");
    }
}
