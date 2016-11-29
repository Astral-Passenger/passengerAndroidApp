package com.spacebartechnologies.connormyers.passenger;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RewardsHistoryActivity extends AppCompatActivity {

    private ImageButton mBackNavButton;
    private RewardsHistoryAdapter mRewardsHistoryAdapter;
    private RecyclerView mRewardsHistoryRecyclerView;
    private FirebaseUser currentUser;
    private DatabaseReference mDatabase;
    private List<RewardsHistory> mRewardsHistory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewards_history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mRewardsHistory = new ArrayList<RewardsHistory>();

        /*RecyclerView Setup */
        mRewardsHistoryRecyclerView = (RecyclerView) findViewById(R.id.rewards_history_recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRewardsHistoryRecyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRewardsHistoryRecyclerView.getContext(), LinearLayoutManager.VERTICAL);
        mRewardsHistoryRecyclerView.addItemDecoration(dividerItemDecoration);
        mRewardsHistoryAdapter = new RewardsHistoryAdapter(new ArrayList<RewardsHistory>());
        mRewardsHistoryRecyclerView.setAdapter(mRewardsHistoryAdapter);


        mBackNavButton = (ImageButton) findViewById(R.id.back_button_rewards_history);
        mBackNavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RewardsHistoryActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        mDatabase = FirebaseDatabase.getInstance().getReference();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();


        mDatabase.child("users").child(currentUser.getUid()).child("rewardsHistory").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String companyName;
                long pointCost;
                String rewardItem;
                String rewardText;

                /* Gets Rewards History Data*/
                for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {
                    companyName = (String) messageSnapshot.child("companyName").getValue();
                    pointCost = (long) messageSnapshot.child("pointCost").getValue();
                    rewardItem = (String) messageSnapshot.child("rewardItem").getValue();
                    rewardText = (String) messageSnapshot.child("rewardText").getValue();
                    mRewardsHistory.add(new RewardsHistory(companyName, pointCost, rewardItem, rewardText));
                }
                mRewardsHistoryAdapter = new RewardsHistoryAdapter(mRewardsHistory);
                mRewardsHistoryRecyclerView.setAdapter(mRewardsHistoryAdapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
