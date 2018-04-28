package com.smartparking.amrbadr12.smartparking;

import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.Nullable;

public class TimerBroadcastService extends Service {
    public static final String TAG = "TimerBroadcastService";
    public static final String COUNTDOWN_BR = "com.smartparking.amrbadr12.smartparking.countdown_br";
    CountDownTimer countDownTimer;
    int hours;
    Intent sentBroadcast;

    @Override
    public void onCreate() {
        super.onCreate();
        sentBroadcast = new Intent(COUNTDOWN_BR);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        hours = intent.getIntExtra("hours", 0);
        long hoursinMillis = hours * 60 * 60000;
        countDownTimer = new CountDownTimer(hoursinMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                sentBroadcast.putExtra("countdown", millisUntilFinished);
                sendBroadcast(sentBroadcast);
            }

            @Override
            public void onFinish() {
                //TODO: send a notification when the timer is finished
                //TODO: automatically unpark the car and send it to the realtime database
                stopSelf();
            }
        };
        countDownTimer.start();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        countDownTimer.cancel();
        super.onDestroy();
    }

    @Nullable

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
