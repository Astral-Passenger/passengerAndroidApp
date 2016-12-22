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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

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
    private ArrayList<MonthlyTransaction> mTransactionList;
    private String merchantKey;



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
                intent.putParcelableArrayListExtra("monthlyTransactions", mTransactionList);
                intent.putExtra("merchantKey", merchantKey);
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
        mTransactionList = extras.getParcelableArrayList("monthlyTransactions");
        Log.d("size", Integer.toString(mTransactionList.size()));
        for (MonthlyTransaction m : mTransactionList) {
            Log.d("email", m.getUserEmail() + " " + m.getDateRecorded());
        }
        merchantKey = extras.getString("merchantKey");
        Log.d("merchantKey", merchantKey);

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
                if (currentPoints >= mReward.getPointCost()) {
                    showRedeemAlertDialog();
                }
                else {
                    showInsufficientPointsDialog();
                }
            
            }
        });
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RedeemActivity.this, MerchantRewardsActivity.class);
                intent.putExtra("type", merchantType);
                intent.putParcelableArrayListExtra("rewardList", mRewardList);
                intent.putExtra("merchantImg", merchantImg);
                intent.putParcelableArrayListExtra("monthlyTransactions", mTransactionList);
                intent.putExtra("merchantKey", merchantKey);
                finish();
                startActivity(intent);
            }
        });
    }
    private void showRedeemAlertDialog() {
        LayoutInflater li = LayoutInflater.from(this);
        View dialogView = li.inflate(R.layout.redeem_dialog, null);
        TextView redeemTitle = (TextView) dialogView.findViewById(R.id.redeem_title);
        TextView redeemDialog = (TextView)dialogView.findViewById(R.id.redeem_text);
        String completeRedeemText = getString(R.string.redeem_dialog);
        completeRedeemText = completeRedeemText.substring(0, 33) + mReward.getCompanyName()+ " " + completeRedeemText.substring(33, completeRedeemText.length())+ " " + mReward.getRewardsName();
        redeemTitle.setText("SHOW TO TELLER");
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
    private void showInsufficientPointsDialog() {
        LayoutInflater li = LayoutInflater.from(this);
        View dialogView = li.inflate(R.layout.redeem_dialog, null);
        TextView redeemTitle = (TextView) dialogView.findViewById(R.id.redeem_title);
        TextView redeemDialog = (TextView)dialogView.findViewById(R.id.redeem_text);
        redeemTitle.setText("NOT ENOUGH POINTS");
        redeemDialog.setText("You currently have " + Long.toString(currentPoints));
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(dialogView);
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    private void redeem() {
        double newPoints;
        int newPointsInt;
        final RewardsHistory rewardsHistory;
        final String dateRecorded = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(Calendar.getInstance().getTime());
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
               // FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid()).child("rewardsHistory").setValue(rewardsHistoryList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //Add to monthly transactions to localmerhant and localmerchantowners
        FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid()).child("email").addListenerForSingleValueEvent(new ValueEventListener() {
            String email;
            MonthlyTransaction monthlyTransaction;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                email = (String) dataSnapshot.getValue();
                monthlyTransaction = new MonthlyTransaction(dateRecorded, mReward.getPointCost(), mReward.getDescription(), mReward.getRewardsName(), mReward.getPrice(), email);
                mTransactionList.add(monthlyTransaction);
                FirebaseDatabase.getInstance().getReference().child(merchantType).child(merchantKey).child("monthlyTransactions").setValue(mTransactionList);
                if (merchantType.equals("localMerchants")) {
                    FirebaseDatabase.getInstance().getReference().child("localMerchantOwners").orderByChild("companyName").equalTo(mReward.getCompanyName()).addListenerForSingleValueEvent(new ValueEventListener() {
                        ArrayList<HashMap> transactionMapList;
                        ArrayList<MonthlyTransaction> transactionList;
                        String dateRecorded;
                        long pointCost;
                        String rewardDescription;
                        String rewardItem;
                        double rewardPrice;
                        String userEmail;
                        String key;
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot messageSnapShot: dataSnapshot.getChildren()) {
                                key = messageSnapShot.getKey();
                                transactionMapList = (ArrayList) messageSnapShot.child("monthlyTransactions").getValue();
                            }
                            transactionList = new ArrayList<MonthlyTransaction>();
                            for (HashMap transaction: transactionMapList) {
                                dateRecorded = (String) transaction.get("dateRecorded");
                                pointCost = (long) transaction.get("pointCost");
                                rewardDescription = (String) transaction.get("rewardDescription");
                                rewardItem = (String) transaction.get("rewardItem");
                                rewardPrice = ((Number) transaction.get("rewardPrice")).doubleValue();
                                userEmail = (String) transaction.get("userEmail");
                                transactionList.add(new MonthlyTransaction(dateRecorded, pointCost, rewardDescription, rewardItem, rewardPrice, userEmail));
                            }
                            transactionList.add(monthlyTransaction);
                            Log.d("key", key);
                            FirebaseDatabase.getInstance().getReference().child("localMerchantOwners").child(key).child("monthlyTransactions").setValue(transactionList);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}
