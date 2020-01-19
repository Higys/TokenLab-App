package com.example.volleybasicexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen extends AppCompatActivity {

    private final int splashTime = 1800;
    Handler handle;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        handle = new Handler();

        handle.postDelayed(new Runnable() {
            @Override
            public void run() {
                goToMainActivity();
            }
        }, splashTime);

    }

    private void goToMainActivity(){
        intent = new Intent(SplashScreen.this, MainActivity.class);
        startActivity(intent);
    }
}
