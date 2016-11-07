package com.netease.scan;

import android.content.Context;
import android.graphics.drawable.Drawable;

/**
 * @author hzzhengrui
 * @Date 16/10/27
 * @Description
 */
public class QrScanConfiguration {

    public int maskColor;
    public int angleColor;
    public int titleHeight;
    public int titleTextColor;
    public int titleTextSize;
    public String titleText;
    public int titleTextRes;
    public String tipText;
    public int tipTextRes;
    public int tipTextColor;
    public int tipTextSize;
    public int tipMarginTop;
    public Drawable slideIcon;
    public int slideIconRes;
    public float scanFrameRectRate;


    public QrScanConfiguration(final Builder builder){
        this.maskColor = builder.maskColor;
        this.angleColor = builder.angleColor;
        this.titleHeight = builder.titleHeight;
        this.titleText = builder.titleText;
        titleTextColor = builder.titleTextColor;
        this.titleTextSize = builder.titleTextSize;
        this.titleTextRes = builder.titleTextRes;
        this.tipText = builder.tipText;
        this.tipTextRes = builder.tipTextRes;
        this.tipTextColor = builder.tipTextColor;
        this.tipTextSize = builder.tipTextSize;
        this.tipMarginTop = builder.tipMarginTop;
        this.slideIcon = builder.slideIcon;
        this.slideIconRes = builder.slideIconRes;
        this.scanFrameRectRate = builder.scanFrameRectRate;
    }

    /**
     * 生成默认的图片选择器配置
     * @param context
     * @return
     */
    public static QrScanConfiguration createDefault(Context context){
        return new Builder(context).build();
    }

    public static class Builder{

        private Context context;

        private int maskColor = -1;
        private int angleColor = -1;
        private int titleHeight = -1;
        private int titleTextColor = -1;
        private int titleTextSize = -1;
        private String titleText = null;
        private int titleTextRes = -1;
        private String tipText = null;
        private int tipTextSize = -1;
        private int tipTextRes = -1;
        private int tipTextColor = -1;
        private int tipMarginTop = -1;
        private Drawable slideIcon = null;
        private int slideIconRes = -1;
        private float scanFrameRectRate = -1;


        public Builder(Context context) {
            this.context = context.getApplicationContext();
        }

        /**
         * 设置扫一扫界面背景颜色
         * @param maskColor
         * @return
         */
        public Builder setMaskColor(int maskColor){
            this.maskColor = maskColor;
            return this;
        }

        /**
         * 设置扫一扫框边上四个角的颜色
         * @param angleColor
         * @return
         */
        public Builder setAngleColor(int angleColor) {
            this.angleColor = angleColor;
            return this;
        }

        /**
         * 设置标题高度
         * @param titleHeight
         * @return
         */
        public Builder setTitleHeight(int titleHeight) {
            this.titleHeight = titleHeight;
            return this;
        }

        /**
         * 设置标题文本
         * @param titleText
         * @return
         */
        public Builder setTitleText(String titleText) {
            this.titleText = titleText;
            return this;
        }

        public Builder setTitleText(int titleTextRes) {
            this.titleTextRes = titleTextRes;
            return this;
        }

        /**
         * 设置标题文字大小
         * @param titleTextSize
         * @return
         */
        public Builder setTitleTextSize(int titleTextSize) {
            this.titleTextSize = titleTextSize;
            return this;
        }

        public Builder setTitleTextColor(int titleTextColor){
            this.titleTextColor = titleTextColor;
            return this;
        }

        /**
         * 设置扫一扫框下的提示文字
         * @param tipText
         * @return
         */
        public Builder setTipText(String tipText) {
            this.tipText = tipText;
            return this;
        }

        public Builder setTipText(int tipTextRes) {
            this.tipTextRes = tipTextRes;
            return this;
        }

        /**
         * 设置提示文字的大小
         * @param tipTextSize
         * @return
         */
        public Builder setTipTextSize(int tipTextSize) {
            this.tipTextSize = tipTextSize;
            return this;
        }

        /**
         * 设置提示文字的颜色
         * @param tipTextColor
         * @return
         */
        public Builder setTipTextColor(int tipTextColor) {
            this.tipTextColor = tipTextColor;
            return this;
        }

        public Builder setTipMarginTop(int tipMarginTop) {
            this.tipMarginTop = tipMarginTop;
            return this;
        }

        /**
         * 设置扫描框中间的移动条
         * @param slideIcon
         * @return
         */
        public Builder setSlideIcon(Drawable slideIcon) {
            this.slideIcon = slideIcon;
            return this;
        }

        public Builder setSlideIcon(int slideIconRes) {
            this.slideIconRes = slideIconRes;
            return this;
        }

        /**
         * 设置扫描框长度相对屏幕宽度的比例
         * @param scanFrameRectRate
         * @return
         */
        public Builder setScanFrameRectRate(float scanFrameRectRate) {
            this.scanFrameRectRate = scanFrameRectRate;
            return this;
        }

        public QrScanConfiguration build(){
            initEmptyFieldsWithDefaultValues();
            return new QrScanConfiguration(this);
        }

        private void initEmptyFieldsWithDefaultValues() {
            if(maskColor == -1){
                maskColor = R.color.scan_mask;
            }
            if(angleColor == -1){
                angleColor = R.color.scan_angle;
            }
            if(titleHeight == -1){
                titleHeight = 53;
            }
            if(titleTextColor == -1){
                titleTextColor = R.color.scan_title;
            }
            if(titleTextSize == -1){
                titleTextSize = 18;
            }
            if(titleTextRes == -1 && titleText == null){
                titleTextRes = R.string.scan_title_text;
            }
            if(tipTextRes == -1 && tipText == null){
                tipTextRes = R.string.scan_tip_text;
            }
            if(tipTextColor == -1){
                tipTextColor = R.color.scan_tip;
            }
            if(tipTextSize == -1){
                tipTextSize = 14;
            }
            if(tipMarginTop == -1){
                tipMarginTop = 35;
            }
            if(slideIconRes == -1 && slideIcon == null){
                slideIconRes = R.drawable.ic_scan_slider;
            }
            if(scanFrameRectRate == -1){
                scanFrameRectRate = 2/3;
            }
        }
    }
}
