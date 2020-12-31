package com.github.dark.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

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
}
