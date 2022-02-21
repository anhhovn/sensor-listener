package com.example.sensorlisteners;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager mSensorManager;
    //Individual light and proximity sensors
    private Sensor mSensorProximity;
    private Sensor mSensorLight;

    //TextViews to display current sensor values
    private TextView mTextSensorLight;
    private TextView mTextSensorProximity;

    //ImageView
    private ImageView mImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        //get textviews and assign them to their respective variables
        mTextSensorLight = (TextView) findViewById(R.id.label_light);
        mTextSensorProximity = (TextView) findViewById(R.id.label_proximity);
        //get instances of the default light and proximity sensors
        mSensorProximity = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        mSensorLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        //get error string
        String sensor_error = getResources().getString(R.string.error_no_sensor);
        if(mSensorLight == null){
            mTextSensorLight.setText(sensor_error);
        }
        if(mSensorProximity == null){
            mTextSensorProximity.setText(sensor_error);
        }
        //get ImageView and assign it the its variable
        mImage = (ImageView) findViewById(R.id.image);


    }

    @Override
    protected void onStart(){
        super.onStart();
        //register sensor listeners
        if(mSensorProximity != null){
            mSensorManager.registerListener(this, mSensorProximity, SensorManager.SENSOR_DELAY_NORMAL);
        }

        if(mSensorLight != null){
            mSensorManager.registerListener(this, mSensorLight, SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    protected void onStop(){
        super.onStop();
        mSensorManager.unregisterListener(this); //unregister all listeners
    }
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        int sensorType = sensorEvent.sensor.getType();
        float currentValue = sensorEvent.values[0];
        switch (sensorType){
            //Event came from the light sensor
            case Sensor.TYPE_LIGHT:
                //Handle light sensor
                System.out.println(currentValue);
                mTextSensorLight.setText(getResources().getString(R.string.label_light, currentValue));
                if(currentValue == 0){
                    getWindow().getDecorView().setBackgroundColor(getResources().getColor(R.color.purple_200));
                }else if(currentValue > 0 && currentValue <=50){
                    getWindow().getDecorView().setBackgroundColor(getResources().getColor(R.color.purple_500));
                }else if(currentValue > 50 && currentValue <=100){
                    getWindow().getDecorView().setBackgroundColor(getResources().getColor(R.color.purple_700));
                }else{
                    getWindow().getDecorView().setBackgroundColor(getResources().getColor(R.color.black));
                }
                break;
            case Sensor.TYPE_PROXIMITY:
                //handle proximity sensor
                mTextSensorProximity.setText(getResources().getString(R.string.label_proximity,currentValue));
                System.out.println(currentValue);

                if(currentValue == 0){
                    mImage.requestLayout();
                    mImage.getLayoutParams().height = 10000;

                }else{
                    mImage.requestLayout();
                    mImage.getLayoutParams().height = 200;
                }

                break;
            default:
                //do nothing

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}