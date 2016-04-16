package ru.lionzxy.yandexmusic.helper;

/**
 * Created by nikit_000 on 12.04.2016.
 */
public class TextHelper {

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
