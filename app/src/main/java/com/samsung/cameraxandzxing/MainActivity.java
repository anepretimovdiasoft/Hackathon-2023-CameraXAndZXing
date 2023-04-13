package com.samsung.cameraxandzxing;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.samsung.cameraxandzxing.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding activityMainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());

        

    }
}