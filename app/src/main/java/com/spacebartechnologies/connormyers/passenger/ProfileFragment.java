package com.spacebartechnologies.connormyers.passenger;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private ImageView profileImageView;
    private TextView usersNameProfile, currentPointsProfile, totalPointsProfile, rewardsReceivedProfile, milesDrivenProfile, timeSpentDrivingProfile;
    private Button editProfileButton;
    private Integer days, hoursFloored, minutes;
    private Double hoursFull;
    private RelativeLayout rewardsReceivedLayout, milesDrivenLayout;


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.profile_fragment, container, false);

        profileImageView = (ImageView) v.findViewById(R.id.profile_profile_image);
        usersNameProfile = (TextView) v.findViewById(R.id.userProfileNameTextView);
        currentPointsProfile = (TextView) v.findViewById(R.id.currentPointsTextView);
        totalPointsProfile = (TextView) v.findViewById(R.id.totalPointsTextView);
        rewardsReceivedProfile = (TextView) v.findViewById(R.id.rewardsReceivedDrivenProfile);
        milesDrivenProfile = (TextView) v.findViewById(R.id.milesDrivenProfile);
        editProfileButton = (Button) v.findViewById(R.id.editProfileButton);
        timeSpentDrivingProfile = (TextView) v.findViewById(R.id.timeSpentDrivingProfile);
        rewardsReceivedLayout = (RelativeLayout) v.findViewById(R.id.rewardsReceivedLayoutProfile);
        milesDrivenLayout = (RelativeLayout) v.findViewById(R.id.milesDrivenLayoutProfile);

        final Context context = getActivity();
        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String name = sharedPref.getString("name","Temp");
        int currentPoints = sharedPref.getInt("currentPoints", 0);
        int totalPoints = sharedPref.getInt("totalPoints", 0);
        long rewardsReceived = sharedPref.getLong("rewardsReceived",0);
        long timeSpentDriving = sharedPref.getLong("timeSpentDriving",0);
        calculateDrivingString(timeSpentDriving);
        long distanceTraveled = sharedPref.getLong("distanceTraveled",0);
        String base64Image = sharedPref.getString("imageLocation", "");
        Log.d("TAG: This is the image", base64Image);
        byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        profileImageView.setImageBitmap(decodedByte);
        usersNameProfile.setText(name);
        timeSpentDrivingProfile.setText(calculateDrivingString(timeSpentDriving));
        currentPointsProfile.setText(String.valueOf(currentPoints) + " current points");
        totalPointsProfile.setText(String.valueOf(totalPoints) + " total points");
        milesDrivenProfile.setText(String.valueOf(distanceTraveled));

        rewardsReceivedProfile.setText(String.valueOf(rewardsReceived));

        rewardsReceivedLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, RewardsHistoryActivity.class);
                startActivity(intent);
            }
        });

        milesDrivenLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PointsHistoryActivity.class);
                startActivity(intent);
            }
        });

        return v;
    }

    private String calculateDrivingString(long timeSpentDriving) {

        String finalString = "";
        Double hoursFull = (Double.valueOf(timeSpentDriving)/3600.0);

        if (hoursFull > 23.999) {
            Double tempDays = Math.floor(hoursFull/24.0);
            days = tempDays.intValue();
            hoursFull = hoursFull - Double.valueOf(days * 24.0);
            Double tempHoursFloored = Math.floor(hoursFull);
            hoursFloored = tempHoursFloored.intValue();
            Double tempMinutes = (hoursFull - Double.valueOf(hoursFloored)) * 60;
            minutes = tempMinutes.intValue();
            finalString = String.valueOf(days) + "d. " + String.valueOf(hoursFloored) + "hr. " + String.valueOf(minutes) + "min.";
        } else {
            Double tempHoursFloored = Math.floor(hoursFull);
            hoursFloored = tempHoursFloored.intValue();
            Double tempMinutes = ((hoursFull - Double.valueOf(hoursFloored)) * 60);
            minutes = tempMinutes.intValue();
            finalString = String.valueOf(hoursFloored) + "hr. " + String.valueOf(minutes) + "min.";
        }

        return finalString;
    }

}