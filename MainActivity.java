package com.example.accelerometer;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.widget.TextView;


public class MainActivity extends Activity implements SensorEventListener {

    private float lastX, lastY, lastZ;

    private SensorManager sensorManager;
    private Sensor accelerometer;


    private double deltaX = 0;
    private double deltaY = 0;
    private double deltaZ = 0;

    private double gravityX = 0;
    private double gravityY = 0;
    private double gravityZ = 0;

    private double alpha = 0;

    private TextView currentX, currentY, currentZ, maxX, maxY, maxZ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeViews();

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            // success! we have an accelerometer

            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        } else {
            // fai! we dont have an accelerometer!
        }


    }

    public void initializeViews() {
        currentX = (TextView) findViewById(R.id.currentX);
        currentY = (TextView) findViewById(R.id.currentY);
        currentZ = (TextView) findViewById(R.id.currentZ);

    }

    //onResume() register the accelerometer for listening the events
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    //onPause() unregister the accelerometer for stop listening the events
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        // clean current values
        displayCleanValues();
        // display the current x,y,z accelerometer values
        displayCurrentValues();
        // display the max x,y,z accelerometer values


        // In this example, alpha is calculated as t / (t + dT),
        // where t is the low-pass filter's time-constant and
        // dT is the event delivery rate.

        alpha =0.8;

        // Isolate the force of gravity with the low-pass filter.
        gravityX = alpha * gravityX + (1 - alpha) * event.values[0];
        gravityY = alpha * gravityY + (1 - alpha) * event.values[1];
        gravityZ = alpha * gravityZ + (1 - alpha) * event.values[2];

        // Remove the gravity contribution with the high-pass filter.
        deltaX = event.values[0] - gravityX;
        deltaY = event.values[1] - gravityY;
        deltaZ = event.values[2] - gravityZ;

        // get the change of the x,y,z values of the accelerometer
        deltaX = Math.abs(lastX - event.values[0]);
        deltaY = Math.abs(lastY - event.values[1]);
        deltaZ = Math.abs(lastZ - event.values[2]);

    }

    public void displayCleanValues() {
        currentX.setText("0.0");
        currentY.setText("0.0");
        currentZ.setText("0.0");
    }

    // display the current x,y,z accelerometer values
    public void displayCurrentValues() {
        currentX.setText(String.format("%.2f m/s^2", deltaX));
        currentY.setText(String.format("%.2f m/s^2", deltaY));
        currentZ.setText(String.format("%.2f m/s^2", deltaZ));
        //.setText(Double.toString(deltaX));
        //currentY.setText(Double.toString(deltaY));
        //currentZ.setText(Double.toString(deltaZ));
    }

    // display the max x,y,z accelerometer values

}
