package com.zdx.service.tk;

import com.zdx.model.dto.MailDto;

public interface EmailService {

    /**
     * 发送简单邮件
     * @param mailDto 邮件
     * @return 返回
     */
    Boolean sendSimple(MailDto mailDto);


    /**
     * 发送html邮件
     * @param mailDto 邮件
     * @return 返回
     */
    Boolean sendHtml(MailDto mailDto);


    /**
     * 校验邮箱验证码code
     * @param email 邮箱
     * @param code 验证码
     * @return 返回
     */
    Boolean checkCode(String email, String code);
}
