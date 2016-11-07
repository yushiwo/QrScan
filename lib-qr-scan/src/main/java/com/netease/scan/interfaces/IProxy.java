package com.netease.scan.interfaces;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;

/**
 * @author hzzhengrui
 * @Date 16/10/27
 * @Description
 */
public interface IProxy {

    int getMaskColor(Resources res);
    int getAngleColor(Resources res);
    int getTitleHeight();
    String getTitleText(Resources res);
    int getTitleTextSize();
    int getTitleTextColor(Resources res);
    String getTipText(Resources res);
    int getTipTextColor(Resources res);
    int getTipTextSize();
    int getTipMarginTop();
    Drawable getSlideIcon(Resources res);
    float getScanFrameRectRate();
}
