package com.fsmanji.demo.fragment;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fsmanji.demo.R;
import com.fsmanji.demo.contentprovider.FlickrPhotoContract;
import com.fsmanji.demo.data.FlickrPhoto;

/**
 * This is a demo for listview + adapter that uses data from a local content provider.
 */
public class ProviderBasedExploreFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private ListView mListView;
    private LocalAdapter mAdapter;
    private static final int FLICKE_LOADER = 0x100;

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

    private class LocalAdapter extends BaseAdapter {

        private Cursor mCursor;

        public void swapCursor(Cursor cursor) {
            if (mCursor != null && !mCursor.isClosed())
                mCursor.close();

            mCursor = cursor;

            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mCursor == null ? 0 : mCursor.getCount();
        }

        @Override
        public FlickrPhoto getItem(int position) {

            mCursor.moveToPosition(position);
            String name = mCursor.getString(mCursor.getColumnIndex(FlickrPhotoContract.TITLE));
            String urlm = mCursor.getString(mCursor.getColumnIndex(FlickrPhotoContract.URL_M));
            return new FlickrPhoto(name, urlm);
        }

        @Override
        public long getItemId(int position) {
            return 0;
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
                String name = mCursor.getString(mCursor.getColumnIndex(FlickrPhotoContract.TITLE));
                holder.textView.setText(name);
                Glide.with(getActivity())
                        .load(photo.url_m)
                        .centerCrop()
                                //.placeholder(R.drawable.loading_spinner)
                        .crossFade()
                        .into(holder.imageView);

            } else {
                holder.textView.setText("invalid cursor index");
            }


            return convertView;
        }

        private class ViewHolder {
            ImageView imageView;
            TextView textView;
        }
    }
}
