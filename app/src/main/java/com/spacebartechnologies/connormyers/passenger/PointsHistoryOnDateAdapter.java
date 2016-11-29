package com.spacebartechnologies.connormyers.passenger;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by tommyduong23 on 11/23/16.
 */

public class PointsHistoryOnDateAdapter extends RecyclerView.Adapter<PointsHistoryOnDateAdapter.ViewHolder>{
    private List<PointsHistory> mPointsHistory;

    public PointsHistoryOnDateAdapter(List<PointsHistory> pointsHistory) {
        mPointsHistory = pointsHistory;
    }

    @Override
    public PointsHistoryOnDateAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.points_history_on_date_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PointsHistoryOnDateAdapter.ViewHolder holder, int position) {
        PointsHistory pointsHistory = mPointsHistory.get(position);
        holder.distance_traveled.setText(String.valueOf(pointsHistory.getDistanceTravel()));
        holder.time.setText(pointsHistory.getTime());
        holder.pointsGenerated.setText("+" + String.valueOf(pointsHistory.getPoints()));
    }

    @Override
    public int getItemCount() {
        return mPointsHistory.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView distance_traveled;
        TextView time;
        TextView pointsGenerated;
        public ViewHolder(View itemView) {
            super(itemView);

            distance_traveled = (TextView) itemView.findViewById(R.id.distance_traveled);
            time = (TextView) itemView.findViewById(R.id.time);
            pointsGenerated = (TextView) itemView.findViewById(R.id.points_generated);
        }
    }
}
