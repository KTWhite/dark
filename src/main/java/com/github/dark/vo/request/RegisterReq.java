package com.github.dark.vo.request;

import lombok.Data;

@Data
public class RegisterReq {
    private String username;
    private String password;
    private String code;
}
