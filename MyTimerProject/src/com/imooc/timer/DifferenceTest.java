package com.imooc.timer;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class DifferenceTest {
    public static void main(String[] args) {
        //规定时间格式
        final SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //获取当前的具体时间
        Calendar calendar=Calendar.getInstance();
        System.out.println("current time is :"+sf.format(calendar.getTime()));
        //设置成六秒前的时间
        calendar.add(Calendar.SECOND,-6);
        Timer timer=new Timer();
        /**
         * 一.首次计划执行时间早于当前时间
         */

        //第一次执行时间为六秒前，之后每隔两秒钟执行一次
        /**
         * 1.schedule会以当前时间为准 首次是执行时间会变为当前时间，然后每隔两秒执行一次
         *
         * current time is :2019-06-05 16:49:27
         Scheduled exec time is:2019-06-05 16:49:27
         Task is being executed
         Scheduled exec time is:2019-06-05 16:49:29
         Task is being executed
         Scheduled exec time is:2019-06-05 16:49:31
         Task is being executed
         Scheduled exec time is:2019-06-05 16:49:33
         Task is being executed
         Scheduled exec time is:2019-06-05 16:49:35
         Task is being executed
         */
       /* timer.schedule(new TimerTask() { //定义一个内部类
            @Override
            public void run() {
                //打印当前的计划执行时间
                System.out.println("Scheduled exec time is:"+sf.format(scheduledExecutionTime()));
                System.out.println("Task is being executed");
            }
        }, calendar.getTime(), 2000);*/

        /**
         * 2.scheduleAtFixedRate会以最开始的时间为准去追赶当前时间去执行task 存在并发性 会连续执行3次 赶上进度
         * current time is :2019-06-05 16:50:29
         scheduleAtFixedRate exec time is:2019-06-05 16:50:23
         scheduleAtFixedRateTask is being executed
         scheduleAtFixedRate exec time is:2019-06-05 16:50:25
         scheduleAtFixedRateTask is being executed
         scheduleAtFixedRate exec time is:2019-06-05 16:50:27
         scheduleAtFixedRateTask is being executed
         scheduleAtFixedRate exec time is:2019-06-05 16:50:29
         scheduleAtFixedRateTask is being executed
         scheduleAtFixedRate exec time is:2019-06-05 16:50:31
         */
        timer.scheduleAtFixedRate(new TimerTask() { //定义一个内部类
            @Override
            public void run() {
                //打印当前的计划执行时间
                System.out.println("scheduleAtFixedRate exec time is:"+sf.format(scheduledExecutionTime()));
                System.out.println("scheduleAtFixedRateTask is being executed");
            }
        }, calendar.getTime(), 2000);

        /**
         *   二.任务执行所需时间超出任务的执行周期间隔
         */
        /**
         * 1.schedule 下次执行时间相对于上一次实际执行完成的时间点，因此执行时间会不断延后。 会间隔三秒
         * current time is :2019-06-05 10:36:21
         Scheduled exec time is:2019-06-05 10:36:21
         Task executes
         Scheduled exec time is:2019-06-05 10:36:24
         Task executes
         Scheduled exec time is:2019-06-05 10:36:27
         Task executes
         *
         */
          Calendar currentcalendar=Calendar.getInstance();
          /*timer.schedule(new TimerTask() { //定义一个内部类
            @Override
            public void run() {
                //打印当前的计划执行时间
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Scheduled exec time is:"+sf.format(scheduledExecutionTime()));
                System.out.println("Task executes");







            }
        }, currentcalendar.getTime(), 2000);*/
        /**
         * 1.scheduleAtFixedRate 下次执行时间相对于上一次开始的时间点，因此执行时间一般不会延后。因此存在并发性
         * current time is :2019-06-05 10:38:35
         Scheduled exec time is:2019-06-05 10:38:35
         Task executes
         Scheduled exec time is:2019-06-05 10:38:37
         Task executes
         Scheduled exec time is:2019-06-05 10:38:39
         Task executes
         Scheduled exec time is:2019-06-05 10:38:41
         Task executes
         */
      /*    timer.scheduleAtFixedRate(new TimerTask() { //定义一个内部类
            @Override
            public void run() {
                //打印当前的计划执行时间
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Scheduled exec time is:"+sf.format(scheduledExecutionTime()));
                System.out.println("Task executes");
            }
        }, currentcalendar.getTime(), 2000);*/


    }


}
