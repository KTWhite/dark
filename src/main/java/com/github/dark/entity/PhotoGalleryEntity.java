package com.github.dark.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "dark_photo_gallery")
@Data
public class PhotoGalleryEntity {

    @Id
    private Integer id;

    @Column(name = "img_name")
    private String imgName;

    @Column(name = "img_url")
    private String imgUrl;

    @Column(name = "img_type")
    private Integer imgType;

    @Column(name = "img_parent")
    private Integer imgParent;

    @Column(name = "userId")
    private Integer userId;

    @Column(name = "create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @Column(name = "update_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @Column(name = "page_no")
    private Integer pageNo;

    @Column(name = "img_cover")
    private Integer cover;
}
