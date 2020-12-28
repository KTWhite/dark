package com.github.dark.app;

import com.github.dark.annotation.IgnoreToken;
import com.github.dark.biz.LoginBiz;
import com.github.dark.config.BaseContextHandler;
import com.github.dark.entity.SysUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Api("用户登录")
@RequestMapping("/login")
public class LoginController {

    @Resource
    private LoginBiz loginBiz;

    @GetMapping("/SignIn")
    @ApiOperation("用户登录")
    public List<SysUser> SignIn(){
        System.out.println(BaseContextHandler.getUserID());
        return loginBiz.signIn();
    }
}
