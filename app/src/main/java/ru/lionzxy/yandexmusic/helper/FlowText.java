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
public class FlowText {

    public static void tryFlowText(String text, View thumbnailView, TextView messageView, Display display) {

        thumbnailView.measure(display.getWidth(), display.getHeight());
        int height = thumbnailView.getMeasuredHeight();
        int width = thumbnailView.getMeasuredWidth();
        float textLineHeight = messageView.getPaint().getTextSize();

        int lines = (int) Math.ceil(height / textLineHeight);
        Log.v("FF",lines + "");
        SpannableString ss = new SpannableString(text);
        ss.setSpan(new LeadingMarginSpan2(lines, width), 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        Log.v("FF",ss.toString());
        messageView.setText(ss);

        // Align the text with the image by removing the rule that the text is to the right of the image
        //ViewGroup.LayoutParams params = messageView.getLayoutParams();
        //int[] rules = params.getRules();
        //rules[RelativeLayout.RIGHT_OF] = 0;
    }

    public static class LeadingMarginSpan2 implements LeadingMarginSpan.LeadingMarginSpan2 {
        private int margin;
        private int lines;
        private boolean wasDrawCalled = false;
        private int drawLineCount = 0;

        public LeadingMarginSpan2(int lines, int margin) {
            this.margin = margin;
            this.lines = lines;
        }

        @Override
        public int getLeadingMargin(boolean first) {
            boolean isFirstMargin = first;

            this.drawLineCount = this.wasDrawCalled ? this.drawLineCount + 1 : 0;
            this.wasDrawCalled = false;
            isFirstMargin = this.drawLineCount <= this.lines;


            return isFirstMargin ? this.margin : 0;
        }

        @Override
        public void drawLeadingMargin(Canvas c, Paint p, int x, int dir, int top, int baseline, int bottom, CharSequence text, int start, int end, boolean first, Layout layout) {
            this.wasDrawCalled = true;
        }

        @Override
        public int getLeadingMarginLineCount() {
            return this.lines;
        }
    }
}
