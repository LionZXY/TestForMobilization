package ru.lionzxy.yandexmusic.helper;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.Layout;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.LeadingMarginSpan;
import android.util.FloatMath;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by nikit_000 on 12.04.2016.
 */
public class TextHelper {
    //TODO Top symbols

    public static String getFileName(String filename) {
        char fileSep = '/';
        char escape = '%';
        int len = filename.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char ch = filename.charAt(i);
            if (ch < ' ' || ch >= 0x7F || ch == fileSep
                    || (ch == '.' && i == 0)
                    || ch == escape) {
                sb.append(escape);
                if (ch < 0x10) {
                    sb.append('0');
                }
                sb.append(Integer.toHexString(ch));
            } else {
                sb.append(ch);
            }
        }
        return sb.toString();
    }

    public static String upperFirstSymbols(String name){
        return Character.isUpperCase(name.charAt(0)) ? name : name.replaceFirst(String.valueOf(name.charAt(0)), String.valueOf(Character.toUpperCase(name.charAt(0))));
    }
}
