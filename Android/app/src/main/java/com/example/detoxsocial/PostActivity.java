package com.example.detoxsocial;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.detoxsocial.Fragments.HomeFragment;
import com.example.detoxsocial.Fragments.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class PostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);


    }
}