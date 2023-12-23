package com.zeroai.wallperhd;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private Button buttonGetStarted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Make the activity full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash); // Make sure this is the name of your layout file

        progressBar = findViewById(R.id.progressBar);
        buttonGetStarted = findViewById(R.id.buttonGetStarted);

        // Simulate progress bar loading
        new Thread(new Runnable() {
            public void run() {
                for (int progress = 0; progress <= 100; progress += 10) {
                    // Update the progress bar
                    progressBar.setProgress(progress);
                    try {
                        Thread.sleep(300); // Sleep for 300 milliseconds
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                // After the loop completes (progress bar is full), hide the progress bar and show the "Get Started" button
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        buttonGetStarted.setVisibility(View.VISIBLE);
                    }
                });
            }
        }).start();

        // Set click listener for the "Get Started" button
        buttonGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent to start MainActivity
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // Close the SplashActivity
            }
        });
    }
}
