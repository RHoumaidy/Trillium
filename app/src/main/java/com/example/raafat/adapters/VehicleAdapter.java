package com.example.raafat.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.raafat.model.Driver;
import com.example.raafat.model.Truck;
import com.example.raafat.model.TruckDrivers;
import com.example.raafat.trillium.MyApplication;
import com.example.raafat.trillium.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

/**
 * Created by Raafat on 19/09/2015.
 */
public class VehicleAdapter extends ArrayAdapter<Truck> {

    private Context ctx;
    private List<Truck> data;
    private LayoutInflater infaInflater;
    private int res;

    public VehicleAdapter(Context context, int resource, List<Truck> objects) {
        super(context, resource, objects);


        this.ctx = context;
        this.data = objects;
        this.infaInflater = LayoutInflater.from(ctx);
        this.res = resource;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = infaInflater.inflate(res, null, false);
        }


        final Truck truck = this.getItem(position);

        TextView truckName = (TextView) convertView.findViewById(R.id.vehicleNameTV);
        ImageView driverProile = (ImageView) convertView.findViewById(R.id.driverProfileIV);
        TextView driver = (TextView) convertView.findViewById(R.id.driverNameTv);

        Driver driverForThisTruck = TruckDrivers.getDriversForTruck(truck.getTruckId(), MyApplication.db);
        if (driverForThisTruck == null)
            driverForThisTruck = new Driver(-1, "No Driver", "http");

        driver.setText(driverForThisTruck.getDriverName());
        truckName.setText(truck.getTruckName());
        Picasso.with(ctx)
                .load(new File(driverForThisTruck.getImgUrl()))
                .error(R.drawable.no_sign)
                .into(driverProile);


//        final MSpinnerAdapter mSpinnerAdapter =
//                new MSpinnerAdapter(ctx, R.layout.spinner_driver_row_layout, allDrivers);


//        spinner.setAdapter(mSpinnerAdapter);
//        spinner.setSelection(mSpinnerAdapter.getPosition(driverForThisTruck));
//
//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Driver driver = ((Driver) parent.getItemAtPosition(position));
//                int indx = data.indexOf(TruckDrivers.getTruckForDriver(driver.getDriverId(), MyApplication.db));
//                TruckDrivers.setDriverForTruck(truck.getTruckId(), driver.getDriverId(), MyApplication.db);
//                Truck otherTruck = data.get(indx);
//
//                Driver driverForThisTruck = TruckDrivers.getDriversForTruck(truck.getTruckId(), MyApplication.db);
//
//                Picasso.with(ctx)
//                        .load(driverForThisTruck.getImgUrl())
//                        .error(android.R.drawable.stat_notify_error)
//                        .into(driverProile);
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

        return convertView;
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
        return position;
    }
}
