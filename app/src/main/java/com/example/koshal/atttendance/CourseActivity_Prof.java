package com.example.koshal.atttendance;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class CourseActivity_Prof extends AppCompatActivity {


    int c_id;
    String course_name,course_id;
    TextView take,sheet,students;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        Toolbar tb= (Toolbar) findViewById(R.id.tb);
        setSupportActionBar(tb);
        init();
    }

    private void init() {
        Intent i=getIntent();
        c_id= Integer.parseInt(i.getStringExtra("c_id"));
        course_id=i.getStringExtra("course_id");
        course_name=i.getStringExtra("course_name");
        take= (TextView) findViewById(R.id.tv_take);
        sheet= (TextView) findViewById(R.id.tv_sheet);

       // course= (TextView) findViewById(R.id.tv_course);

        sheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i=new Intent(CourseActivity_Prof.this,AttendanceSheetActivity.class);
                i.putExtra("c_id",String.valueOf(c_id));
                startActivity(i);

            }
        });



        take.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(CourseActivity_Prof.this,TakeAttendanceActivity.class);
                i.putExtra("c_id",c_id+"");
                i.putExtra("course_name",course_name);
                i.putExtra("course_id",course_id);
                startActivity(i);
            }
        });

        //course.setText(course_name+"\n"+course_id);

    }
}
