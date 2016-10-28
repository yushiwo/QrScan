## 展示  
<img src="https://github.com/yushiwo/QrScan/blob/master/picture/test.gif?raw=true"/>

## 使用  
* build.gradle配置  

```
compile 'com.netease.scan:lib-qr-scan:1.0.0'
```
* AndroidManifest配置

```
	// 设置权限
	 <uses-permission android:name="android.permission.VIBRATE"/>
    <uses-permission android:name="android.permission.CAMERA" />
    <uses-feature android:name="android.hardware.camera" />
    <uses-feature android:name="android.hardware.camera.autofocus" />
	
	// 注册activity
	<activity android:name="com.netease.scan.ui.CaptureActivity"
            android:screenOrientation="portrait"
            android:theme="@style/Theme.AppCompat.NoActionBar"/>
```
* 初始化  
在需要使用此组件的Activity的onCreate方法中，或者在自定义Application的onCreate方法中初始化。

```
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
```
* 启动组件相关方法（在QrScan.java类中已经提供）  
	* 开启扫描界面  
	
	```
	/**
     * 开启扫描界面
     * @param context
     * @param callback
     */
    public void launchScan(Context context, IScanModuleCallBack callback) {
        QrScanProxy.getInstance().setCallBack(callback);
        CaptureActivity.launch(context);
    }
	```
	
	* 关闭扫描界面
	
	```
	/**
     * 关闭扫描界面
     * @param activity
     */
    public void finishScan(CaptureActivity activity) {
        activity.finish();
    }
	```
	* 重启扫描功能  
	
	```
	/**
     * 重启扫描功能
     * @param activity
     */
    public void restartScan(CaptureActivity activity) {
        activity.restartCamera();
    }
	```
* 启动扫描并处理扫描结果

```
QrScan.getInstance().launchScan(MainActivity.this, new IScanModuleCallBack() {
                    @Override
                    public void OnReceiveDecodeResult(final Context context, String result) {
                        mCaptureContext = (CaptureActivity)context;

                        AlertDialog dialog = new AlertDialog.Builder(mCaptureContext)
                                .setMessage(result)
                                .setCancelable(false)
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        QrScan.getInstance().restartScan(mCaptureContext);
                                    }
                                })
                                .setPositiveButton("关闭", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        QrScan.getInstance().finishScan(mCaptureContext);
                                    }
                                })
                                .create();
                        dialog.show();
                    }
                });
```
