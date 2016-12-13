package com.example.igx.problem1;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.Telephony;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.net.URI;

public class MainActivity extends AppCompatActivity implements SensorEventListener /* implements Something1, Something2 */ {

    private LocationManager mLocationManager;
    private SensorManager mSensorManager;
    private Sensor accSen;
    private Sensor gyroSen;
    private Sensor lightSen;
    private Sensor laccSen;

    private SmsManager smsManager;

    public String str_loc = "0 0 0";
    public String str_sen = "0 0 0";

    private int btn_state = 0;

    TextView text_selectedData;
    TextView text_selectedType;
    EditText edit_phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn_getLocation = (Button) findViewById(R.id.btn_getLocation);
        Button btn_getSensors = (Button) findViewById(R.id.btn_getSensors);
        Button btn_sendMessage = (Button) findViewById(R.id.btn_sendMessage);

        text_selectedData = (TextView) findViewById(R.id.text_selectedData);
        text_selectedType = (TextView) findViewById(R.id.text_selectedType);
        edit_phoneNumber = (EditText) findViewById(R.id.edit_phoneNumber);

        text_selectedData.setText("Hello");

        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        accSen = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyroSen = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        lightSen = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        laccSen = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);

        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        btn_getLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text_selectedType.setText("Location");
                text_selectedData.setText(str_loc);
                btn_state = 1;
            }
        });

        btn_getSensors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text_selectedType.setText("Sensors");
                text_selectedData.setText(str_sen);
                btn_state = 2;
            }
        });

        btn_sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = String.valueOf(edit_phoneNumber.getText());
                //smsManager.sendTextMessage(str,str,"Hello",null,null);
                Log.d("테스트",str);
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse(str));
                startService(intent);
            }
        });
    }



    LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            str_loc = String.valueOf(location.getLatitude()) + " "
                    + String.valueOf(location.getLongitude()) + " "
                    + String.valueOf(location.getAltitude());
            Log.d("테스트",str_loc);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this,accSen,SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this,lightSen,SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this,gyroSen,SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this,laccSen,SensorManager.SENSOR_DELAY_NORMAL);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.d("테스트", "permission Error NETWORK");
            return;
        }
        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 0, mLocationListener);
        if (ActivityCompat.checkSelfPermission(this
                , Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this
                , Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.d("테스트", "permission Error GPS");
            return;
        }
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, mLocationListener);
    }

    String str_acc = "0", str_gyr = "0", str_lig = "0", str_lacc = "0";
    @Override
    public void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()){
            case Sensor.TYPE_ACCELEROMETER:
                str_acc = "TYPE_ACCELEROMETER "+ (int)event.values[0] + " "
                        + (int)event.values[1] + " "
                        + (int)event.values[2];
                //Log.d("ACC",str_acc);
                break;
            case Sensor.TYPE_GYROSCOPE:
                str_gyr = "TYPE_GYROSCOPE "+ (int)event.values[0] + " "
                        + (int)event.values[1] + " "
                        + (int)event.values[2];
                //Log.d("ACC",str_gyr);
                break;
            case Sensor.TYPE_LIGHT:
                str_lig = "TYPE_LIGHT "+ (int)event.values[0] + " "
                        + (int)event.values[1] + " "
                        + (int)event.values[2];
                //Log.d("ACC",str_lig);
                break;
            case Sensor.TYPE_LINEAR_ACCELERATION:
                str_lacc = "TYPE_LINEAR_ACCELERATION "+ (int)event.values[0] + " "
                        + (int)event.values[1] + " "
                        + (int)event.values[2];
                //Log.d("ACC",str_lacc);
                break;
        }
        str_sen = str_acc + "\n" + str_gyr +"\n" +  str_lig +"\n" +  str_lacc;
        //Log.d("ACC",str_sen);
        if(btn_state == 2)
            text_selectedData.setText(str_sen);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
