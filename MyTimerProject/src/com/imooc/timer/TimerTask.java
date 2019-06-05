package com.imooc.timer;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;

public class TimerTask {

    public static void main(String[] args) {
        Timer timer=new Timer();
        MyTimerTask myTT=new MyTimerTask("NO1",2000);
        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(sf.format(calendar.getTime()));
        calendar.add(calendar.SECOND,3);
        myTT.setName("schedule1");
        timer.schedule(myTT,calendar.getTime());
    }}
