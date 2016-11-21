package com.spacebartechnologies.connormyers.passenger;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

public class HelpAndSupport extends AppCompatActivity {

    private ImageButton mNavBackButton;
    private RelativeLayout mAccountButton, mHowButton, mWhereButton, mRewardsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_and_support);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mNavBackButton = (ImageButton) findViewById(R.id.back_button_help_support);
        mAccountButton = (RelativeLayout) findViewById(R.id.account_button);
        mHowButton = (RelativeLayout) findViewById(R.id.how_button);
        mWhereButton = (RelativeLayout) findViewById(R.id.where_button);
        mRewardsButton = (RelativeLayout) findViewById(R.id.rewards_button);

        mNavBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        mAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HelpAndSupport.this, HelpExpanded.class);
                intent.putExtra("questionType", "Account");
                intent.putExtra("questionTypeQuery", "Account");
                startActivity(intent);
            }
        });

        mHowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HelpAndSupport.this, HelpExpanded.class);
                intent.putExtra("questionType", "How to use Passenger");
                intent.putExtra("questionTypeQuery", "How");
                startActivity(intent);
            }
        });

        mWhereButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HelpAndSupport.this, HelpExpanded.class);
                intent.putExtra("questionType", "Where to use Passenger");
                intent.putExtra("questionTypeQuery", "Where");
                startActivity(intent);
            }
        });

        mRewardsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HelpAndSupport.this, HelpExpanded.class);
                intent.putExtra("questionType", "Rewards");
                intent.putExtra("questionTypeQuery", "Rewards");
                startActivity(intent);
            }
        });
    }

}
