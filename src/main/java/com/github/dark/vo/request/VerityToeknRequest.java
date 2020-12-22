package com.github.dark.vo.request;

import com.github.dark.entity.SysUser;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import org.springframework.security.core.userdetails.UserDetails;

@Data
public class VerityToeknRequest {
    private String token;
    private String  username;
}
