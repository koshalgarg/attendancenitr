package com.example.koshal.atttendance;



import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
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
import java.util.StringTokenizer;

import static android.content.Context.MODE_APPEND;
import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.RecyclerViewHolders> {


    ArrayList<Search_courses> courses;
    Context context;
    int lastPosition = -1;

    public SearchAdapter(Context SearchPageActivity, ArrayList<Search_courses> courses) {
        this.courses = courses;
        this.context = SearchPageActivity;

    }

    @Override
    public RecyclerViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_row_searchview, parent, false);
        //layoutView.setMinimumHeight(parent.getMeasuredHeight() / 2);
        RecyclerViewHolders rcv = new RecyclerViewHolders(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolders holder, final int position) {
        holder.cname.setText(courses.get(position).getC_name());
        holder.cid.setText(courses.get(position).getC_id());
        holder.fname.setText(courses.get(position).getF_name());
        holder.sem.setText(courses.get(position).getSem());
        holder.dept.setText(courses.get(position).getC_dept());
    }


    @Override
    public int getItemCount() {
        return this.courses.size();

    }

    public class RecyclerViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView cname,cid,fname,sem,dept;

        CardView cv;
        public TextView Course_id;

        public RecyclerViewHolders(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.search_card);
            cname = (TextView) itemView.findViewById(R.id.cname);
            cid = (TextView) itemView.findViewById(R.id.cid);
            fname = (TextView) itemView.findViewById(R.id.fname);
            sem = (TextView) itemView.findViewById(R.id.sem);
            dept = (TextView) itemView.findViewById(R.id.dept);

            cv.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.search_card) {
                onclick_card(getAdapterPosition());
            }
        }

        private void onclick_card(final int adapterPosition) {

            AlertDialog.Builder ab=new AlertDialog.Builder(context);
            ab.setTitle("confirm");
            ab.setMessage("are you sure to add the course?");
            ab.setPositiveButton("confirm", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    String id=String.valueOf(courses.get(adapterPosition).getId());
                    String s_id=SplashScreen.sp.getString("id","-1");
                    fetch_data(id,s_id);
                }
            });
            ab.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            AlertDialog ad=ab.create();
            ad.show();
           
        }
    }
    private void fetch_data(String id,String s_id) {

        String url=context.getResources().getString(R.string.url)+"enroll_student.php";
        final Map<String,String> m =new HashMap<>();
        m.put("c_id",id);
        m.put("s_id",s_id);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {

                    @Override

                    public void onResponse(String response)
                    {
                            Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener()
                {
            @Override
            public void onErrorResponse(final VolleyError error)
            {
                Toast.makeText(context, "ERROR in connection", Toast.LENGTH_SHORT).show();
            }

        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> params = new HashMap<String, String>();
                params=m;
                return params;
            }
        };
        MySingleTon.getInstance(context).addtoRequestQueue(stringRequest);

    }
}