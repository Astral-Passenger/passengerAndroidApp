package com.spacebartechnologies.connormyers.passenger;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

/**
 * Created by tommyduong23 on 12/8/16.
 */

public class RedeemActivity extends AppCompatActivity {

    private ImageButton mBackNavButton;
    private ImageView mMerchantImg;
    private ImageView mRewardImg;
    private TextView mRewardName;
    private TextView mRewardDescription;
    private TextView mPointCost;
    private Button mRedeemButton;
    private Button mCancelButton;

    private Reward mReward;
    private String merchantImg;
    private String merchantType;
    private ArrayList<Reward> mRewardList;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef;
    private OnlineReward onlineReward;
    private FirebaseUser currentUser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redeem);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mBackNavButton = (ImageButton) findViewById(R.id.back_button_redeem);
        mBackNavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RedeemActivity.this, MerchantRewardsActivity.class);
                intent.putExtra("type", merchantType);
                intent.putParcelableArrayListExtra("rewardList", mRewardList);
                intent.putExtra("merchantImg", merchantImg);
                finish();
                startActivity(intent);
            }
        });

        mMerchantImg = (ImageView) findViewById(R.id.merchant_img);
        mRewardImg = (ImageView) findViewById(R.id.reward_img);
        mRewardName = (TextView) findViewById(R.id.reward_name);
        mRewardDescription = (TextView) findViewById(R.id.reward_description);
        mPointCost = (TextView) findViewById(R.id.point_cost);
        mRedeemButton = (Button) findViewById(R.id.redeem_btn);
        mCancelButton = (Button) findViewById(R.id.cancel_btn);

        Bundle extras = getIntent().getExtras();
        mReward = extras.getParcelable("reward");
        merchantImg = extras.getString("merchantImg");
        merchantType = extras.getString("type");
        mRewardList = extras.getParcelableArrayList("rewardList");

        storageRef = storage.getReferenceFromUrl(merchantImg);
        Glide.with(this).using(new FirebaseImageLoader()).load(storageRef).into(mMerchantImg);

        storageRef = storage.getReferenceFromUrl(mReward.getImgLoc());
        Glide.with(this).using(new FirebaseImageLoader()).load(storageRef).into(mRewardImg);

        mRewardName.setText(mReward.getRewardsName());
        mRewardDescription.setText(mReward.getDescription());
        mPointCost.setText(Long.toString(mReward.getPointCost()));

        mRedeemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            
            }
        });
    }

}
