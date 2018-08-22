package com.example.hp.bhelstatus;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class MainField extends AppCompatActivity {
    final HashMap<String, String> map = new HashMap<>();
    String fname,name;
    EditText editText;
    EditText input;
    DatabaseReference mField;
    int i,n;
    TextView text;
    LinearLayout child;
    boolean f = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Enter Field Names");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.field_main);
        LinearLayout ll = (LinearLayout) findViewById(R.id.field_main);
        Button button = new Button(this);
        button.setId(View.generateViewId());
        button.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.button));
        button.setText("SUBMIT");
        button.setTextColor(Color.parseColor("#ffffff"));
        button.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        mField = FirebaseDatabase.getInstance().getReference("bhel").child("fields");

        Intent intent = getIntent();
        n = intent.getIntExtra("n", 0);
        name = intent.getStringExtra("name");
        Log.e("name",name);
        Log.e("n",""+n);

        map.put("name", name);
/*
        LinearLayout.LayoutParams param1 = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1.0f
        );

        LinearLayout.LayoutParams param0 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                0.0f
        );

        for (i = 0; i < n; i++) {

            child = new LinearLayout(this);
            child.setOrientation(LinearLayout.HORIZONTAL);
            child.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            text = new TextView(this);
            text.setText("Field "+(i+1));
            text.setTextColor(Color.parseColor("#000000"));
            text.setLayoutParams(param0);

            editText = new EditText(this);
            editText.setId(i);
            text.setTextColor(Color.parseColor("#000000"));
            text.setLayoutParams(param1);

            child.addView(text);
            child.addView(editText);

            ll.setOrientation(LinearLayout.VERTICAL);
            ll.addView(child);
        }
        */

        View view;
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for (int i = 0; i < n; i++) {

            view= inflater.inflate(R.layout.new_field_layout, null);

            text = (TextView) view.findViewById(R.id.text);
            text.setText("Field "+(i+1)+":");

            editText = (EditText) view.findViewById(R.id.edit);
            editText.setId(i);

            ll.addView(view);
        }

        ll.addView(button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                f = true;
                for (i = 0; i < n && f; i++) {
                    input = (EditText) findViewById(i);
                    fname = input.getText().toString().trim();
                    if (TextUtils.isEmpty(fname)) {
                        Toast.makeText(MainField.this, "Please enter valid data.", Toast.LENGTH_LONG).show();
                        f = false;
                        break;
                    } else {
                        map.put("field" + i, fname);
                        Log.e("map", map.get("field" + i));
                    }
                }
                if(f) {
                    String id = mField.push().getKey();
                    map.put("id", id);
                    map.put("n", "" + n);
                    mField.child(id).setValue(map);

                    Toast.makeText(MainField.this, "Department "+ name + " added succesfully.", Toast.LENGTH_LONG).show();

                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                }
            }
        });
    }
}
