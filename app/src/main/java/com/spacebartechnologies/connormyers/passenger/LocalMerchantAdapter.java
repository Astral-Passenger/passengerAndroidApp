package com.spacebartechnologies.connormyers.passenger;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by tommyduong23 on 12/4/16.
 */

public class LocalMerchantAdapter extends RecyclerView.Adapter<LocalMerchantAdapter.ViewHolder>  {
    private ArrayList<LocalMerchant> mMerchantList;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef;
    private Context context;

    public LocalMerchantAdapter(ArrayList<LocalMerchant> merchantList) {
        mMerchantList = merchantList;
    }
    @Override
    public LocalMerchantAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.local_merchants_row, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LocalMerchantAdapter.ViewHolder holder, int position) {
        final LocalMerchant localMerchant = mMerchantList.get(position);
        DecimalFormat df = new DecimalFormat("0.0");
        storageRef = storage.getReferenceFromUrl(localMerchant.getImgLoc());
        Glide.with(context)
                .using(new FirebaseImageLoader())
                .load(storageRef)
                .into(holder.localImg);
        holder.companyName.setText(localMerchant.getName());
        holder.streets.setText(localMerchant.getStreets());
        holder.distance.setText(df.format(localMerchant.getDistance()));
        holder.localImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MerchantRewardsActivity.class);
                intent.putParcelableArrayListExtra("rewardList", localMerchant.getRewardList());
                intent.putParcelableArrayListExtra("monthlyTransactions", localMerchant.getTransactionList());
                intent.putExtra("merchantKey", localMerchant.getKey());
                intent.putExtra("merchantImg", localMerchant.getImgLoc());
                intent.putExtra("type", "localMerchants");
                context.startActivity(intent);
            }
        });
        holder.distance.bringToFront();
        holder.miles.bringToFront();
    }

    @Override
    public int getItemCount() {
        return mMerchantList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView localImg;
        private TextView companyName;
        private TextView streets;
        private TextView distance;
        private TextView miles;

        public ViewHolder(View itemView) {
            super(itemView);

            localImg = (ImageView) itemView.findViewById(R.id.local_img);
            companyName = (TextView) itemView.findViewById(R.id.company_name);
            streets = (TextView) itemView.findViewById(R.id.streets);
            distance = (TextView) itemView.findViewById(R.id.distance);
            miles = (TextView) itemView.findViewById(R.id.miles);
        }
    }
}
