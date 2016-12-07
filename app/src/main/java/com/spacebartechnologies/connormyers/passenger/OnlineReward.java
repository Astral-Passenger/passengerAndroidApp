package com.spacebartechnologies.connormyers.passenger;

/**
 * Created by tommyduong23 on 12/5/16.
 */

public class OnlineReward extends Reward {

    private String mCouponCode;

    public OnlineReward(String companyName, String couponCode, String imgLoc, long pointCost, double amountSaved, String description, double price, String rewardsName) {
        super(companyName, imgLoc, pointCost, amountSaved, description, price, rewardsName);
        mCouponCode = couponCode;
    }

    public String getCouponCode() {
        return mCouponCode;
    }
}
