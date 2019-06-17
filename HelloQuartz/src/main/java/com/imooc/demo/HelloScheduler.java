package com.imooc.demo;


import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;


public class HelloScheduler {

    public static void main(String[] args) {


        //打印当前的执行时间
        Date date=new Date();
        SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //创建一个JobDetail实例，将该实例与HelloJob Class绑定
        /** 传参的
        *JobDetail jobDetail =JobBuilder.newJob(HelloJob.class)
                .withIdentity("myJob", "group1")
                .usingJobData("message","hello myjob1")
                .build();

           Trigger trigger = TriggerBuilder
                .newTrigger()
                .withIdentity("myTrigger", "group1")
                .usingJobData("message","hello mytrigger1")
                .startNow()
                .withSchedule(
                        SimpleScheduleBuilder.simpleSchedule()
                                .withIntervalInSeconds(2).repeatForever())
                .build();
                */
        JobDetail jobDetail =JobBuilder.newJob(HelloJob.class)
                 .withIdentity("myJob", "group1")
                             .build();
        //获取距离当前时间3秒后的时间
        date.setTime(date.getTime()+2000);

        Date endDate=new Date();
        endDate.setTime(endDate.getTime()+6000);
        //创建一个Trigger实例，定义该job立即执行，并且每隔两秒重复执行，直到永远
        /*SimpleScheduleBuilder
        Trigger trigger = TriggerBuilder
                .newTrigger()
                .withIdentity("myTrigger", "group1")
                .startAt(date)
                .endAt(endDate)
                .withSchedule(
                        SimpleScheduleBuilder.simpleSchedule()
                                .withIntervalInSeconds(2).withRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY))
                .build();*/
        //CronScheduleBuilder
        Trigger trigger = TriggerBuilder
                .newTrigger()
                .withIdentity("myTrigger", "group1")
                .startAt(date)
               // .endAt(endDate)
                .withSchedule(CronScheduleBuilder.cronSchedule("0/2 * * * * ?"))
                .build();
        //创建Scheduler实例
        SchedulerFactory sfact=new StdSchedulerFactory();
        try {
            Scheduler scheduler=sfact.getScheduler();
            scheduler.start();
            System.out.println("current  time is :"+sf.format(date));
            scheduler.scheduleJob(jobDetail,trigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}
