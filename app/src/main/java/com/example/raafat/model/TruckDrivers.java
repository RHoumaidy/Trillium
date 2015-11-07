package com.example.raafat.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteCantOpenDatabaseException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.raafat.trillium.MyApplication;
import com.example.raafat.trillium.Strings;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Raafat on 17/09/2015.
 */
public class TruckDrivers {

    public static final String TABLE_NAME = "TRUCK_DRIVER_TABLE";
    public static final String COL_ID = "TRUCK_DRIVER_ID";
    public static final String COL_DRIVER_ID = "DriverId";
    public static final String COL_TRUCK_ID = "TruckId";
    public static final String COL_LOG_DATE = "LogDate";
    public static final String COL_RECORDED_USER = "RecordUserId";
    public static final String COL_SYNC = "isSync";

    public static final String[] COLS = new String[]{COL_ID, COL_DRIVER_ID,
            COL_TRUCK_ID, COL_LOG_DATE, COL_RECORDED_USER, COL_SYNC};

    public static String getCreateSql() {
        return "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," +
                COL_TRUCK_ID + " INTEGER ," +
                COL_DRIVER_ID + " INTEGER ," +
                COL_LOG_DATE + " DATETIME ," +
                COL_RECORDED_USER + " INTEGER ," +
                COL_SYNC + " BOOLEAN );";
    }

    public static TruckDrivers load(Long id, Long truckId, Long driverId, SQLiteDatabase db) {

        String selection = " 1 = 1 ";
        selection += (id == null) ? "" : " AND " + COL_ID + " = " + id;
        selection += (truckId == null) ? "" : " AND " + COL_TRUCK_ID + " = " + truckId;
        selection += (driverId == null) ? "" : " AND " + COL_DRIVER_ID + " = " + driverId;

        Cursor c = db.query(TABLE_NAME, COLS, selection, null, null, null, COL_LOG_DATE);
        TruckDrivers truckDrivers = null;
        if (c.moveToLast()) {
            truckDrivers = new TruckDrivers();
            truckDrivers.setDriverId(c.getLong(c.getColumnIndex(COL_DRIVER_ID)));
            truckDrivers.setTruckId(c.getLong(c.getColumnIndex(COL_TRUCK_ID)));
            truckDrivers.setTruckDriverId(c.getLong(c.getColumnIndex(COL_ID)));
            truckDrivers.setLogDate(c.getLong(c.getColumnIndex(COL_LOG_DATE)));
            truckDrivers.setRecordUserId(c.getLong(c.getColumnIndex(COL_RECORDED_USER)));

        }

        return truckDrivers;
    }


    public static void deleteEveryThing(SQLiteDatabase db) {
        db.delete(TABLE_NAME, null, null);
    }

    public boolean remove(SQLiteDatabase db) {


        try {
            db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE " + COL_ID + " = '" + this.getTruckDriverId() + "';");
            return true;
        } catch (Exception e) {
            return false;
        }


    }

    public long saveCurrent(SQLiteDatabase db) {
        ContentValues cv = new ContentValues();
        cv.put(COL_DRIVER_ID, this.getDriverId());
        cv.put(COL_TRUCK_ID, this.getTruckId());
        cv.put(COL_LOG_DATE, getLogDate());
        cv.put(COL_RECORDED_USER, this.getRecordUserId());
        cv.put(COL_SYNC, false);

        try {
            db.insert(TABLE_NAME, null, cv);
        } catch (SQLiteConstraintException e) {
        }
        return this.getTruckDriverId();
    }

    private void saveCurrent(SQLiteDatabase db, boolean b) {
        ContentValues cv = new ContentValues();
        cv.put(COL_DRIVER_ID, this.getDriverId());
        cv.put(COL_TRUCK_ID, this.getTruckId());
        cv.put(COL_LOG_DATE, getLogDate());
        cv.put(COL_RECORDED_USER, this.getRecordUserId());
        cv.put(COL_SYNC, b);

        try {
            db.insert(TABLE_NAME, null, cv);
        } catch (SQLiteConstraintException e) {
        }

    }

    public boolean update(SQLiteDatabase db) {

        ContentValues cv = new ContentValues();

        cv.put(COL_DRIVER_ID, this.getDriverId());
        cv.put(COL_TRUCK_ID, this.getTruckId());
        try {
            db.update(TABLE_NAME, cv, COL_ID + " = ?", new String[]{String.valueOf(this.getTruckDriverId())});

        } catch (SQLiteConstraintException e) {

        }

        return false;
    }

    public static Driver getDriversForTruck(long truckId, SQLiteDatabase db) {
        Cursor c = db.query(TABLE_NAME, COLS, COL_TRUCK_ID + "=?",
                new String[]{String.valueOf(truckId)}, null, null, COL_LOG_DATE);
        Driver driver = null;
        try {
            if (c.moveToLast()) {
                driver = Driver.load(c.getLong(c.getColumnIndex(COL_DRIVER_ID)), db);
            }
        } catch (SQLiteCantOpenDatabaseException e) {

        } catch (SQLException e) {

        }
        return driver;
    }

    public static Truck getTruckForDriver(long driverId, SQLiteDatabase db) {
        Cursor c = db.query(TABLE_NAME, COLS, COL_DRIVER_ID + " =?", new String[]{String.valueOf(driverId)}, null, null, null);
        Truck truck = null;
        if (c.moveToFirst()) {
            truck = Truck.load(c.getLong(c.getColumnIndex(COL_TRUCK_ID)), null, db);
        }
        return truck;
    }

    public static void setDriverForTruck(long truckId, long driverId, SQLiteDatabase db) {
//        TruckDrivers truckDrivers = TruckDrivers.load(null, truckId, null, db);
//        TruckDrivers truckDrivers1 = TruckDrivers.load(null, null, driverId, db);
//        if (truckDrivers != null) {
//            if (driverId == -1)
//                truckDrivers.remove(db);
//            else {
//                truckDrivers.setDriverId(driverId);
//                if (truckDrivers1 != null)
//                    truckDrivers1.remove(db);
//                truckDrivers.remove(db);
//                truckDrivers.saveCurrent(db);
//            }
//        } else {
//            truckDrivers = new TruckDrivers();
//            truckDrivers.setDriverId(driverId);
//            truckDrivers.setTruckId(truckId);
//            if (truckDrivers1 != null)
//                truckDrivers1.remove(db);
//            truckDrivers.saveCurrent(db);
//        }

        TruckDrivers truckDrivers;
        if (driverId != -1)
            truckDrivers = new TruckDrivers(0, driverId, truckId);
        else
            truckDrivers = new TruckDrivers(0, null, truckId);
        truckDrivers.setRecordUserId(MyApplication.sp.getLong(User.COL_ID, 0));
        truckDrivers.saveCurrent(db);

    }

    public static List<Driver> getFreeDrivers(SQLiteDatabase db) {
        Cursor c = db.query(TABLE_NAME, COLS, null, null, null, null, null);
        List<Driver> allDrivers = Driver.getAllDrivers(db);

        if (c.moveToFirst()) {
            do {
                Driver driver = new Driver(c.getLong(c.getColumnIndex(COL_DRIVER_ID)), "", "");
                allDrivers.remove(driver);
            } while (c.moveToNext());
        }

        return allDrivers;
    }


    public static List<TruckDrivers> getAll(SQLiteDatabase db) {
        Cursor c = db.query(TABLE_NAME, COLS, COL_SYNC + "=?", new String[]{String.valueOf(0)}, null, null, null);
        List<TruckDrivers> res = new ArrayList<>();

        if (c.moveToFirst()) {
            do {
                res.add(
                        new TruckDrivers(
                                c.getLong(c.getColumnIndex(COL_ID)),
                                c.getLong(c.getColumnIndex(COL_DRIVER_ID)),
                                c.getLong(c.getColumnIndex(COL_TRUCK_ID))
                        )
                );
            } while (c.moveToNext());

        }
        return res;
    }

    public void deleteThis(SQLiteDatabase db) {
        ContentValues cv = new ContentValues();
        cv.put(COL_SYNC, true);

        db.update(TABLE_NAME, cv, COL_ID + "=?", new String[]{String.valueOf(this.getTruckDriverId())});
    }

//..................................//


    private long truckDriverId;
    private Long DriverId;
    private long VehiclesId;
    private Long LogDate = System.currentTimeMillis();
    private long RecordUserId;

    public TruckDrivers() {
    }

    public TruckDrivers(long id, Long driverId, long truckId) {
        this.DriverId = driverId;
        this.VehiclesId = truckId;
        this.truckDriverId = id;
    }


    public Long getLogDate() {
        return LogDate;
    }

    public String getFormatedLogDate() {
        return MyApplication.fromateDate(this.getLogDate());
    }

    public void setLogDate(Long logDate) {
        LogDate = logDate;
    }

    public void setLogDate(String date) {
        this.setLogDate(MyApplication.parseDate(date));
    }

    public long getRecordUserId() {
        return RecordUserId;
    }

    public void setRecordUserId(long recordUserId) {
        RecordUserId = recordUserId;
    }

    public long getTruckDriverId() {
        return truckDriverId;
    }

    public void setTruckDriverId(long truckDriverId) {
        this.truckDriverId = truckDriverId;
    }

    public Long getDriverId() {
        return DriverId;
    }

    public void setDriverId(Long driverId) {
        this.DriverId = driverId;
    }

    public long getTruckId() {
        return VehiclesId;
    }

    public void setTruckId(long truckId) {
        this.VehiclesId = truckId;
    }

    public static void getAllDriverFromUrl() {


        JsonArrayRequest request = new JsonArrayRequest(MyApplication.getFullUrl(MyApplication.TRUCK_DRIVER_URL)
                , new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {

                for (int i = 0; i < jsonArray.length(); ++i) {
                    try {

                        JSONObject record = jsonArray.getJSONObject(i);
                        String recordStr = record.toString();
                        Gson gson = new Gson();
                        final TruckDrivers recordObj = gson.fromJson(recordStr, TruckDrivers.class);


                        recordObj.saveCurrent(MyApplication.db, true);

                    } catch (JSONException e) {

                    }


                }
                MyApplication.doneProcess++;
                MyApplication.isDone();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                MyApplication.doneProcess++;
                MyApplication.isDone();
                Toast.makeText(MyApplication.APP_CTX, "Error fetching Truck's Drivers !"+volleyError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        MyApplication.addToReqeustQue(request);

    }


    public void uploadThis() {

        String url = MyApplication.BASE_POST_URL + "?function=Vehicles_Driver_LogPostList&security_key="
                + MyApplication.calScurityKey()+"&Data=" +getJSON();

        StringRequest request = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String jsonObject) {

                        com.example.raafat.trillium.Response response =
                                com.example.raafat.trillium.Response.fromJson(jsonObject.toString());
                        if (!response.response) {
                            Toast.makeText(MyApplication.APP_CTX, response.responseMessage, Toast.LENGTH_LONG).show();
                            //TruckDrivers.this.deleteThis(MyApplication.db);
                        } else {
                            TruckDrivers.this.deleteThis(MyApplication.db);
                        }
                        MyApplication.updateProgress();
                        MyApplication.doneProcess++;
                        MyApplication.isDone();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                Toast.makeText(MyApplication.APP_CTX, "Error uploading data \n " + volleyError.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                MyApplication.doneProcess++;
                MyApplication.isDone();
            }
        });

        MyApplication.addToReqeustQue(request);

    }

    public static void uploadData() {

        List<TruckDrivers> records = TruckDrivers.getAll(MyApplication.db);
        MyApplication.showDilog2(records.size());
        for (TruckDrivers record : records) {
            record.uploadThis();
        }
        if (records.size() == 0)
            MyApplication.isDone();
    }

    public String getJSON() {
        String res = "[";
        HashMap<String, String> values = new HashMap<>();
        values.put("DriverId", this.getDriverId() + "");
        values.put("VehiclesId", this.getTruckId() + "");
        values.put("LogDate", this.getFormatedLogDate());
        values.put("RecordUserId", this.getRecordUserId() + "");

        JSONObject object = new JSONObject(values);
        res += object.toString();

        res += "]";
        return Strings.urlEncode(res);
    }

}
