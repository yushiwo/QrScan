package com.netease.scan.zxing.util;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * Created by xinyuanhxy on 15/11/18.
 * <p/>
 * 设备相关的方法，获取屏幕分辨率，获取屏幕密度，dip转px, px转dip, 唯一的设备UUID
 *
 * 获取设备status bar 的高度
 */
public class DeviceUtil {

    /**
     * 获取屏幕分辨率
     *
     * @param context
     * @return 数组（宽高分辨率）
     */
    public static int[] getScreenResolution(Context context) {
        int scrennResolution[] = new int[2];
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        android.view.Display display = wm.getDefaultDisplay();
        display.getMetrics(dm);
        scrennResolution[0] = dm.widthPixels;
        scrennResolution[1] = dm.heightPixels;
        return scrennResolution;
    }


    /**
     * 获取屏幕分辨率高度
     *
     * @param context
     * @return  高度 px
     */
    public static int getScreenHeightPx(Context context) {

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        android.view.Display display = wm.getDefaultDisplay();
        display.getMetrics(dm);

        return dm.heightPixels;
    }


    /**
     * 获取屏幕分辨率宽度
     *
     * @param context
     * @return  宽度 px
     */
    public static int getScreenWidthPx(Context context) {

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        android.view.Display display = wm.getDefaultDisplay();
        display.getMetrics(dm);

        return dm.widthPixels;
    }

    /**
     * 获取屏幕 dp高度
     *
     * @param context
     * @return  高度 px
     */
    public static int getScreenHeightDp(Context context) {

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        android.view.Display display = wm.getDefaultDisplay();
        display.getMetrics(dm);

        return (int)(dm.heightPixels/dm.density);
    }


    /**
     * 获取屏幕 dp宽度
     *
     * @param context
     * @return  宽度 px
     */
    public static int getScreenWidthDp(Context context) {

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        android.view.Display display = wm.getDefaultDisplay();
        display.getMetrics(dm);

        return (int)(dm.widthPixels/dm.density);
    }


    /**
     * dip转px
     *
     * @param dipValue
     * @return int  转换完成的px
     */
    public static int dip2px(float dipValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * px转dip
     *
     * @param pxValue
     * @return int  转换完成的dip
     */
    public static int px2dip(float pxValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

}
