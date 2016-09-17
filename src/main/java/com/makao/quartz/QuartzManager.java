package com.makao.quartz;
import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;

public class QuartzManager {
 
	private static final Logger log = Logger
			.getLogger(QuartzManager.class);
 
    private static SchedulerFactory schedulerFactory = new StdSchedulerFactory();
    public static String JOB_GROUP_NAME = "EXTJWEB_JOBGROUP_NAME"; 
     
//    public static final String DATA_KEY = "STATE_DATA";
//     
//    public QuartzManager (){
//        try {
//        	schedulerFactory = new StdSchedulerFactory().getScheduler();
//            log.info("初始化调度器 ");
//        } catch (SchedulerException ex) {
//            log.error("初始化调度器=> [失败]:" + ex.getLocalizedMessage());
//        }
//    }   
    public static void addJob(String name, String group, Class<? extends Job> clazz,String cronExpression) {                 
        try {       
        	Scheduler scheduler = schedulerFactory.getScheduler();
            //构造任务
            JobDetail job = newJob(clazz)
                    .withIdentity(name, group)                  
                    .build();
             
            //构造任务触发器
            Trigger trg = newTrigger()
                    .withIdentity(name, group)
                    .withSchedule(cronSchedule(cronExpression))
                    .build();       
             
            //将作业添加到调度器
            scheduler.scheduleJob(job, trg);
            log.info("创建作业=> [作业名称：" + name + " 作业组：" + group + "] ");
            // 启动  
            if (!scheduler.isShutdown()){  
            	scheduler.start();  
            }  
        } catch (SchedulerException e) {
            e.printStackTrace();
            log.error("创建作业=> [作业名称：" + name + " 作业组：" + group + "]=> [失败]");
        }
    }
     
    public static void removeJob(String name, String group){
        try {
        	Scheduler scheduler = schedulerFactory.getScheduler();
            TriggerKey tk = TriggerKey.triggerKey(name, group);
            scheduler.pauseTrigger(tk);//停止触发器  
            scheduler.unscheduleJob(tk);//移除触发器
            JobKey jobKey = JobKey.jobKey(name, group);
            scheduler.deleteJob(jobKey);//删除作业
            log.info("删除作业=> [作业名称：" + name + " 作业组：" + group + "] ");
        } catch (SchedulerException e) {
            e.printStackTrace();
            log.error("删除作业=> [作业名称：" + name + " 作业组：" + group + "]=> [失败]");
        }
    }
     
    public static void pauseJob(String name, String group){
        try {
        	Scheduler scheduler = schedulerFactory.getScheduler();
            JobKey jobKey = JobKey.jobKey(name, group);
            scheduler.pauseJob(jobKey);
            log.info("暂停作业=> [作业名称：" + name + " 作业组：" + group + "] ");
        } catch (SchedulerException e) {
            e.printStackTrace();
            log.error("暂停作业=> [作业名称：" + name + " 作业组：" + group + "]=> [失败]");
        }
    }
     
    public static void resumeJob(String name, String group){
        try {
        	Scheduler scheduler = schedulerFactory.getScheduler();
            JobKey jobKey = JobKey.jobKey(name, group);         
            scheduler.resumeJob(jobKey);
            log.info("恢复作业=> [作业名称：" + name + " 作业组：" + group + "] ");
        } catch (SchedulerException e) {
            e.printStackTrace();
            log.error("恢复作业=> [作业名称：" + name + " 作业组：" + group + "]=> [失败]");
        }       
    }
     
    public static void modifyTime(String name, String group, String cronExpression){       
        try {
        	Scheduler scheduler = schedulerFactory.getScheduler();
            TriggerKey tk = TriggerKey.triggerKey(name, group);
            //构造任务触发器
            Trigger trg = newTrigger()
                    .withIdentity(name, group)
                    .withSchedule(cronSchedule(cronExpression))
                    .build();       
            scheduler.rescheduleJob(tk, trg);
            log.info("修改作业触发时间=> [作业名称：" + name + " 作业组：" + group + "] ");
        } catch (SchedulerException e) {
            e.printStackTrace();
            log.error("修改作业触发时间=> [作业名称：" + name + " 作业组：" + group + "]=> [失败]");
        }
    }
 
    public static void start() {
        try {
        	Scheduler scheduler = schedulerFactory.getScheduler();
            scheduler.start();
            log.info("启动调度器 ");
        } catch (SchedulerException e) {
            e.printStackTrace();
            log.error("启动调度器=> [失败]");
        }
    }
 
    public static void shutdown() {
        try {
        	Scheduler scheduler = schedulerFactory.getScheduler();
        	if(!scheduler.isShutdown()) {  
                scheduler.shutdown();  
                log.info("停止调度器 ");
            }  
            
        } catch (SchedulerException e) {
            e.printStackTrace();
            log.error("停止调度器=> [失败]");
        }
    }
    
    public static void main(String[] args) throws InterruptedException{
    	QuartzManager.addJob("test job", QuartzManager.JOB_GROUP_NAME, QuartzTestJob.class, "0/3 * * * * ?");
    	Thread.sleep(5000);
    	System.out.println("【修改时间】开始(每1秒输出一次)...");  
    	QuartzManager.modifyTime("test job", QuartzManager.JOB_GROUP_NAME, "15/1 * * * * ?");  
    	Thread.sleep(6000);  
//    	System.out.println("【移除定时】开始...");  
//    	QuartzManager.removeJob("test job",QuartzManager.JOB_GROUP_NAME);  
//    	System.out.println("【移除定时】成功");
    	QuartzManager.shutdown();
    }
}