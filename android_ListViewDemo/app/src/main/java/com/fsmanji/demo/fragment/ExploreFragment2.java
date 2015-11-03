package com.fsmanji.demo.fragment;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fsmanji.demo.R;
import com.fsmanji.demo.provider.FlickrPhotoContract;
import com.fsmanji.demo.data.FlickrPhoto;

/**
 * Created by cristanz on 11/2/15.
 */
public class ExploreFragment2 extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int FLICKE_LOADER = 0x100;
    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_recyclerview, container, false);
        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new MyAdapter();
        mRecyclerView.setAdapter(mAdapter);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //start the loader
        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(FLICKE_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri baseUri;

        baseUri = FlickrPhotoContract.CONTENT_URI;

        String select = null;

        String[] projection = new String[]{
                FlickrPhotoContract._ID,
                FlickrPhotoContract.TITLE,
                FlickrPhotoContract.URL_M
        };
        CursorLoader cursorLoader = new CursorLoader(
                getActivity(),
                baseUri,
                projection,
                select,
                null,
                null);

        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    /**
     * Override the default adapter to customize data and ui
     */
    class MyAdapter extends RecyclerView.Adapter<ViewHolder> {

        private Cursor mCursor;

        public void swapCursor(Cursor cursor) {
            if (mCursor != null && !mCursor.isClosed())
                mCursor.close();

            mCursor = cursor;

            notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            return mCursor == null ? 0 : mCursor.getCount();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());

            View v = inflater.inflate(R.layout.list_item_cardview, parent, false);

            ViewHolder vh = new ViewHolder(v);
            vh.imageView = (ImageView) v.findViewById(R.id.imageView);
            vh.textView = (TextView) v.findViewById(R.id.textView);

            return vh;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            FlickrPhoto photo = getPhoto(position);

            if (photo != null) {
                holder.textView.setText(photo.title);
                Glide.with(getActivity())
                        .load(photo.url_m)
                        .centerCrop()
                                //.placeholder(R.drawable.loading_spinner)
                        .crossFade()
                        .into(holder.imageView);
            }
        }

        private FlickrPhoto getPhoto(int position) {

            mCursor.moveToPosition(position);
            String name = mCursor.getString(mCursor.getColumnIndex(FlickrPhotoContract.TITLE));
            String urlm = mCursor.getString(mCursor.getColumnIndex(FlickrPhotoContract.URL_M));
            return new FlickrPhoto(name, urlm);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView textView;
        ImageView imageView;

        public ViewHolder(View rootView) {
            super(rootView);
        }
    }
}
