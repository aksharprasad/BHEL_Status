package com.example.hp.bhelstatus;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ProjectList extends AppCompatActivity {
    String pid,id,name,deptname,year,q;
    int n;
    ListView listView;
    TextView mEmpty;
    ProgressBar progressBar;
    FloatingActionButton but;
    int admin;
    DatabaseReference db;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.project_list);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        deptname = intent.getStringExtra("name");
        n = intent.getIntExtra("n", 0);
        Log.i("FROM DL to PL NAME", deptname);
        Log.i("FROM DL to PL id", id);
        Log.i("FROM DL to PL n", ""+n);

        Calendar c = Calendar.getInstance();
        String qtr = "Quarter 1";
        int yr = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        switch (month){
            case 1:
            case 2:
            case 3:
                qtr = "Quarter 1";
                break;
            case 4:
            case 5:
            case 6:
                qtr = "Quarter 2";
                break;
            case 7:
            case 8:
            case 9:
                qtr = "Quarter 3";
                break;
            case 10:
            case 11:
            case 12:
                qtr = "Quarter 4";
                break;
        }

        SharedPreferences mPrefs = getSharedPreferences("YearPref",0);
        year = mPrefs.getString("year", ""+yr);
        q = mPrefs.getString("quarter", qtr );

        but = (FloatingActionButton) findViewById(R.id.but);

        final List<Project> plist = new ArrayList<>();
        final ProjectAdapter mProjectAdapter = new ProjectAdapter(this, R.layout.project_list, plist);
        listView = (ListView) findViewById(R.id.plist);
        listView.setAdapter(mProjectAdapter);
        listView.setEmptyView(findViewById(R.id.empty));

        setTitle(deptname+" - "+"Q"+q.charAt(8)+" - "+year+"-"+ Integer.toString((Integer.parseInt(year)+1)));

        /*listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("TAG", "onItemLongClick: 0");
                if(view.getBackground() == ContextCompat.getDrawable(getApplicationContext(),R.color.white_greyish)){
                    view.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.color.red ));
                    Log.i("TAG", "onItemLongClick: 1");
                }
                else if(view.getBackground() == ContextCompat.getDrawable(getApplicationContext(),R.color.red)){
                    view.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.color.white_greyish ));
                    Log.i("TAG", "onItemLongClick: 2");
                }
                return false;
            }
        });*/

        /*
        if(q != "All quarters")
            setTitle(deptname+" - "+"Q"+q.charAt(8)+" - "+year+"-"+ Integer.toString((Integer.parseInt(year)+1)));
        else
            setTitle(deptname+" - "+"Q"+year+"-"+ Integer.toString((Integer.parseInt(year)+1)));
        */

        //if(q != "All quarters") {
            db = FirebaseDatabase.getInstance().getReference("bhel").child("projects").child(id).child(year).child(q);
            db.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    mProjectAdapter.clear();
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        pid = childSnapshot.getKey();
                        Log.i("pid at PL", pid);
                        name = childSnapshot.child("name").getValue(String.class);
                        Log.i("name at PL", name);
                        Project p = new Project(name, id, pid, n, deptname);
                        mProjectAdapter.add(p);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        /*}
        else{
            db = FirebaseDatabase.getInstance().getReference("bhel").child("projects").child(id).child(year);
            db.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    mProjectAdapter.clear();
                    for(DataSnapshot qSnapshot: dataSnapshot.getChildren()){
                        for(DataSnapshot childSnapshot : qSnapshot.getChildren()){
                            pid = childSnapshot.getKey();
                            Log.i("$$$pid at PL", pid);
                            name = childSnapshot.child("name").getValue(String.class);
                            Log.i("$$$name at PL", name);
                            Project p = new Project(name, id, pid, n, deptname);
                            mProjectAdapter.add(p);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        */

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
                    Intent myIntent = new Intent(getApplicationContext(), ProjectAdd.class);
                    myIntent.putExtra("id", id);
                    myIntent.putExtra("deptname", deptname);
                    myIntent.putExtra("n", n);
                    startActivity(myIntent);
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        Intent myIntent = new Intent(getApplicationContext(), ListDepartments.class);
        startActivityForResult(myIntent, 0);
        //super.onBackPressed();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
/*
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        if(admin == 1)
            menu.add(0,1,0,"Add user");
*/  super.onCreateOptionsMenu(menu);

        menu.add(0, 0, 0, "Sign out");
        menu.add(10, 10, 10, "Year and Quarter");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case 0:

                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(this, MainActivity.class));
                break;

            case 10:
                Intent myIntent = new Intent(this, getYear.class);
                myIntent.putExtra("name", deptname);
                myIntent.putExtra("id", id);
                myIntent.putExtra("n", n);
                startActivity(myIntent);
                break;
        }

        return true;
    }

}