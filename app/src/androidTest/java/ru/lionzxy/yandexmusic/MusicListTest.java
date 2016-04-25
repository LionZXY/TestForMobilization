package ru.lionzxy.yandexmusic;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.test.ActivityInstrumentationTestCase2;
import android.test.ViewAsserts;
import android.test.suitebuilder.annotation.SmallTest;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;

/**
 * Created by LionZXY on 25.04.2016.
 * YandexMusic
 */
@SmallTest
public class MusicListTest extends ActivityInstrumentationTestCase2 {

    MusicList activity;
    RecyclerView recyclerView;

    public MusicListTest() {
        super(MusicList.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        activity = (MusicList) getActivity();
        while (!activity.ready) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        recyclerView = (RecyclerView) activity.findViewById(R.id.recyclerview);
    }

    public void testCreated() {
        assertNotNull(activity);
        assertNotNull(recyclerView);
    }

    public void testVisible() {
        ViewAsserts.assertOnScreen(recyclerView,activity.findViewById(R.id.card_view));
        assertTrue(recyclerView.getAdapter().getItemCount() > 1);
    }
}
