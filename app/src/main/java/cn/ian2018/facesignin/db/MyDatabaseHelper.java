package cn.ian2018.facesignin.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by 陈帅 on 2018/12/12.
 * 数据库创建类
 */

public class MyDatabaseHelper extends SQLiteOpenHelper {
    public static final String HISTORY_TABLE = "create table History("
            + "id integer primary key autoincrement,"
            + "number text,"
            + "activeId integer,"
            + "save integer,"
            + "inTime text,"
            + "outTime text)";

    // 记录签到签离信息
    public static final String SIGN_TABLE = "create table Sign("
            + "id integer primary key autoincrement,"
            + "number text,"
            + "activeId integer,"
            + "nid integer,"
            + "inTime text,"
            + "outTime text)";

    // 名言表
    public static final String SAYING_TABLE = "create table Saying("
            + "id integer primary key autoincrement,"
            + "content text)";

    // 签到分析表
    public static final String ANALYZE_SIGN_TABLE = "create table AnalyzeSign("
            + "id integer primary key autoincrement,"
            + "number text,"
            + "activeName text,"
            + "rule integer,"
            + "inTime text,"
            + "outTime text,"
            + "time text,"
            + "endTime text)";

    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(HISTORY_TABLE);
        db.execSQL(SIGN_TABLE);
        db.execSQL(SAYING_TABLE);
        db.execSQL(ANALYZE_SIGN_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (newVersion) {
        }
    }
}
