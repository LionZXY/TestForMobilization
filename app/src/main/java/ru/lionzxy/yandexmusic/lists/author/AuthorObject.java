package ru.lionzxy.yandexmusic.lists.author;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import ru.lionzxy.yandexmusic.LoadingActivity;
import ru.lionzxy.yandexmusic.R;
import ru.lionzxy.yandexmusic.exceptions.ErrorJsonFileException;
import ru.lionzxy.yandexmusic.helper.TextHelper;
import ru.lionzxy.yandexmusic.io.IRecieveImage;
import ru.lionzxy.yandexmusic.io.ImageResource;

/**
 * Created by LionZXY on 09.04.16.
 * YandexMusic
 */

public class AuthorObject implements Serializable {
    public static final AuthorObject UNKNOWN = new AuthorObject();
    private ImageResource bigImage, smallImage;
    int[] genresArr = null;
    public String name, description, link;
    public int authorId = 1, tracks = -1, albums = -1;

    private AuthorObject() {
        this.name = "Неизвестный автор";
        this.description = "Если вы видете это сообщение, значит произошла ошибка в программе. Пожалуйста сообщите об этом разработчику";
        this.link = null;
        this.genresArr = new int[]{0};
        this.bigImage = null;
        this.smallImage = null;

    }

    public AuthorObject(String jsonString) throws JSONException {
        this(new JSONObject(jsonString));
    }

    public AuthorObject(JSONObject jsonObject) throws ErrorJsonFileException, JSONException {

        if (jsonObject == null || !jsonObject.has("id"))
            throw new ErrorJsonFileException();
        authorId = jsonObject.getInt("id");
        name = jsonObject.has("name") ? TextHelper.upperFirstSymbols(jsonObject.getString("name")) : UNKNOWN.name;

        if (jsonObject.has("genres")) {
            int brokenVal = 0;
            JSONArray arr = jsonObject.getJSONArray("genres");
            genresArr = new int[arr.length()];
            for (int i = 0; i < arr.length(); i++) {
                Integer genresObject = LoadingActivity.genresHashMap.get(arr.get(i));
                if (genresObject != null)
                    genresArr[i] = genresObject;
                else {
                    genresArr[i] = -1;
                    brokenVal++;
                }
            }
            if (brokenVal != 0) {
                int tmp[] = new int[arr.length() - brokenVal];
                int pos = 0;
                for (int i = 0; i < genresArr.length; i++) {
                    if (genresArr[i] != -1) {
                        tmp[pos] = genresArr[i];
                        pos++;
                    }
                }
            }
        }

        tracks = jsonObject.has("tracks") ? jsonObject.getInt("tracks") : UNKNOWN.tracks;
        albums = jsonObject.has("albums") ? jsonObject.getInt("albums") : UNKNOWN.albums;
        link = jsonObject.has("link") ? jsonObject.getString("link") : UNKNOWN.link;
        description = jsonObject.has("description") ? TextHelper.upperFirstSymbols(jsonObject.getString("description")) : UNKNOWN.description;

        if (jsonObject.has("cover")) {
            JSONObject covers = jsonObject.getJSONObject("cover");
            smallImage = covers.has("small") ? new ImageResource(false, "author_" + authorId, covers.getString("small")) : UNKNOWN.smallImage;
            bigImage = covers.has("big") ? new ImageResource(true, "author_" + authorId, covers.getString("big")) : UNKNOWN.bigImage;
        }
    }


    public void setImageOnItemView(final Activity activity, final ImageView iv, Resources r, boolean isBigPicture) {
        final ImageResource ir = isBigPicture ? bigImage : smallImage;
        if (ir != null)
            ir.getImage(new IRecieveImage() {
                @Override
                public void recieveResource(final @Nullable Bitmap bitmap) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            final Animation vis = AnimationUtils.loadAnimation(activity, R.anim.alphavisible);
                            Animation unvis = AnimationUtils.loadAnimation(activity, R.anim.alphaunvisible);
                            if (bitmap != null) {
                                new Handler().postDelayed(new Runnable() {
                                    public void run() {
                                        iv.setImageBitmap(bitmap);
                                        iv.setAnimation(vis);
                                    }
                                }, unvis.getDuration());
                                iv.setAlpha(1.0F);
                                iv.startAnimation(unvis);
                            } else {
                                iv.startAnimation(vis);
                                iv.setAlpha(1.0F);
                            }
                        }
                    });
                }
            });
    }

}
