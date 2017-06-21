package com.innovation.studio.paintpic.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

/**
 * Activity for showing the splash screen to the user.
 *
 * @author Chaitanya Agrawal
 */
public class SplashActivity extends AppCompatActivity {
    private static final int START_DELAY_MS = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        (new Handler()).postDelayed(new Runnable() {
            @Override
            public void run() {
                final Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                // TODO(krypten) : Check the method to do finishAfterTransition in sdk below LOLLIPOP
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAfterTransition();
                }
            }
        }, START_DELAY_MS);
    }
}
