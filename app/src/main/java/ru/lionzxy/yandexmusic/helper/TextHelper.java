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

    public static class LeadingMarginSpan2 implements LeadingMarginSpan.LeadingMarginSpan2 {
        private int margin;
        private int lines;

        public LeadingMarginSpan2(int lines, int margin) {
            this.margin = margin;
            this.lines = lines;
        }

        /* Возвращает значение, на которе должен быть добавлен отступ */
        @Override
        public int getLeadingMargin(boolean first) {
            return first ? margin : 0;
        }

        @Override
        public void drawLeadingMargin(Canvas c, Paint p, int x, int dir,
                                      int top, int baseline, int bottom, CharSequence text,
                                      int start, int end, boolean first, Layout layout) {}

        @Override
        public int getLeadingMarginLineCount() {
            return lines;
        }
    }
}
