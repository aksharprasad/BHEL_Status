package com.example.hp.bhelstatus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ProjectList extends AppCompatActivity {
    String id,name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.project_list);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        name = intent.getStringExtra("name");
        Log.i("FROM DL to PL NAME", name);
        Log.i("FROM DL to PL id", id);

        TextView text = (TextView) findViewById( R.id.ptextView);
        text.setText(name);

        final List<Project> plist = new ArrayList<>();
        final ProjectAdapter mProjectAdapter = new ProjectAdapter(this, R.layout.activity_main, plist);
        ListView listView = (ListView) findViewById(R.id.plist);
        listView.setAdapter(mProjectAdapter);

        DatabaseReference db = FirebaseDatabase.getInstance().getReference("bhel").child("projects").child(id);
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mProjectAdapter.clear();
                for(DataSnapshot childSnapshot: dataSnapshot.getChildren()){
                    Project p = childSnapshot.getValue(Project.class);
                    mProjectAdapter.add(p);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Button but = (Button) findViewById(R.id.but);
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getApplicationContext(), ProjectAdd.class);
                myIntent.putExtra("id", id);
                startActivity(myIntent);
            }
        });
    }
}