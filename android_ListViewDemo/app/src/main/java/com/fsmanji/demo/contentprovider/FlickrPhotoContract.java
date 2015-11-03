package com.fsmanji.demo.contentprovider;

import android.net.Uri;

/**
 * Created by cristanz on 10/19/15.
 */
public class FlickrPhotoContract {
    public static final String AUTHORITY = "com.fsmanji.demo.contentprovider.FlickrPhotoProvider";
    public static final int PHOTO_LIST = 0x1;
    public static final int PHOTO_ITEM = 0x10;

    public static final String EXPLORE_BASE_PATH = "interestingness";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + EXPLORE_BASE_PATH);

    public static final String _ID = "_id";
    public static final String TITLE = "title";
    public static final String URL_M = "url_m";
}
