package com.example.boundservicesapp;

import static android.content.ContentValues.TAG;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.Random;

public class MyService extends Service {

    private int mRandomNumber;
    private boolean mIsRandomGeneratorOn;

    private final int MIN = 0;
    private final int MAX = 100;


    class MyServiceBinder extends Binder {
        public MyService getService(){
           return MyService.this;
        }
    }

    private IBinder mBinder = new MyServiceBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    //onDestroy method of service calls stopRandomNumberGenerator() to stop generating number
    public void onDestroy() {
        super.onDestroy();
        stopRandomNumberGenerator();
        Log.i(TAG, "Service Destroyed");
    }

    //new thread is created and service runs on it as generating random numbers is time taking task.
    //UI Thread is allowed to do other ui and small tasks
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "Service Started");
        mIsRandomGeneratorOn = true;
        new Thread(new Runnable() {
            public void run() {
                startRandomNumberGenerator();
            }
        }).start();
        return START_STICKY;
    }


    //random number generator method
    private void startRandomNumberGenerator() {
        while (mIsRandomGeneratorOn) {
            try {
                Thread.sleep(1000);
                if (mIsRandomGeneratorOn) {
                    mRandomNumber = new Random().nextInt(MAX) + MIN;
                    Log.i(TAG, "Random number : " + mRandomNumber);

                }
            } catch (InterruptedException e) {
                Log.i(TAG, "Thread Interrupted");
            }
        }
    }

    //stop number generator method
    private void stopRandomNumberGenerator() {
        mIsRandomGeneratorOn = false;
    }

    public int getRandomNumber() {
        return mRandomNumber;
    }
}
