package com.spacebartechnologies.connormyers.passenger;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

public class ForgotPassword extends AppCompatActivity {

    private ImageButton mBackNavigationButton;
    private EditText mForgotPasswordEditText;
    private Button mSendEmailButton;
    private String mEmailForgotString;
    private Firebase ref = new Firebase("https://passenger-app.firebaseio.com/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mBackNavigationButton = (ImageButton) findViewById(R.id.back_button_forgot_password);
        mForgotPasswordEditText = (EditText) findViewById(R.id.forgot_password_input_email);
        mSendEmailButton = (Button) findViewById(R.id.forgot_password_forgot_button);

        mBackNavigationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgotPassword.this, SignIn.class);
                startActivity(intent);
            }
        });

        mSendEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEmailForgotString = mForgotPasswordEditText.getText().toString();

                ref.resetPassword(mEmailForgotString, new Firebase.ResultHandler() {
                    @Override
                    public void onSuccess() {
                        // password reset email sent
                        AlertDialog.Builder builder =
                                new AlertDialog.Builder(ForgotPassword.this, R.style.AppCompatAlertDialogStyle);
                        builder.setTitle("Password Reset");
                        builder.setMessage("You have been sent an email with further instructions on how to reset your password.");
                        builder.setPositiveButton("OK", null);
                        builder.show();
                        mForgotPasswordEditText.setText("");
                    }
                    @Override
                    public void onError(FirebaseError firebaseError) {
                        // error encountered
                    }
                });
            }
        });
    }

}
