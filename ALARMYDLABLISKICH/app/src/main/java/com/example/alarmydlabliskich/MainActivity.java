package com.example.alarmydlabliskich;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;


public class MainActivity extends AppCompatActivity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        //SMS and GPS Permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {

            int PERMISSION_ALL = 1;
            String[] PERMISSIONS = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.SEND_SMS};
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
//            return;
        }

    }

}

