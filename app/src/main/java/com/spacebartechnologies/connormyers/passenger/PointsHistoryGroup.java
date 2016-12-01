package com.spacebartechnologies.connormyers.passenger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tommyduong23 on 11/28/16.
 */

public class PointsHistoryGroup {
    private String mDate;
    private List<PointsHistory> mPointsHistory;

    public PointsHistoryGroup () {
        mDate =  "";
        mPointsHistory = new ArrayList<PointsHistory>();
    }

    public PointsHistoryGroup (String date, List<PointsHistory> pointsHistory) {
        mDate = date;
        mPointsHistory = pointsHistory;
    }

    public String getDate() {
        return mDate;
    }

    public List<PointsHistory> getPointsHistory() {
        return mPointsHistory;
    }
}
