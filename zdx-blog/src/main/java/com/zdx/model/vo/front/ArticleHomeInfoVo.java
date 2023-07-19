package com.zdx.model.vo.front;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zdx.Constants;
import com.zdx.entity.zdx.Category;
import com.zdx.entity.zdx.Tag;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ArticleHomeInfoVo {
    private Long id;

    private String title;

    @JsonFormat(pattern = Constants.FORMAT_STRING)
    private Date createTime;

    @JsonFormat(pattern = Constants.FORMAT_STRING)
    private Date updateTime;

    private Long viewCount;

    private Category category;

    private String cover;

    private String content;

    private List<Tag> tagVOList;

    private Long likeCount;

    private ArticlePaginationVO lastArticle;

    private ArticlePaginationVO nextArticle;
}
