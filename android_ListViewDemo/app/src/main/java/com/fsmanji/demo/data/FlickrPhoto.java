package com.fsmanji.demo.data;

import org.json.JSONObject;

/**
 * Created by cristanz on 10/1/15.
 */
public class FlickrPhoto {
    public String title;
    public String url_m;
    public FlickrPhoto(JSONObject object) {
        try {
            if (object != null) {
                title = object.getString("title");
                url_m = object.getString("url_m");
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public FlickrPhoto(String title, String urlm) {
        this.title = title;
        this.url_m = urlm;
    }
}
