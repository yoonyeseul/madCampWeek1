package com.example.madcampweek1.ui.notifications;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.madcampweek1.R;

public class TodoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);

        getSupportActionBar().hide();
    }
}