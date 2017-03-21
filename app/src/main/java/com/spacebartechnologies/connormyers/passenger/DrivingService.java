package com.spacebartechnologies.connormyers.passenger;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by connormyers on 3/21/17.
 */

public class DrivingService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private Timer t = new Timer();

    GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private Location mPreviousLocation;
    private double userDistanceTraveled = 0.0;
    private long userTimeTraveled = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }



    @Override
    public void onDestroy() {

        super.onDestroy();

    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        mGoogleApiClient.connect();
        return START_STICKY;

    }

    @Override
    public IBinder onBind(Intent arg0) {

        return null;

    }

    // Location services code below

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.e("TAG", "Here bitches");
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            Log.e("TAG", "" + mLastLocation.getLatitude());
            Log.e("TAG", "" + mLastLocation.getLongitude());
        }
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                getLastLocationFromTimer();
            }
        }, 0, 1000);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    // Handling the driving data

    private double currentDistanceTraveled;
    private double currentDistanceTraveledInTenSeconds;
    private double totalCurrentPoints;
    private int currentTimeTraveled;
    private int timeCounter = 0;
    private int isSittingStillCount = 0;
    private boolean isStoppedDriving = false, didUsePhoneAtStoplight = false, isDriving = false, phoneIsLocked = false, screenIsOff = false;
    private double currentSpeed = 0.0;
    private int seconds = 0;


    private void getLastLocationFromTimer() {
        mPreviousLocation = mLastLocation;
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        Location loc = new Location("dummyprovider");
        loc.setLatitude(36.825474);
        loc.setLongitude(-119.677750);
        float currentDistance = mPreviousLocation.distanceTo(loc);
        Log.e("Distance", "" + currentDistance);
        Log.e("LONG & LAT", "" + mLastLocation.getLatitude() + " , " + mLastLocation.getLongitude());


        if (((timeCounter % 10) == 0 && phoneIsLocked) && isDriving && seconds > 1.0) {

            addPoints();
            screenIsOff = true;
            currentDistanceTraveledInTenSeconds = 0;
            currentTimeTraveled += 10;

        } else if (((timeCounter % 10) == 0 && phoneIsLocked == false) && !isDriving && seconds > 1.0) {

            currentDistanceTraveledInTenSeconds = 0;
            //secondsToAddToUser += 10

        } else if(((timeCounter % 10) == 0 && phoneIsLocked) && !isDriving && seconds > 1.0) {

            currentDistanceTraveledInTenSeconds = 0;
            //secondsToAddToUser += 10

        } else if(((timeCounter % 10) == 0 && phoneIsLocked == false) && isDriving && seconds > 1.0) {

            currentDistanceTraveledInTenSeconds = 0;
            //secondsToAddToUser += 10

        }

        if (currentDistanceTraveledInTenSeconds < 50) {

//            // The user has not driven very far so we assume that they have stopped moving for now
//            currentSpeed = 0.0;
//            isSittingStillCount += 1;
//
//            if (isSittingStillCount > 120 && isStoppedDriving) {
//
//                // The user has stopped moving for 2 minutes so we can assume that they are done driving now
//                if (totalCurrentPoints > 0.99) {
//
//                    // The user has more than 0.99 points so handle saving the information back to the database and for the user
//                    // Then reset all of the data and begin to check for drive again
//
//                    saveDrive();
//                }
//
//            } else if (isSittingStillCount > 9 && phoneIsLocked == false) {
//
//                // User is still stopped but they used their phone. So, if they start driving a again it will cause them to lose points
//                isStoppedDriving = true;
//                didUsePhoneAtStoplight = true;
//
//            } else {
//
//                // They are simply done driving
//                isStoppedDriving = true;
//
//            }
        } else if (didUsePhoneAtStoplight) {

            // User is still driving, check if they used their phone while at a stop light
            totalCurrentPoints = 0.0;
            isDriving = true;
            isStoppedDriving = false;
            isSittingStillCount = 0;
            didUsePhoneAtStoplight = false;

        } else {

            // User is driving like normal
            isDriving = true;
            isStoppedDriving = false;
            isSittingStillCount = 0;

        }

        seconds += 1;

    }

    private void addPoints() {
        totalCurrentPoints = totalCurrentPoints + (10 * 0.0194);
    }

    private void saveDrive() {
        Log.e("DRIVE","Save the drive for the user and reset all of the data");
    }


}
