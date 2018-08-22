package com.example.hp.bhelstatus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class ProjectAdd extends AppCompatActivity {
    String name,deptname,pid,id,ns;
    EditText vname,vid;
    DatabaseReference db;
    int n,i;
    DatabaseReference dbp,dbn;
    HashMap<String, String> map = new HashMap<>();
    Spinner year, quater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.project_add);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        deptname = intent.getStringExtra("deptname");
        n = intent.getIntExtra("n",0 );

        setTitle("Add Project under "+ deptname );

        vname = (EditText) findViewById(R.id.name);
        Button butt = (Button) findViewById(R.id.butt);
        db = FirebaseDatabase.getInstance().getReference("bhel").child("projects").child(id);

        year = (Spinner) findViewById(R.id.year);
        quater = (Spinner) findViewById(R.id.quarter);

        Calendar calender;
        calender= Calendar.getInstance();
        int yr= calender.get(Calendar.YEAR);
        ArrayList<String> years= new ArrayList<String>();
        for(i=yr+3;i>=2000;i--)
            years.add(Integer.toString(i));
        ArrayAdapter<String> adapter= new ArrayAdapter<String>(this,R.layout.view_spinner,years);
        year.setAdapter(adapter);

        ArrayList<String> q = new ArrayList<String>();
        q.add("Quater 1");
        q.add("Quater 2");
        q.add("Quater 3");
        q.add("Quater 4");
        ArrayAdapter<String> qadapter= new ArrayAdapter<String>(this,R.layout.view_spinner,q);
        quater.setAdapter(qadapter);


        butt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = vname.getText().toString().trim();
                if(!TextUtils.isEmpty(name)) {
                    Toast.makeText(ProjectAdd.this,
                            "OnClickListener : " +
                                    "\nSpinner 1 : "+ quater.getSelectedItem().toString() +
                                    "\nSpinner 2 : "+ year.getSelectedItem().toString(),
                            Toast.LENGTH_LONG).show();

                    pid = db.push().getKey();
                    Project p = new Project(name, id, pid, n,deptname);
                    db.child(pid).setValue(p);

                    //Log.i("pid PA",pid);
                    dbp = FirebaseDatabase.getInstance().getReference("bhel").child("projects").child(id).child(pid);

                    Log.i("out", "damn");

                    for (i = 0; i < n; i++) {
                        //Log.i("i at PA",""+i);
                        map.put("field" + i, "Add text here.");
                        if (TextUtils.isEmpty((map.get("field" + i))))
                            Log.i("Map", map.get("field" + i));
                        else
                            Log.i("Map", "nil");
                    }
                    dbp = dbp.child("fvalues");
                    dbp.setValue(map);

                    Toast.makeText(ProjectAdd.this, "Project "+ name + " added succesfully.", Toast.LENGTH_LONG).show();

                    Intent myIntent = new Intent(getApplicationContext(), ProjectList.class);
                    myIntent.putExtra("id", id);
                    myIntent.putExtra("name", deptname);
                    myIntent.putExtra("n", n);
                    startActivity(myIntent);
                }
                else{
                    Toast.makeText(ProjectAdd.this, "Please enter valid data.", Toast.LENGTH_LONG).show();
                }


            }
        });

    }
}
