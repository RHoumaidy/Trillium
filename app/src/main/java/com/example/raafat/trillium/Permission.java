package com.example.raafat.trillium;

import android.content.Intent;

import com.example.raafat.activities.TrucksActivity;

/**
 * Created by Raafat on 13/10/2015.
 */
public class Permission {
    private String name;
    private Class<?> intent;



    public Permission(String s, Class<?> activity) {
        this.name = s;
        this.intent = activity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class<?> getIntent() {
        return intent;
    }

    public void setIntent(Class<?> intent) {
        this.intent = intent;
    }
}
