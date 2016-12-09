package com.spacebartechnologies.connormyers.passenger;

import android.content.Context;
import android.content.Intent;
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

/*
 * Created by tommyduong23 on 12/6/16.
 */

public class OnlineMerchantAdapter extends RecyclerView.Adapter<OnlineMerchantAdapter.ViewHolder> {
    private ArrayList<OnlineMerchant> mMerchantList;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef;
    private Context context;

    public OnlineMerchantAdapter(ArrayList<OnlineMerchant> merchantList) {
        mMerchantList = merchantList;

    }
    @Override
    public OnlineMerchantAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.online_merchants_row, parent, false);
        context = parent.getContext();
        return new OnlineMerchantAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OnlineMerchantAdapter.ViewHolder holder, int position) {
        final OnlineMerchant onlineMerchant = mMerchantList.get(position);
        Log.d("Imgloc", onlineMerchant.getImgLoc());
        storageRef = storage.getReferenceFromUrl(onlineMerchant.getImgLoc());
        Glide.with(context)
                .using(new FirebaseImageLoader())
                .load(storageRef)
                .into(holder.onlineImg);
        holder.companyName.setText(onlineMerchant.getName());

        holder.onlineImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MerchantRewardsActivity.class);
                intent.putParcelableArrayListExtra("rewardList", onlineMerchant.getRewardList());
                intent.putExtra("merchantImg", onlineMerchant.getImgLoc());
                intent.putExtra("type", "onlineMerchants");
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mMerchantList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView onlineImg;
        TextView companyName;
        public ViewHolder(View itemView) {
            super(itemView);

            onlineImg = (ImageView) itemView.findViewById(R.id.online_img);
            companyName = (TextView) itemView.findViewById(R.id.company_name);
        }
    }
}
