package com.smartparking.amrbadr12.smartparking;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

public class TimerBroadcastService extends Service {
    public static final String TAG = "TimerBroadcastService";
    public static final String COUNTDOWN_BR = "com.smartparking.amrbadr12.smartparking.countdown_br";
    CountDownTimer countDownTimer;
    int hours;
    Intent navToThis;
    Intent sentBroadcast;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    @Override
    public void onCreate() {
        super.onCreate();
        sentBroadcast = new Intent(COUNTDOWN_BR);
        navToThis = new Intent(this, MainActivity.class);
        SharedPreferences mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mSharedPreferences.edit();
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
                PendingIntent pendingIntent = PendingIntent.getActivity(TimerBroadcastService.this, 1, navToThis, 0);
                NotificationCompat.Builder builder = new NotificationCompat.Builder(TimerBroadcastService.this);
                builder.setAutoCancel(true).setContentTitle("Park duration has finished")
                        .setContentText("Unparking your car now.").setSubText("Tap to navigate to app")
                        .setContentIntent(pendingIntent).setSmallIcon(R.drawable.car_connected_yellow);
                NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                notificationManager.notify(1, builder.build());
                stopSelf();
                mEditor.putBoolean("STARTED", false);
                mEditor.apply();
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
