package com.example.yman.sensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private SensorManager sensorManager;
    private ImageView compass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        compass = (ImageView) findViewById(R.id.compass);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor magneticSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        Sensor accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(listener,magneticSensor,SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(listener,accelerometerSensor,SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(sensorManager != null){
            sensorManager.unregisterListener(listener);
        }
    }
    private SensorEventListener listener = new SensorEventListener() {

        float[] accelerometerValues = new float[3];
        float[] magneticValues = new float[3];

        private float lastRotateDegree;

        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            if(sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
                accelerometerValues = sensorEvent.values.clone();
            }
            else if (sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD){
                magneticValues = sensorEvent.values.clone();
            }
            float[] R = new float[9];
            float[] values = new float[3];
            //为R赋值
            SensorManager.getRotationMatrix(R,null,accelerometerValues,magneticValues);
            //为values赋值
            SensorManager.getOrientation(R,values);

            float roateDegree = -(float) Math.toDegrees(values[0]);
            if(Math.abs(roateDegree - lastRotateDegree) > 1){
                RotateAnimation animation = new RotateAnimation(lastRotateDegree,roateDegree, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
                animation.setFillAfter(true);
                compass.startAnimation(animation);
                lastRotateDegree = roateDegree;
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };
}
