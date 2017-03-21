package com.spacebartechnologies.connormyers.passenger;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import static android.R.attr.bitmap;
import static android.support.v7.mediarouter.R.id.image;

public class Register extends AppCompatActivity {

    private ImageButton mRegisterBackButton;
    private EditText mNameEditText, mEmailEditText, mPasswordEditText;
    private Button mRegisterButton, mFacebookButton;
    private String mEmailString, mNameString, mPasswordString;
    private Firebase ref = new Firebase("https://passenger-app.firebaseio.com/");
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mRegisterBackButton = (ImageButton) findViewById(R.id.back_button_register);
        mNameEditText = (EditText) findViewById(R.id.register_name_input);
        mEmailEditText = (EditText) findViewById(R.id.register_input_email);
        mPasswordEditText = (EditText) findViewById(R.id.register_input_password);
        mRegisterButton = (Button) findViewById(R.id.register_button);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("Auth", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d("Auth", "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };


        mRegisterBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(Register.this, LoggedOutFirstScreen.class);
                startActivity(registerIntent);
            }
        });

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNameString = mNameEditText.getText().toString();
                mEmailString = mEmailEditText.getText().toString();
                mPasswordString = mPasswordEditText.getText().toString();

                mAuth.createUserWithEmailAndPassword(mEmailString, mPasswordString)
                        .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Log.d("Auth", "createUserWithEmail:onComplete:" + task.isSuccessful());

                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Toast.makeText(Register.this, "Authentication failed",
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    Log.e("Auth","The user is now logged in" + task);
                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    final String newUserID = user.getUid();
                                    System.out.println(newUserID);
                                    StorageReference userImagesRef = storageRef.child("images/"+newUserID);
                                    Bitmap defaultProfileImage = BitmapFactory.decodeResource(getResources(), R.mipmap.default_profile);
                                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                    defaultProfileImage.compress(Bitmap.CompressFormat.PNG, 100, baos);
                                    byte[] data = baos.toByteArray();
                                    UploadTask uploadTask = userImagesRef.putBytes(data);
                                    System.out.println("Here I am!");
                                    Map<String, Object> userMap= new HashMap<>();
                                        userMap.put("currentPoints",0);
                                        userMap.put("distanceTraveled","0");
                                        userMap.put("email",mEmailString);
                                        userMap.put("name",mNameString);
                                        userMap.put("phoneNumber","");
                                        userMap.put("profileImage","");
                                        userMap.put("rewardsReceived",0);
                                        userMap.put("timeSpentDriving","0");
                                        userMap.put("totalPoints",0);
                                        userMap.put("imageLocation","gs://firebase-passenger-app.appspot.com/images/"+newUserID);

                                    mDatabase.child("users").child(newUserID).setValue(userMap);
                                    Context context = Register.this;
                                    final SharedPreferences sharedPref = context.getSharedPreferences(
                                            getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                                    String profileImageBase64 = Base64.encodeToString(data, Base64.DEFAULT);
                                    SharedPreferences.Editor editor = sharedPref.edit();
                                    editor.putString("name", mNameString);
                                    editor.putInt("currentPoints", 0);
                                    editor.putInt("totalPoints",0);
                                    editor.putLong("rewardsReceived",0);
                                    editor.putLong("distanceTraveled",0);
                                    editor.putLong("timeSpentDriving", 0);
                                    editor.putString("imageLocation", profileImageBase64);
                                    editor.commit();
                                    Intent intent = new Intent(Register.this, MainActivity.class);
                                    startActivity(intent);

                                    uploadTask.addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception exception) {
                                            // Handle unsuccessful uploads
                                            Log.e("TAG","Upload failed for the profile pic");
                                        }
                                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                                            Uri downloadUrl = taskSnapshot.getDownloadUrl();

                                        }
                                    });
                                }

                            }
                        });


//                ref.createUser(mEmailString, mPasswordString, new Firebase.ValueResultHandler<Map<String, Object>>() {
//                    @Override
//                    public void onSuccess(Map<String, Object> result) {
//
//                    }
//                    @Override
//                    public void onError(FirebaseError firebaseError) {
//                        // there was an error
//                        System.out.println("User was not registered successfully.");
//                    }
//                });

            }
        });
    }

}
