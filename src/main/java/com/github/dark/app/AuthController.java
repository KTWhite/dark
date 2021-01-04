package com.github.dark.app;

import com.github.dark.commom.ResultData;
import com.github.dark.constants.CommonMessage;
import com.github.dark.entity.LoginUser;
import com.github.dark.service.MyUserDetailsService;
import com.github.dark.utils.IpUtils;
import com.github.dark.utils.JwtUtils;
import com.github.dark.vo.request.AuthenticationRequest;
import com.github.dark.vo.request.RegisterReq;
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
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

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
            loginUser.setLoginTime(new Date().getTime());
            Browser browser = UserAgent.parseUserAgentString(request.getHeader("user-agent")).getBrowser();
            loginUser.setBrowser(browser.getName());
            String Ip = ipUtils.getIpAddr(request);
            //TODO IP地址 和 详细地址
            loginUser.setIpaddr(Ip);
            loginUser.setToken(token);
            jwtUtils.refreshToken(loginUser);
        }
       return ResponseEntity.ok(new AuthenticationResponse(token));
    }

    @RequestMapping(value = "/verityToken",method = RequestMethod.POST)
    public ResponseEntity<?> verityAuthenticationToken(@RequestBody VerityToeknRequest verityToeknRequest){
        Boolean verified = jwtUtils.validateToken(verityToeknRequest.getToken(), verityToeknRequest.getUsername());
        if (verified){
            return ResponseEntity.ok("验证成功!");
        }else{
            return ResponseEntity.ok("验证失败!");
        }
    }

    @RequestMapping(value = "/ign/register",method = RequestMethod.POST)
    public ResponseEntity<?> register(@RequestBody RegisterReq registerReq){
        ResultData<Boolean> resultData = new ResultData<>();
        boolean save = userDetailsService.register(registerReq) > 0 ? true : false;
        if (save){
            resultData.setData(true);
        }else{
            resultData.setData(false);
            resultData.setMsg("注册失败");
            resultData.setCode(CommonMessage.ERROR);
        }
        return ResponseEntity.ok(resultData);
    }
}
