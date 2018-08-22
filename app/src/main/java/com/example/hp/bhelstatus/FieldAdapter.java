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

import java.util.List;

public class FieldAdapter extends ArrayAdapter<Field> {
        private Context mcontext;
        private List<Field> flist;

        public FieldAdapter(Context context, int resource, List<Field> objects) {
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
            final Field current = getItem(position);

            ImageView img = (ImageView) list_item.findViewById(R.id.img);
            img.setImageResource(R.drawable.round_create_black_18dp);

            list_item.setOnClickListener(new View.OnClickListener() {
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
                    mcontext.startActivity(myIntent);
                }
            });
            TextView d = (TextView) list_item.findViewById(R.id.textView);
            TextView val = (TextView) list_item.findViewById(R.id.textView1);
            TextView circle = (TextView) list_item.findViewById(R.id.circle);
            d.setText(current.getFieldName()+":");
            if(TextUtils.equals(current.getFieldValue(),"Add text here." )){
                Log.v("Equated","yes");
                val.setTextColor(Color.parseColor("#D3D3D3"));
                val.setTextSize(16);
            }
            else{
                val.setTextColor(Color.parseColor("#000000"));
                val.setTextSize(24);
            }
            val.setText(current.getFieldValue());
            circle.setText(String.valueOf(current.getFieldName().charAt(0)));
            return list_item;
        }
    }


