package com.github.dark.config;

import com.github.dark.entity.LoginUser;
import com.github.dark.entity.SysUser;
import com.github.dark.utils.JwtUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *token过滤器
 */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    private final static Logger logger = LoggerFactory.getLogger(JwtAuthenticationTokenFilter.class);

    @Resource
    private UserDetailsService userDetailsService;
    @Resource
    private JwtUtils jwtUtils;
    private String tokenHeader =  "Authorization";
    private String tokenStart = "Bearer ";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String header = request.getHeader(tokenHeader);
        String token=null;
        if (StringUtils.isNotBlank(header)&&header.startsWith(tokenStart)){
           token = header.replace(tokenStart,"");
        }
        if (StringUtils.isNotBlank(token)){
            LoginUser loginUser = jwtUtils.getLoginUser(request);
            if (loginUser!=null&&loginUser.getUser()!=null){
                SysUser user = loginUser.getUser();
                if (user.getUserName()!=null){
                    String userName = user.getUserName();
                    logger.info("checking authentication {}", userName);
                    if (userName!=null&& SecurityContextHolder.getContext().getAuthentication()==null){
                        //从已有的user缓存中取了出user信息
                        if (jwtUtils.validateToken(token,userName)){
                            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
                            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                            //设置用户登录状态
                            logger.info("authenticated user {}, setting security context",userName);
                            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                            BaseContextHandler.setUsername(user.getUserName());
                            BaseContextHandler.setUserID(user.getId()+"");
                            BaseContextHandler.setUserRole(user.getRole());
                        }
                    }

                }
            }
        }
        chain.doFilter(request, response);
    }
}
