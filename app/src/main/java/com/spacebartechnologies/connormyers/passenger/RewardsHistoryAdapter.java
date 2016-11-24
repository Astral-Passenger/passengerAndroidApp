package com.spacebartechnologies.connormyers.passenger;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by tommyduong23 on 11/22/16.
 */

public class RewardsHistoryAdapter extends RecyclerView.Adapter<RewardsHistoryAdapter.ViewHolder> {

    private List<RewardsHistory> mRewardsHistory;

    public RewardsHistoryAdapter(List<RewardsHistory> rewardsHistory) {
        mRewardsHistory = rewardsHistory;
    }
    @Override
    public RewardsHistoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rewards_history_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RewardsHistoryAdapter.ViewHolder holder, int position) {
        RewardsHistory rewardsHistory = mRewardsHistory.get(position);
        holder.discount.setText(rewardsHistory.getRewardItem() + " " + rewardsHistory.getRewardText());
        holder.companyName.setText(rewardsHistory.getCompanyName());
        holder.pointCost.setText(String.valueOf(rewardsHistory.getPointCost()));
    }

    @Override
    public int getItemCount() {
        return mRewardsHistory.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView discount;
        private TextView companyName;
        private TextView pointCost;

        public ViewHolder(View itemView) {
            super(itemView);

            discount = (TextView) itemView.findViewById(R.id.discount);
            companyName = (TextView) itemView.findViewById(R.id.company_name);
            pointCost = (TextView) itemView.findViewById(R.id.point_cost);
        }
    }
}
