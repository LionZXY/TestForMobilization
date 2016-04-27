package ru.lionzxy.yandexmusic;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;

import ru.lionzxy.yandexmusic.helper.PixelHelper;

/**
 * Created by LionZXY on 19.04.2016.
 * YandexMusic
 */
public class AndroidHelperTest extends ActivityInstrumentationTestCase2 {

    Activity activity;

    public AndroidHelperTest() {
        super(MusicList.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        activity = getActivity();
    }

    public void testPixelHelper() {
        float answ1 = PixelHelper.pixelFromDP(activity.getResources(), 40);
        float answ2 = PixelHelper.pixelFromDP(null, 40);
        float answ3 = PixelHelper.pixelFromDP(activity.getResources(), -100);
        assertEquals(answ1, activity.getResources().getDisplayMetrics().density * 40);
        assertEquals(answ2, 0F);
        assertEquals(answ3, -200F);
    }

}
