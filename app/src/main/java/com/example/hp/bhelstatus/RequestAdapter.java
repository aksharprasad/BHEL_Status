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

public class RequestAdapter extends ArrayAdapter<UserRequest> {
    private Context mcontext;
    private List<UserRequest> plist;
    String name;

    public RequestAdapter(Context context, int resource, List<UserRequest> objects) {
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
        final UserRequest current = getItem(position);

        TextView circle = (TextView) list_item.findViewById(R.id.circle);
        list_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(mcontext, RequestDetails.class);
                myIntent.putExtra("name", current.getName());
                myIntent.putExtra("eid", current.getEid());
                myIntent.putExtra("email", current.getEmail());
                myIntent.putExtra("dob", current.getDob());
                myIntent.putExtra("cred", current.getCred());
                myIntent.putExtra("id", current.getId());
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
