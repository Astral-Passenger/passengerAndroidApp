package com.spacebartechnologies.connormyers.passenger;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by tommyduong23 on 12/19/16.
 */

public class MonthlyTransaction implements Parcelable {

    private String mDateRecorded;
    private long  mPointCost;
    private String mRewardDescription;
    private String mRewardItem;
    private double mRewardPrice;
    private String mUserEmail;

    public MonthlyTransaction(String dateRecorded, long pointCost, String rewardDescription, String rewardItem, double rewardPrice, String userEmail) {
        mDateRecorded = dateRecorded;
        mPointCost = pointCost;
        mRewardDescription = rewardDescription;
        mRewardItem = rewardItem;
        mRewardPrice = rewardPrice;
        mUserEmail = userEmail;
    }

    public String getDateRecorded() {
        return mDateRecorded;
    }

    public void setDateRecorded(String dateRecorded) {
        mDateRecorded = dateRecorded;
    }

    public long getPointCost() {
        return mPointCost;
    }

    public String getRewardDescription() {
        return mRewardDescription;
    }

    public String getRewardItem () {
        return  mRewardItem;
    }

    public double getRewardPrice() {
        return mRewardPrice;
    }

    public String getUserEmail() {
        return mUserEmail;
    }

    public void setUserEmail(String userEmail) {
        mUserEmail = userEmail;
    }

    protected MonthlyTransaction(Parcel in) {
        mDateRecorded = in.readString();
        mPointCost = in.readLong();
        mRewardDescription = in.readString();
        mRewardItem = in.readString();
        mRewardPrice = in.readDouble();
        mUserEmail = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mDateRecorded);
        parcel.writeLong(mPointCost);
        parcel.writeString(mRewardDescription);
        parcel.writeString(mRewardItem);
        parcel.writeDouble(mRewardPrice);
        parcel.writeString(mUserEmail);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<MonthlyTransaction> CREATOR = new Parcelable.Creator<MonthlyTransaction>() {
        @Override
        public MonthlyTransaction createFromParcel(Parcel in) {
            return new MonthlyTransaction(in);
        }

        @Override
        public MonthlyTransaction[] newArray(int size) {
            return new MonthlyTransaction[size];
        }
    };
}