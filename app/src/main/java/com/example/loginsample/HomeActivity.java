package com.example.loginsample;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        String username = getIntent().getStringExtra("username");

        TextView welcomeTextView = findViewById(R.id.textView2);
        welcomeTextView.setText("Bienvenido " + username);
    }
}
