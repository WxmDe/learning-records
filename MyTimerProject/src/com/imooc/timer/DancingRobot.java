package com.imooc.timer;

import java.text.SimpleDateFormat;
import java.util.TimerTask;

/**
 * 实现两个机器人
 * 第一个机器人会隔两秒打印最近一次计划执行时间，执行内容
 * 第二个机器人会摸你往桶里倒水，直到桶的水满为止
 */
public class DancingRobot extends TimerTask {
    @Override
    public void run() {
        //获取最近一次的任务执行时间。并将其格式化
        SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("scheduled exec time is :"+sf.format(scheduledExecutionTime()));
        System.out.println("Dancing happily");
    }
}
