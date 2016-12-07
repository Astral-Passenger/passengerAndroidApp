package com.spacebartechnologies.connormyers.passenger;

import java.util.ArrayList;

/**
 * Created by tommyduong23 on 12/5/16.
 */

public class Merchant {
    private String mDescription;
    private String mName;
    private String mEmail;
    private String mImgLoc;
    private ArrayList<Reward> mRewardList;

    public Merchant() {
        mDescription = "";
        mName = "";
        mEmail = "";
        mImgLoc = "";
        mRewardList = new ArrayList<Reward>();
    }

    public Merchant(String description, String name, String email, String imgLoc, ArrayList<Reward> rewardList) {
        mDescription = description;
        mName = name;
        mEmail = email;
        mImgLoc = imgLoc;
        mRewardList = rewardList;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getName() {
        return mName;
    }

    public String getEmail() {
        return mEmail;
    }

    public String getImgLoc() {
        return mImgLoc;
    }

    public void setmImgLoc(String imgLoc) {
        mImgLoc = imgLoc;
    }



    public ArrayList<Reward> getRewardList() {
        return mRewardList;
    }
}
