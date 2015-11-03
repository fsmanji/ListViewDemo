package com.fsmanji.demo.fragment;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.fsmanji.demo.R;

/**
 * Created by cristanz on 10/1/15.
 */
public class ContactsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private ListView mListView;
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

        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri baseUri;

        baseUri = ContactsContract.Contacts.CONTENT_URI;

        String select = "((" + ContactsContract.Contacts.DISPLAY_NAME + " NOTNULL) AND ("
                + ContactsContract.Contacts.HAS_PHONE_NUMBER + "=1) AND ("
                + ContactsContract.Contacts.DISPLAY_NAME + " != '' ))";

        String[] projection = new String[]{
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.Contacts.CONTACT_STATUS,
                ContactsContract.Contacts.CONTACT_PRESENCE,
                ContactsContract.Contacts.PHOTO_ID,
                ContactsContract.Contacts.LOOKUP_KEY,
        };

        CursorLoader cursorLoader = new CursorLoader(
                getActivity(),
                baseUri,
                projection,
                select,
                null,
                ContactsContract.Contacts.DISPLAY_NAME);

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
        public Cursor getItem(int position) {
            mCursor.moveToPosition(position);
            return mCursor;
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
                //holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
                holder.textView = (TextView) convertView.findViewById(R.id.textView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (mCursor.moveToPosition(position)) {
                String name = mCursor.getString(mCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                String presense = mCursor.getString(mCursor.getColumnIndex(ContactsContract.Contacts.CONTACT_PRESENCE));
                holder.textView.setText(name + ", " + presense);
            } else {
                holder.textView.setText("invalid cursor index");
            }


            return convertView;
        }

        private class ViewHolder {
            //ImageView imageView;
            TextView textView;
        }
    }
}
