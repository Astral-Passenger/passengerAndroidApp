package com.spacebartechnologies.connormyers.passenger;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.util.LruCache;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

public class LoggedOutFirstScreen extends AppCompatActivity {

    private Button mSignInButton, mRegisterButton;
    private RelativeLayout mBackgroundLayout;
    private AnimationDrawable backgroundAnimation;
    private LruCache<String, Bitmap> mMemoryCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_out_first_screen);

        mBackgroundLayout = (RelativeLayout) findViewById(R.id.loggedOutLayout);
        //startBackgroundAnimation();

        mSignInButton = (Button) findViewById(R.id.signInButton);
        mRegisterButton = (Button) findViewById(R.id.registerButton);

        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = new Intent(LoggedOutFirstScreen.this, SignIn.class);
                startActivity(signInIntent);
            }
        });

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LoggedOutFirstScreen.this, Register.class);
                startActivity(registerIntent);
            }
        });
    }

    private void startBackgroundAnimation() {

        backgroundAnimation = new AnimationDrawable();

        BitmapDrawable newFrame0 = (BitmapDrawable) getResources().getDrawable(R.drawable.tmp_0);
        BitmapDrawable newFrame1 = (BitmapDrawable) getResources().getDrawable(R.drawable.tmp_1);
        BitmapDrawable newFrame2 = (BitmapDrawable) getResources().getDrawable(R.drawable.tmp_2);
        BitmapDrawable newFrame3 = (BitmapDrawable) getResources().getDrawable(R.drawable.tmp_3);
        BitmapDrawable newFrame4 = (BitmapDrawable) getResources().getDrawable(R.drawable.tmp_4);
        BitmapDrawable newFrame5 = (BitmapDrawable) getResources().getDrawable(R.drawable.tmp_5);
        BitmapDrawable newFrame6 = (BitmapDrawable) getResources().getDrawable(R.drawable.tmp_6);
        BitmapDrawable newFrame7 = (BitmapDrawable) getResources().getDrawable(R.drawable.tmp_7);
        BitmapDrawable newFrame8 = (BitmapDrawable) getResources().getDrawable(R.drawable.tmp_8);
        BitmapDrawable newFrame9 = (BitmapDrawable) getResources().getDrawable(R.drawable.tmp_9);

        backgroundAnimation.addFrame(newFrame0,1);
        backgroundAnimation.addFrame(newFrame1,1);
        backgroundAnimation.addFrame(newFrame2,1);
        backgroundAnimation.addFrame(newFrame3,1);
        backgroundAnimation.addFrame(newFrame4,1);
        backgroundAnimation.addFrame(newFrame5,1);
        backgroundAnimation.addFrame(newFrame6,1);
        backgroundAnimation.addFrame(newFrame7,1);
        backgroundAnimation.addFrame(newFrame8,1);
        backgroundAnimation.addFrame(newFrame9,1);

        mBackgroundLayout.setBackground(backgroundAnimation);

        final Handler backgroundHandler = new Handler();
        backgroundHandler.postDelayed(new Runnable() {

            public void run() {

                backgroundAnimation.start();

            }
        }, 10);

    };

}
