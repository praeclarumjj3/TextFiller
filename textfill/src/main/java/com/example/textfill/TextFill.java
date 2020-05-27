package com.example.textfill;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.widget.ImageView;

public class TextFill {



    public Bitmap MaskingProcess(String string,Bitmap original) {
        Bitmap results = null;
        Bitmap mask;

        try{

            mask = textAsBitmap(string,1000, Color.WHITE);

            if(original!=null){
                int w = original.getWidth();
                int h = original.getHeight();

                results = Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888);
                Bitmap maskBitmap = Bitmap.createScaledBitmap(mask, w, h, true);

                Canvas canvas = new Canvas(results);

                Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));

                canvas.drawBitmap(original,0,0,null);
                canvas.drawBitmap(maskBitmap,0,0,paint);

                paint.setXfermode(null);
                paint.setStyle(Paint.Style.STROKE);
            }
        }catch (OutOfMemoryError outOfMemoryError){
            outOfMemoryError.printStackTrace();
        }
        return results;
    }


    public Bitmap textAsBitmap(String text, float textSize, int textColor) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(textSize);
        paint.setColor(textColor);
        paint.setTextAlign(Paint.Align.LEFT);
        float baseline = -paint.ascent(); // ascent() is negative
        int width = (int) (paint.measureText(text) + 0.5f); // round
        int height = (int) (baseline + paint.descent() + 0.5f);
        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(image);
        canvas.drawText(text, 0, baseline, paint);
        return image;
    }

    public void rotateImage(float rotate, ImageView img) {
        Matrix matrix = new Matrix();
        img.setScaleType(ImageView.ScaleType.MATRIX); //required
        matrix.postRotate(rotate, img.getDrawable().getBounds().width()/2,    img.getDrawable().getBounds().height()/2);
        img.setImageMatrix(matrix);
    }


}
