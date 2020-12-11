package com.zhou.demo.service;

import lombok.extern.slf4j.Slf4j;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Date;

/**
 * @ClassName MyScheduler
 * @Description
 * @Author JackZhou
 * @Date 2019/7/29  15:36
 **/
@Slf4j
@Component
public class MyScheduler implements ApplicationListener<ContextRefreshedEvent> {


    @PostConstruct
    public void init(){
        try {
            this.scheduleJobs();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
//        try {
//            this.scheduleJobs();
//        } catch (SchedulerException e) {
//            e.printStackTrace();
//        }
        log.info("----走起---");
    }

    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    static Scheduler scheduler;

    public void scheduleJobs() throws SchedulerException {
        scheduler = schedulerFactoryBean.getScheduler();
        //startJob(); // 每5秒执行一次
        startJob2(); // 每7秒执行一次
    }

    public static void startJob() throws SchedulerException {
        JobDetail jobDetail = JobBuilder.newJob(ScheduledJob.class).withIdentity("job1", "group1").build();
        jobDetail.getJobDataMap().put("name", "李四");
       // JobDetail jobDetail = JobBuilder.newJob(ScheduledJob.class).withIdentity("job1", "group1").build();
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule("0/5 * * * * ?");
        CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity("trigger1", "group1")
                .withSchedule(scheduleBuilder).build();
        scheduler.scheduleJob(jobDetail, cronTrigger);
    }

    public static void deleteJob() throws SchedulerException {
        scheduler.deleteJob(JobKey.jobKey("job1", "group1"));
    }

    public static void startJob2() throws SchedulerException {
        JobDetail jobDetail = JobBuilder.newJob(ScheduledJob.class).withIdentity("job2", "group1").build();
        jobDetail.getJobDataMap().put("name", "张三");
        // JobDetail jobDetail = JobBuilder.newJob(ScheduledJob.class).withIdentity("job1", "group1").build();
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule("0/7 * * * * ?");
        CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity("trigger2", "group1")
                .withSchedule(scheduleBuilder).build();
        scheduler.scheduleJob(jobDetail, cronTrigger);
    }


    public static void modifyJob(String cron) throws SchedulerException {
        TriggerKey triggerKey = TriggerKey.triggerKey("trigger1", "group1");
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cron);
        CronTrigger newTrigger = TriggerBuilder.newTrigger().withIdentity("trigger1", "group1")
                .withSchedule(scheduleBuilder).build();
        scheduler.rescheduleJob(triggerKey, newTrigger);
    }

    public static String getJobStatus() throws SchedulerException {
        TriggerKey triggerKey = TriggerKey.triggerKey("trigger1", "group1");
        return scheduler.getTriggerState(triggerKey).name();
    }

    public static void pauseJob() throws SchedulerException {
        scheduler.pauseJob(JobKey.jobKey("job1", "group1"));
    }

    public static void resumeJob() throws SchedulerException {
        scheduler.resumeJob(JobKey.jobKey("job1", "group1"));
    }

}

@Slf4j
class ScheduledJob implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        String name = (String)context.getJobDetail().getJobDataMap().get("name");

        log.info("{}执行定时任务 {}", name, new Date());
    }
}

