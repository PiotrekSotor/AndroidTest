package com.example.piotrek.acelerometr_1;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class Accelerometr_1MainActivity extends AppCompatActivity implements SensorEventListener{

    Sensor senSensor;
    SensorManager senSensorManager;
    TextView xAxisTextView;
    TextView yAxisTextView;
    TextView zAxisTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accelerometr_1_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_accelerometr_1_main, menu);

        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senSensor = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this, senSensor, SensorManager.SENSOR_STATUS_ACCURACY_HIGH);

        xAxisTextView = (TextView)findViewById(R.id.xAxisTextView);
        yAxisTextView = (TextView)findViewById(R.id.yAxisTextView);
        zAxisTextView = (TextView)findViewById(R.id.zAxisTextView);

        return true;
    }
    protected void onPause(){
        super.onPause();
        if (senSensorManager != null)
            senSensorManager.unregisterListener(this);
    }
    protected void onResume(){
        super.onResume();
        if (senSensor != null)
            senSensorManager.registerListener(this,senSensor, SensorManager.SENSOR_STATUS_ACCURACY_HIGH);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor mySensor = event.sensor;

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER)
        {
            xAxisTextView.setText("X: " + Float.toString(event.values[0]));
            yAxisTextView.setText("Y: " + Float.toString(event.values[1]));
            zAxisTextView.setText("Z: " + Float.toString(event.values[2]));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
