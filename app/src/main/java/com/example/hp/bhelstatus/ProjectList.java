package com.example.hp.bhelstatus;

import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProjectList extends AppCompatActivity {
    String pid,id,name,deptname;
    int n;
    ListView listView;
    TextView mEmpty;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.project_list);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        deptname = intent.getStringExtra("name");
        n = intent.getIntExtra("n", 0);
        Log.i("FROM DL to PL NAME", deptname);
        Log.i("FROM DL to PL id", id);
        Log.i("FROM DL to PL n", ""+n);

        setTitle(deptname);

        final List<Project> plist = new ArrayList<>();
        final ProjectAdapter mProjectAdapter = new ProjectAdapter(this, R.layout.project_list, plist);
        listView = (ListView) findViewById(R.id.plist);
        listView.setAdapter(mProjectAdapter);

        DatabaseReference db = FirebaseDatabase.getInstance().getReference("bhel").child("projects").child(id);
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mProjectAdapter.clear();
                for(DataSnapshot childSnapshot: dataSnapshot.getChildren()){
                    pid = childSnapshot.getKey();
                    Log.i("pid at PL",pid);
                    name = childSnapshot.child("name").getValue(String.class);
                    Log.i("name at PL",name);
                    Project p = new Project(name,id,pid,n,deptname);
                    mProjectAdapter.add(p);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        FloatingActionButton but = (FloatingActionButton) findViewById(R.id.but);
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getApplicationContext(), ProjectAdd.class);
                myIntent.putExtra("id", id);
                myIntent.putExtra("deptname", deptname);
                myIntent.putExtra("n", n);
                startActivity(myIntent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
        return true;
        //return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
        //super.onBackPressed();
    }

}