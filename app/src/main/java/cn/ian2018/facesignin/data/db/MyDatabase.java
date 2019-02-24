package cn.ian2018.facesignin.data.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.ian2018.facesignin.MyApplication;
import cn.ian2018.facesignin.bean.AnalyzeSign;
import cn.ian2018.facesignin.bean.Saying;
import cn.ian2018.facesignin.bean.SignActive;
import cn.ian2018.facesignin.bean.SignItem;
import cn.ian2018.facesignin.utils.Logs;


/**
 * Created by 陈帅 on 2018/12/12.
 * 数据库
 */

public class MyDatabase {
    /**
     * 数据库名
     */
    public static final String DB_NAME = "sign_activity_info";

    /**
     * 数据库版本
     */
    public static final int VERSION = 1;

    private final SQLiteDatabase db;
    private static MyDatabase myDatabase;

    private MyDatabase(Context context) {
        MyDatabaseHelper myDatabaseHelper = new MyDatabaseHelper(context, DB_NAME, null, VERSION);
        db = myDatabaseHelper.getWritableDatabase();
    }

    public synchronized static MyDatabase getInstance() {
        if (myDatabase == null) {
            myDatabase = new MyDatabase(MyApplication.getContext());
        }
        return myDatabase;
    }

    // 保存签到活动
    public void saveSignActive(SignActive signActive) {
        if (signActive != null) {
            ContentValues values = new ContentValues();
            values.put("number", signActive.getNumber());
            values.put("activeId", signActive.getActiveId());
            values.put("inTime", signActive.getInTime());
            values.put("outTime", signActive.getOutTime());
            values.put("save", signActive.getSave());
            db.insert("History", null, values);
        }
    }

    // 更新签到活动状态
    public void updateSignActive(long activeId, boolean save) {
        ContentValues values = new ContentValues();
        values.put("save", save);
        db.update("History", values, "activeId = ?", new String[]{String.valueOf(activeId)});
    }

    // 获得所有未保存成功的活动
    public List<SignActive> getUnSaveActives() {
        List<SignActive> list = new ArrayList<>();
        Cursor cursor = db.query("History", null, "save = ?", new String[]{"0"}, null, null, null);
        while (cursor.moveToNext()) {
            SignActive signActive = new SignActive();
            signActive.setActiveId(cursor.getLong(cursor.getColumnIndex("activeId")));
            signActive.setNumber(cursor.getString(cursor.getColumnIndex("number")));
            signActive.setInTime(cursor.getString(cursor.getColumnIndex("inTime")));
            signActive.setOutTime(cursor.getString(cursor.getColumnIndex("outTime")));
            signActive.setSave(cursor.getInt(cursor.getColumnIndex("save")));
            list.add(signActive);
        }
        cursor.close();
        return list;
    }

    // 将签到记录保存到表中
    public void saveSignItem(SignItem signItem) {
        if (signItem != null) {
            ContentValues values = new ContentValues();
            values.put("number", signItem.getNumber());
            values.put("activeId", signItem.getActiveId());
            values.put("inTime", signItem.getInTime());
            values.put("outTime", signItem.getOutTime());
            values.put("nid", signItem.getNid());
            db.insert("Sign", null, values);
        }
    }

    // 第二种方案  判断一个用户是否已经对一个活动签到了 0 没有签到过    1 已经签到过     2 没有签离
    public int isSign2(String number, long activeId) {
        int flag = 0;
        Cursor cursor = db.query("Sign", new String[]{"inTime", "outTime"}, "number = ? and activeId = ?", new String[]{number, String.valueOf(activeId)}, null, null, null);
        while (cursor.moveToNext()) {
            String in = cursor.getString(cursor.getColumnIndex("inTime"));
            String out = cursor.getString(cursor.getColumnIndex("outTime"));
            if (in.equals(out)) {
                flag = 2;
            } else {
                flag = 1;
            }
        }
        cursor.close();
        return flag;
    }

    // 判断一个活动最近是否签到  第二种表 方案
    public boolean isRecentSign(String number, long activeId) {
        boolean flag = false;
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.getDefault());
            long big = -1;
            String out = "";
            Cursor cursor = db.query("Sign", new String[]{"outTime"}, "number=? and activeId=?", new String[]{number, String.valueOf(activeId)}, null, null, null);
            while (cursor.moveToNext()) {
                String outTime = cursor.getString(cursor.getColumnIndex("outTime"));
                if (big < df.parse(outTime).getTime()) {
                    big = df.parse(outTime).getTime();
                    out = outTime;
                }
            }
            cursor.close();

            // 如果当前时间超过上次签离时间一天，就可以签到
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());
            String currentTime = sdf.format(new Date());
            if (out.equals("") || (sdf.parse(currentTime).getTime() - sdf.parse(out.substring(0, 10)).getTime()) >= 1000 * 60 * 60 * 24) {
                flag = true;
            }

        } catch (ParseException e) {
            e.printStackTrace();
            Logs.e("数据库中对比时间异常:" + e.toString());
            return false;
        }

        return flag;
    }

    // 判断一个活动是否已经签到 但没有签离 1 ture     0 false   2 没有数据    3 异常
    public int isSignOut(String number, long activeId, SignItem signItem) {
        int flag = 1;
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            long big = -1;
            String inTime = "";
            String endTime = "";
            Cursor cursor = db.query("Sign", null, "number=? and activeId=?", new String[]{number, String.valueOf(activeId)}, null, null, null);
            while (cursor.moveToNext()) {
                String outTime = cursor.getString(cursor.getColumnIndex("outTime"));
                if (big < df.parse(outTime).getTime()) {
                    big = df.parse(outTime).getTime();
                    inTime = cursor.getString(cursor.getColumnIndex("inTime"));
                    endTime = outTime;
                    Logs.i("nid:" + cursor.getInt(cursor.getColumnIndex("nid")));

                    signItem.setActiveId(cursor.getInt(cursor.getColumnIndex("activeId")));
                    signItem.setNumber(cursor.getString(cursor.getColumnIndex("number")));
                    signItem.setNid(cursor.getInt(cursor.getColumnIndex("nid")));
                    signItem.setOutTime(cursor.getString(cursor.getColumnIndex("outTime")));
                    signItem.setInTime(inTime);
                }
            }

            // 如果签到时间和签离时间相同，说明还没有签离
            if (inTime.equals(endTime) && !inTime.equals("")) {
                flag = 0;
            }
            // 如果都为空，说明没有数据，还没有签到
            if (inTime.equals("") && endTime.equals("")) {
                flag = 2;
            }

        } catch (ParseException e) {
            e.printStackTrace();
            flag = 3;
            Logs.e("数据库中对比时间异常:" + e.toString());
            return flag;
        }

        return flag;
    }

    // 更新签离时间
    public void updateSignOutTime(int nid, String time) {
        ContentValues values = new ContentValues();
        values.put("outTime", time);
        db.update("Sign", values, "nid = ?", new String[]{String.valueOf(nid)});
    }

    // 保存名言到数据库
    public void saveSaying(Saying.DataBean saying) {
        if (saying != null) {
            ContentValues values = new ContentValues();
            values.put("content", saying.getContent());
            db.insert("Saying", null, values);
        }
    }

    // 获取数据库中的所有名言
    public List<Saying.DataBean> getSaying() {
        List<Saying.DataBean> list = new ArrayList<>();
        Cursor cursor = db.query("Saying", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            Saying.DataBean saying = new Saying.DataBean();
            saying.setContent(cursor.getString(cursor.getColumnIndex("content")));
            list.add(saying);
        }
        cursor.close();
        return list;
    }

    // 删除名言信息
    public void deleteSaying() {
        db.delete("Saying", null, null);
    }

    // 保存需要分析的签到数据
    public void saveAnalyzeSign(AnalyzeSign analyzeSign) {
        if (analyzeSign != null) {
            ContentValues values = new ContentValues();
            values.put("number", analyzeSign.getNumber());
            values.put("activeName", analyzeSign.getActiveName());
            values.put("rule", analyzeSign.getRule());
            values.put("inTime", analyzeSign.getInTime());
            values.put("outTime", analyzeSign.getOutTime());
            values.put("time", analyzeSign.getTime());
            values.put("endTime", analyzeSign.getEndTime());
            db.insert("AnalyzeSign", null, values);
        }
    }

    // 删除数据库中分析签到数据
    public void deleteAnalyzeSign() {
        db.delete("AnalyzeSign", null, null);
    }

    // 获取晨读签到次数（毅力）
    public int getMorningFrequency(String num) {
        int number = 0;
        Cursor cursor = db.query("AnalyzeSign", null, "rule=? and number=?", new String[]{"5",num}, null, null, null);

        while (cursor.moveToNext()) {
            number++;
        }

        return number;
    }

    // 获取活动和培训的签到次数（学识）
    public int getLectureFrequency(String num) {
        int number = 0;
        Cursor cursor = db.query("AnalyzeSign", null, "(rule=? or rule=?) and number=?", new String[]{"1","3",num}, null, null, null);

        while (cursor.moveToNext()) {
            number++;
        }

        cursor.close();
        return number;
    }

    // 获取值班的签到次数（自律）
    public int getOnDutyFrequency(String num) {
        int number = 0;
        Cursor cursor = db.query("AnalyzeSign", null, "rule=? and number=?", new String[]{"2",num}, null, null, null);

        while (cursor.moveToNext()) {
            number++;
        }

        cursor.close();
        return number;
    }

    // 获取跑操的签到次数（活力）
    public int getRunningFrequency(String num) {
        int number = 0;
        Cursor cursor = db.query("AnalyzeSign", null, "rule=? and number=?", new String[]{"4",num}, null, null, null);

        while (cursor.moveToNext()) {
            number++;
        }

        cursor.close();
        return number;
    }

    // 获取签到时间比开始时间早的次数 （守时）
    public int getEarlyFrequency(String num) {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss",Locale.getDefault());
        int number = 0;
        Cursor cursor = db.query("AnalyzeSign", new String[]{"time", "inTime"}, "number=?", new String[]{num}, null, null, null);

        while (cursor.moveToNext()) {
            String time = cursor.getString(cursor.getColumnIndex("time")).replace("T", " ").substring(11, 19);;
            String inTime = cursor.getString(cursor.getColumnIndex("inTime")).replace("T", " ").substring(11, 19);;
            try {
                // 如果开始时间比签到时间大，说明提前签到了
                if (df.parse(time).getTime() >= df.parse(inTime).getTime()) {
                    number++;
                }
            } catch (ParseException e) {
                e.printStackTrace();
                return number;
            }
        }

        cursor.close();
        return number;
    }
}
