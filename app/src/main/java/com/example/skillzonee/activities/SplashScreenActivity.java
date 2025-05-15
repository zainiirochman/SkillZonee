package com.example.android_quizappwithfirebase.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.example.android_quizappwithfirebase.R;
import com.google.firebase.auth.FirebaseAuth;

@SuppressLint("CustomSplashScreen")
public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // Hide the action bar if it exists
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Create a new Handler to delay the transition to the MainActivity
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
//                startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
//                finish();
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    // Sudah login, langsung ke MainActivity
                    startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                } else {
                    // Belum login, arahkan ke LoginActivity
                    startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
                }
                finish();
            }
        }, 2500); // 2500 milliseconds delay (2.5 seconds)
    }
}
