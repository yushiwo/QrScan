/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.netease.scan.zxing.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.google.zxing.ResultPoint;
import com.netease.scan.QrScanProxy;
import com.netease.scan.R;
import com.netease.scan.zxing.camera.CameraManager;

import java.util.Collection;
import java.util.HashSet;

/**
 * This view is overlaid on top of the camera preview. It adds the viewfinder
 * rectangle and partial transparency outside it, as well as the laser scanner
 * animation and result points.
 *
 */
public final class ViewfinderView extends View {
	private static final String TAG = "ViewfinderView";
	/**
	 * 刷新界面的时间
	 */
	private static final long ANIMATION_DELAY = 10L;
	private static final int OPAQUE = 0xFF;
	private static final int TEXT_SIZE = 14;

	/** Tip文字距离扫描边框底部的距离 */
	private static final int TEXT_PADDING_TOP = 30;

	/**
	 * 四个绿色边角对应的长度
	 */
	private int ScreenRate;

	/**
	 * 四个绿色边角对应的宽度
	 */
	private static final int CORNER_WIDTH = 10;
	/**
	 * 扫描框中的中间线的宽度
	 */
	private static final int MIDDLE_LINE_WIDTH = 6;

	/**
	 * 扫描框中的中间线的与扫描框左右的间隙
	 */
	private static final int MIDDLE_LINE_PADDING = 5;

	/**
	 * 中间那条线每次刷新移动的距离
	 */
	private static final int SPEEN_DISTANCE = 10;

	/**
	 * 默认的提示文字
	 */
	private static final String DEFAULT_SCAN_TIP = "将二维码放入框内, 即可自动扫描";

	/**
	 * 手机的屏幕密度
	 */
	private static float density;


	/**
	 * 画笔对象的引用
	 */
	private Paint paint;

	/**
	 * 中间滑动线的最顶端位置
	 */
	private int slideTop;

	/**
	 * 中间滑动线的最底端位置
	 */
	private int slideBottom;

	/**
	 * 扫描框下面提示语
	 */
	private String scanTip = DEFAULT_SCAN_TIP;

	private Bitmap resultBitmap;
	private int maskColor;
	private int angleColor;
	private int mTipAlpha;
	private int mTipColor;
	private final int resultColor;

	private int mTipmMargin = TEXT_PADDING_TOP;

	private Drawable mSlideIcon;

	private final int resultPointColor;
	private Collection<ResultPoint> possibleResultPoints;
	private Collection<ResultPoint> lastPossibleResultPoints;

//	Bitmap bitmap;
	Bitmap mBitmap = null;

	boolean isFirst;

	public ViewfinderView(Context context) {
		this(context, null);
	}

	public ViewfinderView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ViewfinderView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);


		TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.ViewfinderView);

		//获取自定义属性和默认值
		maskColor = mTypedArray.getColor(R.styleable.ViewfinderView_maskColor, 0xbf000000);
		angleColor = mTypedArray.getColor(R.styleable.ViewfinderView_angleColor, Color.GREEN);
		mSlideIcon = mTypedArray.getDrawable(R.styleable.ViewfinderView_slideIcon);

		mTypedArray.recycle();

		density = context.getResources().getDisplayMetrics().density;
		//将像素转换成dp
		ScreenRate = (int)(20 * density);

		paint = new Paint();

		resultColor = 0x60000000;

		mTipAlpha = 0x40;

		resultPointColor = 0x19e64dff;
		possibleResultPoints = new HashSet<ResultPoint>(5);

		initConfig();
	}

	public void initConfig(){
		maskColor = QrScanProxy.getInstance().getMaskColor(getResources());
		angleColor = QrScanProxy.getInstance().getAngleColor(getResources());
		mTipColor = QrScanProxy.getInstance().getTipTextColor(getResources());
		scanTip = QrScanProxy.getInstance().getTipText(getResources());
		mTipmMargin = QrScanProxy.getInstance().getTipMarginTop();
		mSlideIcon = QrScanProxy.getInstance().getSlideIcon(getResources());
	}


	@Override
	public void onDraw(Canvas canvas) {
		//中间的扫描框，你要修改扫描框的大小，去CameraManager里面修改
		CameraManager.init(this.getContext().getApplicationContext());

		Rect frame = null;
		try {
			frame = CameraManager.get().getFramingRect();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}


		if (frame == null) {
			return;
		}

		//获取屏幕的宽和高
		int width = canvas.getWidth();
		int height = canvas.getHeight();

		paint.setColor(maskColor);

		//画出扫描框外面的阴影部分，共四个部分，扫描框的上面到屏幕上面，扫描框的下面到屏幕下面
		//扫描框的左边面到屏幕左边，扫描框的右边到屏幕右边
		canvas.drawRect(0, 0, width, frame.top, paint); //上
		canvas.drawRect(0, frame.top, frame.left, frame.bottom - 1, paint);  //左
		canvas.drawRect(frame.right, frame.top, width, frame.bottom - 1, paint); //右
		canvas.drawRect(0, frame.bottom - 1, width, height, paint);

		paint.setColor(0xffffffff);
		canvas.drawLine(frame.left + 1, frame.top + 1, frame.right - 1, frame.top + 1, paint);
		canvas.drawLine(frame.left + 1,frame.top + 1,frame.left + 1,frame.bottom - 1, paint);
		canvas.drawLine(frame.left + 1,frame.bottom - 1,frame.right -1,frame.bottom - 1,paint);
		canvas.drawLine(frame.right -1,frame.top + 1,frame.right - 1,frame.bottom - 1,paint);

		if (resultBitmap != null) {
			// Draw the opaque result bitmap over the scanning rectangle
			paint.setAlpha(OPAQUE);
			canvas.drawBitmap(resultBitmap, frame.left, frame.top, paint);
		} else {

			//画扫描框边上的角，总共8个部分
			paint.setColor(angleColor);
			canvas.drawRect(frame.left, frame.top, frame.left + ScreenRate,
					frame.top + CORNER_WIDTH, paint);
			canvas.drawRect(frame.left, frame.top, frame.left + CORNER_WIDTH, frame.top
					+ ScreenRate, paint);
			canvas.drawRect(frame.right - ScreenRate, frame.top, frame.right,
					frame.top + CORNER_WIDTH, paint);
			canvas.drawRect(frame.right - CORNER_WIDTH, frame.top, frame.right, frame.top
					+ ScreenRate, paint);
			canvas.drawRect(frame.left, frame.bottom - CORNER_WIDTH, frame.left
					+ ScreenRate, frame.bottom, paint);
			canvas.drawRect(frame.left, frame.bottom - ScreenRate,
					frame.left + CORNER_WIDTH, frame.bottom, paint);
			canvas.drawRect(frame.right - ScreenRate, frame.bottom - CORNER_WIDTH,
					frame.right, frame.bottom, paint);
			canvas.drawRect(frame.right - CORNER_WIDTH, frame.bottom - ScreenRate,
					frame.right, frame.bottom, paint);

			// 如果设置了slideIcon,则显示
			if(mSlideIcon != null){
//				mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.capture_add_scanning);

				BitmapDrawable bd = (BitmapDrawable) mSlideIcon;
				mBitmap = bd.getBitmap();

				//绘制中间的线,每次刷新界面，中间的线往下移动SPEEN_DISTANCE
				if (mBitmap != null){
					mBitmap = Bitmap.createScaledBitmap(mBitmap, frame.right - frame.left, mBitmap.getHeight(), true);
				}


				//初始化中间线滑动的最上边和最下边
				if(!isFirst){
					isFirst = true;
					slideTop = frame.top + mBitmap.getHeight();
					slideBottom = frame.bottom;
				}

				slideTop += SPEEN_DISTANCE;
				if(slideTop >= frame.bottom){
					slideTop = frame.top + mBitmap.getHeight();
				}

				canvas.drawBitmap(mBitmap, frame.left, slideTop - mBitmap.getHeight(), paint);
			}else{
				//初始化中间线滑动的最上边和最下边
				if(!isFirst){
					isFirst = true;
					slideTop = frame.top + MIDDLE_LINE_WIDTH;
					slideBottom = frame.bottom;
				}

				slideTop += SPEEN_DISTANCE;
				if(slideTop >= frame.bottom){
					slideTop = frame.top + MIDDLE_LINE_WIDTH;
				}

				canvas.drawRect(frame.left + MIDDLE_LINE_PADDING, slideTop - MIDDLE_LINE_WIDTH,frame.right - MIDDLE_LINE_PADDING, slideTop, paint);

			}

			// 画扫描框下面的字
			paint.setColor(mTipColor);
			paint.setTextSize(TEXT_SIZE * density);
			paint.setTextAlign(Paint.Align.CENTER);
			paint.setTypeface(Typeface.create("System", Typeface.NORMAL));
			canvas.drawText(scanTip, width/2, (float) (frame.bottom + (float)mTipmMargin * density), paint);



			/*Collection<ResultPoint> currentPossible = possibleResultPoints;
			Collection<ResultPoint> currentLast = lastPossibleResultPoints;
			if (currentPossible.isEmpty()) {
				lastPossibleResultPoints = null;
			} else {
				possibleResultPoints = new HashSet<ResultPoint>(5);
				lastPossibleResultPoints = currentPossible;
				paint.setAlpha(OPAQUE);
				paint.setColor(resultPointColor);
				for (ResultPoint point : currentPossible) {
					canvas.drawCircle(frame.left + point.getX(), frame.top
							+ point.getY(), 6.0f, paint);
				}
			}
			if (currentLast != null) {
				paint.setAlpha(OPAQUE / 2);
				paint.setColor(resultPointColor);
				for (ResultPoint point : currentLast) {
					canvas.drawCircle(frame.left + point.getX(), frame.top
							+ point.getY(), 3.0f, paint);
				}
			}*/


			//只刷新扫描框的内容，其他地方不刷新
			postInvalidateDelayed(ANIMATION_DELAY, frame.left, frame.top, frame.right, frame.bottom);

		}
	}

	public void drawViewfinder() {
		resultBitmap = null;
		invalidate();
	}

	/**
	 * Draw a bitmap with the result points highlighted instead of the live
	 * scanning display.
	 *
	 * @param barcode
	 *            An image of the decoded barcode.
	 */
	public void drawResultBitmap(Bitmap barcode) {
		resultBitmap = barcode;
		invalidate();
	}

	public void addPossibleResultPoint(ResultPoint point) {
		possibleResultPoints.add(point);
	}

}
