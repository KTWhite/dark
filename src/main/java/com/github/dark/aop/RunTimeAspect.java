package com.github.dark.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class RunTimeAspect {

    @Pointcut("@annotation(com.github.dark.annotation.RunTimeLog)")
    private void pointCut(){}

    @Around("pointCut()")
    public Object handleTime(ProceedingJoinPoint joinPoint){

        try {
            long start = System.currentTimeMillis();
            Object proceed = joinPoint.proceed();
            long time = (System.currentTimeMillis() - start) ;
            System.out.println("该方法耗时："+time+"毫秒");
            System.out.println("该方法耗时："+(time/1000)+"秒");
            return proceed;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;

    }
}
