package com.justimaginethat.gogodriver.imagecompress;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.media.ExifInterface;
import android.os.Environment;
import android.text.TextPaint;


import com.justimaginethat.gogodriver.StartUp.FixLabels;

import java.io.File;
import java.io.FileOutputStream;

public class conversionImage {


    public String latitude;
    public String longitude;

    public String compressImage(String imageUri, Activity activityname,
                                float maxHeight, float maxWidth, int quality) {

        GPSCLASS gps = new GPSCLASS(activityname);

        BitmapFactory.Options options = new BitmapFactory.Options();
        String location;
        String date;
        Matrix matrix = null;


        options.inJustDecodeBounds = true;
        int rotation = 0;
        Bitmap bmp = BitmapFactory.decodeFile(imageUri.trim(), options);
        Canvas c = null;
        float actualHeight = options.outHeight;
        float actualWidth = options.outWidth;

        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                // adjust width according to maxHeight
                imgRatio = maxHeight / actualHeight;
                actualWidth = imgRatio * actualWidth;
                actualHeight = maxHeight;
            } else if (imgRatio > maxRatio) {
                // adjust height according to maxWidth
                imgRatio = maxWidth / actualWidth;
                actualHeight = imgRatio * actualHeight;
                actualWidth = maxWidth;
            } else {
                actualHeight = maxHeight;
                actualWidth = maxWidth;
            }
        }
        options.inJustDecodeBounds = false;
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        bmp = BitmapFactory.decodeFile(imageUri, options);

        Bitmap scaledBitmap = null;
        try {
            scaledBitmap = Bitmap.createBitmap((int) actualWidth,
                    (int) actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }
        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        ExifInterface exif = null;
        try {
            exif = new ExifInterface(imageUri);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);

            matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);

            } else if (orientation == 3) {
                matrix.postRotate(180);

            } else if (orientation == 8) {
                matrix.postRotate(270);


            }


            TextPaint textPaint2 = new TextPaint(Paint.ANTI_ALIAS_FLAG
                    | Paint.LINEAR_TEXT_FLAG);
            textPaint2.setStyle(Style.FILL);
            textPaint2.setColor(Color.BLACK);
            textPaint2.setTextSize(10);

            date = exif.getAttribute(ExifInterface.TAG_DATETIME);
            TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG
                    | Paint.LINEAR_TEXT_FLAG);
            textPaint.setStyle(Style.FILL);
            textPaint.setColor(Color.BLACK);
            textPaint.setTextSize(10);


            c = new Canvas(scaledBitmap);
            c.setMatrix(scaleMatrix);
            c.drawBitmap(bmp, middleX - bmp.getWidth() / 2,
                    middleY - bmp.getHeight() / 2, new
                            Paint(Paint.FILTER_BITMAP_FLAG));
            date = exif.getAttribute(ExifInterface.TAG_DATETIME);
            c.rotate(rotation);
            c.rotate(rotation);
            Paint paintText = new Paint(Paint.ANTI_ALIAS_FLAG);
            paintText.setColor(Color.RED);
            paintText.setTextSize(80);
            paintText.setStyle(Style.FILL);


//            if (scaledBitmap.getWidth() == 800 && scaledBitmap.getHeight() == 600) {
//                c.drawText(date, scaledBitmap.getWidth(), scaledBitmap.getHeight() * 2 - 100, paintText);
//                c.drawText(Labels.imageLatitude + Labels.imageLongitude, scaledBitmap.getWidth(), scaledBitmap.getHeight() * 2, paintText);
//            } else if (scaledBitmap.getWidth() == 800 && scaledBitmap.getHeight() == 450) {
//                c.drawText(date, scaledBitmap.getWidth(), scaledBitmap.getHeight() * 3 - 200, paintText);
//                c.drawText(Labels.imageLatitude+" / "+ Labels.imageLongitude, scaledBitmap.getWidth(), scaledBitmap.getHeight() * 3, paintText);
//            } else {
//                c.drawText(date, scaledBitmap.getWidth(), scaledBitmap.getHeight() * 2 - 100, paintText);
//                c.drawText(Labels.imageLatitude +" / "+ Labels.imageLongitude, scaledBitmap.getWidth(), scaledBitmap.getHeight() * 2, paintText);
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        String filename = getFilename();
        try {
            out = new FileOutputStream(filename);

            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, quality, out);

            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);

            c.save();
            c.translate(0, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filename;
    }

    public String getFilename() {

        File file = new File(Environment.getExternalStorageDirectory()
                .getPath(), "GOGO");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/"
                + System.currentTimeMillis() + ".jpg");
        FixLabels.FILE_PATH = uriSting;

        return uriSting;

    }


}


