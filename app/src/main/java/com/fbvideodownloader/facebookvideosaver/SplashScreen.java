package com.fbvideodownloader.facebookvideosaver;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {

    private static int SPLASH_SCREEN = 2500;

    // variables
    Animation fadeAnim, bottomAnim;
    ImageView appIcon;
    TextView vdff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        fadeAnim = AnimationUtils.loadAnimation(this, R.anim.fade_anim);
        appIcon = findViewById(R.id.fb_icon);
        appIcon.setAnimation(fadeAnim);

        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_to_top);
        vdff = findViewById(R.id.vdff);
        vdff.setAnimation(bottomAnim);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        },SPLASH_SCREEN);

    }
}
