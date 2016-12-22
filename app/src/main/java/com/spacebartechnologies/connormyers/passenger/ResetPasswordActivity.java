package com.spacebartechnologies.connormyers.passenger;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by tommyduong23 on 12/21/16.
 */

public class ResetPasswordActivity extends AppCompatActivity {
    TextView mTitleToolbar;
    ImageButton mNavBackButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mTitleToolbar = (TextView) findViewById(R.id.title_toolbar);
        mTitleToolbar.setText("Reset Password");

        mNavBackButton = (ImageButton) findViewById(R.id.back_button);
        mNavBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                finish();
                startActivity(intent);
            }
        });
    }
}
