package com.example.hp.bhelstatus;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.List;
import android.content.SharedPreferences;


public class ProjectAdapter extends ArrayAdapter<Project> {
    private Context mcontext;
    private List<Project> plist;
    String name,year,qtr,yr,q;
    View list_item;
    int bg = 0;
    int c;
    DatabaseReference db;

    public ProjectAdapter(Context context, int resource, List<Project> objects) {
        super(context, resource, objects);
        plist = objects;
        mcontext = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @Nullable ViewGroup parent) {

        list_item = convertView;
        if (list_item == null) {
            list_item = LayoutInflater.from(getContext()).inflate(R.layout.list_project, parent, false);
        }
        final Project current = getItem(position);

        View stat = list_item.findViewById(R.id.img);
        View shor = list_item.findViewById(R.id.shor);
        ImageView download = (ImageView) list_item.findViewById(R.id.download);

        DatabaseReference dab = FirebaseDatabase.getInstance().getReference("bhel").child("fields").child(current.getID()).child("ab");

        dab.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                c = Integer.parseInt(dataSnapshot.getValue(String.class));
                Log.i("AB",""+c);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        //list_item.setBackground(ContextCompat.getDrawable(mcontext, R.color.white_greyish));
        TextView circle = (TextView) list_item.findViewById(R.id.circle);
        stat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(mcontext, ProjectView.class);
                myIntent.putExtra("name", current.getName());
                myIntent.putExtra("id", current.getID());
                myIntent.putExtra("pid", current.getpID());
                myIntent.putExtra("n", current.getN());
                myIntent.putExtra("deptname", current.getDeptname());
                myIntent.putExtra("c",c );
                mcontext.startActivity(myIntent);
            }
        });

        download.setImageResource(R.drawable.baseline_history_black_18dp);

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(mcontext, datePick.class);
                myIntent.putExtra("name", current.getName());
                myIntent.putExtra("id", current.getID());
                myIntent.putExtra("pid", current.getpID());
                myIntent.putExtra("n", current.getN());
                myIntent.putExtra("deptname", current.getDeptname());
                myIntent.putExtra("c",c );
                mcontext.startActivity(myIntent);
            }
        });
        /*list_item.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.i("TAG", "onItemLongClick: 0");
                if (bg == 0) {
                    list_item.setBackground(ContextCompat.getDrawable(mcontext, R.color.red));
                    Log.i("TAG", "onItemLongClick: 1");
                } else if (bg == 1) {
                    list_item.setBackground(ContextCompat.getDrawable(mcontext, R.color.white_greyish));
                    Log.i("TAG", "onItemLongClick: 2");
                    return true;
                }
                return false;
            }
        });*/

        Calendar c = Calendar.getInstance();
        String qtr = "Quater 1";
        int yr = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        switch (month){
            case 1:
            case 2:
            case 3:
                qtr = "Quarter 1";
                break;
            case 4:
            case 5:
            case 6:
                qtr = "Quarter 2";
                break;
            case 7:
            case 8:
            case 9:
                qtr = "Quarter 3";
                break;
            case 10:
            case 11:
            case 12:
                qtr = "Quarter 4";
                break;
        }

        SharedPreferences mPrefs = mcontext.getSharedPreferences("YearPref",0);
        year = mPrefs.getString("year", ""+yr);
        q = mPrefs.getString("quarter", qtr );

        db = FirebaseDatabase.getInstance().getReference("bhel").child("projects");

        final AlertDialog myQuittingDialogBox =new AlertDialog.Builder(getContext())
                //set message, title, and icon
                .setTitle("Delete")
                .setMessage("Do you want to Delete")
                .setIcon(R.drawable.round_delete_black_18dp)

                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        db.child(current.getID()).child(year).child(q).child(current.getpID()).removeValue();
                        dialog.dismiss();
                    }

                })

                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                })
                .create();

        shor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(mcontext, Shortage.class);
                myIntent.putExtra("name", current.getName());
                myIntent.putExtra("id", current.getID());
                myIntent.putExtra("pid", current.getpID());
                myIntent.putExtra("n", current.getN());
                myIntent.putExtra("deptname", current.getDeptname());
                mcontext.startActivity(myIntent);
            }
        });

        list_item.setLongClickable(true);

        list_item.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                myQuittingDialogBox.show();
                return false;
            }
        });

        TextView d = (TextView) list_item.findViewById(R.id.textView);
        name = current.getName();
        d.setText(name);
        circle.setVisibility(View.GONE);

        return list_item;
    }


}
