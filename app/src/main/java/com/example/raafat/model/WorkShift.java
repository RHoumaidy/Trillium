package com.example.raafat.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;

import com.example.raafat.trillium.MyApplication;

/**
 * Created by Raafat on 19/09/2015.
 */
public class WorkShift implements Comparable<WorkShift> {


    public static final String TABLE_NAME = "WORK_SHIFT_TABLE";
    public static final String COL_ID = "WorkShiftId";
    public static final String COL_NAME = "WorkShiftName";
    public static final String COL_START_TIMEE = "WorkShiftStartTime";
    public static final String COL_END_TIME = "WorkShiftEndTime";
    public static final String[] COLS = new String[]{COL_ID, COL_NAME, COL_START_TIMEE, COL_END_TIME};

    public static String getCreateSql() {
        return "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ( " +
                COL_ID + " INTEGER PRIMARY KEY ," +
                COL_NAME + " TEXT ," +
                COL_START_TIMEE + " DATETIME ," +
                COL_END_TIME + " DATETIME );";
    }

    public static WorkShift load(long id, SQLiteDatabase db) {

        Cursor c = db.query(TABLE_NAME, COLS, COL_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);
        WorkShift workShift = null;
        if (c.moveToFirst()) {
            workShift = new WorkShift();
            workShift.setWorkShiftId(c.getLong(c.getColumnIndex(COL_ID)));
            workShift.setWorkShiftName(c.getString(c.getColumnIndex(COL_NAME)));
            workShift.setStartTime(c.getString(c.getColumnIndex(COL_START_TIMEE)));
            workShift.setEndTime(c.getString(c.getColumnIndex(COL_END_TIME)));
        }

        return workShift;

    }

    public long saveCurrent(SQLiteDatabase db) {
        ContentValues cv = new ContentValues();
        cv.put(COL_ID, this.getWorkShiftId());
        cv.put(COL_NAME, this.getWorkShiftName());
        cv.put(COL_START_TIMEE, this.getStartTime());
        cv.put(COL_END_TIME, this.getEndTime());

        try {
            db.insert(TABLE_NAME, null, cv);
        } catch (SQLiteConstraintException e) {

        }
        return this.getWorkShiftId();
    }

    public static WorkShift getCurrentWorkShaft(SQLiteDatabase db) {
        long curTime =MyApplication.parseTime(
                MyApplication.fromateTime(System.currentTimeMillis()));

        Cursor c = db.query(TABLE_NAME, COLS, COL_START_TIMEE + " <= ? AND " + COL_END_TIME + " > ?",
                new String[]{String.valueOf(curTime),String.valueOf(curTime)}, null, null, null);

        WorkShift res = new WorkShift();
        if (c.moveToFirst())
            res = WorkShift.load(c.getLong(c.getColumnIndex(COL_ID)), db);

        return res;


    }

//................................//

    private long workShiftId;
    private String workShiftName;
    private Long startTime;
    private Long endTime;


    public WorkShift() {
    }

    public WorkShift(long workShiftId, String workShiftName, String startTime, String endTime) {
        this.workShiftId = workShiftId;
        this.workShiftName = workShiftName;
        this.setStartTime(startTime);
        this.setEndTime(endTime);
    }

    public WorkShift(long workShiftId, String workShiftName, long startTime, long endTime) {
        this.workShiftId = workShiftId;
        this.workShiftName = workShiftName;
        this.setStartTime(startTime);
        this.setEndTime(endTime);
    }

    public long getWorkShiftId() {
        return workShiftId;
    }

    public void setWorkShiftId(long workShiftId) {
        this.workShiftId = workShiftId;
    }

    public String getWorkShiftName() {
        return workShiftName;
    }

    public void setWorkShiftName(String workShiftName) {
        this.workShiftName = workShiftName;
    }

    public Long getStartTime() {
        return startTime;
    }

    public String getFormatedStartTime() {
        return MyApplication.fromateDate(this.getStartTime());
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public void setStartTime(String starttime) {
        this.startTime = MyApplication.parseTime(starttime);
    }

    public Long getEndTime() {
        return endTime;
    }

    public String getFormatedEndTime() {
        return MyApplication.fromateTime(this.getEndTime());
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = MyApplication.parseTime(endTime);
    }


    @Override
    public int compareTo(WorkShift another) {
        if(this.getStartTime() > another.getEndTime())
            return 1;
        else if (this.getEndTime() < another.getStartTime())
            return -1;
        else
            return 0;
    }
}
