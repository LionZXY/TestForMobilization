package ru.lionzxy.yandexmusic.helper;

import java.util.List;

/**
 * Created by LionZXY on 24.04.2016.
 * YandexMusic
 */
public class ColorHelper {

    static final byte ALPHA_CHANNEL = 24;
    static final byte RED_CHANNEL = 16;
    static final byte GREEN_CHANNEL = 8;
    static final byte BLUE_CHANNEL = 0;

    public static int mixColors(int... color) {

        final float amount = 1.0F / color.length;

        float alpha = 0;
        float red = 0;
        float green = 0;
        float blue = 0;
        for (int oneColor : color) {
            alpha += (float) (oneColor >> ALPHA_CHANNEL & 0xff) * amount;
            red += (float) (oneColor >> RED_CHANNEL & 0xff) * amount;
            green += (float) (oneColor >> GREEN_CHANNEL & 0xff) * amount;
            blue += (float) (oneColor & 0xff) * amount;
        }
        int a = ((int) alpha) & 0xff;
        int r = ((int) red) & 0xff;
        int g = ((int) green) & 0xff;
        int b = ((int) blue) & 0xff;

        return a << ALPHA_CHANNEL | r << RED_CHANNEL | g << GREEN_CHANNEL | b;
    }

    public static int mixColors(List<Integer> colors) {

        final float amount = 1.0F / colors.size();

        float alpha = 0;
        float red = 0;
        float green = 0;
        float blue = 0;
        for (int oneColor : colors) {
            alpha += (float) (oneColor >> ALPHA_CHANNEL & 0xff) * amount;
            red += (float) (oneColor >> RED_CHANNEL & 0xff) * amount;
            green += (float) (oneColor >> GREEN_CHANNEL & 0xff) * amount;
            blue += (float) (oneColor & 0xff) * amount;
        }
        int a = ((int) alpha) & 0xff;
        int r = ((int) red) & 0xff;
        int g = ((int) green) & 0xff;
        int b = ((int) blue) & 0xff;

        return a << ALPHA_CHANNEL | r << RED_CHANNEL | g << GREEN_CHANNEL | b;
    }
}
