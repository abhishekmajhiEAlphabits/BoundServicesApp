package com.example.boundservicesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;

import com.example.boundservicesapp.databinding.ActivityMainBinding;

/*
* Bound Services demo
* */
public class MainActivity extends AppCompatActivity {

    private MyService mService;
    private boolean isServiceBound;
    private ServiceConnection serviceConnection;

    ActivityMainBinding binding;

    private Intent serviceIntent;

    //private boolean mStopLoop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        serviceIntent = new Intent(getApplicationContext(), MyService.class);

        //start service button method
        binding.startServiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startService(serviceIntent);
            }
        });

        //stop service button method
        binding.stopServiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopService(serviceIntent);
            }
        });


        //bind service button method
        binding.bindServiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (serviceConnection == null) {
                    serviceConnection = new ServiceConnection() {
                        @Override
                        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                            MyService.MyServiceBinder mServiceBinder = (MyService.MyServiceBinder) iBinder;
                            mService = mServiceBinder.getService();
                            isServiceBound = true;
                        }

                        @Override
                        public void onServiceDisconnected(ComponentName componentName) {
                            isServiceBound = false;
                        }
                    };

                }

                bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE);
            }
        });


        //unbind service button method
        binding.unbindServiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unbindService(serviceConnection);
                isServiceBound = false;
            }
        });


        //get random number button method
        binding.getRandomNumberBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isServiceBound) {
                    binding.textView.setText("Random Number :" + mService.getRandomNumber());

                } else {
                    binding.textView.setText("Service not bound");
                }


            }
        });

    }


}