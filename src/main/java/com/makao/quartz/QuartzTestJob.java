package com.makao.quartz;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class QuartzTestJob implements Job {
	DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
@Override
public void execute(JobExecutionContext context) throws JobExecutionException {
	
	System.out.println("任务正在执行，执行时间: " + df.format(new Date()));

}

/**
 * @throws JobExecutionException
 * 与spring的MethodInvokingJobDetailFactoryBean集成后不需要继承Job并运行execute(JobExecutionContext context)，
 * 可以自行指定任何方法执行任务
 */
public void execute() throws JobExecutionException {
	
	System.out.println("任务正在执行，执行时间: " + df.format(new Date()));

}

} 