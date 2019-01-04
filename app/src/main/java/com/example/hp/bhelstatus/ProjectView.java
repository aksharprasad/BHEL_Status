package com.example.hp.bhelstatus;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ProjectView extends AppCompatActivity {

    String pid, id, name, key, val, vkey, vval,deptname,year,q;
    DatabaseReference dbf, db, dbn;
    int i, n, j=0,k=0,c,admin;
    //HashMap<String, String> map = new HashMap<>();
    //HashMap<String, String> vmap = new HashMap<>();
    EditText editText;
    TextView projectName;
    Field f[] = new Field[100];
    String ab = "";
    String[] kv = new String[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_project);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        Log.i("BEFORE", "INTENT");
        Intent intent = getIntent();

        pid = intent.getStringExtra("pid");
        Log.i("pid at PV",pid);
        id = intent.getStringExtra("id");
        Log.i("id at PV",id);
        name = intent.getStringExtra("name");
        Log.i("name at PV", name);
        n = intent.getIntExtra("n", 0);
        Log.i("n at PV", "" + n);
        deptname = intent.getStringExtra("deptname");
        c = intent.getIntExtra("c", 0);
        Log.i("C",""+c);

        SharedPreferences mPrefs = getSharedPreferences("YearPref",0);
        year = mPrefs.getString("year", "2018");
        q = mPrefs.getString("quarter","Quater 3" );

        SharedPreferences pref;
        if(c==1) {
            pref = getSharedPreferences("AB", 0);
            ab = pref.getString("c", "a");
        }
        setTitle(name + " " + ab.toUpperCase());

        String uid = "null";
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        if(currentFirebaseUser != null)
            uid = currentFirebaseUser.getUid();
        Log.i("uid at dl",uid);

        final DatabaseReference admindb = FirebaseDatabase.getInstance().getReference("bhel").child("users").child(uid);
        admindb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if(user!= null) {
                    admin = user.getAdmin();
                    //Log.i("J", current.getFieldName()+"><"+current.getType()+"><"+admin)
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i("error at ui",":(");
            }
        });

        for( i=0; i<100; i++) {
            f[i]=new Field();
            f[i].setId(id);
            f[i].setPid(pid);
            f[i].setProjectName(name);
            f[i].setDeptName(deptname);
            f[i].setNumber(n);
        }

        final List<Field> flist = new ArrayList<>();
        final FieldAdapter mFieldAdapter = new FieldAdapter(this, R.layout.view_project, flist);
        ListView listView = (ListView) findViewById(R.id.flist);
        listView.setAdapter(mFieldAdapter);
        listView.setEmptyView(findViewById(R.id.empty));


        dbf = FirebaseDatabase.getInstance().getReference("bhel").child("fields").child(id);
        if(c==1)
            db = FirebaseDatabase.getInstance().getReference("bhel").child("projects").child(id).child(year).child(q).child(pid).child(ab).child("fvalues");
        else
            db = FirebaseDatabase.getInstance().getReference("bhel").child("projects").child(id).child(year).child(q).child(pid).child("fvalues");
        /*
        dbf.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("Came", "here");
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    Log.i("key", childSnapshot.getKey());
                    Log.i("value", childSnapshot.getValue(String.class));
                    key = childSnapshot.getKey();
                    val = childSnapshot.getValue(String.class);
                    //if(key.startsWith("field")) {
                    f[j].setFieldName(val);
                    j = j + 1;
                    //}
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });
        */

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("Came","here too");
                for(DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    Log.i("key =" + childSnapshot.getKey(), "value=" + childSnapshot.getValue(String.class));
                    vkey = childSnapshot.getKey();
                    vval = childSnapshot.getValue(String.class);
                    f[j].setN(j);
                    f[j].setAdmin(admin);
                    f[j++].setFieldValue(vval);
                }
                    dbf.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Log.i("Came", "here");
                            for (DataSnapshot fchildSnapshot : dataSnapshot.getChildren()) {
                                Log.i("key", fchildSnapshot.getKey());
                                Log.i("value", fchildSnapshot.getValue(String.class));
                                key = fchildSnapshot.getKey();
                                val = fchildSnapshot.getValue(String.class);
                                if (key.startsWith("field")) {
                                    Log.i("TAGv", val);
                                    String[] kv = val.split("\\$");
                                    Log.i("TAG", "onDataChange: "+kv[0]+"||"+kv[1]);
                                    f[k].setType(Integer.parseInt(kv[1]));
                                    f[k++].setFieldName(kv[0]);
                                }
                            }

                                    mFieldAdapter.clear();
                                    for(i=0;i<n;i++){
                                        Log.i("for:","entered");
                                        if(f[i].getFieldValue() != null && f[i].getFieldName() != null )
                                            mFieldAdapter.add(f[i]);
                                        else
                                            Log.i("PV for","null");
                                    }



                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) { }
                    });
                }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        //Log.i("first",map.get("field0"));
        //Log.i("second",vmap.get("field0"));
        //Field f = new Field(map.get("field0"),vmap.get("field0"));
        //mFieldAdapter.add(f);

        /*
        for(i=0;i<n;i++){
            Log.i("for:","entered");
            if(f[i].getFieldValue() != null && f[i].getFieldName() != null )
                mFieldAdapter.add(f[i]);
            else
                Log.i("PV for","null");
        }
        */
/*

        final Field f = new Field();
            ValueEventListener mFieldListener = new ValueEventListener() {
                @Override
                public void onDataChange(final DataSnapshot dataSnapshot) {
                    String name = dataSnapshot.getValue().toString();
                    f.setFieldName(name);
                    // Here you make a new event listner

                    db.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(final DataSnapshot datasnapshot) {
                            String phone = datasnapshot.getValue().toString();
                            f.setFieldValue(phone);
                            mFieldAdapater.add(f); // add your user info here
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            };
        dbf.addListenerForSingleValueEvent(mFieldListener);
        };
   */
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
        if(c==1)
            menu.add(0, 1, 0, "A/B");
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
            case 1:

                Intent myIntent = new Intent(getApplicationContext(), getAB.class);
                myIntent.putExtra("name", name);
                myIntent.putExtra("id", id);
                myIntent.putExtra("pid", pid);
                myIntent.putExtra("n", n);
                myIntent.putExtra("deptname", deptname);
                myIntent.putExtra("c",c);
                startActivity(myIntent);
                break;
        }

        return true;
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

