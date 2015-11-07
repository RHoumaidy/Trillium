package com.example.raafat.adapters;

import android.content.Context;
import android.support.v7.widget.AppCompatCheckedTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.raafat.model.DestinationSite;
import com.example.raafat.model.OrginSite;

import java.util.List;

/**
 * Created by Raafat on 23/09/2015.
 */
public class SiteAdapter2 extends ArrayAdapter<DestinationSite> {

    private List<DestinationSite> data;
    private Context ctx;
    private LayoutInflater inflater;
    private int res;


    public SiteAdapter2 (Context context, int resource, List<DestinationSite> objects) {
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
    public DestinationSite getItem(int position) {
        return this.data.get(position);
    }

    @Override
    public int getPosition(DestinationSite item) {
        return this.data.indexOf(item);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = this.inflater.inflate(res, null, false);

        DestinationSite site = this.getItem(position);

        AppCompatCheckedTextView textView = (AppCompatCheckedTextView) convertView;
        //textView.setChecked(true);
        textView.setText(site.getDestinatinSiteName());
        return convertView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null)
            convertView = this.inflater.inflate(res, null, false);

        DestinationSite site = this.getItem(position);

        AppCompatCheckedTextView textView = (AppCompatCheckedTextView) convertView;
        textView.setChecked(true);
        textView.setText(site.getDestinatinSiteName());
        return convertView;
    }
}
