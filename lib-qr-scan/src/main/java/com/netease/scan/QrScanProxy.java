package com.netease.scan;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import com.netease.scan.interfaces.IProxy;

/**
 * @author hzzhengrui
 * @Date 16/10/27
 * @Description
 */
public class QrScanProxy implements IProxy{

    private static final String ERROR_NOT_INIT = "QrScan must be init with configuration before using";

    private static QrScanProxy sInstance;

    QrScanConfiguration configuration;
    IScanModuleCallBack callBack;

    public static QrScanProxy getInstance(){
        if (sInstance == null) {
            synchronized (QrScanProxy.class) {
                if (sInstance == null) {
                    sInstance = new QrScanProxy();
                }
            }
        }
        return sInstance;
    }

    /**
     * 设置图片选择器的配置
     * @param configuration
     */
    public void setConfiguration(QrScanConfiguration configuration){
        this.configuration = configuration;
    }

    public void setCallBack(IScanModuleCallBack callBack){
        this.callBack = callBack;
    }

    public IScanModuleCallBack getCallBack(){
        return callBack;
    }

    private void checkConfiguration() {
        if (configuration == null) {
            throw new IllegalStateException(ERROR_NOT_INIT);
        }
    }

    @Override
    public int getMaskColor(Resources res) {
        checkConfiguration();
        return res.getColor(configuration.maskColor);
    }

    @Override
    public int getAngleColor(Resources res) {
        checkConfiguration();
        return res.getColor(configuration.angleColor);
    }

    @Override
    public int getTitleHeight() {
        checkConfiguration();
        return configuration.titleHeight;
    }

    @Override
    public String getTitleText(Resources res) {
        checkConfiguration();
        return configuration.titleTextRes != -1 ? res.getString(configuration.titleTextRes) : configuration.titleText;
    }

    @Override
    public int getTitleTextSize() {
        checkConfiguration();
        return configuration.titleTextSize;
    }

    @Override
    public int getTitleTextColor(Resources res) {
        checkConfiguration();
        return res.getColor(configuration.titleTextColor);
    }

    @Override
    public String getTipText(Resources res) {
        checkConfiguration();
        return configuration.tipTextRes != -1 ? res.getString(configuration.tipTextRes) : configuration.tipText;
    }

    @Override
    public int getTipTextColor(Resources res) {
        checkConfiguration();
        return res.getColor(configuration.tipTextColor);
    }

    @Override
    public int getTipTextSize() {
        checkConfiguration();
        return configuration.tipTextSize;
    }

    @Override
    public int getTipMarginTop() {
        checkConfiguration();
        return configuration.tipMarginTop;
    }

    @Override
    public Drawable getSlideIcon(Resources res) {
        checkConfiguration();
        return configuration.slideIconRes != -1 ? res.getDrawable(configuration.slideIconRes) : configuration.slideIcon;
    }

    @Override
    public float getScanFrameRectRate() {
        checkConfiguration();
        return configuration.scanFrameRectRate;
    }
}
