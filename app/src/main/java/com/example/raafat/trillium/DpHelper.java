package com.example.raafat.trillium;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.raafat.model.DestinationRecord;
import com.example.raafat.model.DestinationSite;
import com.example.raafat.model.Driver;
import com.example.raafat.model.OrginSite;
import com.example.raafat.model.OriginRecord;
import com.example.raafat.model.Truck;
import com.example.raafat.model.TruckDrivers;
import com.example.raafat.model.User;
import com.example.raafat.model.WorkShift;

/**
 * Created by Raafat on 19/09/2015.
 */
public class DpHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "MY_DB";
    public static final int DB_VERSION = 2;

    private Context cntx;

    public DpHelper(Context context){
        super(context,DB_NAME,null,DB_VERSION);
        this.cntx = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(DestinationSite.getCreateSql());
        db.execSQL(Driver.getCreateSql());
        db.execSQL(OrginSite.getCreateSql());
        db.execSQL(OriginRecord.getCreateSql());
        db.execSQL(DestinationRecord.getCreateSql());
        db.execSQL(Truck.getCreateSql());
        db.execSQL(TruckDrivers.getCreateSql());
        db.execSQL(User.getCreateSql());
        db.execSQL(WorkShift.getCreateSql());

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //

    }
}
