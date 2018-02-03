package com.androidchallenge.searchpic.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.androidchallenge.searchpic.R;

/**
 * Created by waghup on 2/3/18.
 */

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        Handler handler = new Handler();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
                Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(mainIntent);
            }
        },2000);


//        Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//                handler.removeCallbacks(this);
//                finish();
//                Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
//                startActivity(mainIntent);
//            }
//        };
//        handler.postDelayed(runnable, 2000);

    }
}
