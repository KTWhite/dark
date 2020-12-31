package com.github.dark.vo.request;

import lombok.Data;

@Data
public class BaseRequest {
    private Integer pageNo=1;
    private Integer pageSize=10;
}
