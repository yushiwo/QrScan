package com.example.zr.qrscan;

import android.app.Application;

import com.netease.scan.QrScan;
import com.netease.scan.QrScanConfiguration;

/**
 * @author hzzhengrui
 * @Date 16/10/27
 * @Description
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

//        // 默认配置
//        QrScanConfiguration configuration = QrScanConfiguration.createDefault(this);

        // 自定义配置
        QrScanConfiguration configuration = new QrScanConfiguration.Builder(this)
                .setTitleHeight(53)
                .setTitleText("来扫一扫")
                .setTitleTextSize(18)
                .setTitleTextColor(R.color.white)
                .setTipText("将二维码放入框内扫描~")
                .setTipTextSize(14)
                .setTipMarginTop(40)
                .setTipTextColor(R.color.white)
                .setSlideIcon(R.mipmap.capture_add_scanning)
                .setAngleColor(R.color.white)
                .setMaskColor(R.color.black_80)
                .setScanFrameRectRate((float) 0.8)
                .build();
        QrScan.getInstance().init(configuration);
    }
}
