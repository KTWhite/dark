package com.github.dark.service;

import com.github.dark.commom.ResultData;
import com.github.dark.constants.CommonMessage;
import com.github.dark.entity.LoginUser;
import com.github.dark.entity.SysUser;
import com.github.dark.mapper.SysUserMapper;
import com.github.dark.vo.request.RegisterReq;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class MyUserDetailsService implements UserDetailsService {
    private static final String TAG="MyUserDetailsService";
    @Resource
    private SysUserMapper sysUserMapper;

    @Override
    public LoginUser loadUserByUsername(String s) throws UsernameNotFoundException {
        SysUser sysUser = sysUserMapper.selectUserByUserName(s);
        if (sysUser==null){
            log.debug(TAG,"登录用户：{} 不存在.",s);
            throw new UsernameNotFoundException("登录用户：" + s + " 不存在");
        }else {
            return createLoginUser(sysUser);
        }
    }
    public LoginUser createLoginUser(SysUser user)
    {
        return new LoginUser(user);
    }

    public Integer register(RegisterReq registerReq){
        SysUser sysUser = new SysUser();
        sysUser.setUserName(registerReq.getUsername());
        String password = registerReq.getPassword();
        String pwd = DigestUtils.md5DigestAsHex(password.getBytes());
        sysUser.setPassWord(pwd);
       return sysUserMapper.insert(sysUser);
    }
}
