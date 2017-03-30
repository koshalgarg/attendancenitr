package com.example.koshal.atttendance;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddCourseActivity extends AppCompatActivity {

    EditText cname,cid,sem,dept;
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);
        Toolbar tb= (Toolbar) findViewById(R.id.tb);
        setSupportActionBar(tb);
        db=openOrCreateDatabase("attendance",MODE_PRIVATE,null);
        cname= (EditText) findViewById(R.id.cname);
        cid= (EditText) findViewById(R.id.course_id);
        sem= (EditText) findViewById(R.id.semester);
        dept=(EditText) findViewById(R.id.dept);
        Button add= (Button) findViewById(R.id.addcourse);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                value();
            }
        });
    }

    private void value()
    {
        if(cname.getText().toString().equals("") || cid.getText().toString().equals("") || sem.getText().toString().equals("") ||dept.getText().toString().equals(""))
        {
            Toast.makeText(this,"please fill all the details",Toast.LENGTH_LONG).show();
            return;
        }
        else
        {
            String url=getResources().getString(R.string.url)+"addcourse.php";
            final Map<String,String> m =new HashMap<String, String>();
            m.put("name",cname.getText().toString());
            m.put("id",cid.getText().toString());
            m.put("sem",sem.getText().toString());
            m.put("dept",dept.getText().toString());
            m.put("p_id",SplashScreen.sp.getString("id","0"));

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {

                        @Override

                        public void onResponse(String response) {

                            Log.i("log_signup_response",response);


                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(final VolleyError error) {
                    Toast.makeText(AddCourseActivity.this, "ERROR in connection", Toast.LENGTH_SHORT).show();
                }

            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params=m;
                    return params;
                }
            };

            MySingleTon.getInstance(AddCourseActivity.this).addtoRequestQueue(stringRequest);
            startActivity(new Intent(this,MainActivity_Prof.class));
            finish();
        }
    }
}