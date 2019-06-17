# learning-records
# java定时器学习 --MyTimerProject

定义：有且仅有一个后台线程对个业务线程进行定时定频率的调用 
-
Timer-->定时调用-->TimerTask

步骤：

1.创建我的定时任务
    
    public class MyTimerTask extends TimerTask{
      public void run(){
          //定时任务逻辑，要做的事情
      } 
    }

2.定时器启动并执行定时任务
    
    public class MyTimer(){
        public static void main(){
        Timer timer=new Timer();
        MyTimertask myTimerTask=new MyTimerTask("name");
        time.schedule(myTimerTask，2000,1000)；
      }
    }
    
schedule和scheduleAtFixedRate的区别
--
* 首次计划执行时间早于当前时间

    如设置执行时间为6秒钱前，每隔2秒执行一次
    
    schedule`会以当前时间为准 首次是执行时间会变为当前时间，然后每隔两秒执行一次` 
    
        current time is :2019-06-05 16:49:27
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
    
    scheduleAtFixedRate`会以最开始的时间为准去追赶当前时间去执行task 存在并发性 会连续执行3次 赶上进度` 
    
        current time is :2019-06-05 16:50:29
        scheduleAtFixedRate exec time is:2019-06-05 16:50:23
        scheduleAtFixedRateTask is being executed
        scheduleAtFixedRate exec time is:2019-06-05 16:50:25
        scheduleAtFixedRateTask is being executed
        scheduleAtFixedRate exec time is:2019-06-05 16:50:27
        scheduleAtFixedRateTask is being executed
        scheduleAtFixedRate exec time is:2019-06-05 16:50:29
        scheduleAtFixedRateTask is being executed
        scheduleAtFixedRate exec time is:2019-06-05 16:50:31

* 任务执行所需时间超出任务的执行周期间隔
    
    如设置执行时间为当前时间，执行需要3秒，每隔2秒执行一次
    
    schedule`下次执行时间相对于上一次实际执行完成的时间点，因此执行时间会不断延后, 会间隔三秒` 
      
            current time is :2019-06-05 10:36:21
            Scheduled exec time is:2019-06-05 10:36:21
            Task executes
            Scheduled exec time is:2019-06-05 10:36:24
            Task executes
            Scheduled exec time is:2019-06-05 10:36:27
            Task executes
    
    scheduleAtFixedRate`下次执行时间相对于上一次开始的时间点，因此执行时间一般不会延后,因此存在并发性`
      
            current time is :2019-06-05 10:38:35
            Scheduled exec time is:2019-06-05 10:38:35
            Task executes
            Scheduled exec time is:2019-06-05 10:38:37
            Task executes
            Scheduled exec time is:2019-06-05 10:38:39
            Task executes
            Scheduled exec time is:2019-06-05 10:38:41
            Task executes
            
其他重要函数
--
  * TimerTask
    * cancle()取消当前定时任务实例的运行
    * scheduledExecutionTime()返回此任务最近实际执行的已安排执行的时间
  
  * Timer
    * cancle() 终止此计时器，丢弃所有的当前已安排的任务
    * purge() 从此计时器的任务中溢出所有已取消的任务
  
Timer天生的两种缺陷
-- 
  * 管理并发任务的缺陷 有且仅有一个线程去执行定时任务
  * 当任务抛出RunTimeException，Timer会停止所有任务的运行
  
  由于这些缺陷所以接下来要学习Quartz.....
  



# 二. Quartz --HelloQuartz

### 代码目录说明
* com.imooc.demo下的 qartzJdbcTest.java 主要测试持久化存储 实现了项目重启就会执行之前未执行完的定时任务
	其余三个为入门概念练习

## Quartz的三个核心概念：
- 调度器 scheduler
- 任务 job
- 触发器 trigger ：Trigger是Quartz中的触发器用来告诉程序作业什么时候触发，即trigger是用来触发执行job的（常用simpleTrigger和cronTrigger）

常用示例：

         //1.创建一个JobDetail实例，指定Quartz
        JobDetail jobDetail = JobBuilder.newJob(MyJob.class)
                //任务执行类
                .withIdentity("jobTest16", "jGroupTest16")//任务名任务组
				.usingJobData("message","hello myjob1")
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
		//3.创建调度器
        Scheduler scheduler =StdSchedulerFactory.getDefaultScheduler();
		scheduler.scheduleJob(jobDetail, trigger);
		//开始执行该类下所有的定时任务
        scheduler.start();
		//scheduler.pauseJob(jobDetail.getKey());//triggerState 变为PAUSED 如果要再执行该任务 则需要用resumeJOb方法
        //scheduler.shutdown();//关闭任务 ，关闭任务后不可以再重启：即不可再调用start();


## Job & JobDetail
### Job的定义：
    实现业务逻辑的任务接口，job接口非常容易实现，只有一个execute方法，类似TimerTask的run方法，在里面编写业务逻辑。
### Job实例在Quartz中的生命周期:
	每次调度器执行Job时，它在调用execute方法会创建一个新的job实例，当调用完成后，关联的job对象实例会被释放，释放的实例会被垃圾回收机制回收
	
### JobDetail 重要属性
jobName，jobGroup，JobClass，JobDataMap

 		JobDetail jobDetail = JobBuilder.newJob(MyJob.class)//JobClass
            //任务执行类
            .withIdentity("jobTest16", "jGroupTest16")//jobName，jobGroup
            .usingJobData("message","hello myjob1")//JobDataMap
            .build();

JobDataMap 在job中获取

 		public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {     
        /**
         * 1.jobKey  
         */
        JobKey key = jobExecutionContext.getJobDetail().getKey();
		System.out.println("My name and Group are:" + key.getName() + ":" + key.getGroup());
		
		/**
         * 2.TriggerKey
         */
        TriggerKey trKey = jobExecutionContext.getTrigger().getKey();
        System.out.println("My name and Group are:" + trKey.getName() + ":" + trKey.getGroup());
		
		/**第二种方式 直接通过在job中定义成员变量和传入的key名字一样 用setter getter的方式来传入参
		*/
        JobDataMap dataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        System.out.println(dataMap.getString("message"));
        JobDataMap tdataMap = jobExecutionContext.getTrigger().getJobDataMap();
        System.out.println(tdataMap.getString("message"));
		
		 /**
         * 3.合并的key 如果有相同的会覆盖掉 trigger的key会覆盖job的相同的的key
		 * 如果job 和trigger 都有key为message的参数则job的会被覆盖
         */
        JobDataMap allDataMap = jobExecutionContext.getMergedJobDataMap();
        String msg = allDataMap.getString("message");
        System.out.println("msg: "+msg);
### Trigger
#### cronTrigger
	Quartz Cron 表达式支持到七个域  
	名称 是否必须 允许值            特殊字符 
	秒 	是 	0-59 			, - * / 
	分 	是 	0-59 			, - * / 
	时 	是 	0-23 			, - * / 
	日 	是 	1-31 			, - * ? / L W C 
	月 	是 	1-12 或 JAN-DEC  , - * / 
	周 	是 	1-7 或 SUN-SAT   , - * ? / L C # 
	年 	否 	空 或 1970-2099  , - * / 
	? 问号
	
#### 理解特殊字符



	同 UNIX cron 一样，Quartz cron 表达式支持用特殊字符来创建更为复杂的执行计划。然而，Quartz 在特殊字符的支持上比标准 UNIX cron 表达式更丰富了。

	* 星号

	使用星号(*) 指示着你想在这个域上包含所有合法的值。例如，在月份域上使用星号意味着每个月都会触发这个 trigger。

	表达式样例：

	0 * 17 * * ?

	意义：每天从下午5点到下午5:59中的每分钟激发一次 trigger。它停在下午 5:59 是因为值 17 在小时域上，在下午 6 点时，小时变为 18 了，也就不再理会这个 trigger，直到下一天的下午5点。

	在你希望 trigger 在该域的所有有效值上被激发时使用 * 字符。

	? 问号

	? 号只能用在日和周域上，但是不能在这两个域上同时使用。你可以认为 ? 字符是 "我并不关心在该域上是什么值。" 这不同于星号，星号是指示着该域上的每一个值。? 是说不为该域指定值。

	不能同时这两个域上指定值的理由是难以解释甚至是难以理解的。基本上，假定同时指定值的话，意义就会变得含混不清了：考虑一下，如果一个表达式在日域上有值11，同时在周域上指定了 WED。那么是要 trigger 仅在每个月的11号，且正好又是星期三那天被激发？还是在每个星期三的11号被激发呢？要去除这种不明确性的办法就是不能同时在这两个域上指定值。

	只要记住，假如你为这两域的其中一个指定了值，那就必须在另一个字值上放一个 ?。

	表达式样例：

	0 10,44 14 ? 3 WEB

	意义：在三月中的每个星期三的下午 2:10 和 下午 2:44 被触发。

	, 逗号

	逗号 (,) 是用来在给某个域上指定一个值列表的。例如，使用值 0,15,30,45 在秒域上意味着每15秒触发一个 trigger。

	表达式样例：

	0 0,15,30,45 * * * ?

	意义：每刻钟触发一次 trigger。

	/ 斜杠

	斜杠 (/) 是用于时间表的递增的。我们刚刚用了逗号来表示每15分钟的递增，但是我们也能写成这样 0/15。

	表达式样例：

	0/15 0/30 * * * ?

	意义：在整点和半点时每15秒触发 trigger。

	- 中划线

	中划线 (-) 用于指定一个范围。例如，在小时域上的 3-8 意味着 "3,4,5,6,7 和 8 点。"  域的值不允许回卷，所以像 50-10 这样的值是不允许的。

	表达式样例：

	0 45 3-8 ? * *

	意义：在上午的3点至上午的8点的45分时触发 trigger。

	L 字母

	L 说明了某域上允许的最后一个值。它仅被日和周域支持。当用在日域上，表示的是在月域上指定的月份的最后一天。例如，当月域上指定了 JAN 时，在日域上的 L 会促使 trigger 在1月31号被触发。假如月域上是 SEP，那么 L 会预示着在9月30号触发。换句话说，就是不管指定了哪个月，都是在相应月份的时最后一天触发 trigger。

	表达式 0 0 8 L * ? 意义是在每个月最后一天的上午 8:00 触发 trigger。在月域上的 * 说明是 "每个月"。

	当 L 字母用于周域上，指示着周的最后一天，就是星期六 (或者数字7)。所以如果你需要在每个月的最后一个星期六下午的 11:59 触发 trigger，你可以用这样的表达式 0 59 23 ? * L。

	当使用于周域上，你可以用一个数字与 L 连起来表示月份的最后一个星期 X。例如，表达式 0 0 12 ? * 2L 说的是在每个月的最后一个星期一触发 trigger。

	不要让范围和列表值与 L 连用

	虽然你能用星期数(1-7)与 L 连用，但是不允许你用一个范围值和列表值与 L 连用。这会产生不可预知的结果。 

	W 字母

	W 字符代表着平日 (Mon-Fri)，并且仅能用于日域中。它用来指定离指定日的最近的一个平日。大部分的商业处理都是基于工作周的，所以 W 字符可能是非常重要的。例如，日域中的 15W 意味着 "离该月15号的最近一个平日。" 假如15号是星期六，那么 trigger 会在14号(星期四)触发，因为距15号最近的是星期一，这个例子中也会是17号（译者Unmi注：不会在17号触发的，如果是15W，可能会是在14号(15号是星期六)或者15号(15号是星期天)触发，也就是只能出现在邻近的一天，如果15号当天为平日直接就会当日执行）。W 只能用在指定的日域为单天，不能是范围或列表值。

	# 井号

	# 字符仅能用于周域中。它用于指定月份中的第几周的哪一天。例如，如果你指定周域的值为 6#3，它意思是某月的第三个周五 (6=星期五，#3意味着月份中的第三周)。另一个例子 2#1 意思是某月的第一个星期一 (2=星期一，#1意味着月份中的第一周)。注意，假如你指定 #5，然而月份中没有第 5 周，那么该月不会触发。

### job的存储
Quartz java项目会优先去读取目录下的quartz.properties，如果没有就会默认去找jar包里的quartz.properties
quartz.properties中的存储方式配置 二选一

	#默认为RAMJobStore
	org.quartz.jobStore.class = org.quartz.simpl.RAMJobStore      
	#持久化存储，保存到数据库
	org.quartz.jobStore.class = org.quartz.impl.jdbcjobstore.JobStoreTX 


  
  
 

