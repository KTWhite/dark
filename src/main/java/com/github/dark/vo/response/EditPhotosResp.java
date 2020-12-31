package com.github.dark.vo.response;

import lombok.Data;

@Data
public class EditPhotosResp  {
    private Integer id;
    private String imgName;
    private String imgUrl;
    private Integer imgType;
    private Integer imgParent;
}
