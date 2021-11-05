package com.xxxx;

import com.rabbitmq.client.Channel;
import com.xxxx.server.pojo.Employee;
import com.xxxx.server.pojo.MailConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.lang.model.element.VariableElement;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Date;

/**
 * @description: 消息接收者
 * @author: 吉祥
 * @created: 2021/11/04 14:57
 */
@Component
public class MailReceiver {

    public static final Logger LOGGER = LoggerFactory.getLogger(MailReceiver.class);

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private MailProperties mailProperties;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private RedisTemplate redisTemplate;

    @RabbitListener(queues = MailConstants.MAIL_QUEUE_NAME)
    public void receive(Message message, Channel channel){
        //从消息中获取员工信息
        Employee employee = (Employee) message.getPayload();
        //获取消息头
        MessageHeaders headers = message.getHeaders();
        //获取消息序号
        Long tag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);
        //获取消息id
        String msgId = (String) headers.get("spring_returned_message_correlation");
        //获取redis
        HashOperations hashOperations = redisTemplate.opsForHash();
        try {
            if (hashOperations.entries("mail_log").containsKey(msgId)){
                LOGGER.error("消息已经被消费=================》{}",msgId);
                /**
                 * 手动确认
                 * tag:消息序号
                 * multiple:是否确认多条
                 */
                channel.basicAck(tag,false);
                return;
            }
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper =  new MimeMessageHelper(mimeMessage);
            //设置发件人
            helper.setFrom(mailProperties.getUsername());
            //设置收件人
            helper.setTo(employee.getEmail());
            //设置主题
            helper.setSubject("入职欢迎邮件");
            //设置发送日期
            helper.setSentDate(new Date());
            //设置内容
            Context context = new Context();
            context.setVariable("name",employee.getName());
            context.setVariable("posName",employee.getPosition().getName());
            context.setVariable("joblevelName",employee.getJoblevel().getName());
            context.setVariable("departmentName",employee.getDepartment().getName());
            String mail = templateEngine.process("mail", context);
            helper.setText(mail,true);
            //发送
            javaMailSender.send(mimeMessage);
            LOGGER.info("消息发送成功");
            //将消息id存到redis
            hashOperations.put("mail_log",msgId,"OK");
            //手动确认消息
            channel.basicAck(tag,false);
        } catch (Exception e) {
            try {
                /**
                 * 手动确认
                 * tag:消息序号
                 * requeue:是否返回到队列
                 */
                channel.basicNack(tag,false,true);
            } catch (IOException ex) {
                LOGGER.error("邮件发送失败=========>{}",ex.getMessage());
            }
            LOGGER.error("邮件发送失败=========>{}",e.getMessage());
        }

    }


}

