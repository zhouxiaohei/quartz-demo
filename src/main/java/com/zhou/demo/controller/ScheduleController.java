package com.zhou.demo.controller;

import com.zhou.demo.service.MyScheduler;
import io.swagger.annotations.ApiOperation;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName ScheduleController
 * @Description
 * @Author JackZhou
 * @Date 2019/7/29  15:29
 **/

@RestController
@SpringBootApplication
public class ScheduleController {

    @RequestMapping(value = "/start", method = RequestMethod.GET)
    @ApiOperation("开始定时任务")
    @ResponseBody
    public String start() throws SchedulerException {
        MyScheduler.startJob();
        return "success";
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    @ApiOperation("删除定时任务")
    @ResponseBody
    public String delete() throws SchedulerException {
        MyScheduler.deleteJob();
        return "success";
    }

    @RequestMapping(value = "/modify", method = RequestMethod.GET)
    @ApiOperation("修改定时任务策略")
    @ResponseBody
    public String modify() throws SchedulerException {
        MyScheduler.modifyJob("0/1 * * * * ?");
        return "1";
    }

    @RequestMapping(value = "/status", method = RequestMethod.GET)
    @ApiOperation("查看定时任务状态")
    @ResponseBody
    public String status() throws SchedulerException {
        return MyScheduler.getJobStatus();
    }

    @RequestMapping(value = "/pause", method = RequestMethod.GET)
    @ApiOperation("暂停定时任务")
    @ResponseBody
    public  String pause() throws SchedulerException {
        MyScheduler.pauseJob();
        return "1";
    }

    @RequestMapping(value = "/resume", method = RequestMethod.GET)
    @ApiOperation("重启定时任务")
    @ResponseBody
    public String resume() throws SchedulerException {
        MyScheduler.resumeJob();
        return "1";
    }

    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;


    @RequestMapping(value = "/viewScheduler", method = RequestMethod.GET)
    @ApiOperation("查看Scheduler")
    @ResponseBody
    public String viewScheduler(){
        return schedulerFactoryBean.getScheduler().toString();
    }
}
