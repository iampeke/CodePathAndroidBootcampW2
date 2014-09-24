package com.example.rrich.gridimagesearch.models;

import org.json.*;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by rrich on 9/20/14.
 */
public class ImageResult implements Serializable{
    
    public String fullUrl;
    public String thumbUrl;
    public String title;

    // new ImageResult(...raw item json..)
    public ImageResult(JSONObject json) {
        try {
            // Set fields for object based on JSON data.
            this.fullUrl = json.getString("url");
            this.thumbUrl = json.getString("tbUrl");
            this.title = json.getString("title");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Take an array of JSON results and return an arraylist of imageresult's.
    // ImageResult.fromJSONArray([..., ...])
    public static ArrayList<ImageResult> fromJSONArray(JSONArray array) {
        ArrayList<ImageResult> results = new ArrayList<ImageResult>();
        for (int i=0; i < array.length(); i++) {
            try {
                results.add(new ImageResult(array.getJSONObject(i)));
            } catch (JSONException e){
                e.printStackTrace();
            }
        }
        return results;
    }
}
