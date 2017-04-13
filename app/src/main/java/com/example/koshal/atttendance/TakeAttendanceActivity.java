package com.example.koshal.atttendance;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class TakeAttendanceActivity extends AppCompatActivity {

    int c_id;
    boolean boolstart;
    String course_name,course_id;
    TextView tv_date,tv_name,tv_abs,tv_num;
    Button btn_start,btn_yes,btn_no,btn_submit;
    ImageView btn_prev,btn_netx;

    ArrayList<Students> studentsArrayList;
    LinearLayout ll_details;
    HashMap<String,String> attendance;
    int cur_stud=0;
    int cur_id;
    
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_attendance);

        init();
    }
    private void init() {
        boolstart=false;
        Intent i=getIntent();
        c_id= Integer.parseInt(i.getStringExtra("c_id"));
        
        course_id=i.getStringExtra("course_id");
        
        course_name=i.getStringExtra("course_name");
        final Toolbar tb= (Toolbar) findViewById(R.id.tb);
        setSupportActionBar(tb);
        setTitle(course_name);
        tb.setTitleTextColor(Color.WHITE);


        tv_date= (TextView) findViewById(R.id.tv_date);
        tv_name= (TextView) findViewById(R.id.tv_name_roll);
        tv_abs= (TextView) findViewById(R.id.tv_absent);
        
        btn_start= (Button) findViewById(R.id.btn_start_abort);
        btn_no=(Button) findViewById(R.id.btn_no);
        btn_yes=(Button) findViewById(R.id.btn_yes);
        btn_submit=(Button) findViewById(R.id.btn_submit);
        btn_prev=(ImageView) findViewById(R.id.btn_prev);
        btn_netx=(ImageView) findViewById(R.id.btn_next);
        tv_num= (TextView) findViewById(R.id.tv_number);



        ll_details= (LinearLayout) findViewById(R.id.ll_student_details);
        attendance=new HashMap<>();

        btn_netx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cur_stud<studentsArrayList.size()-1)
                {
                    cur_stud++;
                    setDetails();
                }
            }
        });

        btn_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cur_stud>0)
                {
                    cur_stud--;
                    setDetails();
                }
            }
        });


        tv_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(boolstart)
                    return;
                Calendar c=Calendar.getInstance();
                int year=c.get(Calendar.YEAR);
                int mtn=c.get(Calendar.MONTH);
                int day=c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog=new DatePickerDialog(TakeAttendanceActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int date) {

                        String yr= String.valueOf(year);
                        String mon= String.valueOf(month+1);
                        if(month+1<10)
                            mon="0"+mon;

                        String dt= String.valueOf(date);
                        if(date<10)
                            dt="0"+dt;
                        tv_date.setText(yr+"-"+mon+"-"+dt);
                    }
                },year,mtn,day);
                dialog.show();
            }
        });

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(tv_date.getText().toString().equals("Date"))
                {
                    Toast.makeText(TakeAttendanceActivity.this, "choose date", Toast.LENGTH_SHORT).show();
                    return;
                }
                /*if(boolstart)
                {
                    abort();
                }*/


                boolstart=true;
               // btn_start.setText("ABORT");
                studentsArrayList=new ArrayList<Students>();
                fetch_student_details();

            }


        });
        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                attendance.put(studentsArrayList.get(cur_stud).getS_id(),"1");
                cur_stud++;

                if(cur_stud==studentsArrayList.size())
                {
                    Toast.makeText(TakeAttendanceActivity.this, "submit your response", Toast.LENGTH_SHORT).show();
                    cur_stud--;
                }
                setDetails();


            }
        });
        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attendance.put(studentsArrayList.get(cur_stud).getS_id(),"0");
                cur_stud++;
                if(cur_stud==studentsArrayList.size())
                {
                    Toast.makeText(TakeAttendanceActivity.this, "submit your response", Toast.LENGTH_SHORT).show();
                    cur_stud--;
                }

                setDetails();
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject obj=new JSONObject(attendance);

                String url=getResources().getString(R.string.url)+"submit_details.php";

                final Map<String,String> m =new HashMap<String, String>();
                m.put("c_id", String.valueOf(c_id));
                m.put("date", String.valueOf(tv_date.getText().toString()));
                m.put("data", obj.toString());



                final StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {

                            @Override

                            public void onResponse(String response) {

                                studentsArrayList.clear();
                                Log.i("log_login_response",response);
                                Toast.makeText(TakeAttendanceActivity.this, "Successfully submitted", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(TakeAttendanceActivity.this,MainActivity_Prof.class));
                                finish();

                            }

                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(final VolleyError error) {
                        Toast.makeText(TakeAttendanceActivity.this, "ERROR in connection", Toast.LENGTH_SHORT).show();
                    }

                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params=m;
                        return params;
                    }
                };

                MySingleTon.getInstance(TakeAttendanceActivity.this).addtoRequestQueue(stringRequest);

            }
        });


    }

    private void abort() {
    }

    private void submit_attendance() {

       // Toast.makeText(this, today.getText().toString(), Toast.LENGTH_SHORT).show();
     //   b.setVisibility(View.GONE);
    }

    private void fetch_student_details() {

        String url=getResources().getString(R.string.url)+"fetch_student_details.php";

        final Map<String,String> m =new HashMap<String, String>();
        m.put("c_id", String.valueOf(c_id));
        m.put("date", String.valueOf(tv_date.getText().toString()));
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {

                    @Override

                    public void onResponse(String response) {

                        studentsArrayList.clear();
                        Log.i("log_login_response",response);

                        try {
                            JSONArray arr=new JSONArray(response);
                            if(arr.length()==0)
                            {
                                Toast.makeText(TakeAttendanceActivity.this, "No students registered", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            for(int i=0;i<arr.length();i++)
                            {
                                Students s=new Students();
                                JSONObject obj=arr.getJSONObject(i);
                                attendance.put(obj.getString("id"),"1");
                                s.setS_name(obj.getString("name"));
                                s.setS_id(obj.getString("id"));
                                s.setS_roll(obj.getString("roll"));
                                s.setAbsent(Integer.parseInt(obj.getString("classes"))-Integer.parseInt(obj.getString("pre")));
                                studentsArrayList.add(s);
                            }
                            Log.i("log_size",studentsArrayList.size()+"");
                            cur_stud=0;
                            setDetails();
                            btn_submit.setVisibility(View.VISIBLE);
                            ll_details.setVisibility(View.VISIBLE);


                        } catch (JSONException e) {
                            Toast.makeText(TakeAttendanceActivity.this, "JSON ERROR", Toast.LENGTH_SHORT).show();
                        }

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError error) {
                Toast.makeText(TakeAttendanceActivity.this, "ERROR in connection", Toast.LENGTH_SHORT).show();
            }

        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params=m;
                return params;
            }
        };

        MySingleTon.getInstance(TakeAttendanceActivity.this).addtoRequestQueue(stringRequest);

    }

    private void setDetails() {

        int size=studentsArrayList.size();
                if(cur_stud<0 && cur_stud>=size)
                {
                    return;
                }
        tv_num.setText((cur_stud+1)+"/"+size);
        tv_name.setText(studentsArrayList.get(cur_stud).getS_name()+"\n"+studentsArrayList.get(cur_stud).getS_roll());
        tv_abs.setText("Absents "+studentsArrayList.get(cur_stud).getAbsent()+"");
    }
}

