package com.spacebartechnologies.connormyers.passenger;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by tommyduong23 on 12/4/16.
 */

public class Reward implements Parcelable {

    private String mCompanyName;
    private String mImgLoc;
    private long mPointCost;
    private double mAmountSaved;
    private String mDescription;
    private double mPrice;
    private String mRewardsName;
    private String mCouponCode;

    public Reward() {
        mCompanyName = "";
        mImgLoc = "";
        mPointCost = 0;
        mAmountSaved = 0;
        mDescription = "";
        mPrice = 0;
        mRewardsName = "";
        mCouponCode ="";
    }

    public Reward(String companyName, String imgLoc, long pointCost, double amountSaved, String description, double price, String rewardsName) {
        mCompanyName = companyName;
        mImgLoc = imgLoc;
        mPointCost = pointCost;
        mAmountSaved = amountSaved;
        mDescription = description;
        mPrice = price;
        mRewardsName = rewardsName;
        mCouponCode = "";
    }

    public Reward(String companyName, String couponCode, String imgLoc, long pointCost, double amountSaved, String description, double price, String rewardsName) {
        mCompanyName = companyName;
        mCouponCode = couponCode;
        mImgLoc = imgLoc;
        mPointCost = pointCost;
        mAmountSaved = amountSaved;
        mDescription = description;
        mPrice = price;
        mRewardsName = rewardsName;
    }


    public String getCompanyName() {
        return mCompanyName;
    }

    public String getImgLoc() {
        return mImgLoc;
    }

    public long getPointCost() {
        return mPointCost;
    }
    public double getAmountSaved() {
        return mAmountSaved;
    }
    public String getDescription() {
        return mDescription;
    }
    public double getPrice() {
        return mPrice;
    }
    public String getRewardsName() {
        return mRewardsName;
    }
    public String getCouponCode() {
        return mCouponCode;
    }


    protected Reward(Parcel in) {
        mCompanyName = in.readString();
        mImgLoc = in.readString();
        mPointCost = in.readLong();
        mAmountSaved = in.readDouble();
        mDescription = in.readString();
        mPrice = in.readDouble();
        mRewardsName = in.readString();
        mCouponCode = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mCompanyName);
        parcel.writeString(mImgLoc);
        parcel.writeLong(mPointCost);
        parcel.writeDouble(mAmountSaved);
        parcel.writeString(mDescription);
        parcel.writeDouble(mPrice);
        parcel.writeString(mRewardsName);
        parcel.writeString(mCouponCode);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Reward> CREATOR = new Parcelable.Creator<Reward>() {
        @Override
        public Reward createFromParcel(Parcel in) {
            return new Reward(in);
        }

        @Override
        public Reward[] newArray(int size) {
            return new Reward[size];
        }
    };
}