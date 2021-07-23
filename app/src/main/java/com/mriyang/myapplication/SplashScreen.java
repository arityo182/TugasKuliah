package com.mriyang.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.mriyang.myapplication.login.LoginActivityBaru;

public class SplashScreen extends AppCompatActivity {

    Animation logoanim;
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        logoanim = AnimationUtils.loadAnimation(this,R.anim.logo_anim);

        image = findViewById(R.id.logo);
        image.setAnimation(logoanim);


        int splashscreen = 3000;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this, LoginActivityBaru.class);
                SplashScreen.this.startActivity(intent);
                SplashScreen.this.finish();
            }
        },splashscreen);
    }
}