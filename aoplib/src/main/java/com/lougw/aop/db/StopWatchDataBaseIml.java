package com.lougw.aop.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDiskIOException;

import com.lougw.aop.StopWatch;


/**
 * <b>单列模式的数据库操作类</b>
 */
public class StopWatchDataBaseIml implements StopWatchDataBase {
    private StopWatchDBHelper helper;
    private SQLiteDatabase db;
    private SQLiteDatabase newDB;
    private Object mObjLock = new Object();
    private volatile int mTryCount = 0;
    private static final int MAX_TRY_COUNT = 10;
    private static final int TIME_TO_WAIT_NEXT_OPERATE = 300;

    private boolean checkTryCountValid() {
        if (mTryCount > MAX_TRY_COUNT)
            return false;
        try {
            Thread.sleep(TIME_TO_WAIT_NEXT_OPERATE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();

        }
        return true;
    }

    private void incrementTryCount() {
        mTryCount++;
    }

    private SQLiteDatabase getDb() {
        try {
            if (null != db && !db.isOpen()) {
                db = helper.getWritableDatabase();
            }

        } catch (Exception e) {
            db = null;
        }
        return db;
    }

    private void reOpenDb() {
        try {
            if (null != db && db.isOpen()) {
                db.close();
            }
            db = helper.getWritableDatabase();
        } catch (Exception e) {

        }
    }

    public StopWatchDataBaseIml(Context context) {
        try {
            helper = StopWatchDBHelper.getInstance(context);
        } catch (Exception e) {

        }
        try {
            db = helper.getWritableDatabase();
        } catch (Exception e) {

        }

    }

    @Override
    public long insert(StopWatch request) {
        long insertResult = 0;
        ContentValues values = request.toContentValues();
        synchronized (mObjLock) {
            newDB = getDb();
            try {
                if (newDB != null) {
                    insertResult = newDB.insert(StopWatchDBHelper.TABLE_NAME, null, values);
                }

            } catch (SQLiteDiskIOException e) {
                reOpenDb();
                newDB = getDb();
                incrementTryCount();
                if (checkTryCountValid())
                    insertResult = insert(request);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return insertResult;

    }


    @Override
    public Cursor queryStopWatch(String[] projection, String selection,
                                 String[] selectionArgs, String orderBy) {
        Cursor cursorResultCursor = null;
        synchronized (mObjLock) {
            newDB = getDb();
            try {
                if (newDB != null) {
                    cursorResultCursor = newDB.query(StopWatchDBHelper.TABLE_NAME, projection,
                            selection,
                            selectionArgs, null, null, orderBy);
                }
            } catch (SQLiteDiskIOException e) {
                if (null != cursorResultCursor) {
                    cursorResultCursor.close();
                    cursorResultCursor = null;
                }
                reOpenDb();
                newDB = getDb();
                incrementTryCount();
                if (checkTryCountValid())
                    queryStopWatch(projection, selection, selectionArgs, orderBy);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return cursorResultCursor;
    }

}
