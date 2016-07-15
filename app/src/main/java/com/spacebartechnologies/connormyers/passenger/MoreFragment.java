package com.spacebartechnologies.connormyers.passenger;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.firebase.client.Firebase;


/**
 * A simple {@link Fragment} subclass.
 */
public class MoreFragment extends Fragment {

    private Button mLogOutButton;
    private Firebase ref = new Firebase("https://passenger-app.firebaseio.com/");

    public MoreFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.more_fragment, container, false);

        mLogOutButton = (Button) v.findViewById(R.id.log_out_button);
        mLogOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ref.unauth();
                Intent intent = new Intent(getActivity(), LoggedOutFirstScreen.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        return v;
    }

}
