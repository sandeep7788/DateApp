package com.videocall.datingapp.Extra;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import androidx.annotation.Nullable;
import android.widget.Toast;

public class ServiceClass extends Service {
    private MediaPlayer player;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Toast.makeText(this, "yes", Toast.LENGTH_SHORT).show();

        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

    }


}
