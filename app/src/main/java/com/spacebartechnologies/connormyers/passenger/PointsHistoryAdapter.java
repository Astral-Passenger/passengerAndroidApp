package com.spacebartechnologies.connormyers.passenger;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.PixelCopy;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by tommyduong23 on 11/23/16.
 */

public class PointsHistoryAdapter extends RecyclerView.Adapter<PointsHistoryAdapter.ViewHolder>{
    private List<PointsHistoryGroup> mPointsHistoryGroup;
    private PointsHistoryOnDateAdapter mPointsHistoryOnDateAdapter;
    private Context mContext;

    public PointsHistoryAdapter(List<PointsHistoryGroup> pointsHistoryGroups, Context context) {
        mPointsHistoryGroup = pointsHistoryGroups;
        mContext = context;

    }
    @Override
    public PointsHistoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.points_history_row, parent, false);
        return new PointsHistoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PointsHistoryAdapter.ViewHolder holder, int position) {
        PointsHistoryGroup pointsHistoryGroup = mPointsHistoryGroup.get(position);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        holder.date.setText(pointsHistoryGroup.getDate());
        holder.pointHistory.setLayoutManager(layoutManager);
        mPointsHistoryOnDateAdapter = new PointsHistoryOnDateAdapter(pointsHistoryGroup.getPointsHistory());
        holder.pointHistory.setAdapter(mPointsHistoryOnDateAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(holder.pointHistory.getContext(), LinearLayoutManager.VERTICAL);
        holder.pointHistory.addItemDecoration(dividerItemDecoration);


    }

    @Override
    public int getItemCount() {
        return mPointsHistoryGroup.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView date;
        RecyclerView pointHistory;
        CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);

            date = (TextView) itemView.findViewById(R.id.date);
            pointHistory = (RecyclerView) itemView.findViewById(R.id.points_history_on_date_recycler_view);
            cardView = (CardView) itemView.findViewById(R.id.points_history_on_date_card_view);
        }
    }
}
