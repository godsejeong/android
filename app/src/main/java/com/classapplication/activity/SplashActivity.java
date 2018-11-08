package com.classapplication.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.classapplication.R;
import com.classapplication.activity.LoginActivity;
import com.classapplication.activity.MainActivity;
import com.classapplication.data.UserData;

import java.util.List;

import ninja.sakib.pultusorm.core.PultusORM;

public class SplashActivity extends AppCompatActivity {
    PultusORM orm;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        String path = getApplicationContext().getFilesDir().getAbsolutePath();
        orm = new PultusORM("user.db", path);

        try {
            List<Object> userlist = orm.find(new UserData());
            UserData user = (UserData) userlist.get(userlist.size() - 1);
            token = user.getToken();
        } catch (Exception e) {
            //null
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (token != null) {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                } else if (token == null) {
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    finish();
                }
            }
        }, 500);
    }
}
