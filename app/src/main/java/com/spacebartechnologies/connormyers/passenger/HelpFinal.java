package com.spacebartechnologies.connormyers.passenger;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class HelpFinal extends AppCompatActivity {

    private ImageButton mNavBackButton;
    private String questionType,title,mQuestion,mAnswer;
    private TextView mQuestionView, mAnswerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_final);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent previousIntent = getIntent();
        questionType = previousIntent.getStringExtra("questionTypeQuery");
        title = previousIntent.getStringExtra("questionType");
        mQuestion = previousIntent.getStringExtra("question");
        mAnswer = previousIntent.getStringExtra("answer");

        mNavBackButton = (ImageButton) findViewById(R.id.back_button_help_final);
        mQuestionView = (TextView) findViewById(R.id.questionFinalTextView);
        mAnswerView = (TextView) findViewById(R.id.answerFinalTextView);

        mQuestionView.setText(mQuestion);
        mAnswerView.setText(mAnswer);

        mNavBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HelpFinal.this, HelpExpanded.class);
                intent.putExtra("questionTypeQuery", questionType);
                intent.putExtra("questionType", title);
                startActivity(intent);
            }
        });
    }

}
