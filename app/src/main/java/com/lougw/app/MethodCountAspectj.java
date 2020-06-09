package com.lougw.app;

import com.lougw.aop.AOPUtil;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class MethodCountAspectj {
    private static final String TAG = MethodCountAspectj.class.getSimpleName();

    @Pointcut("call(* *(..))")
    public void invokeMethod() {

    }

    @Pointcut("call(* com.lougw.aop..*(..))")
    public void invokeWatch() {

    }

    @Around("invokeMethod() && !invokeWatch()")
    public void aroundMethodExecution(final ProceedingJoinPoint joinPoint) {
        AOPUtil.getInstance().aroundMethodExecution(joinPoint, Thread.currentThread().getName());
    }

}
