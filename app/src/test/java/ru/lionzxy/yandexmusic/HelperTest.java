package ru.lionzxy.yandexmusic;

import android.test.mock.MockResources;
import android.test.suitebuilder.annotation.SmallTest;
import android.util.TypedValue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import ru.lionzxy.yandexmusic.helper.TextHelper;

import static junit.framework.Assert.assertEquals;

/**
 * Created by LionZXY on 19.04.2016.
 * YandexMusic
 */
@RunWith(JUnit4.class)
@SmallTest
public class HelperTest {

    @Test
    public void testTextHelperSymbolsIsUpper() {
        assertEquals("Hello", TextHelper.upperFirstSymbols("hello"));
        assertEquals("42044344144143a4384352041144343a43244b202a6104344444b43220pisdf520202b2d2f2d541022a2f2d2b", TextHelper.getFileName("Русские Буквы *61043фыв pisdf520 +-/-54102*/-+"));
    }
}