package com.spacebartechnologies.connormyers.passenger;

import java.util.ArrayList;

/**
 * Created by tommyduong23 on 12/2/16.
 */

public class LocalMerchant extends Merchant implements Comparable<LocalMerchant> {

    private String mCity;
    private String mStreets;
    private double mLatitude;
    private double mLongitude;

    public LocalMerchant(String city, String description, String name, String streets, String email, String imgLoc, double latitude, double longtitude, ArrayList<Reward> rewardList) {

        super(description, name, email, imgLoc, rewardList);
        mCity = city;
        mStreets = streets;
        mLatitude = latitude;
        mLongitude = longtitude;

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

    @Override
    public int compareTo(LocalMerchant localMerchant) {
        return 0;
    }
}
