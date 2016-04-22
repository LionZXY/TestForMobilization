package ru.lionzxy.yandexmusic.exceptions;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Date;

import ru.lionzxy.yandexmusic.LoadingActivity;
import ru.lionzxy.yandexmusic.R;
import ru.lionzxy.yandexmusic.helper.TextHelper;
import ru.lionzxy.yandexmusic.io.ImageResource;

/**
 * Created by LionZXY on 19.04.2016.
 * YandexMusic
 */
public class ContextDialogException extends RuntimeException implements DialogInterface.OnClickListener {

    public ContextDialogException(Context context, Exception e) {
        this(context, e, R.string.error_default);
    }

    public ContextDialogException(Context context, Exception e, int errorText) {
        Log.e(LoadingActivity.SHAREDTAG, "ContextDialogExceptions", e);

        String mainText = context.getResources().getString(errorText) + context.getResources().getString(R.string.error_add_default);
        new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.dialogStyle)).setMessage(mainText).setNegativeButton(R.string.error_add_choise3, this)
                .setNegativeButton(R.string.error_add_choise2, this).setPositiveButton(R.string.error_add_choise1, this).create().show();

        try {
            File crashFile = new File(ImageResource.DATA_PATH + "/crash/", TextHelper.getFileName(new Date().toString()) + "-crash.log");

            if (!crashFile.getParentFile().exists())
                crashFile.getParentFile().mkdirs();
            crashFile.createNewFile();
            PrintWriter pw = new PrintWriter(new FileWriter(crashFile));
            pw.print("---- Crash Report ----\n\n\n");
            pw.print("Time: \n" + new Date() +
                    "\nDescription: " + e.getLocalizedMessage() + "\n\n");
            e.printStackTrace(pw);
            e.printStackTrace();
            pw.flush();
            pw.close();
        } catch (Exception ex) {
            Log.e(LoadingActivity.SHAREDTAG, "O_o", ex);
        }

    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        // :)
    }
}
