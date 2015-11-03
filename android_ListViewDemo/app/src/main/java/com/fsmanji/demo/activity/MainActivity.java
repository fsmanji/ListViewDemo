package com.fsmanji.demo.activity;

import android.app.Activity;
import android.os.Bundle;

import com.fsmanji.demo.fragment.ContactsFragment;
import com.fsmanji.demo.fragment.ExploreFragment2;
import com.fsmanji.demo.fragment.ProviderBasedExploreFragment;
import com.fsmanji.demo.fragment.ExploreFragment;

import java.util.LinkedList;

/**
 * Created by cristanz on 9/29/15.
 */
public class MainActivity extends Activity {

    private static final String LOG_TAG = "MainActivity";

    private static final int EXPLORE_ID = 100;
    private static final int CONTACTS_ID = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ExploreFragment exploreFragment;
        ContactsFragment contactsFragment;
        ProviderBasedExploreFragment providerBasedExploreFragment;
        if (savedInstanceState != null) {
            //retrive any existing fragment you want
            exploreFragment = (ExploreFragment)
                    getFragmentManager().findFragmentById(EXPLORE_ID);
            contactsFragment = (ContactsFragment) getFragmentManager().findFragmentById(CONTACTS_ID);
        } else {
           /* exploreFragment = new ExploreFragment();
            getFragmentManager().beginTransaction().
                    replace(android.R.id.content, exploreFragment)
                    .commit();*/

            /*contactsFragment = new ContactsFragment();
            getFragmentManager().beginTransaction().
                    replace(android.R.id.content, contactsFragment)
                    .commit();*/

            /*providerBasedExploreFragment = new ProviderBasedExploreFragment();
            getFragmentManager().beginTransaction().
                    replace(android.R.id.content, providerBasedExploreFragment)
                    .commit();*/
            ExploreFragment2 exploreFragment2 = new ExploreFragment2();
            getFragmentManager().beginTransaction().
                    replace(android.R.id.content, exploreFragment2)
                    .commit();
        }


    }

    @Override
    protected void onPause() {
        super.onPause();
        int[] A = new int[]{1,3,1,2,0,5};
        maxSlidingWindow(A, 3);
    }

    //You may assume k is always valid, ie: 1 ≤ k ≤ input array's size for non-empty array.
    private int[] maxSlidingWindow(int[] A, int k) {
        if(A==null || A.length==0) return new int[0];
        if(A.length == k) {
            int max = Integer.MIN_VALUE;
            for(int i=0;i<k;++i){
                max = Math.max(max, A[i]);
            }
            return new int[]{max};
        }
        int[] result = new int[A.length-k+1];
        //linkedlist is a deque
        LinkedList<Integer> que = new LinkedList<Integer>();

        for(int i=0; i<k; i++)
        {
            while(!que.isEmpty() && A[que.peekFirst()] < A[i])
                que.removeFirst();
            que.addLast(i);
        }

        for(int i = k; i<A.length; i++)
        {

            //if x falls out of range, remove it.
            while(!que.isEmpty() && que.peekFirst() <= i-k) {
                que.removeFirst();
            }
            result[i-k] = A[que.peekFirst()];
            //if the first ones smaller than last one, remomve them.
            while(!que.isEmpty() && A[que.peekFirst()] < A[i]) {
                que.removeFirst();
            }

            que.addLast(i);
        }

        result[A.length-k] = A[que.peekFirst()];

        return result;
    }

}
