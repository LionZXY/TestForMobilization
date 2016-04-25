package ru.lionzxy.yandexmusic;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.test.ViewAsserts;
import android.test.suitebuilder.annotation.SmallTest;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;

/**
 * Created by LionZXY on 19.04.2016.
 * YandexMusic
 */
@SmallTest
public class LoadingSimplyTest extends ActivityInstrumentationTestCase2 {

    Activity activity;
    LinearLayout center;
    ImageView imageView;
    RoundCornerProgressBar progressBar;
    TextView textView;

    public LoadingSimplyTest() {
        super(LoadingActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        activity = getActivity();
        center = (LinearLayout) activity.findViewById(R.id.centerView);
        imageView = (ImageView) center.findViewById(R.id.imageView);
        progressBar = (RoundCornerProgressBar) center.findViewById(R.id.progressBar);
        textView = (TextView) center.findViewById(R.id.progress);

    }

    public void testCreated() {
        assertNotNull(activity);
        assertNotNull(center);
        assertNotNull(imageView);
        assertNotNull(progressBar);
        assertNotNull(textView);
    }

    public void testVisible() {
        ViewAsserts.assertOnScreen(center, imageView);
        ViewAsserts.assertOnScreen(imageView.getRootView(), progressBar);
        ViewAsserts.assertOnScreen(progressBar.getRootView(), textView);
    }

}
