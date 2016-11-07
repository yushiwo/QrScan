package com.netease.scan.util;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;


/**
 * Created by zhengrui on 15/11/30.
 */
public class ToastUtil {
    private static Toast sToast;

    public static void showToast(Context context, String msg) {
        showToast(context, msg, Toast.LENGTH_LONG, Gravity.CENTER,0, 0);
    }

    public static void showShortToast(Context context, String msg) {
        showToast(context, msg, Toast.LENGTH_SHORT, Gravity.CENTER,0, 0);
    }

    public static void showToast(Context context, String msg , int duration ) {
        showToast(context, msg, duration , Gravity.CENTER,0, 0);
    }

    public static void showToast(Context context, int msgId) {
        showToast(context, context.getString(msgId), Toast.LENGTH_LONG, Gravity.CENTER,0, 0);
    }

    public static void showShortToast(Context context, int msgId) {
        showToast(context, context.getString(msgId), Toast.LENGTH_SHORT, Gravity.CENTER,0, 0);
    }

    public static void showToast(Context context, int msgId , int duration )
    {
        showToast(context, context.getString(msgId), duration , Gravity.CENTER,0, 0);
    }


    public static void showToast(Context context, int msgId, int duration, int gravity, int xOffset, int yOffset) {
        showToast(context, context.getString(msgId), duration, gravity, xOffset, yOffset);
    }

    public static void showToast(Context context, String message, int duration, int gravity, int xOffset, int yOffset) {
        if (message == null){
            return;
        }

        if (sToast == null) {
            sToast = Toast.makeText(context, "", duration);
        } else {
            sToast.cancel();
            sToast = Toast.makeText(context, "", duration);
        }
        sToast.setText(message);
        sToast.setDuration(duration);
        sToast.setGravity(gravity, xOffset, yOffset);
        sToast.show();
    }
}
