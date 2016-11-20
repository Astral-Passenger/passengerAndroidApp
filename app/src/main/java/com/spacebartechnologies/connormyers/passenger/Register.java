package com.spacebartechnologies.connormyers.passenger;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.Map;

public class Register extends AppCompatActivity {

    private ImageButton mRegisterBackButton;
    private EditText mNameEditText, mEmailEditText, mPasswordEditText;
    private Button mRegisterButton, mFacebookButton;
    private String mEmailString, mNameString, mPasswordString;
    private Firebase ref = new Firebase("https://passenger-app.firebaseio.com/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRegisterBackButton = (ImageButton) findViewById(R.id.back_button_register);
        mNameEditText = (EditText) findViewById(R.id.register_name_input);
        mEmailEditText = (EditText) findViewById(R.id.register_input_email);
        mPasswordEditText = (EditText) findViewById(R.id.register_input_password);
        mRegisterButton = (Button) findViewById(R.id.register_button);

        mRegisterBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(Register.this, LoggedOutFirstScreen.class);
                startActivity(registerIntent);
            }
        });

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNameString = mNameEditText.getText().toString();
                mEmailString = mEmailEditText.getText().toString();
                mPasswordString = mPasswordEditText.getText().toString();

                ref.createUser(mEmailString, mPasswordString, new Firebase.ValueResultHandler<Map<String, Object>>() {
                    @Override
                    public void onSuccess(Map<String, Object> result) {
                        System.out.println("Successfully created user account with uid: " + result.get("uid"));
                        Intent intent = new Intent(Register.this, MainActivity.class);
                        startActivity(intent);
                    }
                    @Override
                    public void onError(FirebaseError firebaseError) {
                        // there was an error
                        System.out.println("User was not registered successfully.");
                    }
                });

            }
        });
    }

}
