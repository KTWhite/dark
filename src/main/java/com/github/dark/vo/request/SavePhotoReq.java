package com.github.dark.vo.request;

import lombok.Data;

@Data
public class SavePhotoReq {
    private Integer id;
    private String imgName;
    private String imgUrl;
    private Integer imgType;
    private Integer imgParent;
}
