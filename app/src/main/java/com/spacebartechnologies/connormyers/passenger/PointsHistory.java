package com.spacebartechnologies.connormyers.passenger;

/**
 * Created by tommyduong23 on 11/23/16.
 */

public class PointsHistory {
    private String mCreatedAt;
    private double mDistanceTraveled;
    private double mPointsGenerated;

    public PointsHistory() {
        mCreatedAt = "";
        mDistanceTraveled = 0;
        mPointsGenerated = 0;
    }

    public PointsHistory(String date, double distanceTraveled, double pointsGenerated) {
        mCreatedAt = date;
        mDistanceTraveled = distanceTraveled;
        mPointsGenerated = pointsGenerated;
    }

    public String getCreatedAt() {
        return mCreatedAt;
    }
    public String getDate() {
        String date;
        String[] fullDate;

        fullDate = mCreatedAt.split(" ");
        date = fullDate[0];

        return date;
    }

    public String getTime() {
        String time;
        String[] fullDate;

        fullDate = mCreatedAt.split(" ");
        time = fullDate[1];

        return time;
    }

    public double getDistanceTravel() {
        return mDistanceTraveled;
    }

    public double getPoints() {
        return mPointsGenerated;
    }

}
