package com.example.barcodescanner.Admin;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.barcodescanner.R;
import com.tbuonomo.morphbottomnavigation.MorphBottomNavigationView;

public class AdminActivity extends AppCompatActivity {
    MorphBottomNavigationView morphBottomNavigationView ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

    }
}