package com.netease.scan;

import android.content.Context;
import android.util.Log;

import com.netease.scan.ui.CaptureActivity;

/**
 * @author hzzhengrui
 * @Date 16/10/27
 * @Description
 */
public class QrScan {

    private static final String TAG = QrScan.class.getSimpleName();

    private static final String WARNING_RE_INIT_CONFIG = "Try to initialize QrScan which had already been initialized before. " + "To re-init QrScan with new configuration call QrScan.destroy() at first.";
    private static final String ERROR_INIT_CONFIG_WITH_NULL = "QrScan configuration can not be initialized with null";
    private static final String ERROR_NOT_INIT = "QrScan must be init with configuration before using";

    private static QrScan sInstance;
    private QrScanConfiguration configuration;

    public static QrScan getInstance(){
        if (sInstance == null) {
            synchronized (QrScan.class) {
                if (sInstance == null) {
                    sInstance = new QrScan();
                }
            }
        }
        return sInstance;
    }

    public void init(QrScanConfiguration configuration){
        if(configuration == null){
            throw new IllegalArgumentException(ERROR_INIT_CONFIG_WITH_NULL);
        }

        if(this.configuration == null){
            this.configuration = configuration;
            QrScanProxy.getInstance().setConfiguration(configuration);
        }else {
            Log.w(TAG, WARNING_RE_INIT_CONFIG);
        }
    }

    public boolean isInited() {
        return configuration != null;
    }

    private void checkConfiguration() {
        if (configuration == null) {
            throw new IllegalStateException(ERROR_NOT_INIT);
        }
    }

    public void destroy() {
        if (configuration != null) {
            configuration = null;
        }
    }


    public void launchScan(Context context, IScanModuleCallBack callback) {
        QrScanProxy.getInstance().setCallBack(callback);
        CaptureActivity.launch(context);
    }

    public void finishScan(CaptureActivity activity) {
        activity.finish();
    }

    public void restartScan(CaptureActivity activity) {
        activity.restartCamera();
    }
}
