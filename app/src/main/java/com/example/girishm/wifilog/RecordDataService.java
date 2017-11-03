package com.example.girishm.wifilog;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;

import java.util.concurrent.ExecutionException;

public class RecordDataService extends Service {
    private final LocalBinder mBinder = new LocalBinder();
    protected Handler handler;

    private class LocalBinder extends Binder {
        public RecordDataService getService() {
            return RecordDataService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final long time = intent.getLongExtra("time", 5000);
        handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    CheckWiFi.recordWifiStatus(getApplicationContext());
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
                handler.postDelayed(this, time);
            }
        });
        return android.app.Service.START_STICKY;
    }

}