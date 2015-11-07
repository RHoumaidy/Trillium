package com.example.raafat.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.raafat.adapters.DriversAdapter;
import com.example.raafat.adapters.VehicleAdapter;
import com.example.raafat.model.Driver;
import com.example.raafat.model.OriginRecord;
import com.example.raafat.model.Truck;
import com.example.raafat.model.TruckDrivers;
import com.example.raafat.trillium.MyApplication;
import com.example.raafat.trillium.R;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Raafat on 19/09/2015.
 */
public class TrucksActivity extends ActionBarActivity {

    private ListView lv;
    private VehicleAdapter adapter;

    private DriversAdapter spinnerAdapter;
    private List<Driver> allDrivers;

    private TimerTask timerTask;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        View view = LayoutInflater.from(this).inflate(R.layout.action_bar_layout, null);
        ((TextView) view.findViewById(R.id.actionBarDate)).setText(MyApplication.sdf3.format(System.currentTimeMillis()));

        actionBar.setCustomView(view);


        allDrivers = Driver.getAllDrivers(MyApplication.db);
        allDrivers.add(0, new Driver(-1, "No Driver", "http"));

        lv = (ListView) findViewById(R.id.vehicleLV);
        MyApplication.workShiftTV = (TextView) findViewById(R.id.workShiftTV);

        adapter = new VehicleAdapter(this, R.layout.vehicl_item_layout, Truck.getAll(MyApplication.db));
        spinnerAdapter = new DriversAdapter(this, R.layout.spinner_driver_row_layout, allDrivers);

        lv.setAdapter(adapter);
        lv.setItemsCanFocus(true);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Dialog dialog = new Dialog(TrucksActivity.this);
                dialog.setContentView(R.layout.dialog_choose_driver_layout);
                final ListView listView = (ListView) dialog.findViewById(R.id.diaogSelectDriverLV);
                final Truck truck = adapter.getItem(position);

                allDrivers.clear();
                allDrivers.addAll(Driver.getAllDrivers(MyApplication.db));
                allDrivers.add(0, new Driver(-1, "No Driver", "http"));

                final Driver driverForThisTruck = TruckDrivers.getDriversForTruck(truck.getTruckId(), MyApplication.db);

                int idx;
                if (driverForThisTruck != null)
                    idx = allDrivers.indexOf(driverForThisTruck);
                else
                    idx = 0;
                allDrivers.get(idx).isSelected = true;
                spinnerAdapter.notifyDataSetChanged();
                listView.setAdapter(spinnerAdapter);


                //listView.getChildAt(spinnerAdapter.getPosition(driverForThisTruck)).findViewById(R.id.DriverNameTv).setSelected(true);
                dialog.setTitle(truck.getTruckName());
                dialog.show();

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        dialog.dismiss();

                        final Driver driver = spinnerAdapter.getItem(position);
                        AlertDialog.Builder builder = new AlertDialog.Builder(TrucksActivity.this);

                        String msg = "";

                        if (driver.getDriverId() != -1)
                            msg = "Do you want to change Driver to " + driver.getDriverName() + " ? ";
                        else
                            msg = "Do you want to remove Driver ?";


                        if (driverForThisTruck == null || driver.getDriverId() != driverForThisTruck.getDriverId()) {
                            builder.setTitle("Change Driver Confirm ")
                                    .setMessage(msg)
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            TruckDrivers.setDriverForTruck(truck.getTruckId(), driver.getDriverId(), MyApplication.db);
                                            adapter.notifyDataSetChanged();

                                        }
                                    })
                                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();


                        }
                    }
                });


            }
        });

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
        timer.schedule(timerTask, 1000, 1000);

    }


    @Override
    protected void onPause() {
        super.onPause();
        timerTask.cancel();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_exit) {
            final Intent intent = new Intent(this, LoginActivity.class);
            new AlertDialog.Builder(this)
                    .setTitle("LogOut")
                    .setMessage("Are you sure you want to log out ??")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            TrucksActivity.this.finish();
                            startActivity(intent);
                        }
                    }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }).show();

            return true;
        } else if (id == R.id.action_sync) {


            new AlertDialog.Builder(this)
                    .setCancelable(false)
                    .setMessage("Are you sure you want to Upload Truck_Driver Log ?")
                    .setTitle("Upload Conformation")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            MyApplication.doneProcess = 0;
                            MyApplication.currContext = TrucksActivity.this;
                            //MyApplication.showDialog("Uploading Data ...");
                            TruckDrivers.uploadData();

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

        if (!backPressed) {
            backPressed = true;
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    backPressed = false;
                }
            }, 3500);
            Toast.makeText(this, ("Press again to exit "), Toast.LENGTH_LONG).show();
        } else
            super.onBackPressed();
    }
}
