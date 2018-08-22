package com.example.hp.bhelstatus;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class DepartmentAdapter extends ArrayAdapter<Department> {
    private Context mcontext;
    private List<Department> dlist;

    public DepartmentAdapter(Context context, int resource, List<Department> objects) {
        super(context, resource, objects);
        dlist = objects;
        mcontext = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @Nullable ViewGroup parent) {

        String name;
        View list_item = convertView;
        if (list_item == null) {
            list_item = LayoutInflater.from(getContext()).inflate(R.layout.list, parent, false);
        }
        final Department current = getItem(position);

        TextView circle = (TextView) list_item.findViewById(R.id.circle);
/*
        GradientDrawable drawable = (GradientDrawable) circle.getBackground().mutate();

        switch (current.isP()) {
            case 0:
                drawable.setColor(0xffff4c4c);
                break;
            case 1:
                drawable.setColor(0xffffff66);
                break;
            case 2:
                drawable.setColor(0xff4c4cff);
                break;
            case 3:
                drawable.setColor(0xff6ef33a);
                break;
        }
*/
        list_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent myIntent = new Intent(mcontext, ProjectList.class);
                myIntent.putExtra("name", current.getName());
                myIntent.putExtra("id", current.getID());
                myIntent.putExtra("n", current.getN());
                mcontext.startActivity(myIntent);
            }
        });
        TextView d = (TextView) list_item.findViewById(R.id.textView);
        name = current.getName();
        d.setText(name);
        circle.setText(String.valueOf(name.charAt(0)));

        return list_item;
    }
}
