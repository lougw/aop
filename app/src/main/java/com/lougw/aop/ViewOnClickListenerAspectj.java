package com.lougw.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class ViewOnClickListenerAspectj {
    private static final String TAG = ViewOnClickListenerAspectj.class.getSimpleName();


    @Pointcut("execution(* android.view.View.OnClickListener.onClick(android.view.View))")
    public void onViewClick() {

    }

    @Pointcut("execution(* android.view.View.OnLongClickListener.onLongClick(android.view.View))")
    public void onViewLongClick() {

    }

    @Before("onViewClick() || onViewLongClick()")
    public void onClickMethodExecution(final JoinPoint joinPoint) throws Throwable {
        AOPUtil.getInstance().onClickMethodExecution(joinPoint);
    }
}
