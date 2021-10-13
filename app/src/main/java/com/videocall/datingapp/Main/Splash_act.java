package com.videocall.datingapp.Main;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.videocall.datingapp.R;

public class Splash_act extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_splash_act);

        requestRead();


    }

    public void showSettingsAlert() {
        try {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Splash_act.this);

            alertDialogBuilder.setTitle("GPS Settings");

            alertDialogBuilder
                    .setMessage("Please enable All Permission. Do you want to go to settings menu?");

            // On pressing Settings button
            alertDialogBuilder.setPositiveButton("Settings",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(
                                    Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(intent);
                        }
                    });

            // on pressing cancel button
            alertDialogBuilder.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });

//added this line
            AlertDialog alertDialog = alertDialogBuilder.create();

            // Showing Alert Message
            alertDialog.setCancelable(false);
            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void requestRead() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        } else {
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    Intent intent = new Intent(Splash_act.this, MainActivity.class);
                    startActivity(intent);
                }
            }, 3500);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requestRead();
            } else {
                showSettingsAlert();
                Toast.makeText(Splash_act.this, "Permission Denied to go setting and apply it", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
