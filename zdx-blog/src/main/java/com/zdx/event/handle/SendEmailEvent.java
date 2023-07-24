package com.zdx.event.handle;

import com.zdx.enums.SendEmailEnum;
import com.zdx.event.EventHandle;
import com.zdx.event.EventObject;
import com.zdx.model.dto.MailDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Component
@RequiredArgsConstructor
@Slf4j
public class SendEmailEvent implements EventHandle {

    /**
     * 邮箱号
     */
    @Value("${spring.mail.username}")
    private String email;

    private final JavaMailSender javaMailSender;

    private final TemplateEngine templateEngine;
    @Override
    public String getKey() {
        return EventObject.Attribute.SEND_EMAIL_KEY;
    }

    @Override
    public void invokeEvent(EventObject event) {
        MailDto mailDto = event.getSource(MailDto.class);
        SendEmailEnum sendEmailEnum = event.getAttribute(EventObject.Attribute.SEND_EMAIL_TYPE, SendEmailEnum.class);
        if (SendEmailEnum.SIMPLE.equals(sendEmailEnum)) {
            SimpleMailMessage simpleMail = new SimpleMailMessage();
            simpleMail.setFrom(email);
            simpleMail.setTo(mailDto.getToEmail());
            simpleMail.setSubject(mailDto.getSubject());
            simpleMail.setText(mailDto.getContent());
            javaMailSender.send(simpleMail);
        }

        if (SendEmailEnum.HTML.equals(sendEmailEnum)) {
            try {
                MimeMessage mimeMessage = javaMailSender.createMimeMessage();
                MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
                Context context = new Context();
                context.setVariables(mailDto.getContentMap());
                String process = templateEngine.process(mailDto.getTemplate(), context);
                mimeMessageHelper.setFrom(email);
                mimeMessageHelper.setTo(mailDto.getToEmail());
                mimeMessageHelper.setSubject(mailDto.getSubject());
                mimeMessageHelper.setText(process, true);
                javaMailSender.send(mimeMessage);
            } catch (MessagingException e) {
                log.error("发送html邮件异常：{}", e.getMessage(), e);
            }
        }
    }
}
