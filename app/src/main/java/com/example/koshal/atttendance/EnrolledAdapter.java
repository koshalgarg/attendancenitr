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

public class EnrolledAdapter extends RecyclerView.Adapter<EnrolledAdapter.RecyclerViewHolders> {


    ArrayList<Enrolled_courses> courses;
    Context context;


    public EnrolledAdapter(Context SearchPageActivity, ArrayList<Enrolled_courses> courses) {
        this.courses = courses;
        this.context = SearchPageActivity;

    }

    @Override
    public RecyclerViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.enrolled_courses_row, parent, false);

        RecyclerViewHolders rcv = new RecyclerViewHolders(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolders holder, final int position) {
        holder.cname.setText(courses.get(position).getC_name());
        holder.cid.setText(courses.get(position).getC_id());
        holder.fname.setText(courses.get(position).getF_name());
        holder.tc.setText("Total: "+courses.get(position).getTotal_classes());
        holder.pc.setText("Present: "+courses.get(position).getPresent_classes());

        int p= Integer.parseInt(courses.get(position).total_classes);
        int t= Integer.parseInt(courses.get(position).present_classes);

        float per= (float) ((t*1.0/p)*100.0);
        int pa=(int)per;

        holder.percent.setText("Percentage: "+pa);


    }


    @Override
    public int getItemCount() {
        return this.courses.size();

    }

    public class RecyclerViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView cname, cid, fname, sem, dept,tc,pc,percent;

        CardView cv;
        public TextView Course_id;

        public RecyclerViewHolders(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.search_card);
            cname = (TextView) itemView.findViewById(R.id.cname);
            cid = (TextView) itemView.findViewById(R.id.cid);
            fname = (TextView) itemView.findViewById(R.id.fname);
            tc = (TextView) itemView.findViewById(R.id.total_classes);
            pc = (TextView) itemView.findViewById(R.id.present_classes);
            percent=(TextView) itemView.findViewById(R.id.percent);



            cv.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.search_card && courses.size()>0) {

                onclick_card(getAdapterPosition());
            }
        }

        private void onclick_card(final int adapterPosition) {

            String id = String.valueOf(courses.get(adapterPosition).getId());
            String c_name=courses.get(adapterPosition).getC_name();
            String c_id=courses.get(adapterPosition).getC_id();
            String s_id = SplashScreen.sp.getString("id", "-1");
           // fetch_data(id, s_id);
            Intent i=new Intent(context,AttendanceDetailsActivity.class);
            i.putExtra("id",id);
            i.putExtra("s_id",s_id);
            i.putExtra("c_name",c_name);
            i.putExtra("c_id",c_id);
            context.startActivity(i);
        }

    }
}