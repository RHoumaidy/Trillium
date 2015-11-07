package com.example.raafat.trillium;

import com.google.gson.Gson;

/**
 * Created by Raafat on 10/10/2015.
 */
public class Response {

    public boolean response;
    public String responseMessage;

    public static Response fromJson(String json){
        Gson gson = new Gson();
        Response response1 = gson.fromJson(json,Response.class);
        return response1;
    }

}
