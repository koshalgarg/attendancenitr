package com.example.koshal.atttendance;



import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.database.sqlite.SQLiteDatabase.openOrCreateDatabase;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolders> {


    ArrayList<Courses> courses;
    Context context;
    int lastPosition = -1;

    public RecyclerViewAdapter(Context mainActivity, ArrayList<Courses> courses) {
        this.courses = courses;
        this.context = mainActivity;

    }

    @Override
    public RecyclerViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.tv_courses, parent, false);
        //layoutView.setMinimumHeight(parent.getMeasuredHeight() / 2);
        RecyclerViewHolders rcv = new RecyclerViewHolders(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolders holder, final int position) {
        holder.Course_name.setText(courses.get(position).getCourse_name());
        holder.Course_id.setText("C_ID: "+courses.get(position).getCourse_id());
        holder.v.setText("Dept: "+courses.get(position).getCourse_dept());
        holder.a.setText("Sem: "+String.valueOf( courses.get(position).getCourse_sem()));
    }


    @Override
    public int getItemCount() {
        return this.courses.size();

    }

    public class RecyclerViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        public TextView Course_name,a,v;

        CardView cv;
        public TextView Course_id;

        public RecyclerViewHolders(View itemView) {
            super(itemView);
            Course_name = (TextView) itemView.findViewById(R.id.tv_course_name);

            Course_id = (TextView) itemView.findViewById(R.id.tv_course_id);
            cv = (CardView) itemView.findViewById(R.id.cv_cards);
            v= (TextView) itemView.findViewById(R.id.tv_course_dept);
            a= (TextView) itemView.findViewById(R.id.tv_course_sem);
            cv.setOnClickListener(this);
            cv.setOnLongClickListener(this);
        }


        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.cv_cards) {
                onclick_card(getAdapterPosition());
            }
        }


        @Override
        public boolean onLongClick(View view) {
            if (view.getId() == R.id.cv_cards) {
                onlongclick_card(getAdapterPosition());

            }

            return true;
        }


        private void onlongclick_card(final int adapterPosition) {

           // Toast.makeText(context, "long click", Toast.LENGTH_SHORT).show();




            final AlertDialog.Builder ab = new AlertDialog.Builder(context);
            ab.setMessage("Are you sure to delete?");
            ab.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    int id=courses.get(adapterPosition).getId();
                    delete_course(id,courses.get(adapterPosition).getCourse_sem(),courses.get(adapterPosition).getCourse_id(),courses.get(adapterPosition).getCourse_dept(),MainActivity_Prof.prof_id);
                    courses.remove(adapterPosition);
                    notifyDataSetChanged();
                }
            });
            ab.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
            ab.show();
        }
        private void delete_course(int id, int sem, String course_id, String course_dept, String prof_id) {

            String url=context.getResources().getString(R.string.url)+"prof_delete_courses.php";
            final Map<String,String> m =new HashMap<String, String>();
            m.put("p_id",prof_id);
            m.put("id",id+"");
            m.put("sem",sem+"");
            m.put("dept",course_dept);
            m.put("code",course_id);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {

                        @Override

                        public void onResponse(String response) {
                            Log.i("log_signup_response",response);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(final VolleyError error) {
                    Toast.makeText(context, "ERROR in connection", Toast.LENGTH_SHORT).show();
                }

            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params=m;
                    return params;
                }
            };
            MySingleTon.getInstance(context).addtoRequestQueue(stringRequest);
        }

        private void onclick_card(int adapterPosition) {
           // Toast.makeText(context, "" + courses.get(adapterPosition).getId(), Toast.LENGTH_SHORT).show();
            Intent i=new Intent(context,CourseActivity_Prof.class);
            i.putExtra("c_id",String.valueOf(courses.get(adapterPosition).getId()));
            i.putExtra("course_name",String.valueOf(courses.get(adapterPosition).getCourse_name()));
            i.putExtra("course_id",String.valueOf(courses.get(adapterPosition).getCourse_id()));
            context.startActivity(i);
        }
    }
}