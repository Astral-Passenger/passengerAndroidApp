package com.spacebartechnologies.connormyers.passenger;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.zip.Inflater;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private LinearLayout mRewardsButtonLayout, mRewardsHistoryButtonLayout, mPointsHistoryButtonLayout;
    private TextView mUsersNameHome, mCurrentPointsHome;
    private ImageView mUserProfilePictureHome;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.home_fragment, container, false);

        mRewardsButtonLayout = (LinearLayout) layout.findViewById(R.id.rewards_button_layout);
        mRewardsHistoryButtonLayout = (LinearLayout) layout.findViewById(R.id.rewards_history_button);
        mPointsHistoryButtonLayout = (LinearLayout) layout.findViewById(R.id.points_History_button);
        mUsersNameHome = (TextView) layout.findViewById(R.id.user_name_home);
        mCurrentPointsHome = (TextView) layout.findViewById(R.id.current_points_home);
        mUserProfilePictureHome = (ImageView) layout.findViewById(R.id.profile_home_image);

        mRewardsButtonLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent rewardsIntent = new Intent(getActivity(), Rewards.class);
                startActivity(rewardsIntent);
            }
        });

        mRewardsHistoryButtonLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent rewardsHistoryIntent = new Intent(getActivity(), RewardsHistory.class);
                startActivity(rewardsHistoryIntent);
            }
        });

        mPointsHistoryButtonLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pointsHistoryIntent = new Intent(getActivity(), PointsHistory.class);
                startActivity(pointsHistoryIntent);
            }
        });

        loadUserData();

        return layout;
    }

    private void loadUserData() {
        Context context = getActivity();
        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String name = sharedPref.getString("name","Temp");
        int currentPoints = sharedPref.getInt("currentPoints", 0);
        String base64Image = sharedPref.getString("imageLocation", "");
        Log.d("TAG: This is the image", base64Image);
        byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
//        Bitmap mutableBitmap = decodedByte.copy(Bitmap.Config.ARGB_8888, true);
//        Canvas canvas = new Canvas(mutableBitmap);
//        Paint mpaint = new Paint();
//        mpaint.setAntiAlias(true);
//        mpaint.setShader(new BitmapShader(mutableBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
//        canvas.drawRoundRect((new RectF(50, 100, 150, 150)), 0, 0, mpaint);// Round Image Corner 100 100 100 100

        mUserProfilePictureHome.setImageBitmap(decodedByte);

        mUsersNameHome.setText(name);
        mCurrentPointsHome.setText(String.valueOf(currentPoints));
        //mUserProfilePictureHome.setImageBitmap(Bitmap.createScaledBitmap(decodedByte, 120, 120, false));
    }

}
