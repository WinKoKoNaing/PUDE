package com.porlar.techhousestudio.pude.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.porlar.techhousestudio.pude.BuildConfig;
import com.porlar.techhousestudio.pude.R;
import com.porlar.techhousestudio.pude.ui.MainActivity;

public class FirstActivity extends AppCompatActivity {
    private TextView tvVersionName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        tvVersionName = findViewById(R.id.tvVersionName);
        tvVersionName.setText("Version "+BuildConfig.VERSION_NAME);
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                Intent i = new Intent(FirstActivity.this, MainActivity.class);

                startActivity(i);


                finish();

            }

        }, 1000); // wait for 5 seconds
    }
}
