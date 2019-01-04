package com.example.hp.bhelstatus;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;

public class FieldChange extends AppCompatActivity {

    private static View view;
    Change ch;
    EditText fieldvalue;
    TextView fieldname;
    String ab = "",fn,fv,id,pid,ufv,projectName,deptName,year,q,chil,del;
    int n,number,c,day,month,day1,month1,flag,f = 0,d;
    Button update;
    DatabaseReference db,dbh, dbc,temp,temp1;
    String u = "unrecognized user";
    long time,time1,NINETY_DAYS = 7776000000L;
    Calendar cal, cal1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_field);

        String uid = "null";
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        if(currentFirebaseUser!=null)
            uid = currentFirebaseUser.getUid();
        Log.i("uid at ui",uid);

        DatabaseReference udb = FirebaseDatabase.getInstance().getReference("bhel").child("users").child(uid);

        udb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if(user!= null) {
                    u = user.getUsername();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i("error at ui",":(");
            }
        });

        Intent i = getIntent();
        fn = i.getStringExtra("fieldname");
        fv = i.getStringExtra("fieldvalue");
        n = i.getIntExtra("n",0 );
        id = i.getStringExtra("id");
        projectName = i.getStringExtra("projectName");
        deptName = i.getStringExtra("deptName");
        pid = i.getStringExtra("pid");
        number = i.getIntExtra("number",0 );
        c = i.getIntExtra("c", 0);
        Log.i("values",fn+"|"+fv+"|"+n+"|"+id+"|"+pid+"|"+c);

        setTitle(fn);

        SharedPreferences mPrefs = getSharedPreferences("YearPref",0);
        year = mPrefs.getString("year", "2018");
        q = mPrefs.getString("quarter","Quater 3" );

        SharedPreferences pref;
        if(c==1) {
            pref = getSharedPreferences("AB", 0);
            ab = pref.getString("c", "a");
        }

        if(c==0){
            db = FirebaseDatabase.getInstance().getReference("bhel").child("projects").child(id).child(year).child(q).child(pid).child("fvalues").child("field" + n);
            dbc = FirebaseDatabase.getInstance().getReference("bhel").child("projects").child(id).child(year).child(q).child(pid).child("fchanges").child("field" + n);
        }
        else {
            db = FirebaseDatabase.getInstance().getReference("bhel").child("projects").child(id).child(year).child(q).child(pid).child(ab).child("fvalues").child("field" + n);
            dbc = FirebaseDatabase.getInstance().getReference("bhel").child("projects").child(id).child(year).child(q).child(pid).child(ab).child("fchanges").child("field" + n);
        }

        dbh = FirebaseDatabase.getInstance().getReference("bhel").child("history").child(id).child(year).child(q);

        update = (Button) findViewById(R.id.add);
        fieldvalue= (EditText) findViewById(R.id.fieldvalue);
        fieldvalue.setText(fv);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ufv = fieldvalue.getText().toString();
                if(!TextUtils.isEmpty(ufv)) {
                    db.setValue(ufv);

                    time = System.currentTimeMillis();
                    cal = Calendar.getInstance();
                    cal.setTimeInMillis(time);
                    day = cal.get(Calendar.DAY_OF_MONTH);
                    month = cal.get(Calendar.MONTH);

                    flag = 1;
                    f = 0;
                    d = 0;
                    dbc.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(flag==1) {
                                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                    Log.i("DBC ", "key =" + childSnapshot.getKey());
                                    time1 = Long.parseLong(childSnapshot.getKey());
                                    Log.i("DBC!!! ", "key =" + time1);
                                    cal1 = Calendar.getInstance();
                                    cal1.setTimeInMillis(time1);
                                    day1 = cal1.get(Calendar.DAY_OF_MONTH);
                                    month1 = cal1.get(Calendar.MONTH);
                                    Log.i("HERE!!! " + day + "||" + month, "" + day1 + "||" + month1);
                                    if (day == day1 && month == month1) {
                                        Log.i("THEY ARE EQUAL", "!!!");
                                        f = 1;
                                        chil = childSnapshot.getKey();
                                    } else if ((time - time1) >= NINETY_DAYS) {
                                        Log.i("OLD IS GOLD", "!!!  " + (time - time1));
                                        del = childSnapshot.getKey();
                                        d = 1;
                                    }
                                }
                                flag = 0;
                                Log.i("TAG", "onDataChange: OUTSIDE");
                                if(f == 0)
                                    dbc.child(""+time).setValue(new Change(ufv,time));
                                else if(f == 1)
                                    dbc.child(chil).setValue(new Change(ufv,time));
                                Log.i("TAG", "onDataChange: OUTSIDE");

                                if(d == 1)
                                    dbc.child(del).removeValue();
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    dbh.push().setValue(new Change(u+" changed "+fn+" from "+fv+" to "+ufv+" under "+projectName +ab +".", time));

                    Toast.makeText(FieldChange.this, "Field updated successfully.", Toast.LENGTH_LONG).show();

                    Intent myIntent = new Intent(getApplicationContext(), ProjectView.class);
                    myIntent.putExtra("n", number);
                    myIntent.putExtra("id", id);
                    myIntent.putExtra("pid", pid);
                    myIntent.putExtra("deptname", deptName);
                    myIntent.putExtra("name", projectName);
                    myIntent.putExtra("c", c);
                    startActivity(myIntent);



                }
                else
                    //new CustomToast().Show_Toast(FieldChange.this,view,  "Please enter valid data.");
                    Toast.makeText(FieldChange.this, "Please enter valid data.", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent myIntent = new Intent(getApplicationContext(), ProjectView.class);
        myIntent.putExtra("n", number);
        myIntent.putExtra("id", id);
        myIntent.putExtra("pid", pid);
        myIntent.putExtra("deptname", deptName);
        myIntent.putExtra("name", projectName);
        myIntent.putExtra("c", c);
        startActivity(myIntent);
    }
}
