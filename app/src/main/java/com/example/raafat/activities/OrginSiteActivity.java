package com.example.raafat.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.raafat.adapters.OrgineSiteAdapter;
import com.example.raafat.adapters.SiteAdapter;
import com.example.raafat.model.DestinationRecord;
import com.example.raafat.model.OrginSite;
import com.example.raafat.model.OriginRecord;
import com.example.raafat.model.Truck;
import com.example.raafat.model.TruckDrivers;
import com.example.raafat.model.User;
import com.example.raafat.trillium.MyApplication;
import com.example.raafat.trillium.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class OrginSiteActivity extends ActionBarActivity {

    private ListView inLv;
    private ListView outLv;

    public static List<Truck> inTruck = new ArrayList<>();
    public static List<Truck> outTruck = new ArrayList<>();
    public static List<OrginSite> allSites = null;
    public static List<Truck> allTrucks = new ArrayList<>();
    public static List<OriginRecord> inRecords = new ArrayList<>();

    public static OrgineSiteAdapter adapter1;
    public static OrgineSiteAdapter adapter2;
    public static SiteAdapter spinnerAdapter;
    public static OriginRecord record = new OriginRecord();

    public static Spinner siteSpinner;
    public int spinnerCurrentSelected = 0;


    private HashMap<OrginSite, List<Truck>> inTruckForEachSite = new HashMap<>();

    private TimerTask timerTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orgin_site);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        View view = LayoutInflater.from(this).inflate(R.layout.action_bar_layout,null);
        ((TextView)view.findViewById(R.id.actionBarDate)).setText(MyApplication.sdf3.format(System.currentTimeMillis()));

        actionBar.setCustomView(view);



        Intent intent = getIntent();

        inLv = (ListView) findViewById(R.id.orginSiteInLV);
        outLv = (ListView) findViewById(R.id.orginSiteOutLV);
        MyApplication.workShiftTV = (TextView) findViewById(R.id.workShiftTV);

        siteSpinner = (Spinner) findViewById(R.id.selectSiteSpinner);

        allSites = OrginSite.getAll(MyApplication.db);
        spinnerAdapter = new SiteAdapter(this, android.R.layout.simple_list_item_checked, allSites);

        allTrucks = Truck.getAll(MyApplication.db);


        siteSpinner.post(new Runnable() {
            @Override
            public void run() {
                siteSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
                        if (position != spinnerCurrentSelected) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(OrginSiteActivity.this);
                            builder.setTitle("Change site")
                                    .setMessage("Are you sure you want to change the site ..")
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .setCancelable(false)
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            spinnerCurrentSelected = position;
                                            fillTrucksLists();

                                            adapter1.notifyDataSetChanged();
                                            adapter2.notifyDataSetChanged();
                                        }
                                    }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    siteSpinner.setSelection(spinnerCurrentSelected);
                                }
                            }).show();
                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        });

        siteSpinner.setAdapter(spinnerAdapter);

        fillTrucksLists();


        adapter1 = new OrgineSiteAdapter(this, R.layout.origin_site_in_list_item_layout, inTruck);
        adapter2 = new OrgineSiteAdapter(this, R.layout.origin_site_out_list_item_layout, outTruck);

        inLv.setAdapter(adapter1);
        outLv.setAdapter(adapter2);

        timerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MyApplication.updateWorkShift();
                    }
                });
            }
        };

        Timer timer = new Timer();
        timer.schedule(timerTask, 1000,1000);

    }


    @Override
    protected void onPause() {
        super.onPause();
        timerTask.cancel();
    }

    public void fillTrucksLists() {

        inRecords.clear();
        outTruck.clear();
        inTruck.clear();
        OrginSite curSite = spinnerAdapter.getItem(spinnerCurrentSelected);
        for (OrginSite site : allSites) {
            inTruckForEachSite.put(site, OriginRecord.getAllTruckStillInSite(site.getOrginSiteId(), MyApplication.db));
        }

        //outTruck.addAll(allTrucks);
        for (Truck truck : allTrucks)
            if (inTruckForEachSite.get(curSite).indexOf(truck) == -1)
                outTruck.add(truck);

        inRecords.addAll(OriginRecord.getAllInRecords(curSite.getOrginSiteId(), MyApplication.db));
        for (OriginRecord record1 : inRecords) {
            inTruck.add(record1.getTruck(MyApplication.db));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    private boolean sillTruckin(){
        fillTrucksLists();

        for(OrginSite key : inTruckForEachSite.keySet()){
            if(inTruckForEachSite.get(key).size()>0)
                return true;
        }

        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_exit) {
            final Intent intent = new Intent(this, LoginActivity.class);
            new AlertDialog.Builder(this)
                    .setTitle("LogOut")
                    .setMessage("Are you sure you want to log out ??")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            OrginSiteActivity.this.finish();
                            startActivity(intent);

                        }
                    }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }).show();

            return true;
        }else if(id == R.id.action_sync){

            if(sillTruckin()) {
                Toast.makeText(this, "There is Truck Still in site", Toast.LENGTH_LONG).show();
                return false;
            }

            new AlertDialog.Builder(this)
                    .setCancelable(false)
                    .setMessage("Are you sure you want to Upload Data ?")
                    .setTitle("Upload Conformation")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            MyApplication.doneProcess = 0;
                            MyApplication.currContext = OrginSiteActivity.this;
                            //MyApplication.showDialog("Uploading Data ...");
                            OriginRecord.uploadData();

                        }
                    }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }).show();

            return true;

        }

        return super.onOptionsItemSelected(item);
    }

    private boolean backPressed = false;

    @Override
    public void onBackPressed() {

        if(!backPressed) {
            backPressed = true;
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    backPressed = false;
                }
            }, 3500);
            Toast.makeText(this, ("Press again to exit "), Toast.LENGTH_LONG).show();
        }else
            super.onBackPressed();
    }

}
