package com.spacebartechnologies.connormyers.passenger;

import android.util.Log;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by tommyduong23 on 11/23/16.
 */

public class PointsHistory {
    private String mCreatedAt;
    private double mDistanceTraveled;
    private int mPointsGenerated;

    public PointsHistory() {
        mCreatedAt = "";
        mDistanceTraveled = 0;
        mPointsGenerated = 0;
    }

    public PointsHistory(String date, double distanceTraveled, int pointsGenerated) {
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
        String[] temp;
        String year;
        String month;
        String day;
        int monthNum;

        fullDate = mCreatedAt.split(" ");

        /*Date in number format*/
        date = fullDate[0];

        /*Split date into year month day */
        temp = date.split("-");
        year = temp[0];
        month = temp[1];
        day = temp[2];

        month = getMonth(Integer.valueOf(month));
        date = month + " " + day + ", " + year;

        return date;
    }

    public String getTime() {
        String time;
        String[] fullDate;
        String hour;
        String min;
        String meridiem = "AM";
        int hourNum;
        int minNum;
        String temp[];

        fullDate = mCreatedAt.split(" ");

        /* Time in 24 hr format */
        time = fullDate[1];

        temp = time.split(":");
        hour = temp[0];
        min = temp[1];

        hourNum = Integer.valueOf(hour);
        minNum = Integer.valueOf(min);

        if (hourNum > 12) {
            hourNum -= 12;
            meridiem = "PM";
        }
        hour = String.valueOf(hourNum);
        time = hour + ":" + min + " " + meridiem;
        Log.d("Time", time);
        return time;
    }

    public double getDistanceTravel() {
        return mDistanceTraveled;
    }

    public int getPoints() {
        return mPointsGenerated;
    }

    private String getMonth(int month) {
        String monthString = "";

        switch (month) {
            case 1:
                monthString = "Jan";
                break;
            case 2:
                monthString = "Feb";
                break;
            case 3:
                monthString = "Mar";
                break;
            case 4:
                monthString = "Apr";
                break;
            case 5:
                monthString = "May";
                break;
            case 6:
                monthString = "Jun";
                break;
            case 7:
                monthString = "Jul";
                break;
            case 8:
                monthString = "Aug";
                break;
            case 9:
                monthString = "Sep";
                break;
            case 10:
                monthString = "Oct";
                break;
            case 11:
                monthString = "Nov";
                break;
            case 12:
                monthString = "Dec";
                break;
            default:
                monthString = "Invalid month"; break;
        }
        return monthString;
    }

}
