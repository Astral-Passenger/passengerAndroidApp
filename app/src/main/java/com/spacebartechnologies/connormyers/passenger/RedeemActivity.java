package com.spacebartechnologies.connormyers.passenger;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.client.Firebase;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
    private SharedPreferences sharedPref;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private int currentPoints;



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
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        sharedPref = this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        currentPoints = sharedPref.getInt("currentPoints", 0);

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
                showRedeemAlertDialog();
            
            }
        });
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RedeemActivity.this, MerchantRewardsActivity.class);
                intent.putExtra("type", merchantType);
                intent.putParcelableArrayListExtra("rewardList", mRewardList);
                intent.putExtra("merchantImg", merchantImg);
                finish();
                startActivity(intent);
            }
        });
    }
    private void showRedeemAlertDialog() {
        LayoutInflater li = LayoutInflater.from(this);
        View dialogView = li.inflate(R.layout.redeem_dialog, null);
        TextView redeemDialog = (TextView)dialogView.findViewById(R.id.redeem_text);
        String completeRedeemText = redeemDialog.getText().toString();
        completeRedeemText = completeRedeemText.substring(0, 33) + mReward.getCompanyName()+ " " + completeRedeemText.substring(33, completeRedeemText.length())+ " " + mReward.getRewardsName();
        redeemDialog.setText(completeRedeemText);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(dialogView);
        alertDialogBuilder.setPositiveButton("Redeem", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                redeem();

            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).setCancelable(false);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
       // alertDialog.getWindow().setLayout(1000, -2);
    }
    private void redeem() {
        double newPoints;
        int newPointsInt;
        final RewardsHistory rewardsHistory;
        rewardsHistory = new RewardsHistory(mReward.getCompanyName(), mReward.getPointCost(), mReward.getRewardsName(), mReward.getDescription());
        //Set new user points
        newPoints = currentPoints - mReward.getPointCost();
        newPointsInt = (int)Math.floor(newPoints);
        FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid()).child("currentPoints").setValue(newPoints);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("currentPoints", newPointsInt);
        editor.commit();
        //Add to reward history
        FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid()).child("rewardsHistory").addListenerForSingleValueEvent(new ValueEventListener() {
            String companyName;
            long pointCost;
            String rewardItem;
            String rewardText;
            ArrayList<RewardsHistory> rewardsHistoryList = new ArrayList<>();
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {
                    companyName = (String) messageSnapshot.child("companyName").getValue();
                    pointCost = (long) messageSnapshot.child("pointCost").getValue();
                    rewardItem = (String) messageSnapshot.child("rewardItem").getValue();
                    rewardText = (String) messageSnapshot.child("rewardText").getValue();
                    rewardsHistoryList.add(new RewardsHistory(companyName, pointCost, rewardItem, rewardText));
                }
                rewardsHistoryList.add(rewardsHistory);
                FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid()).child("rewardsHistory").setValue(rewardsHistoryList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //Add to monthly transactions to localmerhant and localmerchantowners


    }

}
