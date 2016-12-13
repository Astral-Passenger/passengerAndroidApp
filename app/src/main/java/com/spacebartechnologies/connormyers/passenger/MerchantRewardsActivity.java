package com.spacebartechnologies.connormyers.passenger;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by tommyduong23 on 12/6/16.
 */

public class MerchantRewardsActivity extends AppCompatActivity {

    private ImageButton mBackNavButton;
    private RecyclerView mMerchantRewardsRecyclerView;
    private MerchantRewardsAdapter mMerchantRewardsAdapter;
    private ArrayList<Reward> mRewardList;
    private String merchantType;
    private String merchantImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_rewards);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle extras = getIntent().getExtras();
        mRewardList = extras.getParcelableArrayList("rewardList");
        merchantType = extras.getString("type");
        merchantImg = extras.getString("merchantImg");

        mMerchantRewardsRecyclerView = (RecyclerView) findViewById(R.id.merchant_rewards_recycler_view);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        mMerchantRewardsRecyclerView.setLayoutManager(layoutManager);
        mMerchantRewardsRecyclerView.addItemDecoration(new GridSpacingItemDecoration(2, 7, false));

        Collections.sort(mRewardList);
        mMerchantRewardsAdapter = new MerchantRewardsAdapter(mRewardList, merchantImg, merchantType);
        mMerchantRewardsRecyclerView.setAdapter(mMerchantRewardsAdapter);


        mBackNavButton = (ImageButton) findViewById(R.id.back_button_merchant_rewards);
        mBackNavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MerchantRewardsActivity.this, MerchantsActivity.class);
                intent.putExtra("type", merchantType);
                finish();
                startActivity(intent);
            }
        });

    }

}

