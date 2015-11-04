package com.fsmanji.demo.activity;

import android.app.Fragment;
import android.content.Intent;

import com.fsmanji.demo.fragment.ExploreFragment;
import com.fsmanji.demo.fragment.ExploreFragment2;
import com.fsmanji.demo.test.UnitTestBase;

import junit.framework.Assert;

import org.junit.Test;
import org.robolectric.Robolectric;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by cristanz on 11/3/15.
 */
public class MainActivityTest extends UnitTestBase {
    private MainActivity activity;
    @Override
    public void setUp() throws Exception {
        super.setUp();
        // Convenience method to run MainActivity through the Activity Lifecycle methods:
        // onCreate(...) => onStart() => onPostCreate(...) => onResume()
        activity = Robolectric.setupActivity(MainActivity.class);
    }

    @Test
    public void test_onCreate() {
        activity.onCreate(null);

        Fragment f = activity.getFragmentManager().findFragmentById(android.R.id.content);
        Assert.assertTrue(f.getClass() == activity.getActiveFragment());
    }

    @Test
    public void test_intentStartActivity(){
        Intent i = new Intent(mApplication, MainActivity.class);
        i.putExtra(MainActivity.EXTRA_MODE, MainActivity.EMode.Explore);

        MainActivity mainActivity = Robolectric.buildActivity(MainActivity.class)
                .withIntent(i).create().get();

        Fragment f = mainActivity.getFragmentManager().findFragmentById(android.R.id.content);

        Assert.assertTrue(mainActivity.getLaunchMode() == MainActivity.EMode.Explore);
        Assert.assertTrue(ExploreFragment.class == f.getClass());
    }
}
