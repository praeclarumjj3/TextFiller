package com.example.textfiller;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.textfiller.R;

import static android.hardware.Sensor.TYPE_ORIENTATION;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    ImageView img;
    Bitmap results,maskBitmap;
    public static int x;
    public static int y;

    private SensorManager sensorManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        img = findViewById(R.id.mask);
        Bitmap finalmasking = MaskingProcess("JJ ROCKS");
        img.setImageBitmap(finalmasking);

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent)
    {
        {
            if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                // the values you were calculating originally here were over 10000!
                x = (int) Math.pow(sensorEvent.values[0], 1.5);
                y = (int) Math.pow(sensorEvent.values[2], 1.5);
                rotateImage((float) (y*0.1/(x+0.1)));

            }


        }
    }

    private void rotateImage(float rotate) {
        Matrix matrix = new Matrix();
        img.setScaleType(ImageView.ScaleType.MATRIX); //required
        matrix.postRotate(rotate, img.getDrawable().getBounds().width()/2,    img.getDrawable().getBounds().height()/2);
        img.setImageMatrix(matrix);
    }

    // I've chosen to not implement this method
    public void onAccuracyChanged(Sensor arg0, int arg1)
    {
        // TODO Auto-generated method stub

    }

    @Override
    protected void onResume()
    {
        super.onResume();
        // Register this class as a listener for the accelerometer sensor
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        // ...and the orientation sensor
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onStop()
    {
        // Unregister the listener
        sensorManager.unregisterListener(this);
        super.onStop();
    }


    private Bitmap MaskingProcess(String string) {
        try{
            Bitmap original,mask;
            original = BitmapFactory.decodeResource(getResources(), R.drawable.univ);
            mask = textAsBitmap(string,1000, Color.WHITE);

            if(original!=null){
                int w = original.getWidth();
                int h = original.getHeight();

                results = Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888);
                maskBitmap = Bitmap.createScaledBitmap(mask,w,h,true);

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
        img.setImageBitmap(results);
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


}
