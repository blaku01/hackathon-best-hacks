package com.example.alarmydlabliskich;
import android.content.Context;
import android.media.Image;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.location.LocationListener;
import androidx.core.app.ActivityCompat;
import android.location.LocationManager;
import android.hardware.Sensor;
import android.Manifest;
import android.content.pm.PackageManager;
import android.content.Intent;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    private static MainActivity instance;
    private static boolean fall = false;
    private static int i = 3;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        instance = this;

        //SMS and GPS Permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {

            int PERMISSION_ALL = 1;
            String[] PERMISSIONS = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.SEND_SMS};
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }

        Intent intent= new Intent(getApplicationContext(), IService2.class);
        startService(intent);

    }
    public static MainActivity getInstance() {
        return instance;
    }
    public void DetectedFall(){
        if (fall==true) {
            return;
        }
        fall = true;
        MediaPlayer beep = MediaPlayer.create(this, R.raw.beep);
        beep.start();
        beep.setLooping(true);
        beep.setVolume(0.4f,0.4f);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true);
            setTurnScreenOn(true);
        }
        setContentView(R.layout.guzik);
        TextView CountdownText = (TextView)findViewById(R.id.timer);

        new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
                CountdownText.setText( Double.toString(Math.floor(millisUntilFinished / 1000)));
                if(i==0){
                    this.cancel();
                    beep.stop();
                    fall=false;
                    setContentView(R.layout.main);
                    i=3;

                    return;

                }
            }

            public void onFinish() {
                beep.stop();
                MediaPlayer ivona = MediaPlayer.create(getInstance(), R.raw.ivona);
                ivona.setLooping(true);
                ivona.start();
            }
        }.start();
    }

    public void onPanicButtonClick(View view){
        if(i>0) {
            i--;
            MainActivity.getInstance().runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    //alarm_text4.setText(i);


                }
            });

        }else{

        }
    }
}

