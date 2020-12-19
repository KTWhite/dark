package com.github.dark.app;

import com.github.dark.biz.LoginBiz;
import com.github.dark.entity.User;
import com.github.dark.service.MyUserDetailsService;
import com.github.dark.utils.JwtUtils;
import com.github.dark.vo.request.AuthenticationRequest;
import com.github.dark.vo.response.AuthenticationResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Api("用户认证登录")
@RequestMapping("/auth")
public class AuthController {
    @Resource
    private LoginBiz loginBiz;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private JwtUtils jwtUtils;
    @RequestMapping(value = "/authenticate",method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception{
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),authenticationRequest.getPassword())
            );
        }catch (BadCredentialsException e){
            throw new Exception("Incorrect username or password",e);
        }
       final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
       final String token = jwtUtils.generateToken(userDetails);
       return ResponseEntity.ok(new AuthenticationResponse(token));
    }
}
