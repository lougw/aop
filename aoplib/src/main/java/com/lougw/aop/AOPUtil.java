package com.lougw.aop;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;

import com.lougw.aop.db.StopWatchDataBaseIml;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;


/**
 *
 */

public class AOPUtil {
    private static final String TAG = AOPUtil.class.getSimpleName();
    /**
     * 线程管理
     */
    private static final int MSG_DO_DOWNLOAD = 1;
    private static Context mContext;
    private static StopWatchDataBaseIml mDataBaseIml;
    private static HandlerThread mAOPManagerThread;
    private static AOPHandler mAOPManagerHandler;

    private static class AopUtilHolder {
        private static final AOPUtil instance = new AOPUtil();
    }

    private AOPUtil() {
    }

    public static AOPUtil getInstance() {
        return AopUtilHolder.instance;
    }

    public void init(Context context) {
        mContext = context.getApplicationContext();
        mDataBaseIml = new StopWatchDataBaseIml(mContext);
        mAOPManagerThread = new HandlerThread("aop manager thread");
        mAOPManagerThread.start();
        mAOPManagerHandler = new AOPHandler(mAOPManagerThread.getLooper());
    }

    class AOPHandler extends Handler {

        public AOPHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_DO_DOWNLOAD:
                    doSave((StopWatch) msg.obj);
                    break;
            }
        }
    }


    public void save(StopWatch info) {
        if (mContext == null || info == null) {
            return;
        }
        if (mAOPManagerHandler != null && mAOPManagerThread.isAlive()) {
            mAOPManagerHandler.sendMessage(mAOPManagerHandler.obtainMessage(MSG_DO_DOWNLOAD, info));
        }
    }

    private void doSave(StopWatch info) {
        try {
            mDataBaseIml.insert(info);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 获取资源
     *
     * @return Resources
     */
    public static Resources getResources() {
        return mContext.getResources();
    }

    /**
     * 跟距id獲得字符串
     *
     * @param resId name
     * @return name
     */
    public static String getResourceEntryName(int resId) {
        try {
            return getResources().getResourceEntryName(resId);
        } catch (Exception e) {
            return "";
        }
    }

    public void aroundMethodExecution(final ProceedingJoinPoint joinPoint, boolean main) {
        try {
            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
            String className = methodSignature.getDeclaringType().getSimpleName();
            String methodName = methodSignature.getName();
            final StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            joinPoint.proceed();
            stopWatch.stop();
            stopWatch.setClassName(className);
            stopWatch.setMethodName(methodName);
            if (Thread.currentThread() == Looper.getMainLooper().getThread() || !main) {
                AOPUtil.getInstance().save(stopWatch);
            }
            Log.d(TAG, buildLogMessage(className, methodName, stopWatch.getTotalTimeMillis()));
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

    }

    private String buildLogMessage(String className, String methodName, long methodDuration) {
        StringBuilder message = new StringBuilder();
        message.append(" --> ");
        message.append(className);
        message.append(" --> ");
        message.append(methodName);
        message.append(" --> ");
        message.append("[  ");
        message.append(methodDuration);
        message.append("ms");
        message.append("  ]");
        return message.toString();
    }

    public void onClickMethodExecution(final JoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String className = methodSignature.getDeclaringType().getSimpleName();
        String methodName = methodSignature.getName();
        String viewId = "";

        if (joinPoint != null && joinPoint.getArgs() != null) {
            for (int i = 0; i < joinPoint.getArgs().length; i++) {
                Object obj = joinPoint.getArgs()[i];
                if (obj != null && obj instanceof View) {
                    viewId = AOPUtil.getResourceEntryName(((View) obj).getId());
                }
            }
        }
        Log.d(TAG, buildLogMessage(className, methodName, viewId));
    }


    private String buildLogMessage(String className, String methodName, String viewId) {
        StringBuilder message = new StringBuilder();
        message.append(" --> ");
        message.append(className);
        message.append(" --> ");
        message.append(methodName);
        message.append(" --> ");
        message.append(" viewId --> ");
        message.append(viewId);
        return message.toString();
    }

    public void onBeforeMethodExecution(final JoinPoint joinPoint) {
        try {
            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
            String className = methodSignature.getDeclaringType().getSimpleName();
            String methodName = methodSignature.getName();
            Log.d(TAG, buildLogMessage(className, methodName));
        } catch (Exception e) {

        }

    }

    private String buildLogMessage(String className, String methodName) {
        StringBuilder message = new StringBuilder();
        message.append(" --> ");
        message.append(className);
        message.append(" --> ");
        message.append(methodName);
        return message.toString();
    }
}