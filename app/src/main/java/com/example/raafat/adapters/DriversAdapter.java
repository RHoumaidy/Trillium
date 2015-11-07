package com.example.raafat.adapters;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatCheckedTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.raafat.model.Driver;
import com.example.raafat.trillium.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.File;
import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by Raafat on 19/09/2015.
 */
public class DriversAdapter extends ArrayAdapter<Driver> {

    private List<Driver> data;
    private Context ctx;
    private LayoutInflater inflater;
    private int res;


    public DriversAdapter(Context context, int resource, List<Driver> objects) {
        super(context, resource, objects);
        this.ctx = context;
        this.res = resource;
        this.inflater = LayoutInflater.from(ctx);
        this.data = objects;
    }


    @Override
    public int getCount() {
        return this.data.size();
    }

    @Override
    public Driver getItem(int position) {
        return this.data.get(position);
    }

    @Override
    public int getPosition(Driver item) {
        int id = 0;

        for (Driver d : this.data) {
            if (d.getDriverId() == item.getDriverId())
                return id;
            id++;
        }
        return id;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null)
            convertView = this.inflater.inflate(res, null, false);

        Driver driver = this.getItem(position);

        CheckedTextView textView = (CheckedTextView) convertView.findViewById(R.id.DriverNameTv);
        ImageView driverProfile = (ImageView)convertView.findViewById(R.id.DriverProfileIV);

        textView.setText(driver.getDriverName());
        textView.setChecked(driver.isSelected);

        try {
            Picasso.with(ctx)
                    .load(new File(driver.getImgUrl()))
                    .error(R.drawable.no_sign)
                    .into(driverProfile);
        }catch (Exception e){

        }
        return convertView;
    }
}
