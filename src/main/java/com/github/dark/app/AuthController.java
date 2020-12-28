package com.github.dark.app;

import com.github.dark.annotation.IgnoreToken;
import com.github.dark.biz.LoginBiz;
import com.github.dark.entity.LoginUser;
import com.github.dark.entity.SysUser;
import com.github.dark.service.MyUserDetailsService;
import com.github.dark.utils.IpUtils;
import com.github.dark.utils.JwtUtils;
import com.github.dark.vo.request.AuthenticationRequest;
import com.github.dark.vo.request.VerityToeknRequest;
import com.github.dark.vo.response.AuthenticationResponse;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.UserAgent;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import sun.rmi.runtime.Log;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.swing.*;
import java.util.Date;
import java.util.logging.Logger;

@RestController
@Api("用户认证登录")
@RequestMapping("/auth")
@Slf4j
public class AuthController {
    private static final String TAG="AuthController";

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private IpUtils ipUtils;

    @RequestMapping(value = "/authenticate",method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest, HttpServletRequest request) throws Exception{
        String token=null;
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),authenticationRequest.getPassword())
            );
        }catch (BadCredentialsException e){
            throw new Exception("Incorrect username or password",e);
        }
        LoginUser loginUser = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        if (loginUser!=null){
            token = jwtUtils.generateToken(loginUser);
            Date loginTime= new Date();
            loginUser.setLoginTime(loginTime.getTime());
            Browser browser = UserAgent.parseUserAgentString(request.getHeader("user-agent")).getBrowser();
            loginUser.setBrowser(browser.getName());
            String Ip = ipUtils.getIpAddr(request);
            loginUser.setIpaddr(Ip);
            loginUser.setLoginLocation(ipUtils.ipToAddress(Ip));
            //TODO
            System.out.println("用户信息："+loginUser);
            jwtUtils.refreshToken(loginUser);
        }

       return ResponseEntity.ok(new AuthenticationResponse(token));
    }
    @IgnoreToken
    @RequestMapping(value = "/verityToken",method = RequestMethod.POST)
    public ResponseEntity<?> verityAuthenticationToken(@RequestBody VerityToeknRequest verityToeknRequest){
        Boolean veritied = jwtUtils.validateToken(verityToeknRequest.getToken(), verityToeknRequest.getUsername());
        if (veritied){
            return ResponseEntity.ok("验证成功!");
        }else{
            return ResponseEntity.ok("验证失败!");
        }
    }
}
