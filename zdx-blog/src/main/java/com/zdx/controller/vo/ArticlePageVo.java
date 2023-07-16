package com.zdx.controller.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zdx.Constants;
import com.zdx.entity.zdx.Category;
import com.zdx.entity.zdx.Tag;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ArticlePageVo {

    private Long id;

    private String articleTitle;

    private String articleCover;

    private Boolean isTop;


    @JsonFormat(pattern = Constants.FORMAT_STRING)
    private Date createTime;

    private List<Tag> tagVOList;

    private Category category;
}
