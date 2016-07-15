package com.spacebartechnologies.connormyers.passenger;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.util.LruCache;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class SplashScreen extends AppCompatActivity {

    private LruCache<String, Bitmap> mMemoryCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Firebase.setAndroidContext(this);
        FirebaseFuncs firebase = new FirebaseFuncs();
        final AuthData authData = firebase.ref.getAuth();

        final Handler splashScreenHandler = new Handler();
        splashScreenHandler.postDelayed(new Runnable() {

            public void run() {

                if (authData != null) {
                    // user authenticated
                    Log.e("Auth State", "The user is logged in");
                    Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    // no user authenticated
                    Log.e("Auth State", "The user is not logged in");
                    Intent intent = new Intent(getApplicationContext(), LoggedOutFirstScreen.class);
                    startActivity(intent);
                }

            }
        }, 5000);
        loadUserData();
    }

    private void loadUserData() {
        Context context = this;
        final SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        FirebaseFuncs firebase = new FirebaseFuncs();
        final AuthData authData = firebase.ref.getAuth();
        String userID = authData.getUid();
        Firebase userExactRef = firebase.usersRef.child("" + userID);

        userExactRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = (String) dataSnapshot.child("name").getValue();
                Double currentPoints = (Double) dataSnapshot.child("currentPoints").getValue();
                int currentPointsInt = (int) Math.floor(currentPoints);
                String base64Image = (String) dataSnapshot.child("profileImage").getValue();

                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("name", name);
                editor.putInt("currentPoints", currentPointsInt);
                editor.putString("profileImage", base64Image);
                editor.commit();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    };

}
