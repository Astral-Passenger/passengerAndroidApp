package com.spacebartechnologies.connormyers.passenger;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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

public class PointsHistoryActivity extends AppCompatActivity {

    private ImageButton mBackNavButton;
    private PointsHistoryAdapter mPointsHistoryAdapter;
    private RecyclerView mPointsHistoryRecyclerView;
    private FirebaseUser currentUser;
    private DatabaseReference mDatabase;
    private List<PointsHistoryGroup> mPointHistoryGroupList;
    private List<PointsHistory> mPointHistory;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_points_history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mContext = this;
        mPointHistory = new ArrayList<PointsHistory>();
        mPointHistoryGroupList = new ArrayList<PointsHistoryGroup>();

        /* RecyclerView setup */
        mPointsHistoryRecyclerView = (RecyclerView) findViewById(R.id.points_history_recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mPointsHistoryRecyclerView.setLayoutManager(layoutManager);
        mPointsHistoryAdapter = new PointsHistoryAdapter(mPointHistoryGroupList, this);
        mPointsHistoryRecyclerView.setAdapter(mPointsHistoryAdapter);
        
        mBackNavButton = (ImageButton) findViewById(R.id.back_button_points_history);
        mBackNavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PointsHistoryActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        mDatabase = FirebaseDatabase.getInstance().getReference();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        mDatabase.child("users").child(currentUser.getUid()).child("pointsHistory").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String createdAt = "";
                double distanceTraveled;
                double points;
                int pointsGenerated;

                /* Get Points History Data */
                for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {
                    createdAt = (String) messageSnapshot.child("createdAt").getValue();
                    distanceTraveled = (double) messageSnapshot.child("distanceTraveled").getValue();
                    points = (double) messageSnapshot.child("pointsGenerated").getValue();

                    pointsGenerated = (int) points;

                    mPointHistory.add(new PointsHistory(createdAt, distanceTraveled, pointsGenerated));
                }

                formatPointsHistoryGroup();

                mPointsHistoryAdapter = new PointsHistoryAdapter(mPointHistoryGroupList, mContext);
                mPointsHistoryRecyclerView.setAdapter(mPointsHistoryAdapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void formatPointsHistoryGroup () {

        PointsHistoryGroup pointsHistoryGroup;
        String date = "";
        List<PointsHistory> tempPointsHistory = new ArrayList<PointsHistory>();
        List<PointsHistoryGroup> tempPointsHistoryGroupList = new ArrayList<PointsHistoryGroup>();

        date = mPointHistory.get(0).getDate();

        for (PointsHistory ph : mPointHistory) {
            if (date.equals(ph.getDate())) {
                tempPointsHistory.add(ph);
            }
            else {
                pointsHistoryGroup = new PointsHistoryGroup(date, tempPointsHistory);
                mPointHistoryGroupList.add(pointsHistoryGroup);
                date = ph.getDate();
                tempPointsHistory = new ArrayList<PointsHistory>();
                tempPointsHistory.add(ph);

            }
        }
        pointsHistoryGroup = new PointsHistoryGroup(date, tempPointsHistory);
        mPointHistoryGroupList.add(pointsHistoryGroup);

        for (int i = mPointHistoryGroupList.size() - 1; i >= 0; i--) {
            tempPointsHistoryGroupList.add(mPointHistoryGroupList.get(i));

        }
        mPointHistoryGroupList = tempPointsHistoryGroupList;
    }

}
