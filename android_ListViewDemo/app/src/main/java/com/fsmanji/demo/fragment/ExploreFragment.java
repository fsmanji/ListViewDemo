package com.fsmanji.demo.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import com.bumptech.glide.Glide;
import com.fsmanji.demo.data.FlickrPhoto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.fsmanji.demo.R;
import com.fsmanji.demo.network.VolleyHelper;

/**
 * Created by cristanz on 10/1/15.
 */
public class ExploreFragment extends Fragment {

    private static String url = "https://api.flickr.com/services/rest/?method=flickr.interestingness.getList&api_key=2310b1efa6d1052d27e2e19dc60d6c66&format=json&nojsoncallback=1";

    static {
        url += "&extras=url_m";
    }

    private static final String LOG_TAG = "ExploreFragment";
    private ListView mListView;
    private List<FlickrPhoto> mPhotos = new ArrayList<>();
    private LocalAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_listview, container, false);
        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mListView = (ListView) view.findViewById(R.id.listview);
        mAdapter = new LocalAdapter();
        mListView.setAdapter(mAdapter);

        loadFlickrExplorePhotos();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
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
                        mPhotos.add(photo);
                    }

                    Log.d(LOG_TAG, "array size =" + mPhotos.size());
                    mAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    Toast.makeText(getActivity(), "flickr link expired.", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(LOG_TAG, "onError:" + error.toString());
                Toast.makeText(getActivity(), "volley error response.", Toast.LENGTH_SHORT).show();
            }
        });

        VolleyHelper.getSharedQueue(getActivity()).add(jsonRequest);
    }

    private class LocalAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mPhotos != null ? mPhotos.size() : 0;
        }

        @Override
        public FlickrPhoto getItem(int position) {

            return mPhotos.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getActivity());
                convertView = inflater.inflate(R.layout.list_item, parent, false);
                holder = new ViewHolder();
                holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
                holder.textView = (TextView) convertView.findViewById(R.id.textView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            FlickrPhoto photo = getItem(position);

            if (photo != null) {
                holder.textView.setText(photo.title);
                Glide.with(getActivity())
                        .load(photo.url_m)
                        .centerCrop()
                                //.placeholder(R.drawable.loading_spinner)
                        .crossFade()
                        .into(holder.imageView);
            }

            return convertView;
        }

        private class ViewHolder {
            ImageView imageView;
            TextView textView;
        }
    }
}
