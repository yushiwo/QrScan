package com.netease.scan;

import android.content.Context;

/**
 * Created by zr on 2016/10/8.
 */

public interface IScanModuleCallBack {
    void OnReceiveDecodeResult(Context context, String result);
}
