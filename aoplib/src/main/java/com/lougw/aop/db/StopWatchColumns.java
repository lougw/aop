package com.lougw.aop.db;

import android.provider.BaseColumns;

/**
 * 数据库列信息
 */
public final class StopWatchColumns implements BaseColumns {

    /**
     * 开始时间
     */
    public static final String START_TIME = "start_time";
    /**
     * 结束时间
     */
    public static final String END_TIME = "end_time";
    /**
     * 所需时间
     */
    public static final String ELAPSED_TIME = "elapsed_time";
    /**
     * 类名
     */
    public static final String CLASS_NAME = "class_name";
    /**
     * 方法名字
     */
    public static final String METHOD_NAME = "method_name";

}
