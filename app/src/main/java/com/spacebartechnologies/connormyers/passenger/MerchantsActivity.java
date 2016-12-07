package com.spacebartechnologies.connormyers.passenger;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by tommyduong23 on 12/2/16.
 */

public class MerchantsActivity extends AppCompatActivity {

    private ImageButton mBackNavButton;
    private RecyclerView mMerchantsRecyclerView;
    private LocalMerchantAdapter mLocalMerchantAdapter;
    private ArrayList<LocalMerchant> mLocalMerchantList;
    private ArrayList<OnlineMerchant> mOnlineMerchantList;
    private OnlineMerchantAdapter mOnlineMerchantAdapter;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    final long ONE_MEGABYTE = 1024 * 1024;
    private String merchantType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchants);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle extras = getIntent().getExtras();
        merchantType = extras.getString("type");

        mMerchantsRecyclerView = (RecyclerView) findViewById(R.id.merchants_recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mMerchantsRecyclerView.setLayoutManager(layoutManager);

        if (merchantType.equals("localMerchants")) {
            mLocalMerchantList = new ArrayList<LocalMerchant>();
            mLocalMerchantAdapter = new LocalMerchantAdapter(mLocalMerchantList);
            mMerchantsRecyclerView.setAdapter(mLocalMerchantAdapter);
        }
        else {
            mOnlineMerchantList = new ArrayList<OnlineMerchant>();
            mOnlineMerchantAdapter = new OnlineMerchantAdapter(mOnlineMerchantList);
            mMerchantsRecyclerView.setAdapter(mOnlineMerchantAdapter);
        }



        mBackNavButton = (ImageButton) findViewById(R.id.back_button_merchants);
        mBackNavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MerchantsActivity.this, RewardsActivity.class);
                startActivity(intent);
            }
        });
        mDatabase.child(merchantType).addListenerForSingleValueEvent(new ValueEventListener() {

            LocalMerchant localMerchant;
            OnlineMerchant onlineMerchant;
            String companyName;
            String city;
            String description;
            String streets;
            String email;
            String imgLoc;
            double latitude;
            double longitude;
            ArrayList<Reward> rewardList;
            ArrayList<OnlineReward> onlineRewardList;

            ArrayList<HashMap> rewardMapList;
            Reward reward;
            OnlineReward onlineReward;
            String rewardCompanyName;
            String rewardImgLoc;
            long rewardPointCost;
            double rewardAmountSaved;
            String rewardDescription;
            double rewardPrice;
            String rewardsName;
            String rewardCoupon;
            int count;
            StorageReference storageRef;

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {
                    description = (String) messageSnapshot.child("companyDescription").getValue();
                    companyName = (String) messageSnapshot.child("companyName").getValue();
                    email = (String) messageSnapshot.child("email").getValue();
                    imgLoc = (String) messageSnapshot.child("imageLocation").getValue();
                    if (merchantType.equals("localMerchants")) {
                        city = (String) messageSnapshot.child("city").getValue();
                        streets = (String) messageSnapshot.child("crossStreets").getValue();
                        latitude = (double) messageSnapshot.child("latitude").getValue();
                        longitude = (double) messageSnapshot.child("longitude").getValue();
                    }
                    rewardList = new ArrayList<Reward>();

                    rewardMapList = (ArrayList) messageSnapshot.child("rewards").getValue();
                    for (HashMap r: rewardMapList) {
                        rewardCompanyName = (String) r.get("companyName");
                        rewardImgLoc = (String) r.get("imageLocation");
                        rewardPointCost = (long) r.get("pointCost");
                        rewardAmountSaved = ((Number)r.get("rewardAmountSaved")).doubleValue();
                        rewardDescription = (String) r.get("rewardDescription");
                        rewardPrice = ((Number)r.get("rewardPrice")).doubleValue();
                        rewardsName = (String) r.get("rewardsName");
                        if (merchantType.equals("onlineMerchants")) {
                            rewardCoupon = (String) r.get("couponCode");
                            onlineReward = new OnlineReward(rewardCompanyName, rewardCoupon, imgLoc, rewardPointCost, rewardAmountSaved, rewardDescription, rewardPrice, rewardsName);
                            rewardList.add(onlineReward);
                        }
                        else {
                            reward = new Reward(rewardCompanyName, rewardImgLoc, rewardPointCost, rewardAmountSaved, rewardDescription, rewardPrice, rewardsName);
                            rewardList.add(reward);
                        }

                    }

                    if (merchantType.equals("localMerchants")) {
                        localMerchant = new LocalMerchant(city, description, companyName, streets, email, imgLoc, latitude, longitude, rewardList);
                        mLocalMerchantList.add(localMerchant);
                    }
                    else {
                        onlineMerchant = new OnlineMerchant(description, companyName, email, imgLoc, rewardList);
                        mOnlineMerchantList.add(onlineMerchant);
                    }

                }
                if (merchantType.equals("localMerchants")) {
                    mLocalMerchantAdapter = new LocalMerchantAdapter(mLocalMerchantList);
                    mMerchantsRecyclerView.setAdapter(mLocalMerchantAdapter);
                }
                else {
                    mOnlineMerchantAdapter = new OnlineMerchantAdapter(mOnlineMerchantList);
                    mMerchantsRecyclerView.setAdapter(mOnlineMerchantAdapter);
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
