package com.imooc.timer;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;

public class MyTimer {
    public static void main(String[] args) {
        //创建一个timer实例
        Timer timer=new Timer();
        //传建一个MyTimertask实例
       // MyTimerTask myTimerTask=new MyTimerTask("No.1");
        //通过timer定时定频调用myTimerTask的业务逻辑
            //即第一次执行时在当前时间的2秒之后，之后每隔一秒钟执行一次
        //timer.schedule(myTimerTask,2000,1000);

//        timer.schedule(myTimerTask,2000);
//
//        timer.schedule(myTimerTask,new Date());
//
//        timer.schedule(myTimerTask,new Date(),1000);
//
//        timer.scheduleAtFixedRate(myTimerTask,1000,1000);
//
//        timer.scheduleAtFixedRate(myTimerTask,new Date(),1000);

        MyTimerTask myTimerTask1=new MyTimerTask("NO1",2000);
        MyTimerTask myTimerTask2=new MyTimerTask("NO2",2000);

        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("current time is " +sf.format(calendar.getTime()));
        timer.schedule(myTimerTask1,calendar.getTime());
        timer.schedule(myTimerTask2,calendar.getTime());
    }
}
