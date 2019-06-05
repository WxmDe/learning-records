package com.imooc.timer;

import java.util.Timer;
import java.util.TimerTask;

public class WaterRobot extends TimerTask {

    private Timer timer;
    //最大容量为5L
    private Integer bucketCapacity=0;

    public WaterRobot(Timer inputTimer){
        timer=inputTimer;
    }
    @Override
    public void run() {
    //灌水直至桶满为止
        if(bucketCapacity<5){
            System.out.println("Add 1L water into the bucket");
            bucketCapacity++;
        }else{
            //水满之后就停止
            System.out.println("the number of cancleed task in timer is "+timer.purge());
            cancel();
            System.out.println("the WaterRobot has been aborted");
            System.out.println("the number of cancleed task in timer is "+timer.purge());
            System.out.println("current water is "+bucketCapacity+"L");
            //等待两秒钟终止timer里面的所有内容
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            timer.cancel();
        }
    }
}
