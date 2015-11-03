package com.fsmanji.demo.contentprovider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fsmanji.demo.data.FlickrPhoto;
import com.fsmanji.demo.network.VolleyHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * The content provider will be launched when the app process is created,
 * even before the Application object is initialized. It fetches a list of photos
 * from flickr server, then notifies any parties that's interested in the uri.
 */
public class FlickrPhotoProvider extends ContentProvider {
    private static final String LOG_TAG = "FlickrPhotoProvider";
    private static String url = "https://api.flickr.com/services/rest/?method=flickr.interestingness.getList&api_key=e94b89904a09519e72a4425f2d05722c&format=json&nojsoncallback=1";

    static {
        url += "&extras=url_m";
    }

    private static final UriMatcher sURIMatcher = new UriMatcher(
            UriMatcher.NO_MATCH);

    static {
        sURIMatcher.addURI(FlickrPhotoContract.AUTHORITY, FlickrPhotoContract.EXPLORE_BASE_PATH, FlickrPhotoContract.PHOTO_LIST);
        sURIMatcher.addURI(FlickrPhotoContract.AUTHORITY, FlickrPhotoContract.EXPLORE_BASE_PATH + "/#", FlickrPhotoContract.PHOTO_ITEM);
    }

    ArrayList<FlickrPhoto> mEntries = new ArrayList<>();

    @Override
    public boolean onCreate() {
        Log.d(LOG_TAG, "onCreate");
        loadFlickrExplorePhotos();
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        int uriType = sURIMatcher.match(uri);
        switch (uriType) {
            case FlickrPhotoContract.PHOTO_LIST:
                return getAllPhotos(uri);

            case FlickrPhotoContract.PHOTO_ITEM:
                break;
        }
        // Log.d(LOG_TAG, "query:" + uri.toString() + ", cursor count=" + mCursor.getCount());
        /**
         * MUST create a new cursor every time on query, otherwise it would
         * throw exception on notifyChange() later.
         **/
        return null;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        //insert records here
        getContext().getContentResolver().notifyChange(uri, null);
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        //delete records
        getContext().getContentResolver().notifyChange(uri, null);
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        //update records here
        getContext().getContentResolver().notifyChange(uri, null);
        return 0;
    }

    private MatrixCursor getAllPhotos(Uri uri) {
        MatrixCursor cursor = new MatrixCursor(new String[]{
                FlickrPhotoContract._ID,
                FlickrPhotoContract.TITLE,
                FlickrPhotoContract.URL_M});

        for (int i = 0; i < mEntries.size(); ++i) {
            FlickrPhoto photo = mEntries.get(i);
            cursor.newRow().add(i).add(photo.title).add(photo.url_m);
        }
        //set uri to be notified on later changes/updates.
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    /**
     * Load flickr interesting photos using the following api:
     * photos.interestingness.getList
     */
    private void loadFlickrExplorePhotos() {
        // Request a string response

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            response = response.getJSONObject("photos");
                            JSONArray array = response.getJSONArray("photo");
                            for (int i = 0; i < array.length(); ++i) {
                                FlickrPhoto photo = new FlickrPhoto(array.getJSONObject(i));
                                mEntries.add(photo);
                            }
                            /** notify observers on uri change **/
                            update(FlickrPhotoContract.CONTENT_URI, null, null, null);

                            Log.d(LOG_TAG, "array size =" + mEntries.size());

                        } catch (JSONException e) {
                            Toast.makeText(getContext(), "flickr link expired.", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(LOG_TAG, "onError:" + error.toString());
                Toast.makeText(getContext(), "volley error response.", Toast.LENGTH_SHORT).show();
            }
        });

        VolleyHelper.getSharedQueue(getContext()).add(jsonRequest);
    }
}

