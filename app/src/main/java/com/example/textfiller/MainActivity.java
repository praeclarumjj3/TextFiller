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

import com.example.textfill.TextFill;
import com.example.textfiller.R;

import static android.hardware.Sensor.TYPE_ORIENTATION;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    ImageView img;
    Bitmap results,maskBitmap;
    public static int x;
    public static int y;
    TextFill textFill;

    private SensorManager sensorManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        img = findViewById(R.id.mask);
        textFill = new TextFill();
        Bitmap univ = BitmapFactory.decodeResource(getResources(), R.drawable.univ);
        Bitmap finalmasking = textFill.MaskingProcess("JJ ROCKS", univ);
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
                textFill.rotateImage((float) (y*0.05/(x+0.1)),img);

            }


        }
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

}
