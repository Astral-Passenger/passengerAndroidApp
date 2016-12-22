package com.spacebartechnologies.connormyers.passenger;

import java.util.ArrayList;

/**
 * Created by tommyduong23 on 12/4/16.
 */

public class OnlineMerchant extends Merchant{

    public OnlineMerchant(String description, String name, String email, String imgLoc, ArrayList<Reward> rewardList, ArrayList<MonthlyTransaction> transactionList, String key) {
        super(description, name, email, imgLoc, rewardList, transactionList, key);
    }

}
