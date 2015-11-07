package com.example.raafat.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.raafat.trillium.MyApplication;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Raafat on 17/09/2015.
 */
public class Driver {

    public static final String TABLE_NAME = "DRIVER_TABLE";
    public static final String COL_ID = "DriverId";
    public static final String COL_NAME = "DriverName";
    public static final String COL_IMG_URL = "DriverProfileImage";
    public static final String[] COLS = new String[]{COL_ID, COL_NAME, COL_IMG_URL};

    public static String getCreateSql() {
        return "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY , " +
                COL_NAME + " TEXT ," +
                COL_IMG_URL + " TEXT );";
    }

    public static Driver load(long id, SQLiteDatabase db) {
        Cursor c = db.query(TABLE_NAME, COLS, COL_ID + " = ? ", new String[]{String.valueOf(id)}, null, null, null);
        Driver driver = null;
        if (c.moveToFirst()) {
            driver = new Driver();
            driver.setDriverId(c.getLong(c.getColumnIndex(COL_ID)));
            driver.setDriverName(c.getString(c.getColumnIndex(COL_NAME)));
            driver.setImgUrl(c.getString(c.getColumnIndex(COL_IMG_URL)));
        }
        // driver.setTrucks(TruckDrivers.getTruckForDriver(id, db));
        return driver;
    }

    public long saveCurrent(SQLiteDatabase db) {
        ContentValues cv = new ContentValues();
        cv.put(COL_ID, this.getDriverId());
        cv.put(COL_NAME, this.getDriverName());
        cv.put(COL_IMG_URL, this.getImgUrl());
        try {
            db.insert(TABLE_NAME, null, cv);
        } catch (SQLiteConstraintException e) {
            //Error
        }
        return this.getDriverId();

    }

    public static List<Driver> getAllDrivers(SQLiteDatabase db) {
        Cursor c = db.query(TABLE_NAME, COLS, null, null, null, null, COL_NAME);
        List<Driver> res = new ArrayList<>();
        if (c.moveToFirst()) {
            do {
                res.add(
                        new Driver(c.getLong(c.getColumnIndex(COL_ID)),
                                c.getString(c.getColumnIndex(COL_NAME)),
                                c.getString(c.getColumnIndex(COL_IMG_URL)))
                );
            } while (c.moveToNext());
        }

        return res;
    }

    public static void delete(SQLiteDatabase db) {
        db.delete(TABLE_NAME, null, null);
    }


//.......................................//

    private long Id;
    private String Driver;
    private String Image;
    public boolean isSelected = false;

    public Driver() {

    }

    public Driver(long Id, String Driver, String imgUrl) {
        this.Id = Id;
        this.Driver = Driver;
        this.Image = imgUrl;
    }


    public long getDriverId() {
        return Id;
    }

    public void setDriverId(long driverId) {
        this.Id = driverId;
    }

    public String getDriverName() {
        return Driver;
    }

    public void setDriverName(String driverName) {
        this.Driver = driverName;
    }

    public String getImgUrl() {
        return Image;
    }

    public void setImgUrl(String imgUrl) {
        this.Image = imgUrl;
    }

    @Override
    public boolean equals(Object o) {
        return this.getDriverId() == ((Driver) o).getDriverId();
    }


    public static void getAllDriverFromUrl() {

        JsonArrayRequest request = new JsonArrayRequest(MyApplication.getFullUrl(MyApplication.DRIVERS_URL)
                , new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {

               com.example.raafat.model.Driver.delete(MyApplication.db);
                for (int i = 0; i < jsonArray.length(); ++i) {
                    try {

                        JSONObject driver = jsonArray.getJSONObject(i);
                        String driverStr = driver.toString();
                        Gson gson = new Gson();
                        final Driver driverObj = gson.fromJson(driverStr, com.example.raafat.model.Driver.class);

                        ImageRequest imReq = new ImageRequest(driverObj.getImgUrl(), new Response.Listener<Bitmap>() {
                            @Override
                            public void onResponse(Bitmap bitmap) {
                                try {
                                    MyApplication.saveBitmap(driverObj.getDriverName()+".jpg", bitmap);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                MyApplication.doneProcess ++;
                                MyApplication.isDone();
                            }
                        }, 0, 0, null, null);
                        driverObj.setImgUrl(MyApplication.filesDir+driverObj.getDriverName()+".jpg");
                        driverObj.saveCurrent(MyApplication.db);
                        MyApplication.addToReqeustQue(imReq);
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
                Toast.makeText(MyApplication.APP_CTX, "Error fetching Drivers !"+volleyError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        MyApplication.addToReqeustQue(request);

    }


}
