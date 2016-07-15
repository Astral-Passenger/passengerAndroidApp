package com.spacebartechnologies.connormyers.passenger;

import com.firebase.client.Firebase;

/**
 * Created by connormyers on 7/14/16.
 */
public class FirebaseFuncs {

    Firebase ref = new Firebase("https://passenger-app.firebaseio.com/");
    Firebase usersRef = ref.child("users");
    Firebase helpRef = ref.child("help");
    Firebase rewardsRef = ref.child("rewards");
    Firebase merchantSignUpSubmitRef = ref.child("merchantSignUpSubmit");

    FirebaseFuncs() {

    }



}
