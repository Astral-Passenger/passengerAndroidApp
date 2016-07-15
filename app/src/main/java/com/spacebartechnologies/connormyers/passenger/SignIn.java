package com.spacebartechnologies.connormyers.passenger;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

public class SignIn extends AppCompatActivity {

    private ImageButton mSignInBackButton;
    private EditText mEmailEditText, mPasswordEditText;
    private Button mSignInButton, mFacebookButton, mForgotPasswordButton;
    private String mEmailString, mPasswordString;
    private Firebase ref = new Firebase("https://passenger-app.firebaseio.com/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mSignInBackButton = (ImageButton) findViewById(R.id.back_button_sign_in);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        mEmailEditText = (EditText) findViewById(R.id.sign_in_input_email);
        mPasswordEditText = (EditText) findViewById(R.id.sign_in_input_password);
        mSignInButton = (Button) findViewById(R.id.sign_in_button);
        mFacebookButton = (Button) findViewById(R.id.sign_in_facebook_button);
        mForgotPasswordButton = (Button) findViewById(R.id.forgot_password_button);

        mSignInBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = new Intent(SignIn.this, LoggedOutFirstScreen.class);
                startActivity(signInIntent);
            }
        });

        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEmailString = mEmailEditText.getText().toString();
                mPasswordString = mPasswordEditText.getText().toString();

                Firebase.AuthResultHandler authResultHandler = new Firebase.AuthResultHandler() {
                    @Override
                    public void onAuthenticated(AuthData authData) {
                        // Authenticated successfully with payload authData
                        Intent intent = new Intent(SignIn.this, MainActivity.class);
                        startActivity(intent);
                    }
                    @Override
                    public void onAuthenticationError(FirebaseError firebaseError) {
                        // Authenticated failed with error firebaseError
                        Log.e("Auth", "There was an error logging the user in.");
                    }
                };

                ref.authWithPassword(mEmailString, mPasswordString, authResultHandler);


            }
        });

        mFacebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignIn.this, MainActivity.class);
                startActivity(intent);
            }
        });

        mForgotPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignIn.this, ForgotPassword.class);
                startActivity(intent);
            }
        });
    }

}
