package com.example.koshal.atttendance;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity_Prof extends AppCompatActivity {

    LinearLayoutManager lLayout;
    RecyclerView recyclerView;
    ArrayList<Courses> courses;
    RecyclerViewAdapter rcAdapter;
    FloatingActionButton fab;
    int requestcode=1;

    public static String prof_email,prof_name,prof_contact_no,prof_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__prof);

        Toolbar tb= (Toolbar) findViewById(R.id.tb);
        setSupportActionBar(tb);


        SharedPreferences sp=SplashScreen.sp;
        if(sp.getString("login_status","false").equals("false"))
        {
            startActivity(new Intent(MainActivity_Prof.this,MainActivity_Prof.class));
            finish();
        }
        prof_email=sp.getString("email","a");
        prof_contact_no=sp.getString("contact_no","a");
        prof_name=sp.getString("name","a");
        prof_id=sp.getString("id","a");

        RVfunc();

        fab=(FloatingActionButton)findViewById(R.id.fabadd);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity_Prof.this,AddCourseActivity.class));


            }
        });
    }

    private void RVfunc() {

        recyclerView = (RecyclerView) findViewById(R.id.rv_courses);

        courses=new ArrayList<>();
        lLayout = new LinearLayoutManager(this);
        rcAdapter = new RecyclerViewAdapter(this, courses);


        lLayout = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(lLayout);

        recyclerView.setAdapter(rcAdapter);
        setData();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.logout)
        {
            logout();


        }
        return true;
    }

    private void logout() {
        AlertDialog.Builder ab=new AlertDialog.Builder(this);
        ab.setTitle("Logout");
        ab.setMessage("are you sure to logout?");
        ab.setPositiveButton("logout", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                SplashScreen.edit.putString("login_status","false");
                SplashScreen.edit.commit();
                startActivity(new Intent(MainActivity_Prof.this,LoginActivity.class));
                finish();
            }
        });
        ab.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog abv=ab.create();
        abv.show();
    }


    private void setData() {

        String url=getResources().getString(R.string.url)+"prof_fetch_courses.php";
        final Map<String,String> m =new HashMap<String, String>();
        m.put("p_id",prof_id);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        courses.clear();
                        Log.i("log_prof_courses",response);
                        try {
                            JSONArray arr=new JSONArray(response);
                            for(int i=0;i<arr.length();i++) {
                                JSONObject obj=arr.getJSONObject(i);
                                Courses a = new Courses();
                                a.setId(Integer.parseInt(obj.getString("id")));
                                a.setCourse_dept(obj.getString("dept"));
                                a.setCourse_sem(Integer.parseInt(obj.getString("sem")));
                                a.setCourse_id(obj.getString("code"));
                                a.setCourse_name(obj.getString("name"));
                                courses.add(a);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        rcAdapter.notifyDataSetChanged();

                                    }
                                });
                            }
                        } catch (Exception e) {
                            Toast.makeText(MainActivity_Prof.this, "Email already Exists", Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError error) {
                Toast.makeText(MainActivity_Prof.this, "ERROR in connection", Toast.LENGTH_SHORT).show();
            }

        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params=m;
                return params;
            }
        };
        MySingleTon.getInstance(MainActivity_Prof.this).addtoRequestQueue(stringRequest);
        
    }

    @Override
    protected void onResume() {
        super.onResume();
       setData();
    }
}

