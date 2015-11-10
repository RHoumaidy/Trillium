package com.example.raafat.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.raafat.trillium.MyApplication;
import com.example.raafat.trillium.Strings;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Raafat on 19/09/2015.
 */
public class DestinationRecord {


    public static final String TABLE_NAME = "DEST_RECORD_TABLE";
    public static final String COL_ID = "RecordNumber";
    public static final String COL_TRUCK_ID = "TruckId";
    public static final String COL_DEST_SITE_ID = "DestinationSiteId";
    public static final String COL_DATE_IN = "DateIn";
    public static final String COL_DATE_OUT = "DateOut";
    public static final String COL_RECORDED_USER_ID = "RecordUserId";
    public static final String COL_LOADED_WIEGHT = "LoadedWeight";
    public static final String COl_EMPTY_WIEGHT = "EmptyWeight";
    public static final String COL_SYNC = "isSync";
    public static final String[] COLS = new String[]{COL_ID, COL_TRUCK_ID, COL_DEST_SITE_ID, COL_DATE_IN, COL_DATE_OUT,
            COL_RECORDED_USER_ID, COL_LOADED_WIEGHT, COl_EMPTY_WIEGHT,COL_SYNC};

    public static String getCreateSql() {
        return "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ( " +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," +
                COL_TRUCK_ID + " INTEGER ," +
                COL_DEST_SITE_ID + " INTEGER ," +
                COL_DATE_IN + " DATETIME ," +
                COL_DATE_OUT + " DATETIME ," +
                COL_RECORDED_USER_ID + " INTEGER ," +
                COL_LOADED_WIEGHT + " FLOAT ," +
                COl_EMPTY_WIEGHT + " FLOAT ,"+
                COL_SYNC +" BOOLEAN );";
    }

    public static DestinationRecord load(long id, SQLiteDatabase db) {
        Cursor c = db.query(TABLE_NAME, COLS, COL_ID + "= ? ", new String[]{String.valueOf(id)}, null, null, null);
        DestinationRecord destRecord = null;
        if (c.moveToFirst()) {
            destRecord = new DestinationRecord();
            destRecord.setRecordId(c.getLong(c.getColumnIndex(COL_ID)));
            destRecord.setTruckId(c.getLong(c.getColumnIndex(COL_TRUCK_ID)));
            destRecord.setDestSiteId(c.getLong(c.getColumnIndex(COL_DEST_SITE_ID)));
            destRecord.setDateIn(c.getString(c.getColumnIndex(COL_DATE_IN)));
            destRecord.setDateOut(c.getString(c.getColumnIndex(COL_DATE_OUT)));
            destRecord.setRecordedUserId(c.getLong(c.getColumnIndex(COL_RECORDED_USER_ID)));
            destRecord.setLoadedWieght(c.getFloat(c.getColumnIndex(COL_LOADED_WIEGHT)));
            destRecord.setEmptyWieght(c.getFloat(c.getColumnIndex(COl_EMPTY_WIEGHT)));
        }

        return destRecord;
    }


    public static void deleteEveryThing(SQLiteDatabase db){
        db.delete(TABLE_NAME,null,null);
    }

    public static List<Truck> getAllTruckStillInSite(long siteId,SQLiteDatabase db){
        Cursor c = db.query(TABLE_NAME,COLS,COL_DEST_SITE_ID +" = ? ",new String[]{String.valueOf(siteId)},null,null,null);
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


    public static List<DestinationRecord> getAllInRecords(long siteId,SQLiteDatabase db){
        Cursor c = db.query(TABLE_NAME,COLS,COL_DEST_SITE_ID +" = ? AND ("+COL_DATE_OUT + " IS NULL OR "+ COL_DATE_OUT +"=? )",
                new String[]{String.valueOf(siteId),"0"},null,null,null);
        List<DestinationRecord> res = new ArrayList<>();

        if(c.moveToFirst()){
            do {
                res.add(
                        new DestinationRecord(
                                c.getLong(c.getColumnIndex(COL_ID)),
                                c.getLong(c.getColumnIndex(COL_TRUCK_ID)),
                                c.getLong(c.getColumnIndex(COL_DEST_SITE_ID)),
                                c.getString(c.getColumnIndex(COL_DATE_IN)),
                                c.getString(c.getColumnIndex(COL_DATE_OUT)),
                                c.getLong(c.getColumnIndex(COL_RECORDED_USER_ID)),
                                c.getFloat(c.getColumnIndex(COL_LOADED_WIEGHT)),
                                c.getFloat(c.getColumnIndex(COl_EMPTY_WIEGHT))
                        )
                );
            }while (c.moveToNext());
        }

        return res;
    }

    public Truck getTruck(SQLiteDatabase db) {
        Cursor c = db.query(TABLE_NAME, new String[]{COL_TRUCK_ID}, COL_ID + "=?", new String[]{String.valueOf(this.getRecordId())}, null, null, null);
        Truck res = null;

        if (c.moveToFirst())
            res = (Truck.load(c.getLong(c.getColumnIndex(COL_TRUCK_ID)), null, db));

        return res;
    }

    public static List<DestinationRecord> getAllRecordsForTruck(long truckId, SQLiteDatabase db) {
        Cursor c = db.query(TABLE_NAME, COLS, COL_TRUCK_ID + "=?", new String[]{String.valueOf(truckId)}, null, null, null);
        List<DestinationRecord> records = new ArrayList<>();

        if (c.moveToFirst()) {
            do {
                records.add(DestinationRecord.load(c.getLong(c.getColumnIndex(COL_ID)), db));
            } while (c.moveToNext());
        }

        return records;

    }

    public static List<DestinationRecord> getAllRecordsForSite(long siteId, SQLiteDatabase db) {
        Cursor c = db.query(TABLE_NAME, COLS, COL_DEST_SITE_ID + "=?", new String[]{String.valueOf(siteId)}, null, null, null);
        List<DestinationRecord> records = new ArrayList<>();

        if (c.moveToFirst()) {
            do {
                records.add(DestinationRecord.load(c.getLong(c.getColumnIndex(COL_ID)), db));

            } while (c.moveToNext());
        }

        return records;
    }


    public static List<Truck> getAllTruckForSite(long siteId, SQLiteDatabase db) {

        Cursor c = db.query(TABLE_NAME, COLS, COL_DEST_SITE_ID + "=? ", new String[]{String.valueOf(siteId)}, null, null, null);
        List<Truck> trucks = new ArrayList<>();
        if (c.moveToFirst()) {
            do {
                Truck truck = Truck.load(c.getLong(c.getColumnIndex(COL_TRUCK_ID)), null, db);
                trucks.add(truck);
            } while (c.moveToNext());
        }
        return trucks;
    }

    public static List<DestinationSite> getAllSiteForTruck(long truckId, SQLiteDatabase db) {
        Cursor c = db.query(TABLE_NAME, COLS, COL_TRUCK_ID + " = ?", new String[]{String.valueOf(truckId)}, null, null, null);
        List<DestinationSite> sites = new ArrayList<>();
        if (c.moveToFirst()) {
            do {
                DestinationSite site = DestinationSite.load(c.getLong(c.getColumnIndex(COL_DEST_SITE_ID)), db);
                sites.add(site);
            } while (c.moveToNext());
        }

        return sites;
    }

    public long saveCurrent(SQLiteDatabase db) {
        ContentValues cv = new ContentValues();
        cv.put(COL_TRUCK_ID, this.getTruckId());
        cv.put(COL_DEST_SITE_ID, this.getDestSiteId());
        cv.put(COL_DATE_IN, this.getFromatedDateIn());
        cv.put(COL_DATE_OUT, this.getFromatedDateOut());
        cv.put(COL_RECORDED_USER_ID, this.getRecordedUserId());
        cv.put(COL_LOADED_WIEGHT, this.getLoadedWieght());
        cv.put(COl_EMPTY_WIEGHT, this.getEmptyWieght());
        cv.put(COL_SYNC,false);

        try {
          this.setRecordId(db.insert(TABLE_NAME, null, cv));
        } catch (SQLiteConstraintException e) {

        }
        return this.getTruckId();
    }

    public boolean update(SQLiteDatabase db){

        ContentValues cv = new ContentValues();
        cv.put(COL_DATE_OUT,this.getFromatedDateOut());
        cv.put(COL_LOADED_WIEGHT,this.getLoadedWieght());
        cv.put(COl_EMPTY_WIEGHT,this.getEmptyWieght());

        return db.update(TABLE_NAME,cv,COL_ID +"=?",new String[]{String.valueOf(this.getRecordId())}) == 1;
    }



    public void deleteThis(SQLiteDatabase db){
        ContentValues cv = new ContentValues();
        cv.put(COL_SYNC,true);

        db.update(TABLE_NAME,cv,COL_ID +"=?",new String[]{String.valueOf(this.getRecordId())});
    }

    public static List<DestinationRecord> getAll(SQLiteDatabase db){
        Cursor c = db.query(TABLE_NAME,COLS,COL_SYNC +"=?",new String[]{String.valueOf(0)},null,null,null);
        List<DestinationRecord> res = new ArrayList<>();

        if(c.moveToFirst()){
            do{
                res.add(
                        new DestinationRecord(
                                c.getLong(c.getColumnIndex(COL_ID)),
                                c.getLong(c.getColumnIndex(COL_TRUCK_ID)),
                                c.getLong(c.getColumnIndex(COL_DEST_SITE_ID)),
                                c.getString(c.getColumnIndex(COL_DATE_IN)),
                                c.getString(c.getColumnIndex(COL_DATE_OUT)),
                                c.getLong(c.getColumnIndex(COL_RECORDED_USER_ID)),
                                c.getFloat(c.getColumnIndex(COL_LOADED_WIEGHT)),
                                c.getFloat(c.getColumnIndex(COl_EMPTY_WIEGHT))
                        )
                );
            }while (c.moveToNext());

        }
        return res;
    }


//.................................//


    private long recordId;
    private long truckId;
    private long destSiteId;
    private Long dateIn = null;
    private Long dateOut = null;
    private long recordedUserId;
    private float loadedWieght;
    private float emptyWieght;

    public DestinationRecord(){}

    public DestinationRecord(long recordId, long truckId, long destSiteId, String dateIn, String dateOut,
                             long recordedUserId, float loadedWieght, float emptyWieght) {
        this.recordId = recordId;
        this.truckId = truckId;
        this.destSiteId = destSiteId;
        this.setDateIn(dateIn);
        this.setDateOut(dateOut);
        this.recordedUserId = recordedUserId;
        this.loadedWieght = loadedWieght;
        this.emptyWieght = emptyWieght;
    }

    public float getEmptyWieght() {
        return emptyWieght;
    }

    public void setEmptyWieght(float emptyWieght) {
        this.emptyWieght = emptyWieght;
    }

    public float getLoadedWieght() {
        return loadedWieght;
    }

    public void setLoadedWieght(float loadedWieght) {
        this.loadedWieght = loadedWieght;
    }

    public long getRecordedUserId() {
        return recordedUserId;
    }

    public void setRecordedUserId(long recordedUserId) {
        this.recordedUserId = recordedUserId;
    }

    public long getRecordId() {
        return recordId;
    }

    public void setRecordId(long recordId) {
        this.recordId = recordId;
    }

    public long getTruckId() {
        return truckId;
    }

    public void setTruckId(long truckId) {
        this.truckId = truckId;
    }

    public long getDestSiteId() {
        return destSiteId;
    }

    public void setDestSiteId(long destSiteId) {
        this.destSiteId = destSiteId;
    }

    public Long getDateIn() {
        return dateIn;
    }

    public String getFromatedDateIn(){return MyApplication.fromateDate(this.getDateIn());}

    public void setDateIn(Long dateIn) {
        this.dateIn = dateIn;
    }

    public void setDateIn(String dateIn){
        this.dateIn = MyApplication.parseDate(dateIn);
    }

    public Long getDateOut() {
        return dateOut;
    }

    public String getFromatedDateOut(){return MyApplication.fromateDate(this.getDateOut());}

    public void setDateOut(Long dateOut) {
        this.dateOut = dateOut;
    }

    public void setDateOut(String dateOut){
        this.dateOut = MyApplication.parseDate(dateOut);
    }

    @Override
    public boolean equals(Object o) {
        return this.getRecordId() == ((DestinationRecord)o).getRecordId();
    }

    public void uploadThis(){


        HashMap<String,String> params = new HashMap<>();
        params.put("function","DestinationLogsPostList");
        params.put("security_key",MyApplication.calScurityKey()+"");
        params.put("Data",getJSON());

        String url = MyApplication.BASE_POST_URL+"?function=DestinationLogsPostList&Data="+
                getJSON()+"&security_key="+MyApplication.calScurityKey();
        JSONObject object = new JSONObject(params);

        StringRequest request = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String jsonObject) {
                        //String responseStr = jsonObject.toString();
                        com.example.raafat.trillium.Response response =
                                com.example.raafat.trillium.Response.fromJson(jsonObject.toString());
                        if(!response.response) {
                            Toast.makeText(MyApplication.APP_CTX, response.responseMessage, Toast.LENGTH_LONG).show();
                            DestinationRecord.this.deleteThis(MyApplication.db);
                        }else{
                            DestinationRecord.this.deleteThis(MyApplication.db);
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

        List<DestinationRecord> records = DestinationRecord.getAll(MyApplication.db);
        MyApplication.showDilog2(records.size());
        for(DestinationRecord record : records){
            record.uploadThis();
        }

        if(records.size() == 0)
            MyApplication.isDone();
    }

    public String getJSON(){
        String res = "[";
        HashMap<String,String> values  = new HashMap<>();
        values.put("DestinationSiteId",this.getDestSiteId()+"");
        values.put("TruckId",this.getTruckId()+"");
        values.put("DateIn",this.getFromatedDateIn());
        values.put("DateOut",this.getFromatedDateOut());
        values.put("EmptyWeight",this.getEmptyWieght()+"");
        values.put("LoadedWeight",this.getLoadedWieght()+"");
        values.put("RecordUserId",this.getRecordedUserId()+"");


        JSONObject object = new JSONObject(values);
        res += object.toString();

        res += "]";
        return Strings.urlEncode(res);
    }
}
