package com.example.koshal.atttendance;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AttendanceSheetActivity extends AppCompatActivity {

    String c_id;
    ArrayList<Students> students;
    HashMap<String,String> dates;
    ArrayList<String> datesarray;
    TableLayout tl_sheet;
    int arr[][];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_sheet);
        Intent i=getIntent();
        c_id=i.getStringExtra("c_id");
        dates=new HashMap<>();
        datesarray=new ArrayList<>();

        students=new ArrayList<>();
        tl_sheet= (TableLayout) findViewById(R.id.tl_sheet);
        fetch_data();
    }

    private void fetch_data() {
        String url=getResources().getString(R.string.url)+"fetch_attendance_sheet.php";
        // Volly v=new Volly();
        final Map<String,String> m =new HashMap<String, String>();
        m.put("c_id",c_id);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {

                    @Override

                    public void onResponse(String response) {

                        Log.i("log_login_response",response);
                        setData(response);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError error) {
                Toast.makeText(AttendanceSheetActivity.this, "ERROR in connection", Toast.LENGTH_SHORT).show();
            }

        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params=m;
                return params;
            }
        };

        MySingleTon.getInstance(AttendanceSheetActivity.this).addtoRequestQueue(stringRequest);

    }

    public void setData(String jsonstring) {

        try {

            JSONObject obj=new JSONObject(jsonstring);
            JSONArray datesarr=obj.getJSONArray("alldates");

            JSONArray students=obj.getJSONArray("attendance");
            arr=new int[students.length()][datesarr.length()];

            //initializing to zero
            for(int i=0;i<arr.length;i++)
            {
                for(int j=0;j<arr[0].length;j++)
                {
                    arr[i][j]=0;

                }
            }

            addToDates(datesarr);
            addToStudents(students);
            showMAtrix();





        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void showMAtrix() {


        TableRow row= new TableRow(this);
        TextView v=getTV();
        v.setText("SLNO");
        row.addView(v);

        TextView v1=getTV();
        v1.setText("NAME");
        row.addView(v1);

        TextView v2=getTV();
        v2.setText("ROLL");
        row.addView(v2);


      for(int i=0;i<datesarray.size();i++)
        {
            TextView v3=getTV();
            v3.setText(datesarray.get(i));
            row.addView(v3);
        }

        tl_sheet.addView(row);


        for(int i=0;i<arr.length;i++)
        {
            TableRow r=new TableRow(this);
            TextView a=getTV();
            a.setText((i+1)+"");
            r.addView(a);

            TextView b=getTV();
            b.setText(students.get(i).getS_name());
            r.addView(b);

            TextView c=getTV();
            c.setText(students.get(i).getS_roll());
            r.addView(c);

            final int p=i;



            for(int j=0;j<arr[0].length;j++)
            {

                final int q=j;
                final TextView d=getTV();
                d.setText(arr[i][j]+"");
                r.addView(d);
                d.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        new AlertDialog.Builder(AttendanceSheetActivity.this)
                                .setTitle("Change Status")
                                .setMessage("Are you sure you want to change this entry?")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        change_status(c_id,students.get(p).getS_id(),datesarray.get(q),p,q,d);
                                    }
                                })
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // do nothing
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();

                        Log.i("loglongpress",c_id+" "+ students.get(p).getS_id()+" "+datesarray.get(q));
                        return true;
                    }
                });
            }

            tl_sheet.addView(r);

        }

    }

    private void change_status(final String c_id, String s_id, String s, final int p, final int q, final TextView d) {

        String url=getResources().getString(R.string.url)+"change_status.php";
        // Volly v=new Volly();
        final Map<String,String> m =new HashMap<String, String>();
        m.put("c_id",c_id);
        m.put("s_id",s_id);
        m.put("date",s);
        m.put("stat",String.valueOf((arr[p][q]+1)%2));

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {

                    @Override

                    public void onResponse(String response) {

                        Log.i("log_login_response",response);
                        if(response.equals("s"))
                        {
                            int k=(arr[p][q]+1)%2;
                            arr[p][q]=k;
                            d.setText(k+"");
                        }
                        else {
                            Toast.makeText(AttendanceSheetActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(final VolleyError error) {
                Toast.makeText(AttendanceSheetActivity.this, "ERROR in connection", Toast.LENGTH_SHORT).show();
            }

        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params=m;
                return params;
            }
        };

        MySingleTon.getInstance(AttendanceSheetActivity.this).addtoRequestQueue(stringRequest);

    }


    private void addToStudents(JSONArray studentsjsonarr) {
        for(int i=0;i<studentsjsonarr.length();i++)
        {
            Students s=new Students();
            try {
                JSONObject o=studentsjsonarr.getJSONObject(i);
                s.setS_name(o.getString("name"));
                s.setS_roll(o.getString("roll"));
                s.setS_id(o.getString("s_id"));
                students.add(s);
                JSONArray jarr=o.getJSONArray("dates");

                for(int j=0;j<jarr.length();j++)
                {
                    JSONObject newobj=jarr.getJSONObject(j);
                    String date=newobj.getString("date");
                    String stat=newobj.getString("status");
                    int pos= Integer.parseInt(dates.get(date));
                    arr[i][pos]=Integer.parseInt(stat);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    private void addToDates(JSONArray datesarr) {
        for(int i=0;i<datesarr.length();i++)
        {
            try {
                JSONObject o=datesarr.getJSONObject(i);
                dates.put(o.getString("date"),String.valueOf(i));
                datesarray.add(o.getString("date"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
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
