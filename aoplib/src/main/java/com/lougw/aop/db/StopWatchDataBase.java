package com.lougw.aop.db;


import android.database.Cursor;

import com.lougw.aop.StopWatch;


public interface StopWatchDataBase {
    long insert(StopWatch StopWatch);

    Cursor queryStopWatch(String[] projection, String selection,
                          String[] selectionArgs, String orderBy);
}
