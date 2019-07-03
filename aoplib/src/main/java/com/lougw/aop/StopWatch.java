package com.lougw.aop;

import android.content.ContentValues;
import android.database.Cursor;

import com.lougw.aop.db.StopWatchColumns;

import java.util.concurrent.TimeUnit;

public class StopWatch {
    private long startTime;
    private long endTime;
    private long elapsedTime;
    private String className;
    private String methodName;

    public StopWatch() {
        //empty
    }

    private void reset() {
        startTime = 0;
        endTime = 0;
        elapsedTime = 0;
    }

    public void start() {
        reset();
        startTime = System.nanoTime();
    }

    public void stop() {
        if (startTime != 0) {
            endTime = System.nanoTime();
            elapsedTime = endTime - startTime;
        } else {
            reset();
        }
    }

    public long getTotalTimeMillis() {
        return (elapsedTime != 0) ? TimeUnit.NANOSECONDS.toMillis(endTime - startTime) : 0;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public StopWatch(Cursor cursor) {
        startTime = cursor.getLong(cursor
                .getColumnIndex(StopWatchColumns.START_TIME));
        endTime = cursor.getLong(cursor
                .getColumnIndex(StopWatchColumns.END_TIME));
        elapsedTime = cursor.getLong(cursor
                .getColumnIndex(StopWatchColumns.ELAPSED_TIME));
        className = cursor.getString(cursor
                .getColumnIndex(StopWatchColumns.CLASS_NAME));
        methodName = cursor.getString(cursor
                .getColumnIndex(StopWatchColumns.METHOD_NAME));
    }

    synchronized public ContentValues toContentValues() {
        ContentValues value = new ContentValues();
        value.put(StopWatchColumns.START_TIME, startTime);
        value.put(StopWatchColumns.END_TIME, endTime);
        value.put(StopWatchColumns.ELAPSED_TIME, getTotalTimeMillis());
        value.put(StopWatchColumns.CLASS_NAME, className);
        value.put(StopWatchColumns.METHOD_NAME, methodName);
        return value;
    }

}
