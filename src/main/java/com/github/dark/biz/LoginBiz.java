package com.github.dark.biz;

import com.github.dark.entity.User;
import com.github.dark.mapper.LoginMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class LoginBiz {

    @Resource
    private LoginMapper loginMapper;

    public List<User> signIn(){
        return loginMapper.selectAll();
    }
}
