package com.example.raafat.model;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.raafat.activities.DestinationSiteAcitvity;
import com.example.raafat.activities.LoginActivity;
import com.example.raafat.activities.OrginSiteActivity;
import com.example.raafat.activities.TrucksActivity;
import com.example.raafat.trillium.MyApplication;
import com.example.raafat.trillium.Permission;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Raafat on 17/09/2015.
 */
public class User {

    public static final String TABLE_NAME = "USERS_TABLE";
    public static final String COL_ID = "UserId";
    public static final String COL_USER_NAME = "UserName";
    public static final String COL_PASSWORD = "Password";
    public static final String COL_TRUCK_PERMISSION = "TruckDriversRecordPermission";
    public static final String COL_ORGIN_PERMISSION = "OriginSiteRecordPermission";
    public static final String COL_DESTN_PERMISSION = "DestinationSiteRecordPermission";
    public static final String[] COLS = new String[]{COL_ID, COL_USER_NAME, COL_PASSWORD, COL_TRUCK_PERMISSION,
            COL_ORGIN_PERMISSION, COL_DESTN_PERMISSION};


    public static String getCreateSql() {
        return "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ( " +
                COL_ID + " INTEGER PRIMARY KEY ," +
                COL_USER_NAME + " TEXT ," +
                COL_PASSWORD + " TEXT ," +
                COL_TRUCK_PERMISSION + " BOOLEAN ," +
                COL_ORGIN_PERMISSION + " BOOLEAN ," +
                COL_DESTN_PERMISSION + " BOOLEAN );";
    }

    public static User load(Long id, String userName, String password, SQLiteDatabase db) {

        List<String> selectionArgs = new ArrayList<>();
        String selection = "";
        if (id != null) {
            selectionArgs.add(String.valueOf(id));
            selection += COL_ID + "=?";
        }
        if (userName != null) {
            selectionArgs.add(String.valueOf(userName));
            if (selection.length() > 0)
                selection += " AND ";
            selection += COL_USER_NAME + "=?";
        }
        if (password != null) {
            selectionArgs.add(String.valueOf(password));
            if (selection.length() > 0)
                selection += " AND ";
            selection += COL_PASSWORD + "=?";
        }

        String[] selArg = new String[selectionArgs.size()];
        selectionArgs.toArray(selArg);

        User user = null;
        Cursor c = db.query(TABLE_NAME, COLS, selection, selArg, null, null, null);
        if (c.moveToFirst()) {
            user = new User();
            user.setUserId(c.getLong(c.getColumnIndex(COL_ID)));
            user.setUserName(c.getString(c.getColumnIndex(COL_USER_NAME)));
            user.setPassword(c.getString(c.getColumnIndex(COL_PASSWORD)));
            user.setTruckDriverRecordPermission(c.getInt(c.getColumnIndex(COL_TRUCK_PERMISSION)) == 1);
            user.setOrginSiteRecordPermission(c.getInt(c.getColumnIndex(COL_ORGIN_PERMISSION)) == 1);
            user.setDestinationRecordPermission(c.getInt(c.getColumnIndex(COL_DESTN_PERMISSION)) == 1);
        }
        return user;

    }

    public long saveCurrent(SQLiteDatabase db) {
        ContentValues cv = new ContentValues();
        cv.put(COL_ID, this.getUserId());
        cv.put(COL_USER_NAME, this.getUserName());
        cv.put(COL_PASSWORD, this.getPassword());
        cv.put(COL_TRUCK_PERMISSION, this.isTruckDriverRecordPermission());
        cv.put(COL_ORGIN_PERMISSION, this.isOrginSiteRecordPermission());
        cv.put(COL_DESTN_PERMISSION, this.isDestinationRecordPermission());
        try {
            db.insert(TABLE_NAME, null, cv);
        } catch (SQLiteConstraintException e) {
            //Error
        }
        return this.getUserId();
    }


    public static User isExists(String userName, SQLiteDatabase db) {
        return User.load(null, userName, null, db);


    }

    public static User isCorrect(String userName, String password, SQLiteDatabase db) {

        return User.load(null, userName, password, db);

    }

    public List<Permission> getUserPermissions() {

        List<Permission> res = new ArrayList<>();

        if (this.isTruckDriverRecordPermission())
            res.add(new Permission("Truck Admin", TrucksActivity.class));
        if (this.isOrginSiteRecordPermission())
            res.add(new Permission("OriginSite Admin", OrginSiteActivity.class));
        if (this.isDestinationRecordPermission())
            res.add(new Permission("DestinationSite Admin", DestinationSiteAcitvity.class));

        return res;

    }

    public static void delete(SQLiteDatabase db) {
        db.delete(TABLE_NAME, null, null);
    }

//......................................//


//......................................//

    private long Id;
    private String Username;
    private String Password;
    private boolean TruckDriversRecordPermission;
    private boolean OriginSiteRecordPermission;
    private boolean DestinationSiteRecordPermission;


    public User() {
    }

    public User(long userId, String userName, String password,
                boolean truckDriverRecordPermission, boolean orginSiteRecordPermission,
                boolean destinationRecordPermission) {
        this.Id = userId;
        this.Username = userName;
        this.Password = password;
        this.TruckDriversRecordPermission = truckDriverRecordPermission;
        this.OriginSiteRecordPermission = orginSiteRecordPermission;
        this.DestinationSiteRecordPermission = destinationRecordPermission;
    }


    public long getUserId() {
        return Id;
    }

    public void setUserId(long userId) {
        this.Id = userId;
    }

    public String getUserName() {
        return Username;
    }

    public void setUserName(String userName) {
        this.Username = userName;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        this.Password = password;
    }

    public boolean isTruckDriverRecordPermission() {
        return TruckDriversRecordPermission;
    }

    public void setTruckDriverRecordPermission(boolean truckDriverRecordPermission) {
        this.TruckDriversRecordPermission = truckDriverRecordPermission;
    }

    public boolean isOrginSiteRecordPermission() {
        return OriginSiteRecordPermission;
    }

    public void setOrginSiteRecordPermission(boolean orginSiteRecordPermission) {
        this.OriginSiteRecordPermission = orginSiteRecordPermission;
    }

    public boolean isDestinationRecordPermission() {
        return DestinationSiteRecordPermission;
    }

    public void setDestinationRecordPermission(boolean destinationRecordPermission) {
        this.DestinationSiteRecordPermission = destinationRecordPermission;
    }


    public static void getAllDriverFromUrl() {


        JsonArrayRequest request = new JsonArrayRequest(MyApplication.getFullUrl(MyApplication.USERS_URL)
                , new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                User.delete(MyApplication.db);
                for (int i = 0; i < jsonArray.length(); ++i) {
                    try {

                        JSONObject user = jsonArray.getJSONObject(i);
                        String driverStr = user.toString();
                        Gson gson = new Gson();
                        final User userObj = gson.fromJson(driverStr, User.class);


                        userObj.saveCurrent(MyApplication.db);

                    } catch (JSONException e) {

                    }


                }
                MyApplication.doneProcess++;
                MyApplication.isDone();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                MyApplication.doneProcess ++;
                MyApplication.isDone();
                Toast.makeText(MyApplication.APP_CTX, "Error fetching Users !"+volleyError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        MyApplication.addToReqeustQue(request);

    }
}
