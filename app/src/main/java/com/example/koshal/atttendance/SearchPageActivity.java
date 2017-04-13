package com.example.koshal.atttendance;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SearchPageActivity extends AppCompatActivity {

    ArrayList<Search_courses> search_courses;
    RecyclerView recyclerView;
    LinearLayoutManager llayout;
    SearchAdapter rcAdapter;
    LinearLayout ll;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_page);
        ll= (LinearLayout) findViewById(R.id.activity_search_page);
        Toolbar tb= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tb);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            search_courses=new ArrayList<>();
            rv_func();
        SearchView sc= (SearchView) findViewById(R.id.seachview);
        sc.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                fetch_data(query);

                View view = SearchPageActivity.this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }
    private void rv_func() {

        recyclerView = (RecyclerView) findViewById(R.id.recycle);

        search_courses=new ArrayList<>();

        rcAdapter = new SearchAdapter(this, search_courses);


        llayout = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(llayout);

        recyclerView.setAdapter(rcAdapter);

    }


    private void fetch_data(String query) {

        search_courses.clear();
        String url=getResources().getString(R.string.url)+"query.php";
        final Map<String,String> m =new HashMap<String, String>();
        m.put("query",query);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {

                    @Override

                    public void onResponse(String response) {

                        Log.i("log_signup_response",response);
                        try {
                            JSONArray arr=new JSONArray(response);
                            for(int i=0;i<arr.length();i++) {
                                JSONObject obj=arr.getJSONObject(i);
                                Search_courses a = new Search_courses();
                                a.setId(Integer.parseInt(obj.getString("c_id")));
                                a.setC_name(obj.getString("c_name"));
                                a.setC_dept(obj.getString("dept"));
                                a.setSem(obj.getString("sem"));
                                a.setF_name(obj.getString("fac_name"));
                                a.setC_id(obj.getString("c_code"));
                                search_courses.add(a);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        rcAdapter.notifyDataSetChanged();

                                    }
                                });
                            }

                        } catch (Exception e) {
                            //Toast.makeText(SearchPageActivity.this, "Email already Exists", Toast.LENGTH_SHORT).show();
                            Log.i("dibya",e.toString());
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError error) {
               Toast.makeText(SearchPageActivity.this, error.toString() ,Toast.LENGTH_SHORT).show();
                Log.i("error", error.toString());
            }

        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params=m;
                return params;
            }
        };
        MySingleTon.getInstance(SearchPageActivity.this).addtoRequestQueue(stringRequest);

    }
}
