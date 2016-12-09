package com.spacebartechnologies.connormyers.passenger;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by tommyduong23 on 12/5/16.
 */

public class OnlineReward extends Reward implements Parcelable {

    private String mCouponCode;

    public OnlineReward(String companyName, String couponCode, String imgLoc, long pointCost, double amountSaved, String description, double price, String rewardsName) {
        super(companyName, imgLoc, pointCost, amountSaved, description, price, rewardsName);
        mCouponCode = couponCode;
    }

    public String getCouponCode() {
        return mCouponCode;
    }

    protected OnlineReward(Parcel in) {
        mCouponCode = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mCouponCode);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<OnlineReward> CREATOR = new Parcelable.Creator<OnlineReward>() {
        @Override
        public OnlineReward createFromParcel(Parcel in) {
            return new OnlineReward(in);
        }

        @Override
        public OnlineReward[] newArray(int size) {
            return new OnlineReward[size];
        }
    };
}