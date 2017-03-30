package com.example.koshal.atttendance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
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

import java.util.HashMap;
import java.util.Map;

public class AttendanceDetailsActivity extends AppCompatActivity {

    String c_name,c_id,id,s_id;
    TextView tv_s_name,tv_c_name,tv_c_id,tv_total, tv_present,tv_per;
    TableLayout sheet;
    int total;
    int absent;
    double per;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_details);

        init();

        Toast.makeText(this, c_name+c_id+s_id+id, Toast.LENGTH_SHORT).show();
    }

    private void init() {

        Intent i=getIntent();
        c_name=i.getStringExtra("c_name");
        c_id=i.getStringExtra("c_id");
        id=i.getStringExtra("id");
        s_id=i.getStringExtra("s_id");

        tv_c_id= (TextView) findViewById(R.id.tv_c_id_2);
        tv_c_name= (TextView) findViewById(R.id.tv_c_name_2);
        tv_s_name= (TextView) findViewById(R.id.tv_s_name_2);

        tv_total= (TextView) findViewById(R.id.tv_total);
        tv_present = (TextView) findViewById(R.id.tv_present);
        tv_per= (TextView) findViewById(R.id.tv_per);

        tv_c_id.setText(c_id);
        tv_c_name.setText(c_name);
        tv_s_name.setText(s_id);

        total=0;absent=0;

        sheet= (TableLayout) findViewById(R.id.tl_sheet);

        TableRow t=new TableRow(this);
        TextView v=getTV();
        v.setText("DATE");
        t.addView(v);
        v=getTV();
        v.setText("status");
        t.addView(v);
        sheet.addView(t);
        
        fetchData();
        





    }

    private void fetchData() {
        String url=getResources().getString(R.string.url)+"sheet_student_course.php";
        Toast.makeText(this, url, Toast.LENGTH_SHORT).show();
        Log.i("logurl",url);
        final Map<String,String> m =new HashMap<String, String>();
        m.put("s_id",s_id);
        m.put("c_id",id);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        Toast.makeText(AttendanceDetailsActivity.this, response, Toast.LENGTH_SHORT).show();
                        Log.i("log_login_response",response);

                        showData(response);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError error) {
                Toast.makeText(AttendanceDetailsActivity.this, "ERROR in connection", Toast.LENGTH_SHORT).show();
            }

        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params=m;
                return params;
            }
        };

        MySingleTon.getInstance(AttendanceDetailsActivity.this).addtoRequestQueue(stringRequest);
    }

    private void showData(String response) {

        try {
            JSONArray arr=new JSONArray(response);
            total=arr.length();

            for(int i=0;i<arr.length();i++)
            {
                JSONObject obj=arr.getJSONObject(i);
                TableRow t=new TableRow(this);
                TextView v=getTV();
                v.setText(obj.getString("date"));
                t.addView(v);
                v=getTV();
                v.setText(obj.getString("status"));
                if(v.getText().toString().equals("1"))
                    absent++;
                t.addView(v);
                sheet.addView(t);
            }

        } catch (JSONException e) {
            Toast.makeText(AttendanceDetailsActivity.this, "Json error", Toast.LENGTH_SHORT).show();
        }

        tv_total.setText(total+"");
        tv_present.setText(absent+"");
        per=((absent*1.0)*100)/total;
        tv_per.setText(per+"%");


    }

    TextView getTV()
    {
        TextView v=new TextView(this);
        v.setBackground(getResources().getDrawable(R.drawable.cell));
        v.setGravity(Gravity.CENTER);
        v.setPadding(6,6,6,6);
        v.setHeight(150);

        return v;
    }
}
