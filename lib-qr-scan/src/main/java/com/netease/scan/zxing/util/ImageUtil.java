package com.netease.scan.zxing.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.view.View;
import android.widget.ImageView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by king.wu on 12/10/15.
 *
 * 与图片处理相关的接口
 */
public class ImageUtil {

    //根据路径读取图片
    public static Bitmap getBitmapFromSd(File imgFile) {

        if (!imgFile.exists()){
            return null;
        }

        FileInputStream fis = null;

        try {

            fis = new FileInputStream(imgFile);//文件输入流
            Bitmap bmp = BitmapFactory.decodeStream(fis);
            return bmp;
        } catch (OutOfMemoryError error){

        }
        catch (Exception e) {
            e.printStackTrace();
        }finally{
            try {
                if(fis!=null){
                    fis.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return null;
    }

    //根据路径 保存图片
    public static void saveBitmap(File file, Bitmap bitmap){
        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);

        }  catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if(fos!=null){
                    fos.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    //将bitmap转换成二进制流
    public static byte[] getBitmapByte(Bitmap bitmap){
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        try {
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }




    /**
     * 获取图片拍摄时的旋转角度
     *
     * @param path
     * @return
     */public static int getPictureRotateDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;

    }

    /**
     * 图片旋转
     *
     * @param angle
     * @param bitmap
     * @return
     */
    public static Bitmap rotateImageView(int angle, Bitmap bitmap) {
        // 旋转图片 动作
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap,0,0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizedBitmap;
    }


    //压缩图片
    public static Bitmap compressImage(Bitmap image, int quality) {

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, quality, os);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        ByteArrayInputStream in = new ByteArrayInputStream(os.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(in, null, null);//把ByteArrayInputStream数据生成图片

        return bitmap;
    }


    /**
     * 读取图片,会计算inSampleSize,用来减少内存
     * @param bitmap  Bitmap
     * @param quality 图片质量
     * @param reqWidth  需要的宽度,如 imageView的width
     * @param reqHeight 需要的高度,如 imageView的height
     * @return
     */
    public static Bitmap decodeSampledBitmapFromBitmap(Bitmap bitmap, int quality,
                                                       int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.outWidth = bitmap.getWidth();
        options.outHeight = bitmap.getHeight();

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;

        // Decode bitmap with inSampleSize set

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, os);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        ByteArrayInputStream in = new ByteArrayInputStream(os.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中

        Bitmap resultBitmap = BitmapFactory.decodeStream(in, null, options);//把ByteArrayInputStream数据生成图片

        return  resultBitmap;
    }

    /**
     * 读取图片,会计算inSampleSize,用来减少内存
     * @param path 文件路径
     * @param reqWidth  需要的宽度,如 imageView的width
     * @param reqHeight 需要的高度,如 imageView的height
     * @return
     */
    public static Bitmap decodeSampledBitmapFromFile(String path,
                                                     int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }


    /**
     *
     * @param res
     * @param resId
     * @param outRes 传入一个大小为2的数组.输出结果,outRes[0]是宽,outRes[1]是高
     */
    public static void getBitmapSize(Resources res, int resId, int[] outRes){
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        try {
            outRes[0] = options.outWidth;
            outRes[1] = options.outHeight;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 读取图片,会计算inSampleSize,用来减少内存
     * @param res  Resources
     * @param resId resId
     * @param reqWidth  需要的宽度,如 imageView的width
     * @param reqHeight 需要的高度,如 imageView的height
     * @return
     */
    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }


    /**
     * 根据图片的大小 取得其采样的大小
     * @param options   BitmapFactory.Options
     * @param reqWidth  需要的宽度,如 imageview的width
     * @param reqHeight 需要的高度,如 imageview的height
     * @return
     */
    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
//
//    //根据scale值,计算sampleSize
//    public static int calculateInSampleSizeMore(
//            BitmapFactory.Options options, int reqWidth, int reqHeight) {
//        if (reqHeight == 0 || reqWidth == 0){
//            return 1;
//        }
//        // Raw height and width of image
//        final int height = options.outHeight;
//        final int width = options.outWidth;
//
//        float scaleWidth = (float)height/reqHeight;
//        float scaleHeight = (float)width/reqWidth;
//
//        float maxScale = Math.max(scaleWidth, scaleHeight);
//
//        if (maxScale < 1){
//            maxScale = 1.f;
//        }
//
//        int inSampleSize = (int)Math.ceil(maxScale);
//
//        return inSampleSize;
//    }



    //回收图片资源, 在 onDestroy中 调用 System.gc();来统一释放内容,不需要每一张图片调用一次System.gc()
    public static void recycleBitmap(Bitmap bitmap){
        //回收这个图片的内存
        if(bitmap != null && !bitmap.isRecycled()){
            bitmap.recycle();
            bitmap = null;
        }
    }

    /**
     * 回收imageview图片资源
     * @param imageView
     */
    public static void releaseImageViewResouce(ImageView imageView) {
        if (imageView == null) return;
        Drawable drawable = imageView.getDrawable();
        if (drawable != null && drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            Bitmap bitmap = bitmapDrawable.getBitmap();
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
            }
        }
    }

    /**
     * 回收view的background资源
     *
     * @param view
     */
    public static void releaseBackgroundResouce(View view) {
        if (view == null) return;
        Drawable drawable = view.getBackground();
        if (drawable != null && drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            Bitmap bitmap = bitmapDrawable.getBitmap();
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
            }
        }
    }

    public static void releaseDrawable(Drawable drawable){
        if (drawable == null) return;
        if (drawable != null && drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            Bitmap bitmap = bitmapDrawable.getBitmap();
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
            }
        }
    }


    /**
     *  圆角bitmap
     * @param context context
     * @param input  source bitmap
     * @param pixels  corner pix
     * @param w       width
     * @param h       height
     * @param squareTL   is top left need rounded
     * @param squareTR   is top right need rounded
     * @param squareBL   is bottom left need rounded
     * @param squareBR   is bottom right need rounded
     * @return  result bitmap
     */
    public static Bitmap getRoundedCornerBitmap(Context context, Bitmap input, int pixels , int w , int h , boolean squareTL, boolean squareTR, boolean squareBL, boolean squareBR  ) {

        Bitmap output = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final float densityMultiplier = context.getResources().getDisplayMetrics().density;

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, w, h);
        final RectF rectF = new RectF(rect);

        //make sure that our rounded corner is scaled appropriately
        final float roundPx = pixels*densityMultiplier;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);


        //draw rectangles over the corners we want to be square
        if (squareTL ){
            canvas.drawRect(0, h/2, w/2, h, paint);
        }
        if (squareTR ){
            canvas.drawRect(w/2, h/2, w, h, paint);
        }
        if (squareBL ){
            canvas.drawRect(0, 0, w/2, h/2, paint);
        }
        if (squareBR ){
            canvas.drawRect(w/2, 0, w, h/2, paint);
        }


        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(input, 0,0, paint);

        return output;
    }
}
