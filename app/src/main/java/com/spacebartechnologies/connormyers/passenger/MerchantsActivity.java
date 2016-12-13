package com.spacebartechnologies.connormyers.passenger;

import android.content.Intent;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.identity.intents.Address;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by tommyduong23 on 12/2/16.
 */

public class MerchantsActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private ImageButton mBackNavButton;
    private TextView titleToolbar;
    private RecyclerView mMerchantsRecyclerView;
    private LocalMerchantAdapter mLocalMerchantAdapter;
    private ArrayList<LocalMerchant> mLocalMerchantList;
    private ArrayList<OnlineMerchant> mOnlineMerchantList;
    private OnlineMerchantAdapter mOnlineMerchantAdapter;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    final long ONE_MEGABYTE = 1024 * 1024;
    private String merchantType;
    private GoogleApiClient mGoogleApiClient;
    private Location mLocation;
    private String mCity;
    private String mState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchants);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        titleToolbar = (TextView) findViewById(R.id.title_toolbar);

        Bundle extras = getIntent().getExtras();
        merchantType = extras.getString("type");

        mMerchantsRecyclerView = (RecyclerView) findViewById(R.id.merchants_recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mMerchantsRecyclerView.setLayoutManager(layoutManager);

        if (merchantType.equals("localMerchants")) {
            mLocalMerchantList = new ArrayList<LocalMerchant>();
            mLocalMerchantAdapter = new LocalMerchantAdapter(mLocalMerchantList);
            mMerchantsRecyclerView.setAdapter(mLocalMerchantAdapter);
        }
        else {
            mOnlineMerchantList = new ArrayList<OnlineMerchant>();
            mOnlineMerchantAdapter = new OnlineMerchantAdapter(mOnlineMerchantList);
            mMerchantsRecyclerView.setAdapter(mOnlineMerchantAdapter);
        }

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }



        mBackNavButton = (ImageButton) findViewById(R.id.back_button_merchants);
        mBackNavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MerchantsActivity.this, RewardsActivity.class);
                finish();
                startActivity(intent);
            }
        });
        mDatabase.child(merchantType).addListenerForSingleValueEvent(new ValueEventListener() {

            LocalMerchant localMerchant;
            OnlineMerchant onlineMerchant;
            String companyName;
            String city;
            String description;
            String streets;
            String email;
            String imgLoc;
            double latitude;
            double longitude;
            ArrayList<Reward> rewardList;

            ArrayList<HashMap> rewardMapList;
            Reward reward;
            String rewardCompanyName;
            String rewardImgLoc;
            long rewardPointCost;
            double rewardAmountSaved;
            String rewardDescription;
            double rewardPrice;
            String rewardsName;
            String rewardCoupon;
            StorageReference storageRef;

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {
                    description = (String) messageSnapshot.child("companyDescription").getValue();
                    companyName = (String) messageSnapshot.child("companyName").getValue();
                    email = (String) messageSnapshot.child("email").getValue();
                    imgLoc = (String) messageSnapshot.child("imageLocation").getValue();
                    if (merchantType.equals("localMerchants")) {
                        city = (String) messageSnapshot.child("city").getValue();
                        streets = (String) messageSnapshot.child("crossStreets").getValue();
                        latitude = (double) messageSnapshot.child("latitude").getValue();
                        longitude = (double) messageSnapshot.child("longitude").getValue();
                    }
                    rewardList = new ArrayList<Reward>();

                    rewardMapList = (ArrayList) messageSnapshot.child("rewards").getValue();
                    for (HashMap r: rewardMapList) {
                        rewardCompanyName = (String) r.get("companyName");
                        rewardImgLoc = (String) r.get("imageLocation");
                        rewardPointCost = (long) r.get("pointCost");
                        rewardAmountSaved = ((Number)r.get("rewardAmountSaved")).doubleValue();
                        rewardDescription = (String) r.get("rewardDescription");
                        rewardPrice = ((Number)r.get("rewardPrice")).doubleValue();
                        rewardsName = (String) r.get("rewardsName");
                        if (merchantType.equals("onlineMerchants")) {
                            rewardCoupon = (String) r.get("couponCode");
                            reward = new Reward(rewardCompanyName, rewardCoupon, rewardImgLoc, rewardPointCost, rewardAmountSaved, rewardDescription, rewardPrice, rewardsName);
                            rewardList.add(reward);
                        }
                        else {
                            reward = new Reward(rewardCompanyName, rewardImgLoc, rewardPointCost, rewardAmountSaved, rewardDescription, rewardPrice, rewardsName);
                            rewardList.add(reward);
                        }

                    }

                    if (merchantType.equals("localMerchants")) {
                        localMerchant = new LocalMerchant(city, description, companyName, streets, email, imgLoc, latitude, longitude, rewardList);
                        localMerchant.setDistance(mLocation);
                        if (localMerchant.getDistance() <= 25)
                            mLocalMerchantList.add(localMerchant);
                    }
                    else {
                        onlineMerchant = new OnlineMerchant(description, companyName, email, imgLoc, rewardList);
                        mOnlineMerchantList.add(onlineMerchant);
                    }

                }
                if (merchantType.equals("localMerchants")) {
                    Collections.sort(mLocalMerchantList);
                    mLocalMerchantAdapter = new LocalMerchantAdapter(mLocalMerchantList);
                    mMerchantsRecyclerView.setAdapter(mLocalMerchantAdapter);
                }
                else {
                    mOnlineMerchantAdapter = new OnlineMerchantAdapter(mOnlineMerchantList);
                    mMerchantsRecyclerView.setAdapter(mOnlineMerchantAdapter);
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Geocoder gcd = new Geocoder(this, Locale.getDefault());
        List<android.location.Address> addresses;
        mLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLocation != null) {
            Log.d("Latitude", String.valueOf(mLocation.getLatitude()));
            Log.d("Longitude",String.valueOf(mLocation.getLongitude()));
            try {
                addresses = gcd.getFromLocation(mLocation.getLatitude(), mLocation.getLongitude(),1);
                mCity = addresses.get(0).getLocality();
                mState = getAbbreviationFromUSState(addresses.get(0).getAdminArea());
                Log.d("city", mCity);
                Log.d("state", mState);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (merchantType.equals("localMerchants")) {
                titleToolbar.setText(mCity + ", " + mState);
            }

        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public static final Map<String, String> STATE_MAP;
    static {
        STATE_MAP = new HashMap<String, String>();
        STATE_MAP.put("Alabama", "AL");
        STATE_MAP.put("Alaska", "AK");
        STATE_MAP.put("Alberta", "AB");
        STATE_MAP.put("Arizona", "AZ");
        STATE_MAP.put("Arkansas", "AR");
        STATE_MAP.put("British Columbia", "BC");
        STATE_MAP.put("California", "CA");
        STATE_MAP.put("Colorado", "CO");
        STATE_MAP.put("Connecticut", "CT");
        STATE_MAP.put("Delaware", "DE");
        STATE_MAP.put("District Of Columbia", "DC");
        STATE_MAP.put("Florida", "FL");
        STATE_MAP.put("Georgia", "GA");
        STATE_MAP.put("Guam", "GU");
        STATE_MAP.put("Hawaii", "HI");
        STATE_MAP.put("Idaho", "ID");
        STATE_MAP.put("Illinois", "IL");
        STATE_MAP.put("Indiana", "IN");
        STATE_MAP.put("Iowa", "IA");
        STATE_MAP.put("Kansas", "KS");
        STATE_MAP.put("Kentucky", "KY");
        STATE_MAP.put("Louisiana", "LA");
        STATE_MAP.put("Maine", "ME");
        STATE_MAP.put("Manitoba", "MB");
        STATE_MAP.put("Maryland", "MD");
        STATE_MAP.put("Massachusetts", "MA");
        STATE_MAP.put("Michigan", "MI");
        STATE_MAP.put("Minnesota", "MN");
        STATE_MAP.put("Mississippi", "MS");
        STATE_MAP.put("Missouri", "MO");
        STATE_MAP.put("Montana", "MT");
        STATE_MAP.put("Nebraska", "NE");
        STATE_MAP.put("Nevada", "NV");
        STATE_MAP.put("New Brunswick", "NB");
        STATE_MAP.put("New Hampshire", "NH");
        STATE_MAP.put("New Jersey", "NJ");
        STATE_MAP.put("New Mexico", "NM");
        STATE_MAP.put("New York", "NY");
        STATE_MAP.put("Newfoundland", "NF");
        STATE_MAP.put("North Carolina", "NC");
        STATE_MAP.put("North Dakota", "ND");
        STATE_MAP.put("Northwest Territories", "NT");
        STATE_MAP.put("Nova Scotia", "NS");
        STATE_MAP.put("Nunavut", "NU");
        STATE_MAP.put("Ohio", "OH");
        STATE_MAP.put("Oklahoma", "OK");
        STATE_MAP.put("Ontario", "ON");
        STATE_MAP.put("Oregon", "OR");
        STATE_MAP.put("Pennsylvania", "PA");
        STATE_MAP.put("Prince Edward Island", "PE");
        STATE_MAP.put("Puerto Rico", "PR");
        STATE_MAP.put("Quebec", "QC");
        STATE_MAP.put("Rhode Island", "RI");
        STATE_MAP.put("Saskatchewan", "SK");
        STATE_MAP.put("South Carolina", "SC");
        STATE_MAP.put("South Dakota", "SD");
        STATE_MAP.put("Tennessee", "TN");
        STATE_MAP.put("Texas", "TX");
        STATE_MAP.put("Utah", "UT");
        STATE_MAP.put("Vermont", "VT");
        STATE_MAP.put("Virgin Islands", "VI");
        STATE_MAP.put("Virginia", "VA");
        STATE_MAP.put("Washington", "WA");
        STATE_MAP.put("West Virginia", "WV");
        STATE_MAP.put("Wisconsin", "WI");
        STATE_MAP.put("Wyoming", "WY");
        STATE_MAP.put("Yukon Territory", "YT");
    }

    public static String getAbbreviationFromUSState(String state) {
        if (STATE_MAP.containsKey(state)) {
            return STATE_MAP.get(state);
        }else{
            return state;
        }
    }
}
