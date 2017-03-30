package com.example.koshal.atttendance;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

public class SplashScreen extends AppCompatActivity {

    LinearLayout ll;
    public static  SharedPreferences sp;
    public static SharedPreferences.Editor edit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        sp=getSharedPreferences("shared",MODE_PRIVATE);
        edit=sp.edit();

/*
        ll= (LinearLayout) findViewById(R.id.activity_splash_screen);

        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(SplashScreen.this,Password.class);
                startActivity(i);
                finish();

            }
        });*/
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if(sp.getString("login_status","false").equals("false")) {
                    Intent i = new Intent(SplashScreen.this, LoginActivity.class);
                    startActivity(i);
                }
                else if(sp.getString("profile","").equals("Student"))
                {
                    Intent i = new Intent(SplashScreen.this, MainActivity_Student.class);
                    startActivity(i);
                }
                else {
                    Intent i = new Intent(SplashScreen.this, MainActivity_Prof.class);
                    startActivity(i);

                }
                finish();

            }
        },1200);
    }
}
