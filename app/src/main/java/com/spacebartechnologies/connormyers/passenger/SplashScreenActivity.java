package com.spacebartechnologies.connormyers.passenger;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.util.LruCache;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class SplashScreenActivity extends AppCompatActivity {

    private LruCache<String, Bitmap> mMemoryCache;
    private FirebaseUser currentUser;
    private FirebaseStorage storage = FirebaseStorage.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        final Handler splashScreenHandler = new Handler();
        splashScreenHandler.postDelayed(new Runnable() {
            public void run() {

                currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser != null) {
                    // user authenticated
                    loadUserData();
                } else {
                    // no user authenticated
                    Log.e("Auth State", "The user is not logged in");
                    Intent intent = new Intent(getApplicationContext(), LoggedOutFirstScreen.class);
                    startActivity(intent);
                }
            }
        }, 5000);

    }

    private void loadUserData() {
        Context context = this;
        final SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user information
                        final String name = (String) dataSnapshot.child("name").getValue();
                        final Double currentPoints = (Double) dataSnapshot.child("currentPoints").getValue();
                        final int currentPointsInt = (int) Math.floor(currentPoints);
                        final Double totalPoints = (Double) dataSnapshot.child("totalPoints").getValue();
                        final int totalPointsInt = (int) Math.floor(totalPoints);
                        final long rewardsReceived = (long) dataSnapshot.child("rewardsReceived").getValue();
                        final Double distanceTraveled = (Double) dataSnapshot.child("distanceTraveled").getValue();
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
                                editor.putString("imageLocation", profileImageBase64);
                                editor.putInt("totalPoints",totalPointsInt);
                                editor.putLong("rewardsReceived",rewardsReceived);
                                editor.putLong("distanceTraveled",distanceTraveledInt);
                                editor.putLong("timeSpentDriving", timeSpentDriving);
                                editor.commit();
                                Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
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
    };

}
