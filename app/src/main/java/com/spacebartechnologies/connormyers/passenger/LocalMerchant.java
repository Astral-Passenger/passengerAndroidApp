package com.spacebartechnologies.connormyers.passenger;

import android.location.Location;

import java.util.ArrayList;

/**
 * Created by tommyduong23 on 12/2/16.
 */

public class LocalMerchant extends Merchant implements Comparable<LocalMerchant> {

    private String mCity;
    private String mStreets;
    private double mLatitude;
    private double mLongitude;
    private Location mLocation;
    private double mDistance;

    public LocalMerchant(String city, String description, String name, String streets, String email, String imgLoc, double latitude, double longtitude,
                         ArrayList<Reward> rewardList, ArrayList<MonthlyTransaction> transactionList, String key) {

        super(description, name, email, imgLoc, rewardList, transactionList, key);
        mCity = city;
        mStreets = streets;
        mLatitude = latitude;
        mLongitude = longtitude;
        mLocation = new Location("");
        mLocation.setLatitude(mLatitude);
        mLocation.setLongitude(mLongitude);

    }

    public String getCity() {
        return mCity;
    }
    public String getStreets() {
        return mStreets;
    }
    public double getLatitude() {
        return mLatitude;
    }

    public double getLongtitude() {
        return mLongitude;
    }
    public Location getLocation() {return mLocation; }

    public void setDistance(Location currentLocation) {
        mDistance = currentLocation.distanceTo(mLocation);
        mDistance = mDistance * 0.000621371192;
    }
    public double getDistance() {
        return mDistance;
    }



    @Override
    public int compareTo(LocalMerchant localMerchant) {
        double distance = localMerchant.getDistance();

        if (mDistance > distance) {
            return 1;
        }
        else if (mDistance < distance) {
            return -1;
        }
        else {
            return 0;
        }
    }
}
