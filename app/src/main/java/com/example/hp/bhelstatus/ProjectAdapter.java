package com.example.hp.bhelstatus;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ProjectAdapter extends ArrayAdapter<Project> {
    private Context mcontext;
    private List<Project> plist;

    public ProjectAdapter(Context context, int resource, List<Project> objects) {
        super(context, resource, objects);
        plist = objects;
        mcontext = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @Nullable ViewGroup parent) {

        View list_item = convertView;
        if (list_item == null) {
            list_item = LayoutInflater.from(getContext()).inflate(R.layout.list, parent, false);
        }
        final Project current = getItem(position);

        list_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Add later.
            }
        });
        TextView d = (TextView) list_item.findViewById(R.id.textView);
        d.setText(current.getName());

        return list_item;
    }
}
