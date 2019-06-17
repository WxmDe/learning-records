package com.imooc.timer;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimerTask;

public class MyTimerTask extends TimerTask {
    private String name;
    private long costTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public
    MyTimerTask(String inputName,long inputCostTime){
        name=inputName;
        costTime=inputCostTime;
    }

    @Override
    public void run() {
        //打印当前name的内容
        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(name + "'s Current exec time is " +sf.format(calendar.getTime()));
        try {
            Thread.sleep(costTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        calendar=Calendar.getInstance();
        System.out.println(name+"'s finish time is:"+sf.format(calendar.getTime()));
    }
}
