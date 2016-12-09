package com.spacebartechnologies.connormyers.passenger;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

/**
 * Created by tommyduong23 on 12/4/16.
 */

public class MerchantRewardsAdapter extends RecyclerView.Adapter<MerchantRewardsAdapter.ViewHolder>{

    ArrayList<Reward> mRewardList;
    Context context;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef;
    String mMerchantImg;
    String mMerchantType;

    public MerchantRewardsAdapter(ArrayList<Reward> rewardList, String merchantImg, String merchantType) {
        mRewardList = rewardList;
        mMerchantImg = merchantImg;
        mMerchantType = merchantType;
    }

    @Override
    public MerchantRewardsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.merchant_rewards_row, parent, false);
        context = parent.getContext();
        return new MerchantRewardsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MerchantRewardsAdapter.ViewHolder holder, int position) {
        final Reward reward = mRewardList.get(position);
        storageRef = storage.getReferenceFromUrl(reward.getImgLoc());
        Glide.with(context)
                .using(new FirebaseImageLoader())
                .load(storageRef)
                .into(holder.rewardImg);
        Log.d("Class", reward.getClass().toString());
        holder.pointCost.setText(Long.toString(reward.getPointCost()));
        holder.rewardName.setText(reward.getRewardsName());
        holder.rewardDescription.setText(reward.getDescription());
        holder.rewardImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, RedeemActivity.class);
                intent.putExtra("reward", reward);
                intent.putExtra("type", mMerchantType);
                intent.putExtra("merchantImg", mMerchantImg);
                intent.putParcelableArrayListExtra("rewardList", mRewardList);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mRewardList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView rewardImg;
        TextView pointCost;
        TextView rewardName;
        TextView rewardDescription;
        public ViewHolder(View itemView) {
            super(itemView);

            rewardImg = (ImageView) itemView.findViewById(R.id.reward_img);
            pointCost = (TextView) itemView.findViewById(R.id.point_cost);
            rewardName = (TextView) itemView.findViewById(R.id.reward_name);
            rewardDescription = (TextView) itemView.findViewById(R.id.reward_description);
        }
    }
}
