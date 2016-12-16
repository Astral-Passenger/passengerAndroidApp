package com.spacebartechnologies.connormyers.passenger;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class ProfileSettingsActivity extends AppCompatActivity {

    private ImageButton mNavBackButton;
    private TextView mTitleToolbar;
    private ImageView mProfileImg;
    private String mName;
    private EditText mEditName;
    private Button mResetPasswordBtn;
    private Button mSaveBtn;
    private ProfileSettingsActivity profileSettingsActivity;
    private static final int SELECT_PHOTO = 100;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef;
    private FirebaseUser currentUser;
    private Bitmap newImgBitMap;
    private ImageView saveCheckmark;
    SharedPreferences sharedPref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mTitleToolbar = (TextView) findViewById(R.id.title_toolbar);
        mTitleToolbar.setText("Profile Settings");

        mNavBackButton = (ImageButton) findViewById(R.id.back_button);
        mNavBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                finish();
                startActivity(intent);
            }
        });



        mProfileImg = (ImageView) findViewById(R.id.profile_img);
        mEditName = (EditText) findViewById(R.id.edit_name);
        mResetPasswordBtn = (Button) findViewById(R.id.reset_password);
        mSaveBtn = (Button) findViewById(R.id.save);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        profileSettingsActivity = this;
        sharedPref = this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String name = sharedPref.getString("name","Temp");
        String base64Image = sharedPref.getString("imageLocation", "");
        byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        mName = name;
        mEditName.setText(name);
        mEditName.setSelection(mEditName.getText().length());
        newImgBitMap = decodedByte;
        mProfileImg.setImageBitmap(decodedByte);

        mProfileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);

            }
        });
        mEditName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEditName.setCursorVisible(true);
            }
        });
        mEditName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    mEditName.clearFocus();
                    mEditName.setCursorVisible(false);
                    String editName = mEditName.getText().toString().trim();
                    mEditName.setText(editName);
                    if (editName.equals("")) {
                        mEditName.setText(mName);
                    }
                    else {
                        mName = editName;

                    }
                }
                return false;
            }
        });
        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertDialog();
                saveUserInfo();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch(requestCode) {
            case SELECT_PHOTO:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    InputStream imageStream = null;
                    try {
                        imageStream = getContentResolver().openInputStream(selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    newImgBitMap = BitmapFactory.decodeStream(imageStream);
                    newImgBitMap = Bitmap.createScaledBitmap(newImgBitMap, 200, 200, false);
                    mProfileImg.setImageBitmap(newImgBitMap);
                }
        }
    }

    private void saveUserInfo() {

        FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid()).child("name").setValue(mName);

        FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid()).child("imageLocation").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String imgloc = (String) dataSnapshot.getValue();
                Log.d("imgloc", imgloc);
                storageRef = storage.getReferenceFromUrl(imgloc);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                newImgBitMap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                final byte[] data = baos.toByteArray();
                UploadTask uploadTask = storageRef.putBytes(data);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("upload", "failed");
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.d("upload", "success");
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        newImgBitMap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                        byte[] byteArray = byteArrayOutputStream .toByteArray();
                        String imageLocation = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("name", mName);
                        editor.putString("imageLocation", imageLocation);
                        editor.commit();



                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void showAlertDialog() {
        LayoutInflater li = LayoutInflater.from(this);
        View dialogView = li.inflate(R.layout.profile_setting_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(dialogView);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        alertDialog.show();
        alertDialog.getWindow().setLayout(800, -2);
    }
}
