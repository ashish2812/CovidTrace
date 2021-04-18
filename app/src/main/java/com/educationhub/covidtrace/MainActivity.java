package com.educationhub.covidtrace;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    ImageView splashImage;
    TextView slogan, appName, madeWithLove;
    Animation bottomAnimation, middleAnimation, rightAnimation, topAnimation;
    SharedPreferences sharedPreferences;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        splashImage = findViewById(R.id.splashScreenImage);
        slogan = findViewById(R.id.slogan);
        appName = findViewById(R.id.appName);
        madeWithLove = findViewById(R.id.madeWithLove);

        rightAnimation = AnimationUtils.loadAnimation(this, R.anim.right);
        topAnimation = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        middleAnimation = AnimationUtils.loadAnimation(this, R.anim.middle_animation);
        bottomAnimation = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        splashImage.setAnimation(topAnimation);
        appName.setAnimation(topAnimation);
        slogan.setAnimation(topAnimation);
        madeWithLove.setAnimation(bottomAnimation);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                sharedPreferences = getSharedPreferences("onBoardingScreen", MODE_PRIVATE);
                boolean isFirstTime = sharedPreferences.getBoolean("firstTime", true);
                if(isFirstTime){
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("firstTime", false);
                    editor.commit();
                    Intent intent = new Intent(getApplicationContext(), OnBoarding.class);
                    startActivity(intent);
                    finish();
                }else{
                    Intent intent = new Intent(getApplicationContext(), HomePage.class);
                    startActivity(intent);
                    finish();
                }

            }
        }, 2000);
    }
}