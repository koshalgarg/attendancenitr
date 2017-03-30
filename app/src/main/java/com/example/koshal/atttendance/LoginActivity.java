package com.example.koshal.atttendance;

import android.animation.Animator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.codetail.animation.ViewAnimationUtils;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    Button login,login_go,signup,signup_go;
    LinearLayout login_form,signup_form;
    EditText login_email,login_pwd,su_email,su_pwd,su_pwd2,su_contact_no,su_name;
    Spinner dropdown;
    RadioButton rb1,rb2,rb_s1,rb_s2;
    RadioGroup rb_s;
    RadioGroup rb;
    TextView txt;
    TextInputLayout tx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        Toolbar tb= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        tb.setTitleTextColor(Color.WHITE);
        txt= (TextView) findViewById(R.id.testtext);
        tx= (TextInputLayout) findViewById(R.id.text_input);
        if(SplashScreen.sp.getString("login_status","false").equals("true"))
        {
            if(SplashScreen.sp.getString("profile","Student").equals("Student"))
            {
                startActivity(new Intent(LoginActivity.this,MainActivity_Student.class));
                finish();

            }
            else
            {
                startActivity(new Intent(LoginActivity.this,MainActivity_Prof.class));
                finish();
            }
        }

        initialize();





    }

    private void initialize() {
        rb1 = (RadioButton) findViewById(R.id.prof);
        rb2 = (RadioButton) findViewById(R.id.stud);
        rb1.setSelected(true);
        rb_s1= (RadioButton) findViewById(R.id.prof_s);
        rb_s2= (RadioButton) findViewById(R.id.stud_s);
        rb_s1.setSelected(true);
        rb = (RadioGroup) findViewById(R.id.rg);
        rb.check(R.id.prof);
        login = (Button) findViewById(R.id.btn_login);
        login.setOnClickListener(this);
        login_go = (Button) findViewById(R.id.btn_login_go);
        login_go.setOnClickListener(this);
        rb_s = (RadioGroup) findViewById(R.id.rg_s);
        rb_s.check(R.id.prof_s);
        signup = (Button) findViewById(R.id.btn_signup);
        signup.setOnClickListener(this);
        signup_go = (Button) findViewById(R.id.btn_signup_go);
        signup_go.setOnClickListener(this);


        login_form = (LinearLayout) findViewById(R.id.ll_login_form);
        signup_form = (LinearLayout) findViewById(R.id.ll_signup_form);

        login_email = (EditText) findViewById(R.id.et_login_email);
        login_pwd = (EditText) findViewById(R.id.et_login_pwd);

        su_email = (EditText) findViewById(R.id.et_signup_email);
        su_pwd = (EditText) findViewById(R.id.et_signup_pwd);
        su_pwd2 = (EditText) findViewById(R.id.et_signup_pwd2);
        su_contact_no = (EditText) findViewById(R.id.et_signup_phone);
        su_name = (EditText) findViewById(R.id.et_signup_name);

        su_pwd2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                validateEditText(editable);


            }
        });


    }
    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private void validateEditText(Editable s)
    {
        if (su_pwd2.getText().toString().length()==0) {
            tx.setError(null);
        //    Toast.makeText(this,"hi",Toast.LENGTH_SHORT).show();
            requestFocus(su_pwd2);
        }
        else if(!su_pwd2.getText().toString().equals(su_pwd.getText().toString())) {
                tx.setError("password dont match");
                //Toast.makeText(this,"no",Toast.LENGTH_SHORT).show();
                requestFocus(su_pwd2);
            }

        else
        {
            tx.setErrorEnabled(false);
        }
    }
    private void signup() {
        String url=getResources().getString(R.string.url)+"signup.php";
        final Map<String,String> m =new HashMap<String, String>();
        m.put("name",su_name.getText().toString());

        m.put("email",su_email.getText().toString());
        m.put("phone",su_contact_no.getText().toString());
        m.put("pwd",su_pwd.getText().toString());

        if(rb_s.getCheckedRadioButtonId()==R.id.prof_s){


            m.put("type", "professor");
            Log.i("mmmm","professor");
        }
        else if(rb_s.getCheckedRadioButtonId()==R.id.stud_s) {
            m.put("type", "Student");
            Log.i("mmmm","Students");
        }
//        Toast.makeText(this, v.fetchdata(url,m,LoginActivity.this), Toast.LENGTH_SHORT).show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {

                    @Override

                    public void onResponse(String response) {

                         Log.i("log_signup_response",response);
                        try {
                            JSONObject obj=new JSONObject(response);

                            SplashScreen.edit.putString("login_status", "true");

                            SplashScreen.edit.putString("email", obj.getString("email"));

                            SplashScreen.edit.putString("name", obj.getString("name"));

                            SplashScreen.edit.putString("contact_no", obj.getString("contact_no"));

                            SplashScreen.edit.putString("id", obj.getString("id"));


                            if(rb_s.getCheckedRadioButtonId()==R.id.prof_s){

                                SplashScreen.edit.putString("profile", "prof");
                                startActivity(new Intent(LoginActivity.this,MainActivity_Prof.class));
                                finish();
                            }
                            else if(rb_s.getCheckedRadioButtonId()==R.id.stud_s) {

                                SplashScreen.edit.putString("profile", "Student");
                                startActivity(new Intent(LoginActivity.this,MainActivity_Student.class));
                                finish();
                            }


                            SplashScreen.edit.commit();
                        } catch (Exception e) {
                            Toast.makeText(LoginActivity.this, "email already exists", Toast.LENGTH_SHORT).show();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError error) {
              //  Toast.makeText(LoginActivity.this, "ERROR in connection", Toast.LENGTH_SHORT).show();
                Snackbar.make(signup_go,"error in connection",Snackbar.LENGTH_SHORT).show();
            }

        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params=m;
                return params;
            }
        };

        MySingleTon.getInstance(LoginActivity.this).addtoRequestQueue(stringRequest);

    }

    private void login() {
        String url=getResources().getString(R.string.url)+"login.php";
        // Volly v=new Volly();
        final Map<String,String> m =new HashMap<String, String>();
        m.put("email",login_email.getText().toString());
        m.put("pwd",login_pwd.getText().toString());

        if(rb.getCheckedRadioButtonId()==R.id.prof){


            m.put("type", "professor");
            Log.i("mmmm","professor");
        }
        else if(rb.getCheckedRadioButtonId()==R.id.stud) {
            m.put("type", "Student");
            Log.i("mmmm","Students");
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {

                    @Override

                    public void onResponse(String response) {

                        Log.i("log_login_response",response);
                        try {

                            JSONObject obj=new JSONObject(response);
                            SplashScreen.edit.putString("login_status", "true");
                            SplashScreen.edit.putString("email", obj.getString("email"));
                            SplashScreen.edit.putString("name", obj.getString("name"));
                            SplashScreen.edit.putString("contact_no", obj.getString("contact_no"));
                            SplashScreen.edit.putString("id", obj.getString("id"));

                            if(rb.getCheckedRadioButtonId()==R.id.prof){

                                SplashScreen.edit.putString("profile", "prof");
                                startActivity(new Intent(LoginActivity.this,MainActivity_Prof.class));
                                finish();
                            }
                            else if(rb.getCheckedRadioButtonId()==R.id.stud) {
                                SplashScreen.edit.putString("profile", "Student");
                                int cx = (login_go.getLeft() + login_go.getRight()) / 2;
                                int cy = (login_go.getTop() + login_go.getBottom()) / 2;

                                // get the final radius for the clipping circle
                                int dx = Math.max(cx, login_go.getWidth() - cx);
                                int dy = Math.max(cy, login_go.getHeight() - cy);
                                float finalRadius = (float) Math.hypot(dx, dy);

                                // Android native animator
                                Animator animator =
                                        ViewAnimationUtils.createCircularReveal(login_go, cx, cy, 0, finalRadius);
                                animator.setInterpolator(new AccelerateDecelerateInterpolator());
                                animator.setDuration(1500);
                                animator.start();

                                startActivity(new Intent(LoginActivity.this,MainActivity_Student.class));
                                finish();
                            }

                            SplashScreen.edit.commit();
                        } catch (Exception e) {
                            Toast.makeText(LoginActivity.this, "Error in Login", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError error) {

                Toast.makeText(LoginActivity.this, "ERROR in connection", Toast.LENGTH_SHORT).show();
            }

        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params=m;
                return params;
            }
        };

        MySingleTon.getInstance(LoginActivity.this).addtoRequestQueue(stringRequest);

    }

    @Override
    public void onClick(View view) {
        switch(view.getId())
        {
            case R.id.btn_login:
                login_form.setVisibility(View.VISIBLE);
                signup_form.setVisibility(View.GONE);
                break;
            case R.id.btn_login_go:
                if(login_email.getText().toString().length()==0 || login_pwd.getText().toString().length()==0)
                {
                    //Toast.makeText(this, "Fill All the Fields", Toast.LENGTH_SHORT).show();
                    Snackbar.make(login,"Fill all the fields",Snackbar.LENGTH_SHORT).show();
                    return;
                }
                login();


                break;

            case R.id.btn_signup:
                login_form.setVisibility(View.GONE);
                signup_form.setVisibility(View.VISIBLE);
                break;

            case R.id.btn_signup_go:
                if(su_name.getText().toString().length()==0 || su_email.getText().toString().length()==0 || su_contact_no.getText().toString().length()==0 || su_pwd.getText().toString().length()==0 || su_pwd2.getText().toString().length()==0  )
                {
                    //Toast.makeText(this, "Fill All the Fields", Toast.LENGTH_SHORT).show();
                    Snackbar.make(signup_go,"Fill all the fields",Snackbar.LENGTH_SHORT).show();

                    return;
                }
                if(!su_pwd.getText().toString().equals(su_pwd2.getText().toString()))
                {
                    Toast.makeText(this, "PAssword Mismatch", Toast.LENGTH_SHORT).show();
                    return;
                }
                signup();
                break;
        }
    }
}
