package com.example.raafat.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;

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
public class OrginSite {

    public static final String TABLE_NAME = "ORGINE_SITE_TABLE";
    public static final String COL_ID = "OriginSiteId";
    public static final String COL_NAME = "OriginSiteName";
    public static final String[] COLS = new String[]{COL_ID,COL_NAME};

    public static String getCreateSql(){
        return "CREATE TABLE IF NOT EXISTS "+TABLE_NAME +" ("+
                COL_ID+" INTEGER PRIMARY KEY ,"+
                COL_NAME + " TEXT );";
    }

    public static OrginSite load(long id,SQLiteDatabase db){
        Cursor c = db.query(TABLE_NAME,COLS,COL_ID +" =? ",new String[]{String.valueOf(id)},null,null,null);
        OrginSite orginSite = new OrginSite();

        if(c.moveToFirst()){
            orginSite.setOrginSiteId(c.getLong(c.getColumnIndex(COL_ID)));
            orginSite.setOrginSiteName(c.getString(c.getColumnIndex(COL_NAME)));
        }

        return orginSite;
    }

    public static List<OrginSite> getAll(SQLiteDatabase db){
        Cursor c = db.query(TABLE_NAME,COLS,null,null,null,null,null);
        List<OrginSite> res = new ArrayList<>();
        if(c.moveToFirst()){
            do {
                res.add(
                        new OrginSite(c.getLong(c.getColumnIndex(COL_ID)),c.getString(c.getColumnIndex(COL_NAME)))
                );
            }while (c.moveToNext());
        }

        return res;
    }

    public long saveCurrent (SQLiteDatabase db){
        ContentValues cv = new ContentValues();
        cv.put(COL_ID,this.getOrginSiteId());
        cv.put(COL_NAME,this.getOrginSiteName());
        try{
            db.insert(TABLE_NAME,null,cv);
        }catch (SQLiteConstraintException e){
            //Error
        }
        return this.getOrginSiteId();
    }
//.......................................//


    private long Id;
    private String OriginSite;

    public OrginSite(){}

    public OrginSite(long orginSiteId, String orginSiteName) {
        this.Id = orginSiteId;
        this.OriginSite = orginSiteName;
    }

    public String getOrginSiteName() {
        return OriginSite;
    }

    public void setOrginSiteName(String orginSiteName) {
        this.OriginSite = orginSiteName;
    }

    public long getOrginSiteId() {
        return Id;
    }

    public void setOrginSiteId(long orginSiteId) {
        this.Id = orginSiteId;
    }

    public static void getAllDriverFromUrl() {

        JsonArrayRequest request = new JsonArrayRequest(MyApplication.getFullUrl(MyApplication.ORIGINE_SITE_URL)
                , new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                OrginSite.delete(MyApplication.db);
                for (int i = 0; i < jsonArray.length(); ++i) {
                    try {

                        JSONObject site = jsonArray.getJSONObject(i);
                        String siteStr = site.toString();
                        Gson gson = new Gson();
                        final OrginSite siteObj = gson.fromJson(siteStr, OrginSite.class);

                        siteObj.saveCurrent(MyApplication.db);

                    } catch (JSONException e) {

                    }


                }
                MyApplication.doneProcess ++;
                MyApplication.isDone();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        MyApplication.addToReqeustQue(request);

    }

    private static void delete(SQLiteDatabase db) {
        db.delete(TABLE_NAME,null,null);
    }


}
