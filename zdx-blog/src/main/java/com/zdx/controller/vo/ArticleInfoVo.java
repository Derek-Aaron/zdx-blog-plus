package com.zdx.controller.vo;


import com.zdx.entity.zdx.Category;
import com.zdx.entity.zdx.Tag;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ArticleInfoVo {

    private String id;

    private String articleTitle;

    private String articleCover;

    private String content;

    private Date createTime;

    private Date updateTime;

    private Long viewCount;

    private Long likeCount;

    private Category category;

    private List<Tag> tagVOList;

    private ArticlePaginationVO lastArticle;

    private  ArticlePaginationVO nextArticle;


}
