package com.example.koshal.atttendance;

import android.animation.Animator;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
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

import io.codetail.animation.ViewAnimationUtils;

public class MainActivity_Student extends AppCompatActivity {

    SearchView searchView;

    ArrayList<Enrolled_courses> search_courses;
    RecyclerView recyclerView;
    LinearLayoutManager llayout;
    EnrolledAdapter rcAdapter;
    ArrayList<String> spinneraray;
    LinearLayout ll;
    FloatingActionButton fab;
    TextInputLayout tx;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__student);

        Toolbar tb= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        fab= (FloatingActionButton) findViewById(R.id.fabsearch);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                        startActivity(new Intent(MainActivity_Student.this,SearchPageActivity.class));



            }
        });
        search_courses=new ArrayList<>();
        rv_func();
        SharedPreferences sp1=SplashScreen.sp;

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
                SplashScreen.edit.putString("passcode",null);
                SplashScreen.edit.commit();
                startActivity(new Intent(MainActivity_Student.this,LoginActivity.class));
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

    @Override
    protected void onResume() {
        super.onResume();
       fetch_data(SplashScreen.sp.getString("id",null));
    }

    private void rv_func() {

        recyclerView = (RecyclerView) findViewById(R.id.recycle);

        search_courses=new ArrayList<>();

        rcAdapter = new EnrolledAdapter(this, search_courses);


        llayout = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(llayout);

        recyclerView.setAdapter(rcAdapter);

    }


    private void fetch_data(String query) {

        search_courses.clear();
        String url=getResources().getString(R.string.url)+"student_enrolled_courses.php";
        final Map<String,String> m =new HashMap<String, String>();
        m.put("s_id",query);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {

                    @Override

                    public void onResponse(String response) {

                        Log.i("log_signup_response",response);
                        try {
                            JSONArray arr=new JSONArray(response);
                            for(int i=0;i<arr.length();i++) {
                                JSONObject obj=arr.getJSONObject(i);
                                Enrolled_courses a = new Enrolled_courses();
                                a.setF_name(obj.getString("fac_name"));
                                a.setC_name(obj.getString("name"));
                                a.setC_id(obj.getString("code"));
                                a.setPresent_classes(obj.getString("pre"));
                                a.setTotal_classes(obj.getString("total"));
                                a.setId(Integer.parseInt(obj.getString("id")));

                                search_courses.add(a);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        rcAdapter.notifyDataSetChanged();

                                    }
                                });
                            }

                        } catch (Exception e) {
                            Log.i("dibya",e.toString());
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError error) {

                Snackbar snackbar = Snackbar.make(fab, "Error in connection" , Snackbar.LENGTH_LONG);

                snackbar.show();
            }

        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params=m;
                return params;
            }
        };
        MySingleTon.getInstance(MainActivity_Student.this).addtoRequestQueue(stringRequest);

    }
}

