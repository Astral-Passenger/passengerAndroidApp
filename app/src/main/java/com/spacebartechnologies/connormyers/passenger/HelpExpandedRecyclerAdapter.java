package com.spacebartechnologies.connormyers.passenger;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import static android.R.id.list;

/**
 * Created by connormyers on 11/19/16.
 */

public class HelpExpandedRecyclerAdapter extends RecyclerView.Adapter<HelpExpandedRecyclerAdapter.QuestionHolder> {

    private ArrayList<String> mQuestions;
    private ArrayList<String> mAnswers;
    private String mQuestionType, mTitle;

    public HelpExpandedRecyclerAdapter(ArrayList<String> questions, ArrayList<String> answers, String questionType, String title) {
        mQuestions = questions;
        mAnswers = answers;
        mQuestionType = questionType;
        mTitle = title;
    }

    @Override
    public HelpExpandedRecyclerAdapter.QuestionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.help_expanded_recycler_view_row, parent, false);
        return new QuestionHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(HelpExpandedRecyclerAdapter.QuestionHolder holder, int position) {
        holder.bindQuestions(mQuestions.get(position), mAnswers.get(position), mQuestionType, mTitle);
    }

    @Override
    public int getItemCount() {
        return mQuestions.size();
    }

    public static class QuestionHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //2
        private TextView mQuestionTextView;
        private String mQuestion, mAnswer, mQuestionType, mTitle;

        //3
        private static final String PHOTO_KEY = "PHOTO";

        //4
        public QuestionHolder(View v) {
            super(v);

            mQuestionTextView = (TextView) v.findViewById(R.id.question_text_view);
            v.setOnClickListener(this);
        }

        //5
        @Override
        public void onClick(View v) {
            Context context = mQuestionTextView.getContext();
            Intent intent = new Intent(context, HelpFinal.class);
            intent.putExtra("questionTypeQuery", mQuestionType);
            intent.putExtra("question", mQuestion);
            intent.putExtra("answer", mAnswer);
            intent.putExtra("questionType", mTitle);
            Log.e("Question", mQuestion);
            context.startActivity(intent);
        }

        public void bindQuestions(String question, String answer, String questionType, String title) {
            mQuestionTextView.setText(question);
            mQuestion = question;
            mQuestionType = questionType;
            mAnswer = answer;
            mTitle = title;
        }
    }
}
