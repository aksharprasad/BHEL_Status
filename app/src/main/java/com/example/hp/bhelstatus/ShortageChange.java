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

public class ShortageChange extends AppCompatActivity {

    private static View view;
    EditText fieldvalue;
    TextView fieldname;
    String name,sho,no,rem,des,mat,ab = "",pdc,fn,fv,id,pid,ufv,projectName,deptname,year,q,sid;
    int n,number,c;
    Button update;
    DatabaseReference db,dbh ;
    String u = "unrecognized user";

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
        fv = i.getStringExtra("fv");
        fn = i.getStringExtra("fn");
        n = i.getIntExtra("n",0 );
        sid = i.getStringExtra("sid");
        id = i.getStringExtra("id");
        //projectName = i.getStringExtra("projectName");
        deptname = i.getStringExtra("deptname");
        pid = i.getStringExtra("pid");
        pdc = i.getStringExtra("pdc");
        //number = i.getIntExtra("number",0 );
        //c = i.getIntExtra("c", 0);
        year = i.getStringExtra("year");
        q = i.getStringExtra("q");
        Log.i("values",u+"|"+fn+"|"+fv+"|"+sid+"|"+pid+"|"+id+"|"+year+"|"+q);
        mat = i.getStringExtra("mat");
        Log.i("mat", mat);
        des = i.getStringExtra("des");
        Log.i("des", des);
        no = i.getStringExtra("no");
        Log.i("no", no);
        sho = i.getStringExtra("sho");
        Log.i("sho", "" + sho);
        rem = i.getStringExtra("rem");
        Log.i("rem",rem);
        name = i.getStringExtra("name");

        setTitle(fn);

        db = FirebaseDatabase.getInstance().getReference("bhel").child("projects").child(id).child(year).child(q).child(pid).child("svalues").child(sid).child(fn);
        dbh = FirebaseDatabase.getInstance().getReference("bhel").child("history").child(id).child(year).child(q);
        update = (Button) findViewById(R.id.add);
        fieldvalue= (EditText) findViewById(R.id.fieldvalue);
        fieldvalue.setHint(fv);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ufv = fieldvalue.getText().toString();
                if(!TextUtils.isEmpty(ufv)) {
                    db.setValue(ufv);

                    dbh.push().setValue(new Change(u+" changed "+fn+" from "+fv+" to "+ufv+" under "+name+" shortages.",System.currentTimeMillis()));

                    Toast.makeText(ShortageChange.this, "Shortage updated successfully.", Toast.LENGTH_LONG).show();

                    Intent myIntent = new Intent(ShortageChange.this, ShortageView.class);
                    myIntent.putExtra("des", des);
                    myIntent.putExtra("mat", mat);
                    myIntent.putExtra("rem",rem);
                    myIntent.putExtra("sho", sho);
                    myIntent.putExtra("no", no);
                    myIntent.putExtra("sid", sid);
                    myIntent.putExtra("pid", pid);
                    myIntent.putExtra("id",id);
                    myIntent.putExtra("year", year);
                    myIntent.putExtra("q", q);
                    myIntent.putExtra("name", name);
                    //myIntent.putExtra("projectName", current.getProjectName());
                    myIntent.putExtra("deptname", deptname);
                    myIntent.putExtra("number", n);
                    myIntent.putExtra("pdc", pdc);
                    startActivity(myIntent);
                }
                else
                    new CustomToast().Show_Toast(ShortageChange.this,view,  "Please enter valid data.");
                //Toast.makeText(FieldChange.this, "Please enter valid data.", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent myIntent = new Intent(ShortageChange.this, ShortageView.class);
        myIntent.putExtra("des", des);
        myIntent.putExtra("mat", mat);
        myIntent.putExtra("rem",rem);
        myIntent.putExtra("sho", sho);
        myIntent.putExtra("no", no);
        myIntent.putExtra("sid", sid);
        myIntent.putExtra("pid", pid);
        myIntent.putExtra("id",id);
        myIntent.putExtra("year", year);
        myIntent.putExtra("q", q);
        myIntent.putExtra("name", name);
        //myIntent.putExtra("projectName", current.getProjectName());
        myIntent.putExtra("deptname", deptname);
        myIntent.putExtra("number", n);
        startActivity(myIntent);
    }
}
