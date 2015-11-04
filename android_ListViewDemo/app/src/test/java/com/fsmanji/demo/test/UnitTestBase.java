package com.fsmanji.demo.test;

import android.app.Application;

import com.fsmanji.demo.BuildConfig;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

/**
 * Created by cristanz on 11/3/15.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public abstract class UnitTestBase {
    protected Application mApplication;
    @Before
    public void setUp() throws Exception {
        mApplication = RuntimeEnvironment.application;
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDown() throws Exception {
    }
}
