package com.spacebartechnologies.connormyers.passenger;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class SignIn extends AppCompatActivity {

    private ImageButton mSignInBackButton;
    private EditText mEmailEditText, mPasswordEditText;
    private Button mSignInButton, mFacebookButton, mForgotPasswordButton;
    private String mEmailString, mPasswordString;
    private FirebaseAuth mAuth;
    private FirebaseStorage storage = FirebaseStorage.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        mAuth = FirebaseAuth.getInstance();
        mSignInBackButton = (ImageButton) findViewById(R.id.back_button_sign_in);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        mEmailEditText = (EditText) findViewById(R.id.sign_in_input_email);
        mPasswordEditText = (EditText) findViewById(R.id.sign_in_input_password);
        mSignInButton = (Button) findViewById(R.id.sign_in_button);
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

                mAuth.signInWithEmailAndPassword(mEmailString,mPasswordString).addOnCompleteListener(SignIn.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("TAG", "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w("TAG", "signInWithEmail:failed", task.getException());
                            Toast.makeText(SignIn.this, "Authorization failed",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                            Context context = SignIn.this;
                            final SharedPreferences sharedPref = context.getSharedPreferences(
                                    getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                            FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid())
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            // Get user information
                                            final String name = (String) dataSnapshot.child("name").getValue();
                                            Double currentPoints = ((Number)dataSnapshot.child("currentPoints").getValue()).doubleValue();
                                            final int currentPointsInt = (int) Math.floor(currentPoints);
                                            final Double totalPoints = ((Number) dataSnapshot.child("totalPoints").getValue()).doubleValue();
                                            final int totalPointsInt = (int) Math.floor(totalPoints);
                                            final long rewardsReceived = (long) dataSnapshot.child("rewardsReceived").getValue();
                                            final Double distanceTraveled = ((Number) dataSnapshot.child("distanceTraveled").getValue()).doubleValue();
                                            final long distanceTraveledInt = (long) Math.floor(distanceTraveled);
                                            final long timeSpentDriving = (long) dataSnapshot.child("timeSpentDriving").getValue();
                                            final String imageLocation = (String) dataSnapshot.child("imageLocation").getValue();
                                            StorageReference storageRef = storage.getReferenceFromUrl(imageLocation);

                                            final long ONE_MEGABYTE = 3000 * 3000;
                                            storageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                                @Override
                                                public void onSuccess(byte[] bytes) {
                                                    String profileImageBase64 = Base64.encodeToString(bytes, Base64.DEFAULT);
                                                    SharedPreferences.Editor editor = sharedPref.edit();
                                                    editor.putString("name", name);
                                                    editor.putInt("currentPoints", currentPointsInt);
                                                    editor.putInt("totalPoints",totalPointsInt);
                                                    editor.putLong("rewardsReceived",rewardsReceived);
                                                    editor.putLong("distanceTraveled",distanceTraveledInt);
                                                    editor.putLong("timeSpentDriving", timeSpentDriving);
                                                    editor.putString("imageLocation", profileImageBase64);
                                                    editor.commit();

                                                    Intent intent = new Intent(SignIn.this, MainActivity.class);
                                                    startActivity(intent);
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception exception) {
                                                    // Handle any errors
                                                    Log.e("ERROR", "" + exception);
                                                }
                                            });

                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                        }

                        // ...
                    }
                });
            }
        });

        mForgotPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignIn.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });
    }

}
