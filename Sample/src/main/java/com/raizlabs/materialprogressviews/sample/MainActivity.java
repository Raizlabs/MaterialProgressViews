package com.raizlabs.materialprogressviews.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.raizlabs.materialprogressviews.CircularProgressView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CircularProgressView indicator = (CircularProgressView) findViewById(R.id.loading);

        indicator.animateContinuous();
    }
}
