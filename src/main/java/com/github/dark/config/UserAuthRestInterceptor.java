package com.github.dark.config;

import com.github.dark.annotation.IgnoreToken;
import com.github.dark.entity.LoginUser;
import com.github.dark.entity.SysUser;
import com.github.dark.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class UserAuthRestInterceptor extends HandlerInterceptorAdapter {
    private static final String TAG="UserAuthRestInterceptor";
    @Autowired
    private JwtUtils jwtUtils;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        IgnoreToken annotation = handlerMethod.getBeanType().getAnnotation(IgnoreToken.class);
        if (annotation!=null){
            annotation = handlerMethod.getMethodAnnotation(IgnoreToken.class);
            log.debug(TAG,"忽视token，不进行拦截");
            return super.preHandle(request, response, handler);
        }else{
            //拦截请求，进行请求校验token，通过token获取请求携带的令牌，即用户信息
            //如果用户信息不为空，进行token校验
            LoginUser loginUser = jwtUtils.getLoginUser(request);
            if (loginUser!=null){
                jwtUtils.verifyToken(loginUser);
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
            return super.preHandle(request, response, handler);
        }
    }
}
