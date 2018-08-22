package com.example.hp.bhelstatus;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DeptList extends AppCompatActivity {

    EditText vname;
    EditText vno;
    Button butt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Add Department");
        vname = (EditText) findViewById(R.id.name);
        vno = (EditText) findViewById(R.id.no);
        butt = (Button) findViewById(R.id.butt);
        butt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = vname.getText().toString().trim();
                String no = vno.getText().toString().trim();
                if(name == null ||  !isInteger(no)){
                    Toast.makeText(DeptList.this, "Please enter valid data.", Toast.LENGTH_LONG).show();
                }
                else {
                    int n = Integer.parseInt(no);
                    Intent myIntent = new Intent(getApplicationContext(), MainField.class);
                    myIntent.putExtra("name", name);
                    myIntent.putExtra("n", n);
                    startActivity(myIntent);
                }
            }
        });
    }
    public static boolean isInteger(String s) {
        int t;
        try {
           t = Integer.parseInt(s);
        } catch(NumberFormatException e) {
            return false;
        } catch(NullPointerException e) {
            return false;
        }

        if(t<1||t>100)
            return false;
        return true;
    }
}