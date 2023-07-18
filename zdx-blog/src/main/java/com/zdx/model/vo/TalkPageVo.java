package com.zdx.model.vo;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.zdx.Constants;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class TalkPageVo {

    private Long id;

    private String avatar;

    private String nickname;

    private String content;

    private Boolean isTop;

    private Boolean status;

    private List<String> imgList;

    @JsonFormat(pattern = Constants.FORMAT_STRING)
    private Date createTime;
}
