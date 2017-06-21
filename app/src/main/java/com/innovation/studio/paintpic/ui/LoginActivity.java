package com.innovation.studio.paintpic.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.innovation.studio.paintpic.R;

/**
 * A login screen that offers login via email/password.
 *
 * @author Chaitanya Agrawal
 */
public class LoginActivity extends AppCompatActivity {
    CallbackManager mCallbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mCallbackManager = CallbackManager.Factory.create();
        final LoginManager loginManager = LoginManager.getInstance();
        loginManager.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
                Toast.makeText(getApplicationContext(), "onSuccess", Toast.LENGTH_LONG);
                MainActivity.sIsLoggedIn = true;
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), "onCancel", Toast.LENGTH_LONG);
            }

            @Override
            public void onError(final FacebookException error) {
                Toast.makeText(getApplicationContext(), "onError", Toast.LENGTH_LONG);
            }
        });
        findViewById(R.id.login_button_skip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.sIsLoggedIn = true;
                finish();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }
}

