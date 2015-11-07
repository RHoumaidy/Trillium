package com.example.raafat.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.raafat.trillium.MyApplication;
import com.example.raafat.trillium.Strings;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Raafat on 19/09/2015.
 */
public class OriginRecord {

    public static final String TABLE_NAME = "ORIGIN_RECORD_TABLE";
    public static final String COL_ID = "RecordNumber";
    public static final String COL_TRUCK_ID = "TruckId";
    public static final String COL_ORIGIN_SITE_ID  = "OriginSiteId";
    public static final String COL_DATE_IN = "DateIn";
    public static final String COL_DATE_OUT = "DateOut";
    public static final String COL_RECORDED_USER_ID = "RecordUserId";
    public static final String COL_SYNC = "isSync";

    public static final String[] COLS = new String[]{COL_ID,COL_TRUCK_ID,COL_ORIGIN_SITE_ID,COL_DATE_IN,
            COL_DATE_OUT,COL_RECORDED_USER_ID,COL_SYNC};



    public static String getCreateSql(){
        return "CREATE TABLE IF NOT EXISTS "+ TABLE_NAME+" ( "+
                COL_ID +" INTEGER PRIMARY KEY AUTOINCREMENT ,"+
                COL_TRUCK_ID +" INTEGER ,"+
                COL_ORIGIN_SITE_ID +" INTEGER ,"+
                COL_DATE_IN +" DATETIME ,"+
                COL_DATE_OUT+ " DATETIME ,"+
                COL_RECORDED_USER_ID+ " TEXT ," +
                COL_SYNC +" BOOLEAN );";
    }

    public static OriginRecord load(long id , SQLiteDatabase db){
        Cursor c = db.query(TABLE_NAME,COLS,COL_ID +"= ? ",new String[]{String.valueOf(id)},null,null,null);
        OriginRecord originRecord = null;
        if(c.moveToFirst()){
            originRecord = new OriginRecord();
            originRecord.setRecordId(c.getLong(c.getColumnIndex(COL_ID)));
            originRecord.setTruckId(c.getLong(c.getColumnIndex(COL_TRUCK_ID)));
            originRecord.setOrginSiteId(c.getLong(c.getColumnIndex(COL_ORIGIN_SITE_ID)));
            originRecord.setDateIn(c.getString(c.getColumnIndex(COL_DATE_IN)));
            originRecord.setDateOut(c.getString(c.getColumnIndex(COL_DATE_OUT)));
            originRecord.setRecordedUserId(c.getLong(c.getColumnIndex(COL_RECORDED_USER_ID)));
        }

        return originRecord;
    }


    public static void deleteEveryThing(SQLiteDatabase db){
        db.delete(TABLE_NAME,null,null);
    }

    public static List<Truck> getAllTruckStillInSite(long siteId,SQLiteDatabase db){
        Cursor c = db.query(TABLE_NAME,COLS,COL_ORIGIN_SITE_ID +" = ? ",new String[]{String.valueOf(siteId)},null,null,null);
        List<Truck> res = new ArrayList<>();
        if(c.moveToFirst()){
            do{
                Long dateIn = c.getLong(c.getColumnIndex(COL_DATE_IN));
                Long dateOut = c.getLong(c.getColumnIndex(COL_DATE_OUT));
                if( dateIn != 0 && dateOut == 0){
                    Truck truck = Truck.load(c.getLong(c.getColumnIndex(COL_TRUCK_ID)),null,db);
                    if(res.indexOf(truck) == -1)
                        res.add(truck);
                }
            } while (c.moveToNext());
        }
        return res;
    }

    public static List<OriginRecord> getAllInRecords(long siteId,SQLiteDatabase db){
        Cursor c = db.query(TABLE_NAME,COLS,COL_ORIGIN_SITE_ID +" = ? AND ("+COL_DATE_OUT + " IS NULL OR "+ COL_DATE_OUT +"=? )",
                new String[]{String.valueOf(siteId),"0"},null,null,null);
        List<OriginRecord> res = new ArrayList<>();

        if(c.moveToFirst()){
            do {
                res.add(
                      new OriginRecord(
                              c.getLong(c.getColumnIndex(COL_ID)),
                              c.getLong(c.getColumnIndex(COL_TRUCK_ID)),
                              c.getLong(c.getColumnIndex(COL_ORIGIN_SITE_ID)),
                              c.getString(c.getColumnIndex(COL_DATE_IN)),
                              c.getString(c.getColumnIndex(COL_DATE_OUT)),
                              c.getLong(c.getColumnIndex(COL_RECORDED_USER_ID))
                      )
                );
            }while (c.moveToNext());
        }

        return res;
    }

    public Truck getTruck(SQLiteDatabase db){
        Cursor c = db.query(TABLE_NAME,new String[]{COL_TRUCK_ID},COL_ID +"=?",
                new String[]{String.valueOf(this.getRecordId())},null,null,null);
        Truck truck = null;
        if(c.moveToFirst()){
            truck = Truck.load(c.getLong(c.getColumnIndex(COL_TRUCK_ID)),null,db);
        }

        return truck;
    }

    public static List<OriginRecord> getAll(SQLiteDatabase db){
        Cursor c = db.query(TABLE_NAME,COLS,COL_SYNC +"=?",new String[]{String.valueOf(0)},null,null,null);
        List<OriginRecord> res = new ArrayList<>();

        if(c.moveToFirst()){
            do{
                res.add(
                        new OriginRecord(
                                c.getLong(c.getColumnIndex(COL_ID)),
                                c.getLong(c.getColumnIndex(COL_TRUCK_ID)),
                                c.getLong(c.getColumnIndex(COL_ORIGIN_SITE_ID)),
                                c.getString(c.getColumnIndex(COL_DATE_IN)),
                                c.getString(c.getColumnIndex(COL_DATE_OUT)),
                                c.getLong(c.getColumnIndex(COL_RECORDED_USER_ID))
                        )
                );
            }while (c.moveToNext());

        }
        return res;
    }

    public long saveCurrent(SQLiteDatabase db){
        ContentValues cv = new ContentValues();
        cv.put(COL_TRUCK_ID,this.getTruckId());
        cv.put(COL_ORIGIN_SITE_ID,this.getOrginSiteId());
        cv.put(COL_DATE_IN, this.getFormatedDateIn());
        cv.put(COL_DATE_OUT,this.getFormatedDateOut());
        cv.put(COL_RECORDED_USER_ID,this.getRecordedUserId());
        cv.put(COL_SYNC,false);

        try{
          this.setRecordId( db.insert(TABLE_NAME,null,cv));
        }catch (SQLiteConstraintException e){

        }
        return this.getTruckId();
    }

    public void deleteThis(SQLiteDatabase db){
        ContentValues cv = new ContentValues();
        cv.put(COL_SYNC,true);

        db.update(TABLE_NAME,cv,COL_ID +"=?",new String[]{String.valueOf(this.getRecordId())});
    }

    public boolean update(SQLiteDatabase db){

        ContentValues cv = new ContentValues();
        cv.put(COL_DATE_OUT,this.getDateOut());
        return db.update(TABLE_NAME,cv,COL_ID +"=?",new String[]{String.valueOf(this.getRecordId())}) == 1;
    }

//.......................................//

    private long recordId;
    private long TruckId;
    private long OriginSiteId;
    private Long DateIn = null;
    private Long DateOut = null;
    private long RecordUserId;

    public OriginRecord (){}

    public OriginRecord(long orginRecordId, long truckId, long orginSiteId, String dateIn, String dateOut
            ,long recordedUserId) {
        this.recordId = orginRecordId;
        this.TruckId = truckId;
        this.OriginSiteId = orginSiteId;
        this.setDateIn(dateIn);
        this.setDateOut(dateOut);
        this.RecordUserId = recordedUserId;
       // Toast.makeText(MyApplication.APP_CTX,getJSON(),Toast.LENGTH_SHORT).show();

    }

    public OriginRecord(long orginRecordId, long truckId, long orginSiteId, Long dateIn, Long dateOut
            ,long recordedUserId) {
        this.recordId = orginRecordId;
        this.TruckId = truckId;
        this.OriginSiteId = orginSiteId;
        this.setDateIn(dateIn);
        this.setDateOut(dateOut);
        this.RecordUserId = recordedUserId;
        // Toast.makeText(MyApplication.APP_CTX,getJSON(),Toast.LENGTH_SHORT).show();

    }


    public long getRecordedUserId() {
        return RecordUserId;
    }

    public void setRecordedUserId(long recordedUserId) {
        this.RecordUserId = recordedUserId;
    }

    public long getRecordId() {
        return recordId;
    }

    public void setRecordId(long recordId) {
        this.recordId = recordId;
    }

    public long getTruckId() {
        return TruckId;
    }

    public void setTruckId(long truckId) {
        this.TruckId = truckId;
    }

    public long getOrginSiteId() {
        return OriginSiteId;
    }

    public void setOrginSiteId(long orginSiteId) {
        this.OriginSiteId = orginSiteId;
    }

    public Long getDateIn() {
        return DateIn;
    }

    public void setDateIn(Long dateIn){
        this.DateIn =(dateIn);
    }

    public void setDateIn(String dateIn){ this.setDateIn(MyApplication.parseDate(dateIn));}

    public Long getDateOut() {
        return DateOut;
    }

    public void setDateOut(Long dateOut) {
        this.DateOut = dateOut;
    }

    public void setDateOut(String dateOut) {this.setDateOut(MyApplication.parseDate(dateOut));}

    public String getFormatedDateIn(){ return MyApplication.fromateDate(this.getDateIn());}

    public String getFormatedDateOut(){ return MyApplication.fromateDate(this.getDateOut());}

    @Override
    public boolean equals(Object o) {
        OriginRecord originRecord = ((OriginRecord) o);

        return this.getRecordId() == originRecord.getRecordId();
    }
   
    public void uploadThis(){
        HashMap<String,String> params = new HashMap<>();
        params.put("function","OriginLogsPostList");
        params.put("security_key",MyApplication.calScurityKey()+"");
        params.put("Data",getJSON());

        String url = MyApplication.BASE_POST_URL+"?function=OriginLogsPostList&Data="+
                 getJSON()+"&security_key="+MyApplication.calScurityKey();

        JSONObject object = new JSONObject(params);

        StringRequest request = new StringRequest(url,
                new Response.Listener<String>() {
            @Override
            public void onResponse(String jsonObject) {
               //String responseStr = jsonObject.toString();
                com.example.raafat.trillium.Response response = com.example.raafat.trillium.Response.fromJson(jsonObject.toString());
                if(!response.response) {
                    Toast.makeText(MyApplication.APP_CTX, response.responseMessage, Toast.LENGTH_LONG).show();
                    OriginRecord.this.deleteThis(MyApplication.db);
                }else{
                   OriginRecord.this.deleteThis(MyApplication.db);
                }
                MyApplication.updateProgress();
                MyApplication.doneProcess++;
                MyApplication.isDone();

            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(MyApplication.APP_CTX,"Error uploading data \n "+volleyError.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                MyApplication.doneProcess++;
                MyApplication.isDone();
            }
        });

        MyApplication.addToReqeustQue(request);

    }

    public static void uploadData(){

        List<OriginRecord> records = OriginRecord.getAll(MyApplication.db);
        MyApplication.showDilog2(records.size());
        for(OriginRecord record : records){
            record.uploadThis();
        }
        if(records.size() == 0)
            MyApplication.isDone();
    }

    public String getJSON(){
        String res = "[";
        HashMap<String,String> values  = new HashMap<>();
        values.put("OriginSiteId",this.getOrginSiteId()+"");
        values.put("TruckId",this.getTruckId()+"");
        values.put("DateIn",this.getFormatedDateIn());
        values.put("DateOut",this.getFormatedDateOut());
        values.put("RecordUserId",this.getRecordedUserId()+"");

        JSONObject object = new JSONObject(values);
        res += object.toString();

        res += "]";
        return Strings.urlEncode(res);
    }
}
