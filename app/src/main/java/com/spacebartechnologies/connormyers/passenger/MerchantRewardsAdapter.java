package com.spacebartechnologies.connormyers.passenger;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by tommyduong23 on 12/4/16.
 */

public class MerchantRewardsAdapter extends RecyclerView.Adapter<MerchantRewardsAdapter.ViewHolder>{

    ArrayList<Reward> mRewardList;

    public MerchantRewardsAdapter(ArrayList<Reward> rewardList)

    @Override
    public MerchantRewardsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(MerchantRewardsAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
