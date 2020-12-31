package com.github.dark.aop;

import com.github.dark.config.BaseContextHandler;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import javax.naming.NoPermissionException;

@Aspect
@Component
public class PhotosAspect {
    @Around("execution(* com.github.dark.app.PhotoGalleryController.*(..))")
    public Object checkPhotos(ProceedingJoinPoint joinPoint) throws Throwable{
        String userRole = BaseContextHandler.getUserRole();
        if (!StringUtils.equals(userRole,"vip")){
            throw new NoPermissionException("Vip用户才可使用此功能");
        }
        return joinPoint.proceed();
    }
}
