package com.imooc.demo;

import org.quartz.*;
import org.quartz.impl.StdScheduler;
import org.quartz.impl.StdSchedulerFactory;

import java.util.List;

public class QuartzJdbcTest {

    public static void main(String[] args) {

        startSchedule();
        //可以将想要恢复执行的job，恢复执行
        resumeJob();
    }

    /**
     * 开始一个simpleSchedule()调度
     * 将新的job加入调度，如果存在直接执行该项目的定时任务。如果不存在则保存到数据然后再执行调度。
     */
    public static void startSchedule() {


        //1.创建一个JobDetail实例，指定Quartz
        JobDetail jobDetail = JobBuilder.newJob(MyJob.class)
                //任务执行类
                .withIdentity("jobTest16", "jGroupTest16")//任务名任务组
                //加上之后数据库中detail表会有数据
                .build();
        //触发器类型
      /*  SimpleScheduleBuilder builder = SimpleScheduleBuilder
                //.repeatHourlyForever();//设置执行次数
                .repeatHourlyForTotalCount(5);//设置每秒执行重复5次*/
        CronScheduleBuilder builder1=CronScheduleBuilder.cronSchedule("0/2 * * * * ?");

        //2.创建Trigger
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("triggerTest16", "tGroupTest16").startNow()
                .withSchedule(builder1)
                .build();
        //3.创建Scheduler
        Scheduler scheduler = null;
        try {
            scheduler = StdSchedulerFactory.getDefaultScheduler();
            List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobDetail.getKey());
            //在这里判断新加的这个jobDetail是否存在,如果存在则直接执行数据库中存储的所有定时任务
            if (triggers.size() > 0) {
                scheduler.start();
            } else {
                //4.调度执行 执行数据库对应表会插入对应的定时任务的信息
                scheduler.scheduleJob(jobDetail, trigger);
                //开始执行该类下所有的定时任务
                scheduler.start();
                try {
                    Thread.sleep(20000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                   //scheduler.pauseJob(jobDetail.getKey());//triggerState 变为PAUSED 如果要再执行该任务 则需要用resumeJOb方法
                   //scheduler.shutdown();//关闭任务 ，关闭任务后不可以再重启：即不可再调用start();

            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    /**
     *  可以将想要恢复执行的job，恢复执行.然后再执行调度
     */
    public static void resumeJob() {
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();

        Scheduler scheduler = null;
        try {
            scheduler = schedulerFactory.getScheduler();//会执行所有的定时任务

            JobKey jobKey = new JobKey("jobTest16", "jGroupTest16");
            List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);

            if (triggers.size() > 0) { //存在执行
                for (Trigger tg : triggers) {
                    if ((tg instanceof CronTrigger) || (tg instanceof SimpleTrigger)) {
                        scheduler.resumeJob(jobKey);//恢复Job 如果不回复直接start的话那么暂停的job将永远不会被执行。
                    }
                }
                System.out.println("kaishi ");
                scheduler.start();//执行定时任务，其实是会执行所有的定时任务
                System.out.println("结束");
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

}

