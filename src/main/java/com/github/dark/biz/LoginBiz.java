package com.github.dark.biz;

import com.github.dark.entity.SysUser;
import com.github.dark.mapper.SysUserMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class LoginBiz {

    @Resource
    private SysUserMapper sysUserMapper;

    public List<SysUser> signIn(){
        return sysUserMapper.selectAll();
    }
}
