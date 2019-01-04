package com.example.hp.bhelstatus;

import android.content.Context;
import android.content.Intent;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ProgressAdapter extends ArrayAdapter<Progress> {
    private Context mcontext;
    private List<Progress> flist;
    int c,admin;
    ImageView img;

    public ProgressAdapter(Context context, int resource, List<Progress> objects) {
        super(context, resource, objects);
        flist = objects;
        mcontext = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @Nullable ViewGroup parent) {

        View list_item = convertView;
        if (list_item == null) {
            list_item = LayoutInflater.from(getContext()).inflate(R.layout.list_progress, parent, false);
        }
        final Progress current = getItem(position);

        DatabaseReference dab = FirebaseDatabase.getInstance().getReference("bhel").child("fields").child(current.getId()).child("ab");

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

        /*
        Log.i("J", current.getFieldName()+"><"+current.getType()+"><"+current.getAdmin());
        if(current.getAdmin()==0 || (current.getAdmin() == 2 && current.getType() == 0)) {
            Log.i("K", "Came here");
            img.setVisibility(View.GONE);
        }
        if(current.getAdmin()==1 || (current.getAdmin() == 2 && current.getType() == 1)) {
            Log.i("J", "Came here ");
            img.setVisibility(View.VISIBLE);
        }
        if(img.getVisibility()!=View.GONE) {
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent myIntent = new Intent(mcontext, FieldChange.class);
                    myIntent.putExtra("fieldname", current.getFieldName());
                    myIntent.putExtra("fieldvalue", current.getFieldValue());
                    myIntent.putExtra("n", current.getN());
                    myIntent.putExtra("id", current.getId());
                    myIntent.putExtra("pid", current.getPid());
                    myIntent.putExtra("projectName", current.getProjectName());
                    myIntent.putExtra("deptName", current.getDeptName());
                    myIntent.putExtra("number", current.getNumber());
                    myIntent.putExtra("c", c);
                    mcontext.startActivity(myIntent);
                }
            });
        }
        */

        TextView d = (TextView) list_item.findViewById(R.id.textView);
        TextView from = (TextView) list_item.findViewById(R.id.textView1);
        TextView to = (TextView) list_item.findViewById(R.id.textView2);
        d.setText(current.getFieldName()+":");
        if(TextUtils.equals(current.getFromValue(),"Add text here." )){
            Log.v("Equated","yes");
            from.setTextColor(Color.parseColor("#D3D3D3"));
            from.setTextSize(16);
        }
        else{
            from.setTextColor(Color.parseColor("#000000"));
        }
        from.setText(current.getFromValue());

        to.setTextColor(Color.parseColor("#000000"));
        to.setText(current.getToValue());
        return list_item;
    }
}


