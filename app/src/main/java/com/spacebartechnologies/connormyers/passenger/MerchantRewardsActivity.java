package com.spacebartechnologies.connormyers.passenger;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

/**
 * Created by tommyduong23 on 12/6/16.
 */

public class MerchantRewardsActivity extends AppCompatActivity {

    private ImageButton mBackNavButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_rewards);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mBackNavButton = (ImageButton) findViewById(R.id.back_button_merchant_rewards);
        mBackNavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MerchantRewardsActivity.this, MerchantsActivity.class);
                startActivity(intent);
            }
        });

    }

}

