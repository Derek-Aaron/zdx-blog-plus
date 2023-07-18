package com.zdx.model.vo;


import lombok.Data;

@Data
public class AlbumPhotoCountVo {

    private Long id;

    private String name;

    private String cover;

    private String description;

    private Boolean status;

    private Long photoCount;
}
