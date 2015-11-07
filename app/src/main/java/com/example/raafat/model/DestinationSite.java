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
public class DestinationSite {

    public static final String TABLE_NAME = "DEST_SITE_TABLE";
    public static final String COL_ID = "DestinationSiteId";
    public static final String COL_NAME = "DestinationSiteName";
    public static final String[] COLS = new String[]{COL_ID,COL_NAME};

    public static String getCreateSql(){
        return "CREATE TABLE IF NOT EXISTS "+ TABLE_NAME +" ("+
                COL_ID +" INTEGER PRIMARY KEY ,"+
                COL_NAME + " TEXT );";
    }

    public static DestinationSite load(long id ,SQLiteDatabase db){
        Cursor c = db.query(TABLE_NAME,COLS,COL_ID+ " =? ",new String[]{String.valueOf(id)},null,null,null);
        DestinationSite destinationSite = null;
        if(c.moveToFirst()){
            destinationSite = new DestinationSite();
            destinationSite.setDestinatinSiteName(c.getString(c.getColumnIndex(COL_NAME)));
            destinationSite.setDestinationSiteId(c.getLong(c.getColumnIndex(COL_ID)));
        }

        return destinationSite;
    }

    public static List<DestinationSite> getAll(SQLiteDatabase db){
        Cursor c = db.query(TABLE_NAME,COLS,null,null,null,null,null);
        List<DestinationSite> res = new ArrayList<>();

        if(c.moveToFirst()){
            do{
                res.add(
                        new DestinationSite(c.getLong(c.getColumnIndex(COL_ID)),
                                            c.getString(c.getColumnIndex(COL_NAME)))
                );
            }while (c.moveToNext());
        }

        return res;
    }

    public long saveCurrent(SQLiteDatabase db){
        ContentValues cv = new ContentValues();
        cv.put(COL_ID,this.getDestinationSiteId());
        cv.put(COL_NAME,this.getDestinatinSiteName());
        try{
            db.insert(TABLE_NAME,null,cv);
        }catch (SQLiteConstraintException e){
            //Error
        }

        return this.getDestinationSiteId();
    }

//.......................................//

    private long Id;
    private String DestinationSite;

    public DestinationSite(){

    }

    public DestinationSite( long destinationSiteId,String destinatinSiteName) {
        this.DestinationSite = destinatinSiteName;
        this.Id = destinationSiteId;
    }

    public long getDestinationSiteId() {
        return Id;
    }

    public void setDestinationSiteId(long destinationSiteId) {
        this.Id = destinationSiteId;
    }

    public String getDestinatinSiteName() {
        return DestinationSite;
    }

    public void setDestinatinSiteName(String destinatinSiteName) {
        this.DestinationSite = destinatinSiteName;
    }


    @Override
    public boolean equals(Object o) {
        return this.getDestinationSiteId() == ((DestinationSite)o).getDestinationSiteId();
    }

    public static void getAllDriverFromUrl() {

        JsonArrayRequest request = new JsonArrayRequest(MyApplication.getFullUrl(MyApplication.DESTINATION_SITE_URL)
                , new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                com.example.raafat.model.DestinationSite.delete(MyApplication.db);
                for (int i = 0; i < jsonArray.length(); ++i) {
                    try {

                        JSONObject site = jsonArray.getJSONObject(i);
                        String siteStr = site.toString();
                        Gson gson = new Gson();
                        final DestinationSite siteObj = gson.fromJson(siteStr, DestinationSite.class);

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
