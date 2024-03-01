package com.zdx.service.tk.impl;

import com.zdx.Constants;
import com.zdx.model.dto.MailDto;
import com.zdx.service.tk.EmailService;
import com.zdx.service.tk.RedisService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;



@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private MailProperties mailProperties;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private RedisService redisService;
    @Override
    public Boolean sendSimple(MailDto mailDto) {
        SimpleMailMessage simpleMail = new SimpleMailMessage();
        simpleMail.setFrom(mailProperties.getUsername());
        simpleMail.setTo(mailDto.getToEmail());
        simpleMail.setSubject(mailDto.getSubject());
        simpleMail.setText(mailDto.getContent());
        try {
            mailSender.send(simpleMail);
            return true;
        } catch (MailException e) {
            log.error("发送简单邮件异常：{}", e.getMessage(), e);
           return false;
        }
    }

    @Override
    public Boolean sendHtml(MailDto mailDto) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
            Context context = new Context();
            context.setVariables(mailDto.getContentMap());
            String process = templateEngine.process(mailDto.getTemplate(), context);
            mimeMessageHelper.setFrom(mailProperties.getUsername());
            mimeMessageHelper.setTo(mailDto.getToEmail());
            mimeMessageHelper.setSubject(mailDto.getSubject());
            mimeMessageHelper.setText(process, true);
            mailSender.send(mimeMessage);
            return true;
        } catch (MessagingException e) {
            log.error("发送html邮件异常：{}", e.getMessage(), e);
            return false;
        }
    }

    @Override
    public Boolean checkCode(String email, String code) {
        if (!redisService.hasKey(Constants.CODE_KEY + email)) {
           return false;
        }
        String val = redisService.getObject(Constants.CODE_KEY + email);
        return val.equalsIgnoreCase(code);
    }
}
