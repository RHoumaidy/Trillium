package com.example.raafat.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.Image;
import android.text.InputType;
import android.text.Layout;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.raafat.activities.DestinationSiteAcitvity;
import com.example.raafat.activities.OrginSiteActivity;
import com.example.raafat.model.DestinationRecord;
import com.example.raafat.model.DestinationSite;
import com.example.raafat.model.Driver;
import com.example.raafat.model.OrginSite;
import com.example.raafat.model.OriginRecord;
import com.example.raafat.model.Truck;
import com.example.raafat.model.TruckDrivers;
import com.example.raafat.model.User;
import com.example.raafat.trillium.MyApplication;
import com.example.raafat.trillium.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.security.Key;
import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by Raafat on 21/09/2015.
 */
public class OrgineSiteAdapter extends ArrayAdapter<Truck> {

    private List<Truck> data;
    private Context ctx;
    private LayoutInflater inflater;
    private int res;

    public OrgineSiteAdapter(Context context, int resource, List<Truck> objects) {
        super(context, resource, objects);
        this.ctx = context;
        this.res = resource;
        this.data = objects;
        this.inflater = LayoutInflater.from(ctx);
    }


    @Override
    public int getCount() {
        return this.data.size();
    }

    @Override
    public Truck getItem(int position) {
        return this.data.get(position);
    }

    @Override
    public int getPosition(Truck item) {
        return this.data.indexOf(item);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return super.getDropDownView(position, convertView, parent);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null)
            convertView = this.inflater.inflate(res, null, false);

        ImageView profileImage = (ImageView) convertView.findViewById(R.id.driverProfileIV);
        final TextView truckName = (TextView) convertView.findViewById(R.id.vehicleNameTV);
        TextView driverName = (TextView) convertView.findViewById(R.id.driverNameTv);
        final TextView leavBtn = (TextView) convertView.findViewById(R.id.leavBtn);
        final EditText loadedWieghtET = (EditText) convertView.findViewById(R.id.loadedWieghtET);
        final EditText emptyWieghtET = (EditText) convertView.findViewById(R.id.emptyWieghtET);
        LinearLayout ll = (LinearLayout)convertView.findViewById(R.id.editText);

        final Truck truck = this.getItem(position);
        final AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setTitle("Confirm Transaction")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setCancelable(false);


        if (ctx instanceof DestinationSiteAcitvity) {

            if (res == R.layout.origin_site_in_list_item_layout) {

                ll.setVisibility(View.VISIBLE);

                leavBtn.setVisibility(View.INVISIBLE);
                emptyWieghtET.setEnabled(false);
                loadedWieghtET.setEnabled(false);

                DestinationSiteAcitvity.record = DestinationSiteAcitvity.inRecords.get(position);

                loadedWieghtET.setText(DestinationSiteAcitvity.record.getLoadedWieght()+"" );
                emptyWieghtET.setText(DestinationSiteAcitvity.record.getEmptyWieght()+"");

                if(DestinationSiteAcitvity.record.getLoadedWieght() <= 0.01f){
                    loadedWieghtET.setEnabled(true);
                }else {
                    if (DestinationSiteAcitvity.record.getEmptyWieght() <= 0.01f)
                        emptyWieghtET.setEnabled(true);
                    else
                        leavBtn.setVisibility(View.VISIBLE);
                }



                loadedWieghtET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if ((event != null  && event.getAction() == KeyEvent.ACTION_DOWN &&  event.getKeyCode() == KeyEvent.KEYCODE_ENTER) ||
                                ((actionId == EditorInfo.IME_ACTION_DONE) ||
                                (actionId == EditorInfo.IME_ACTION_NEXT) ||
                                (actionId == EditorInfo.IME_ACTION_GO))) {

                            builder.setMessage("Are you Sure you you want to set loaded Weight \nfor Truck : "
                                    + truck.getTruckName()
                                    + "\nto :" + loadedWieghtET.getText() + " Kg ?")
                                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            DestinationSiteAcitvity.record = DestinationSiteAcitvity.inRecords.get(position);
                                            DestinationSiteAcitvity.record.setLoadedWieght(
                                                    Float.parseFloat(loadedWieghtET.getText().toString()));
                                            DestinationSiteAcitvity.record.update(MyApplication.db);

                                            emptyWieghtET.setEnabled(true);
                                            loadedWieghtET.setEnabled(false);
                                        }
                                    }).setNegativeButton(android.R.string.no,new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).show();
                            return true;

                        }
                        return false;
                    }
                });

                emptyWieghtET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if ((event != null  && event.getAction() == KeyEvent.ACTION_DOWN &&  event.getKeyCode() == KeyEvent.KEYCODE_ENTER) ||
                                (actionId == EditorInfo.IME_ACTION_DONE) ||
                                (actionId == EditorInfo.IME_ACTION_NEXT) ||
                                (actionId == EditorInfo.IME_ACTION_GO)) {

                            builder.setMessage("Are you Sure you you want to set empty Weight \nfor Truck : "
                                    + truck.getTruckName()
                                    + "\nto :" + emptyWieghtET.getText() + " Kg ?")
                                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            DestinationSiteAcitvity.record = DestinationSiteAcitvity.inRecords.get(position);
                                            DestinationSiteAcitvity.record = DestinationSiteAcitvity.inRecords.get(position);
                                            DestinationSiteAcitvity.record.setEmptyWieght(
                                                    Float.parseFloat(emptyWieghtET.getText().toString()));
                                            DestinationSiteAcitvity.record.update(MyApplication.db);

                                            leavBtn.setVisibility(View.VISIBLE);
                                            emptyWieghtET.setEnabled(false);
                                        }
                                    }).setNegativeButton(android.R.string.no,new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).show();
                            return true;

                        }
                        return false;
                    }
                });
            }
        }


        leavBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final long currTime = System.currentTimeMillis();

                // OrginSite
                if (ctx instanceof OrginSiteActivity) {


                    if (res == R.layout.origin_site_in_list_item_layout) {

                        builder.setMessage("Are you sure you want to Check out the Truck " + truck.getTruckName())
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {


                                        OrginSiteActivity.record = OrginSiteActivity.inRecords.get(position);
                                        OrginSiteActivity.record.setDateOut(currTime);

                                        if (OrginSiteActivity.record.update(MyApplication.db)) {
                                            OrginSiteActivity.inRecords.remove(OrginSiteActivity.record);
                                            OrginSiteActivity.inTruck.remove(truck);
                                            OrginSiteActivity.outTruck.add(truck);
                                        }

                                        OrginSiteActivity.adapter1.notifyDataSetChanged();
                                        OrginSiteActivity.adapter2.notifyDataSetChanged();
                                    }
                                }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();

                    } else if (res == R.layout.origin_site_out_list_item_layout) {
                        builder.setMessage("Are you sure you want to Check in the truck " + truck.getTruckName())
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        OrginSiteActivity.outTruck.remove(truck);
                                        OrginSiteActivity.inTruck.add(truck);

                                        OrginSiteActivity.record = new OriginRecord();
                                        OrginSiteActivity.record.setDateIn(currTime);
                                        OrginSiteActivity.record.setRecordedUserId(MyApplication.sp.getLong(User.COL_ID, 0));
                                        OrginSiteActivity.record.setTruckId(truck.getTruckId());
                                        OrginSiteActivity.record.setOrginSiteId(
                                                ((OrginSite) OrginSiteActivity.siteSpinner.getSelectedItem())
                                                        .getOrginSiteId());

                                        OrginSiteActivity.record.saveCurrent(MyApplication.db);
                                        OrginSiteActivity.inRecords.add(OrginSiteActivity.record);

                                        OrginSiteActivity.adapter1.notifyDataSetChanged();
                                        OrginSiteActivity.adapter2.notifyDataSetChanged();
                                    }
                                }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();

                    }

                } else { //Destenation Site

                    if (res == R.layout.origin_site_in_list_item_layout) {

                        builder.setMessage("are you sure you want to Check out \nTruck : " + truck.getTruckName())
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        //set empty weight
                                        DestinationSiteAcitvity.record = DestinationSiteAcitvity.inRecords.get(position);
                                        DestinationSiteAcitvity.record.setDateOut(currTime);

                                        if (DestinationSiteAcitvity.record.update(MyApplication.db)) {
                                            DestinationSiteAcitvity.inTruck.remove(truck);
                                            DestinationSiteAcitvity.outTruck.add(truck);
                                            DestinationSiteAcitvity.inRecords.remove(DestinationSiteAcitvity.record);

                                        }

                                        DestinationSiteAcitvity.adapter1.notifyDataSetChanged();
                                        DestinationSiteAcitvity.adapter2.notifyDataSetChanged();


                                    }
                                }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();


                    } else if (res == R.layout.origin_site_out_list_item_layout) {
                        builder.setMessage("Are you sure you want to Check in the truck " + truck.getTruckName())
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {


                                        DestinationSiteAcitvity.outTruck.remove(truck);
                                        DestinationSiteAcitvity.inTruck.add(truck);

                                        DestinationSiteAcitvity.record = new DestinationRecord();
                                        DestinationSiteAcitvity.record.setDateIn(currTime);
                                        DestinationSiteAcitvity.record.setRecordedUserId(MyApplication.sp.getLong(User.COL_ID, 0));
                                        DestinationSiteAcitvity.record.setTruckId(truck.getTruckId());
                                        DestinationSiteAcitvity.record.setDestSiteId(
                                                ((DestinationSite) DestinationSiteAcitvity.siteSpinner.getSelectedItem())
                                                        .getDestinationSiteId());


                                        DestinationSiteAcitvity.record.saveCurrent(MyApplication.db);
                                        DestinationSiteAcitvity.inRecords.add(DestinationSiteAcitvity.record);

                                        DestinationSiteAcitvity.adapter1.notifyDataSetChanged();
                                        DestinationSiteAcitvity.adapter2.notifyDataSetChanged();


                                    }
                                }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
                    }


                }
            }
        });


        Driver driver = TruckDrivers.getDriversForTruck(truck.getTruckId(), MyApplication.db);
        if (driver == null)
            driver = new Driver(-1, "Unknown", "http");
        Picasso.with(ctx)
                .load(new File(driver.getImgUrl()))
                .error(R.drawable.no_sign)
                .into(profileImage);

        truckName.setText(truck.getTruckName());
        driverName.setText(driver.getDriverName());


        return convertView;
    }

}
