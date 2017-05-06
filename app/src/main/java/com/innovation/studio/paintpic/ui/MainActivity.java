package com.innovation.studio.paintpic.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.innovation.studio.paintpic.R;

/**
 * Main Activity displaying the content to the users.
 */
public class MainActivity extends AppCompatActivity {
	public static boolean sIsLoggedIn = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		FacebookSdk.sdkInitialize(getApplicationContext());
		AppEventsLogger.activateApp(this);
		if (!sIsLoggedIn) {
			startActivity(new Intent(this, LoginActivity.class));
		}
		Toast.makeText(this, "hi", Toast.LENGTH_LONG);
	}
}
