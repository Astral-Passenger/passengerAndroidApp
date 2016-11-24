package com.spacebartechnologies.connormyers.passenger;

/**
 * Created by tommyduong23 on 11/22/16.
 */

public class RewardsHistory {

    private String mCompanyName;
    private long mPointCost;
    private String mRewardItem;
    private String mRewardText;

    public RewardsHistory() {
        mCompanyName = "";
        mPointCost = 0;
        mRewardItem = "";
        mRewardText = "";
    }

    public RewardsHistory(String company, long pointCost, String rewardItem, String rewardText) {
        mCompanyName = company;
        mPointCost = pointCost;
        mRewardItem = rewardItem;
        mRewardText = rewardText;
    }

    public String getCompanyName() {
        return mCompanyName;
    }

    public void setCompanyName(String companyName) {
        mCompanyName = companyName;
    }

    public long getPointCost() {
        return mPointCost;
    }

    public void setPointCost(int pointCost) {
        mPointCost = pointCost;
    }

    public String getRewardItem() {
        return mRewardItem;
    }

    public void setRewardItem(String rewardItem) {
        mRewardItem = rewardItem;
    }

    public String getRewardText() {
        return mRewardText;
    }

    public void setRewardText(String rewardText) {
        mRewardText = rewardText;
    }
}
