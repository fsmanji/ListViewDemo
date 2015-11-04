package com.fsmanji.demo.data;

import com.fsmanji.demo.test.UnitTestBase;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * Created by cristanz on 11/3/15.
 */

public class FlickrPhotoTest extends UnitTestBase {

    private FlickrPhoto mPhoto;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        mPhoto = new FlickrPhoto("title", "http://google.com");
    }

    @Test
    public void test_parseJson() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title", "title");
        jsonObject.put("url_m", "http://google.com");

        FlickrPhoto photo = new FlickrPhoto(jsonObject);

        Assert.assertEquals(photo.title, mPhoto.title);
        Assert.assertEquals(photo.url_m, mPhoto.url_m);

        assertThat("they should be equal", photo.title, equalTo(mPhoto.title));
    }
}
