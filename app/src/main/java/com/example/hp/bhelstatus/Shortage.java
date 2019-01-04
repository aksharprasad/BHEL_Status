package com.example.hp.bhelstatus;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Shortage extends AppCompatActivity {
    String deptname,pid, id, year, q, name;
    int n,admin;
    FloatingActionButton but;
    DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shortages);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        Log.i("BEFORE", "INTENT");
        Intent intent = getIntent();

        name = intent.getStringExtra("name");
        deptname = intent.getStringExtra("deptname");
        n = intent.getIntExtra("n", 0);
        Log.i("N", ""+n+"|"+deptname+"|"+name);
        pid = intent.getStringExtra("pid");
        Log.i("pid at PV", pid);
        id = intent.getStringExtra("id");
        Log.i("id at PV", id);


        but = (FloatingActionButton) findViewById(R.id.but);

        final List<Short> flist = new ArrayList<>();
        final ShortageAdapter mShortAdapter = new ShortageAdapter(this, R.layout.shortages, flist);
        final ListView listView = (ListView) findViewById(R.id.plist);
        listView.setAdapter(mShortAdapter);
        listView.setEmptyView(findViewById(R.id.empty));

        SharedPreferences mPrefs = getSharedPreferences("YearPref", 0);
        year = mPrefs.getString("year", "2018");
        q = mPrefs.getString("quarter", "Quater 3");

        db = FirebaseDatabase.getInstance().getReference("bhel").child("projects").child(id).child(year).child(q).child(pid).child("svalues");

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mShortAdapter.clear();
                for(DataSnapshot childSnapshot: dataSnapshot.getChildren()){
                    Short s = childSnapshot.getValue(Short.class);
                    mShortAdapter.add(s);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        String uid = "null";
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        if(currentFirebaseUser!=null)
            uid = currentFirebaseUser.getUid();
        Log.i("uid at dl",uid);

        DatabaseReference admindb = FirebaseDatabase.getInstance().getReference("bhel").child("users").child(uid);

        admindb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if(user!= null) {
                    admin = user.getAdmin();
                    if(admin == 0 || admin == 2)
                        but.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i("error at ui",":(");
            }
        });

        if(but != null) {
            but.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent myIntent = new Intent(getApplicationContext(), Upload.class);
                    myIntent.putExtra("name", name);
                    myIntent.putExtra("id", id);
                    myIntent.putExtra("pid", pid);
                    myIntent.putExtra("n", n);
                    myIntent.putExtra("deptname", deptname);
                    startActivity(myIntent);
                }
            });
        }

    }
    @Override
    public void onBackPressed() {
        Intent myIntent = new Intent(getApplicationContext(), ProjectList.class);
        myIntent.putExtra("id",id );
        myIntent.putExtra("n",n );
        myIntent.putExtra("name",deptname );
        startActivityForResult(myIntent, 0);
        //super.onBackPressed();
    }

}
