package com.lougw.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
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

    @Pointcut("call(* android.content.ContentValues.*(..))")
    public void invokeContentValues() {

    }

    @Pointcut("call(* android.graphics.Canvas.*(..))")
    public void invokeCanvas() {

    }

    @Pointcut("call(* com.chehejia.car.fmradio.ui2.PlayIndicator.*(..))")
    public void invokePlayIndicator() {

    }

    @Around("invokeMethod() && !invokeWatch() && !invokeContentValues() && !invokeCanvas() && !invokePlayIndicator()")
    public void aroundMethodExecution(final ProceedingJoinPoint joinPoint) {
        AOPUtil.getInstance().aroundMethodExecution(joinPoint,true);
    }
    @Before("invokeMethod() && !invokeWatch() && !invokeContentValues() && !invokeCanvas() && !invokePlayIndicator()")
    public void beforeMethodExecution(final JoinPoint joinPoint) {

        AOPUtil.getInstance().onBeforeMethodExecution(joinPoint);


    }
}
