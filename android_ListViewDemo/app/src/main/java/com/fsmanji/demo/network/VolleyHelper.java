package com.fsmanji.demo.network;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

/**
 * Created by cristanz on 10/1/15.
 */
public class VolleyHelper {
    private RequestQueue mRequestQueue;
    private static VolleyHelper sHelper;

    private VolleyHelper(Context context) {
        mRequestQueue = Volley.newRequestQueue(context);
    }
    private RequestQueue getRequestQueue() {
        return mRequestQueue;
    }
    public static RequestQueue getSharedQueue(Context context) {
        if (sHelper == null) {
            sHelper = new VolleyHelper(context);
        }
        return sHelper.getRequestQueue();
    }
}
