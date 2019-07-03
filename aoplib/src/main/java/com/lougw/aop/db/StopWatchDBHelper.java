package com.lougw.aop.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 断点续传的数据库管理
 */
public class StopWatchDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "aop.db";

    public static final String TABLE_NAME = "aop";

    private static final int DATABASE_VERSION = 1;

    private static StopWatchDBHelper instance;

    private StopWatchDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public synchronized static StopWatchDBHelper getInstance(Context context) {
        if (instance == null) {
            instance = new StopWatchDBHelper(context);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createDownloadTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private void createDownloadTable(SQLiteDatabase db) {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE ").append(TABLE_NAME).append("(")
                .append(StopWatchColumns._ID)
                .append(" INTEGER PRIMARY KEY autoincrement,")
                .append(StopWatchColumns.ELAPSED_TIME).append(" Long,")
                .append(StopWatchColumns.CLASS_NAME).append(" TEXT,")
                .append(StopWatchColumns.METHOD_NAME).append(" TEXT,")
                .append(StopWatchColumns.START_TIME).append(" Long,")
                .append(StopWatchColumns.END_TIME).append(" Long")
                .append(");");
        db.execSQL(sb.toString());
    }

}
