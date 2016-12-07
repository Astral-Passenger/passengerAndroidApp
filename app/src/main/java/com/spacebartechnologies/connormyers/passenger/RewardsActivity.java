package com.spacebartechnologies.connormyers.passenger;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

public class RewardsActivity extends AppCompatActivity {

    private ImageButton mBackNavButton;

    private ImageView mOnlineImageView;
    private ImageView mLocalImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewards);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mOnlineImageView = (ImageView) findViewById(R.id.online_img);
        mLocalImageView = (ImageView) findViewById(R.id.local_img);

        mBackNavButton = (ImageButton) findViewById(R.id.back_button_rewards);
        mBackNavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RewardsActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        mOnlineImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RewardsActivity.this, MerchantsActivity.class);
                intent.putExtra("type", "onlineMerchants");
                startActivity(intent);

            }
        });

        mLocalImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RewardsActivity.this, MerchantsActivity.class);
                intent.putExtra("type", "localMerchants");
                startActivity(intent);
            }
        });

    }

}
