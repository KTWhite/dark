package com.github.dark.aop;

import com.github.dark.config.BaseContextHandler;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import javax.naming.NoPermissionException;
import java.util.HashSet;
import java.util.Set;

@Aspect
@Component
public class PhotosAspect {
    private Set<String> permissions=new HashSet<>();
    @Around("execution(* com.github.dark.app.PhotoGalleryController.getPhotos(*))")
    public Object checkPhotos(ProceedingJoinPoint joinPoint) throws Throwable{
        permissions.add("vip");
        permissions.add("admin");
        String userRole = BaseContextHandler.getUserRole();
        if (!permissions.contains(userRole)){
            throw new NoPermissionException("用户权限不足，无法使用该功能！");
        }
        return joinPoint.proceed();
    }
    @Around("execution(* com.github.dark.app.PhotoGalleryController.findPhotosInfoById(*))")
    public Object monitorAction(ProceedingJoinPoint joinPoint) throws Throwable{
        System.out.println("用户点击图片");
        //TODO 记录用户点击行为 进行后期的统计分析
        return joinPoint.proceed();
    }
}
