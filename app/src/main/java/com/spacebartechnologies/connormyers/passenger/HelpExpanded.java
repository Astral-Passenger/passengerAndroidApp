package com.spacebartechnologies.connormyers.passenger;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class HelpExpanded extends AppCompatActivity {

    private ImageButton mNavBackButton;
    private TextView mTabBarTitleTextView;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private HelpExpandedRecyclerAdapter mAdapter;
    private ArrayList<String> mQuestions = new ArrayList<String>();
    private ArrayList<String> mAnswers = new ArrayList<String>();
    private String title, questionType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_expanded);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent previousIntent = getIntent();
        title = previousIntent.getStringExtra("questionType");
        questionType = previousIntent.getStringExtra("questionTypeQuery");

        loadHelpData();
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        mNavBackButton = (ImageButton) findViewById(R.id.back_button_help_expanded);
        mTabBarTitleTextView = (TextView) findViewById(R.id.helpExpandedNavTitle);

        mTabBarTitleTextView.setText(title);

        mNavBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HelpExpanded.this, HelpAndSupport.class);
                startActivity(intent);
            }
        });
    }

    private void loadHelpData() {
        FirebaseDatabase.getInstance().getReference().child("help")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user information

                        for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {
                            String questionTypeString = (String) messageSnapshot.child("questionType").getValue();
                            String question = (String) messageSnapshot.child("question").getValue();
                            String answer = (String) messageSnapshot.child("answer").getValue();
                            if (questionTypeString.equals(questionType)) {
                                mQuestions.add(question);
                                mAnswers.add(answer);
                            }
                        }
                        mAdapter = new HelpExpandedRecyclerAdapter(mQuestions, mAnswers, questionType, title);
                        mRecyclerView.setAdapter(mAdapter);
                        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                                mLinearLayoutManager.getOrientation());
                        mRecyclerView.addItemDecoration(dividerItemDecoration);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

}
