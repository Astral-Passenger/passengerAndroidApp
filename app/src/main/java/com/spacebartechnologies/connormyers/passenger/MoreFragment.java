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

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;


/**
 * A simple {@link Fragment} subclass.
 */
public class MoreFragment extends Fragment {

    private Button mLogOutButton;
    private RelativeLayout editProfileInfoButton, helpSupportButton;
    private ImageView profileImageView;
    private TextView profileUserName;
    private FirebaseAuth mAuth;

    public MoreFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.more_fragment, container, false);
        mAuth = FirebaseAuth.getInstance();
        mLogOutButton = (Button) v.findViewById(R.id.log_out_button);
        editProfileInfoButton = (RelativeLayout) v.findViewById(R.id.profileSettingsButtonLayout);
        helpSupportButton = (RelativeLayout) v.findViewById(R.id.helpButtonLayout);
        profileImageView = (ImageView) v.findViewById(R.id.profile_profile_image);
        profileUserName = (TextView) v.findViewById(R.id.userNameMoreView);

        final Context context = getActivity();
        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String name = sharedPref.getString("name","Temp");
        String base64Image = sharedPref.getString("imageLocation", "");
        Log.d("TAG: This is the image", base64Image);
        byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        profileUserName.setText(name);
        profileImageView.setImageBitmap(decodedByte);

        editProfileInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ProfileSettings.class);
                startActivity(intent);
            }
        });

        helpSupportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), HelpAndSupport.class);
                startActivity(intent);
            }
        });

        mLogOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent = new Intent(getActivity(), LoggedOutFirstScreen.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        return v;
    }

}
