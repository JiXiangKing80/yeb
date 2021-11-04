package com.xxxx.server.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.xxxx.server.pojo.Employee;
import com.xxxx.server.pojo.MailConstants;
import com.xxxx.server.pojo.MailLog;
import com.xxxx.server.service.IEmployeeService;
import com.xxxx.server.service.IMailLogService;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @description: 邮箱发送定时任务
 * @author: 吉祥
 * @created: 2021/11/04 20:06
 */
@Component
public class MailTask {

    @Autowired
    private IMailLogService mailLogService;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private IEmployeeService employeeService;

    @Scheduled(cron = "0/10 * * * * ?") //每十秒钟执行一次
    public void run(){
        //获取未投递成功成功(重新尝试时间小于当前时间)数据
        List<MailLog> list = mailLogService.list(new QueryWrapper<MailLog>().eq("status", 0).lt("tryTime", LocalDateTime.now()));
        for (MailLog mailLog : list) {
            //判断当前重新尝试次数大于当前设置最大尝试次数
            if (mailLog.getCount()>= MailConstants.MAX_TRY_COUNT){
                //设置为失败
                mailLogService.update(new UpdateWrapper<MailLog>().eq("msgId",mailLog.getMsgId()).set("status",2));
                continue;
            }
            //重新投递
            Employee emp = employeeService.getEmployee(mailLog.getEid()).get(0);
            mailLogService.update(new UpdateWrapper<MailLog>().set("count",mailLog.getCount()+1).set("updateTime",
                    LocalDateTime.now()).set("tryTime",LocalDateTime.now().plusMinutes(MailConstants.MSG_TIMEOUT)).eq("msgId",mailLog.getMsgId()));
            rabbitTemplate.convertAndSend(MailConstants.MAIL_EXCHANGE_NAME,MailConstants.MAIL_ROUTING_KEY_NAME,emp,new CorrelationData(mailLog.getMsgId()));

        }
    }
}

