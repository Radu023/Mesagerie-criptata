package com.example.mesagerie_criptata;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class activity_splash extends AppCompatActivity {

    private static int SPLASH_SCREEN_TIME_OUT = 2000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(activity_splash.this, activity_login.class);
                    startActivity(intent);
                    finish();
                }
            }, SPLASH_SCREEN_TIME_OUT);
        }

}