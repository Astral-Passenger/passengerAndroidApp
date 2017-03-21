package com.spacebartechnologies.connormyers.passenger;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;

/**
 * Created by connormyers on 7/14/16.
 */
public class PassengerApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }

}
