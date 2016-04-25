package ru.lionzxy.yandexmusic;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.test.ActivityInstrumentationTestCase2;
import android.test.ViewAsserts;

/**
 * Created by LionZXY on 25.04.2016.
 * YandexMusic
 */
public class AboutAuthorTest extends ActivityInstrumentationTestCase2 {

    AboutAuthor activity;
    CardView cardView;

    public AboutAuthorTest() {
        super(AboutAuthor.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        activity = (AboutAuthor) getActivity();
        cardView = (CardView) activity.findViewById(R.id.card_view);
    }

    public void testCreated() {
        assertNotNull(activity);
        assertNotNull(cardView);
    }

    public void testVisible() {
        ViewAsserts.assertOnScreen(activity.findViewById(R.id.scrollViewList), cardView);
        ViewAsserts.assertOnScreen(cardView, activity.findViewById(R.id.description));
        ViewAsserts.assertOnScreen(cardView, activity.findViewById(R.id.imageView));
        ViewAsserts.assertOnScreen(cardView, activity.findViewById(R.id.head_author));

    }
}
