package com.imooc.demo;

import org.quartz.*;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HelloJob implements Job {

    private String message;
    private Float FloatJobValue;
    private Double DoubleTriggerValue;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Float getFloatJobValue() {
        return FloatJobValue;
    }

    public void setFloatJobValue(Float floatJobValue) {
        FloatJobValue = floatJobValue;
    }

    public Double getDoubleTriggerValue() {
        return DoubleTriggerValue;
    }

    public void setDoubleTriggerValue(Double doubleTriggerValue) {
        DoubleTriggerValue = doubleTriggerValue;
    }

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        //打印当前的执行时间
        Date date = new Date();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("current exec time is :" + sf.format(date));
        //编写具体的业务逻辑
        System.out.println("hello world!");

        /**
         * 1.jobKey
         */
       /* JobKey key = jobExecutionContext.getJobDetail().getKey();
        System.out.println("My name and Group are:" + key.getName() + ":" + key.getGroup());*/

        /**
         * 2.TriggerKey
         */
      /*  TriggerKey trKey = jobExecutionContext.getTrigger().getKey();
        System.out.println("My name and Group are:" + trKey.getName() + ":" + trKey.getGroup());
        JobDataMap dataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        System.out.println(dataMap.getString("message"));
        JobDataMap tdataMap = jobExecutionContext.getTrigger().getJobDataMap();
        System.out.println(tdataMap.getString("message"));*/

        /**
         * 3.合并的key 如果有相同的会覆盖掉 trigger的key会覆盖job的相同的的key
         */
       /* JobDataMap allDataMap = jobExecutionContext.getMergedJobDataMap();
        String msg = allDataMap.getString("message");
        System.out.println("msg: "+msg);*/
        /**
         * 4. 直接通过在job中定义成员变量和传入的key名字一样用setter getter的方式来传入，         */
        //System.out.println("-----:"+message);


        Trigger currentTrigger=jobExecutionContext.getTrigger();
        System.out.println(sf.format(currentTrigger.getStartTime()));
        System.out.println(sf.format(currentTrigger.getEndTime()));
        JobKey jobKey=currentTrigger.getJobKey();
        System.out.println(jobKey.getName()+"----"+jobKey.getGroup());
    }

}
