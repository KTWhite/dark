package com.github.dark.config;

import com.github.dark.entity.LoginUser;
import com.github.dark.entity.SysUser;
import com.github.dark.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        LoginUser loginUser = jwtUtils.getLoginUser(request);
        if (loginUser!=null){
            jwtUtils.verifyToken(loginUser);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            logger.info("进入自定义拦截方法");
            SysUser user = loginUser.getUser();
            BaseContextHandler.setUserID(user.getId()+"");
            BaseContextHandler.setUsername(user.getUserName());
            BaseContextHandler.setToken(loginUser.getToken());
            logger.info("用户信息解析完毕");

        }
        chain.doFilter(request,response);
    }
}
