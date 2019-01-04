package com.example.hp.bhelstatus;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.List;

public class ShortageAdapter extends ArrayAdapter<Short> {
    private Context mcontext;
    private List<Short> flist;
    DatabaseReference db;
    String year,q;

    public ShortageAdapter(Context context, int resource, List<Short> objects) {
        super(context, resource, objects);
        flist = objects;
        mcontext = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @Nullable ViewGroup parent) {

        View list_item = convertView;
        if (list_item == null) {
            list_item = LayoutInflater.from(getContext()).inflate(R.layout.list_field, parent, false);
        }
        final Short current = getItem(position);

        ImageView img = (ImageView) list_item.findViewById(R.id.img);
        img.setVisibility(View.GONE);

        list_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(mcontext, ShortageView.class);
                myIntent.putExtra("des", current.getDes());
                myIntent.putExtra("mat", current.getMat());
                myIntent.putExtra("rem", current.getRem());
                myIntent.putExtra("sho", current.getSho());
                myIntent.putExtra("no", current.getNo());
                myIntent.putExtra("pdc", current.getPdc());
                myIntent.putExtra("sid", current.getSid());
                myIntent.putExtra("pid", current.getPid());
                myIntent.putExtra("id", current.getId());
                myIntent.putExtra("year", current.getYear());
                myIntent.putExtra("q", current.getQ());
                myIntent.putExtra("name", current.getName());
                //myIntent.putExtra("projectName", current.getProjectName());
                myIntent.putExtra("deptname", current.getDeptname());
                myIntent.putExtra("n", current.getN());
                mcontext.startActivity(myIntent);
            }
        });

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
                        db.child(current.getId()).child(year).child(q).child(current.getPid()).child("svalues").child(current.getSid()).removeValue();
                        dialog.dismiss();
                    }

                })

                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                })
                .create();

        list_item.setLongClickable(true);

        list_item.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                myQuittingDialogBox.show();
                return false;
            }
        });

        TextView d = (TextView) list_item.findViewById(R.id.textView);
        TextView val = (TextView) list_item.findViewById(R.id.textView1);
        d.setText(current.getMat()+":");
        if(TextUtils.equals(current.getDes(),"Add text here." )){
            Log.v("Equated","yes");
            val.setTextColor(Color.parseColor("#D3D3D3"));
        }
        else{
            val.setTextColor(Color.parseColor("#000000"));
        }
        val.setText(current.getDes());
        return list_item;
    }
}


