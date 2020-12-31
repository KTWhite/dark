package com.github.dark.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "dark_photo_comment")
@Data
public class PhotoCommentEntity {
    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "comment")
    private String comment;

    @Column(name = "userId")
    private Integer userId;

    @Column(name = "create_time")
    private Date createTime;


}
