package com.example.raafat.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.raafat.trillium.MyApplication;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Raafat on 17/09/2015.
 */
public class Truck {

    public static final String TABLE_NAME = "TRUCKS_TABLE";
    public static final String COL_ID = "TruckId";
    public static final String COL_NAME = "TruckName";
    public static final String[] COLS = new String[]{COL_ID, COL_NAME};

    public static String getCreateSql() {
        return "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ( " +
                COL_ID + " INTEGER PRIMARY KEY , " +
                COL_NAME + " TEXT );";
    }

    public static Truck load(Long id, String truckName, SQLiteDatabase db) {

        String selectin = " 1 = 1 ";
        selectin += (id == null) ? "" : " AND " + COL_ID + " = " + id;
        selectin += (truckName == null) ? "" : " AND " + COL_NAME + " = " + truckName;


        Truck truck = null;
        Cursor c = db.query(TABLE_NAME, COLS, selectin, null, null, null, null);
        if (c.moveToFirst()) {
            truck = new Truck();
            truck.setTruckId(c.getLong(c.getColumnIndex(COL_ID)));
            truck.setTruckName(c.getString(c.getColumnIndex(COL_NAME)));
        }
        // truck.setDrivers(TruckDrivers.getDriversForTruck(id, db));

        return truck;
    }

    public long saveCurrent(SQLiteDatabase db) {
        ContentValues cv = new ContentValues();
        cv.put(COL_ID, this.getTruckId());
        cv.put(COL_NAME, this.getTruckName());
        try {
            db.insert(TABLE_NAME, null, cv);
        } catch (SQLiteConstraintException e) {
            //Error
        }
        return this.getTruckId();
    }

    public static List<Truck> getAll(SQLiteDatabase db) {

        Cursor c = db.query(TABLE_NAME, COLS, null, null, null, null,COL_NAME);
        List<Truck> res = new ArrayList<>();

        if (c.moveToFirst()) {
            do {
                res.add(
                     new Truck(c.getLong(c.getColumnIndex(COL_ID)),c.getString(c.getColumnIndex(COL_NAME)))
                );
            } while (c.moveToNext());
        }
        return res;
    }

    public static List<Truck> getAll(boolean withDriver, SQLiteDatabase db) {

        Cursor c = db.query(TABLE_NAME, COLS, null, null, null, null, null);
        List<Truck> res = new ArrayList<>();

        if (c.moveToFirst()) {
            do {
                Truck truck = Truck.load(c.getLong(c.getColumnIndex(COL_ID)), null, db);
                Driver driver = TruckDrivers.getDriversForTruck(truck.getTruckId(), db);
                if (driver != null)
                    res.add(truck);
            } while (c.moveToNext());
        }
        return res;
    }

//.................................//

    private long Id;
    private String Vehicles;

    public Truck() {

    }

    public Truck(long truckId, String truckName) {
        this.Id = truckId;
        this.Vehicles = truckName;
    }

    public long getTruckId() {
        return Id;
    }

    public void setTruckId(long truckId) {
        this.Id = truckId;
    }

    public String getTruckName() {
        return Vehicles;
    }

    public void setTruckName(String truckName) {
        this.Vehicles = truckName;
    }


    @Override
    public boolean equals(Object o) {
        Truck other = (Truck) o;
        return this.getTruckId() == other.getTruckId();
    }


    public static void getAllDriverFromUrl() {


        JsonArrayRequest request = new JsonArrayRequest(MyApplication.getFullUrl(MyApplication.VEHICLES_URL)
                , new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                Truck.delete(MyApplication.db);
                for (int i = 0; i < jsonArray.length(); ++i) {
                    try {

                        JSONObject truck = jsonArray.getJSONObject(i);
                        String driverStr = truck.toString();
                        Gson gson = new Gson();
                        final Truck truckObj = gson.fromJson(driverStr, Truck.class);

                        truckObj.saveCurrent(MyApplication.db);

                    } catch (JSONException e) {

                    }


                }
                MyApplication.doneProcess ++;
                MyApplication.isDone();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                MyApplication.doneProcess ++;
                MyApplication.isDone();
                Toast.makeText(MyApplication.APP_CTX,"Error fetching Trucks !"+volleyError.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
        MyApplication.addToReqeustQue(request);

    }

    private static void delete(SQLiteDatabase db) {
        db.delete(TABLE_NAME,null,null);
    }
}
