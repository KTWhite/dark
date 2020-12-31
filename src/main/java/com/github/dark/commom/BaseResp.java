package com.github.dark.commom;

import lombok.Data;

import java.util.List;

@Data
public class BaseResp<T> {
    private List<T> list;
    private Long total;
}