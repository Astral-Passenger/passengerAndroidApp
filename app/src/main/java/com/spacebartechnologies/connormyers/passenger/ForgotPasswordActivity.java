package com.spacebartechnologies.connormyers.passenger;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    private ImageButton mBackNavigationButton;
    private EditText mForgotPasswordEditText;
    private Button mSendEmailButton;
    private String mEmailForgotString;
    private FirebaseAuth auth = FirebaseAuth.getInstance();

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
                Intent intent = new Intent(ForgotPasswordActivity.this, SignIn.class);
                startActivity(intent);
            }
        });

        mSendEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEmailForgotString = mForgotPasswordEditText.getText().toString();
                auth.sendPasswordResetEmail(mEmailForgotString)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()){
                                    AlertDialog.Builder builder =
                                            new AlertDialog.Builder(ForgotPasswordActivity.this, R.style.AppCompatAlertDialogStyle);
                                    builder.setTitle("Password Reset");
                                    builder.setMessage("You have been sent an email with further instructions on how to reset your password.");
                                    builder.setPositiveButton("OK", null);
                                    builder.show();
                                    mForgotPasswordEditText.setText("");
                                }
                                else {
                                    Toast.makeText(getApplicationContext(), "Invalid Email", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
    // password reset email sent


}
