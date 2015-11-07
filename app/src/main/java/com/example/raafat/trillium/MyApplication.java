package com.example.raafat.trillium;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.raafat.model.WorkShift;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Raafat on 19/09/2015.
 */
public class MyApplication extends Application {


    public static DpHelper dpHelper;

    public static SQLiteDatabase db;
    public static Context APP_CTX;
    public static SharedPreferences sp;

    public static SimpleDateFormat sdf1 = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
    public static SimpleDateFormat sdf3 = new SimpleDateFormat("MM/dd/yyyy");
    public static SimpleDateFormat sdf2 = new SimpleDateFormat("hh:mm");

    public static TextView workShiftTV;

    public static String[] permissions = new String[]{"Truck Admin", "OriginSite Admin", "DestinationSite Admin"};

    public static final String TRUCK_ADMIN = "Truck Admin";
    public static final String ORIGINE_ADMINE = "OrgineSite Admin";
    public static final String DESTINEATION_ADMIN = "DestinatenSite Admin";

    public static final String BASE_URL = "http://vls.trilliumholding.com.lb/services/GetService.aspx";
    public static final String BASE_POST_URL = "http://vls.trilliumholding.com.lb/services/PostService.aspx";
    public static String DRIVERS_URL = BASE_URL + "?function=getDriverList";
    public static String USERS_URL = BASE_URL + "?function=GetUserList";
    public static String VEHICLES_URL = BASE_URL +"?function=getVehiclesList";
    public static String ORIGINE_SITE_URL = BASE_URL + "?function=GetOriginSiteList";
    public static String DESTINATION_SITE_URL = BASE_URL +"?function=GetDestinationSiteList";
    public static String TRUCK_DRIVER_URL = BASE_URL+"?function=GetVehicles_Driver_LogList";


    private static RequestQueue queue;
    private static MyApplication instance;

    public static ProgressDialog dialog;

    public static int doneProcess = 0;
    public static int processCount = 0;
    public static String filesDir = "data/data/";

    public static Context currContext;
    @Override
    public void onCreate() {
        super.onCreate();

        dpHelper = new DpHelper(getApplicationContext());
        db = dpHelper.getWritableDatabase();
        APP_CTX = getApplicationContext();
        sp = PreferenceManager.getDefaultSharedPreferences(APP_CTX);

        instance = this;
        queue = Volley.newRequestQueue(APP_CTX);
        filesDir += APP_CTX.getPackageName()+"/files/";

    }

    public static String fromateTime(Long time) {
        if (time == null)
            return null;
        return MyApplication.sdf2.format(time);
    }

    public static Long parseTime(String time) {
        try {
            return MyApplication.sdf2.parse(time).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String fromateDate(Long date) {
        if (date == null)
            return null;
        return MyApplication.sdf1.format(date);
    }

    public static Long parseDate(String date) {
        if (date == null)
            return null;
        try {
            return MyApplication.sdf1.parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return System.currentTimeMillis();
    }

    public static void updateWorkShift() {
        workShiftTV.setText(WorkShift.getCurrentWorkShaft(MyApplication.db).getWorkShiftName());
    }


    public static long calScurityKey() {


        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.DAY_OF_MONTH) * (cal.get(Calendar.MONTH) + 1) * cal.get(Calendar.YEAR);

        //return res;
    }

    public static String getFullUrl(String Url) {
        return Url + "&security_key=" + MyApplication.calScurityKey();
    }


    public static MyApplication getInstance() {
        return instance;
    }

    public static <T> void addToReqeustQue(Request<T> req) {
        queue.add(req);
        processCount ++;
    }


    public static void saveBitmap(String fileName, Bitmap img) throws IOException {

        OutputStream os = null;
        os = APP_CTX.openFileOutput(fileName,MODE_PRIVATE);
        img.compress(Bitmap.CompressFormat.JPEG, 100, os);

        os.flush();
        os.close();

    }

    public static void showDialog(String msg) {
        dialog = new ProgressDialog(currContext);
        dialog.setCanceledOnTouchOutside(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setMessage(msg);
        dialog.getWindow().setBackgroundDrawable(APP_CTX.getResources().getDrawable(R.drawable.dialog_background));
        MyApplication.dialog.show();
    }

    public static void showDilog2(int max){
        dialog = new ProgressDialog(currContext);
        dialog.setCancelable(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setMessage("Uploading Data ...");
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setProgress(0);
        dialog.setMax(max);
        dialog.show();

    }

    public static void updateProgress(){
        dialog.incrementProgressBy(1);
    }


    public static void hideDialog() {
        if (MyApplication.dialog != null && MyApplication.dialog.isShowing())
            MyApplication.dialog.hide();
    }

    public static boolean isDone() {
        if (MyApplication.doneProcess >= processCount) {
            processCount = 0;
            MyApplication.hideDialog();
            Toast.makeText(APP_CTX,"Done..",Toast.LENGTH_LONG).show();
        }
        return (MyApplication.doneProcess >= processCount);
    }
}
