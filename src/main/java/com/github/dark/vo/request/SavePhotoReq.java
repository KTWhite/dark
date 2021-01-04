package com.github.dark.vo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SavePhotoReq {
    @ApiModelProperty("序号")
    private Integer id;
    @ApiModelProperty("图片名称")
    private String imgName;
    @ApiModelProperty("图片url路径")
    private String imgUrl;
    @ApiModelProperty("图片类型")
    private Integer imgType;
    @ApiModelProperty("上级")
    private Integer imgParent;
    @ApiModelProperty("页码")
    private Integer pageNo;
    @ApiModelProperty("0非封面 1 封面")
    private Integer cover=0;
}
