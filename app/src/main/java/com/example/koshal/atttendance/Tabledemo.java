package com.example.koshal.atttendance;

import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class Tabledemo extends AppCompatActivity {

    TableLayout tb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabledemo);

        tb = (TableLayout) findViewById(R.id.table);


        int ar[][] = new int[5][5];

        int cnt=0;
        for (int i = 0; i < 50; i++)
        {
            TableRow r=new TableRow(this);

            for (int j = 0; j < 5; j++) {
                TextView tv=new TextView(this);
                tv.setText(j+"");
                tv.setMinWidth(500);
                tv.setMinHeight(500);
                tv.setTextSize(24);
                tv.setTextColor(getResources().getColor(R.color.btntextcolor));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                }
                if(cnt%2==0)
                {
                    tv.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                }
                else
                    tv.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

                cnt++;


                r.addView(tv);
            }
            tb.addView(r);
        }
    }
}
