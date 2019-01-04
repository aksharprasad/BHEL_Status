package com.example.hp.bhelstatus;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewChanges extends AppCompatActivity {
    String pid, id, name, deptname, year, q;
    int n;
    long t;
    ListView listView;
    TextView mEmpty;
    ProgressBar progressBar;
    FloatingActionButton but;
    int admin;
    SharedPreferences.Editor editor;
    ArrayAdapter<String> adapter;
    Change c;
    DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.project_list);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        but = (FloatingActionButton) findViewById(R.id.but);
        but.setVisibility(View.GONE);

        mEmpty = (TextView) findViewById(R.id.empty);
        mEmpty.setText("No changes have been made in the last 24 hours.");

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        deptname = intent.getStringExtra("name");
        n = intent.getIntExtra("n", 0);
        Log.i("FROM DL to PL NAME", deptname);
        Log.i("FROM DL to PL id", id);
        Log.i("FROM DL to PL n", "" + n);

        SharedPreferences mPrefs = getSharedPreferences("YearPref",0);
        year = mPrefs.getString("year", "2018");
        q = mPrefs.getString("quarter", "Quarter 3" );

        setTitle(deptname);

        final List<String> plist = new ArrayList<>();
        listView = (ListView) findViewById(R.id.plist);
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, android.R.id.text1, plist);
        listView.setAdapter(adapter);
        listView.setEmptyView(mEmpty);

        db = FirebaseDatabase.getInstance().getReference("bhel").child("history").child(id).child(year).child(q);
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                adapter.clear();
                for(DataSnapshot childSnapshot: dataSnapshot.getChildren()){
                    c = childSnapshot.getValue(Change.class);
                    t = System.currentTimeMillis();
                    if((t - c.getTime()) < (86400000L))
                        adapter.add(c.getC());
                    else{
                        db.child(childSnapshot.getKey()).setValue(null);
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}