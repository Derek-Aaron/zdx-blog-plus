package com.zdx.security.vo;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.zdx.Constants;
import lombok.Data;

import java.util.Date;

@Data
public class UserAgent {

    private String personId;

    private String username;

    private String nickname;

    private String ip;

    private String address;

    private String os;

    private String browser;

    @JsonFormat(pattern = Constants.FORMAT_STRING)
    private Date loginTime;
}
