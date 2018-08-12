package com.example.hp.bhelstatus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class MainField extends AppCompatActivity {
    final HashMap<String, String> map = new HashMap<>();
    String fname;
    EditText editText;
    EditText input;
    DatabaseReference mField;
    int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.field_main);
        LinearLayout ll = (LinearLayout) findViewById(R.id.field_main);
        Button button = new Button(this);
        button.setId(View.generateViewId());

        mField = FirebaseDatabase.getInstance().getReference("bhel").child("fields");

        Intent intent = getIntent();
        final int n = intent.getIntExtra("n", 0);
        String name = intent.getStringExtra("name");
        Log.e("name",name);
        Log.e("n",""+n);

        map.put("name", name);
        for (i = 0; i < n; i++) {
            editText = new EditText(this);
            editText.setHint("Enter name of field" + i);
            editText.setId(i);
            ll.addView(editText);
        }
        ll.addView(button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (i = 0; i < n; i++) {
                    input = (EditText) findViewById(i);
                    fname = input.getText().toString().trim();
                    map.put("field" + i, fname);
                    Log.e("map",map.get("field"+i));
                }
                String id = mField.push().getKey();
                map.put("id", id);
                mField.child(id).setValue(map);
            }
        });


    }
}
