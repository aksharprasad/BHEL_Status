package com.example.hp.bhelstatus;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.widget.ImageView;

public class ShortageView extends AppCompatActivity {

    String pid, id, name, key, val, vkey, vval, deptname, year, q;
    DatabaseReference dbf, db, dbn;
    int n,admin = 1;
    //int i, n, j = 0, k = 0;
    //HashMap<String, String> map = new HashMap<>();
    //HashMap<String, String> vmap = new HashMap<>();
    EditText editText;
    TextView one,two,three,four,five,six;
    String des,rem,no,mat,sho,sid,pdc;
    Field f[] = new Field[100];
    ImageView img1,img2,img3,img4,img5,img6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_short);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        Log.i("BEFORE", "INTENT");
        Intent intent = getIntent();
        n = intent.getIntExtra("n", 0);
        deptname = intent.getStringExtra("deptname");
        Log.i("HERE**",deptname+n+"");
        mat = intent.getStringExtra("mat");
        Log.i("mat", mat);
        des = intent.getStringExtra("des");
        Log.i("des", des);
        no = intent.getStringExtra("no");
        Log.i("no", no);
        sho = intent.getStringExtra("sho");
        Log.i("sho", "" + sho);
        rem = intent.getStringExtra("rem");
        Log.i("rem",rem);
        sid = intent.getStringExtra("sid");
        Log.i("sid", sid);
        pid = intent.getStringExtra("pid");
        Log.i("pid", pid);
        id = intent.getStringExtra("id");
        Log.i("id", id);
        year = intent.getStringExtra("year");
        Log.i("year", year);
        q = intent.getStringExtra("q");
        Log.i("q", q);
        name = intent.getStringExtra("name");
        Log.i("name", name);
        pdc = intent.getStringExtra("pdc");

        one = (TextView) findViewById(R.id.one);
        two = (TextView) findViewById(R.id.two);
        three = (TextView) findViewById(R.id.three);
        four = (TextView) findViewById(R.id.four);
        five = (TextView) findViewById(R.id.five);
        six = (TextView) findViewById(R.id.six);

        one.setText(mat);
        two.setText(des);
        three.setText(no);
        four.setText(sho);
        five.setText(rem);
        six.setText(pdc);

        img1 = (ImageView) findViewById(R.id.img1);
        img2 = findViewById(R.id.img2);
        img3 = findViewById(R.id.img3);
        img4 = findViewById(R.id.img4);
        img5 = findViewById(R.id.img5);
        img6 = findViewById(R.id.img6);

        String uid = "null";
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        if(currentFirebaseUser!=null)
            uid = currentFirebaseUser.getUid();
        Log.i("uid at dl",uid);

        final DatabaseReference admindb = FirebaseDatabase.getInstance().getReference("bhel").child("users").child(uid);

        admindb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if(user!= null) {
                    admin = user.getAdmin();
                    Log.i("AD!!",""+admin);
                    if(admin == 0 || admin == 2) {
                        img1.setVisibility(View.GONE);
                        img2.setVisibility(View.GONE);
                        img3.setVisibility(View.GONE);
                        img4.setVisibility(View.GONE);
                        img5.setVisibility(View.GONE);
                        img6.setVisibility(View.GONE);
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i("error at ui",":(");
            }
        });


        //Log.i("Here","YTES");

        if(img1 != null) {
            img1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Log.i("came here wtf", "WHOHOWO");
                    Intent myIntent = new Intent(ShortageView.this, ShortageChange.class);
                    myIntent.putExtra("sid", "" + sid);
                    myIntent.putExtra("pid", "" + pid);
                    myIntent.putExtra("id", "" + id);
                    myIntent.putExtra("year", "" + year);
                    myIntent.putExtra("q", "" + q);
                    myIntent.putExtra("fv", "" + mat);
                    myIntent.putExtra("fn", "mat");
                    myIntent.putExtra("des", "" + des);
                    myIntent.putExtra("rem", "" + rem);
                    myIntent.putExtra("sho", "" + sho);
                    myIntent.putExtra("no", "" + no);
                    myIntent.putExtra("name", "" + name);
                    myIntent.putExtra("mat", mat);
                    myIntent.putExtra("pdc", pdc);
                    myIntent.putExtra("deptname", deptname);
                    myIntent.putExtra("n", n);
                    startActivity(myIntent);
                }
            });
        }

        if(img2!=null) {
            img2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Log.i("came here wtf", "WHOHOWO");
                    Intent myIntent = new Intent(ShortageView.this, ShortageChange.class);
                    myIntent.putExtra("sid", "" + sid);
                    myIntent.putExtra("pid", "" + pid);
                    myIntent.putExtra("id", "" + id);
                    myIntent.putExtra("year", "" + year);
                    myIntent.putExtra("q", "" + q);
                    myIntent.putExtra("fv", "" + des);
                    myIntent.putExtra("fn", "des");
                    myIntent.putExtra("des", "" + des);
                    myIntent.putExtra("rem", "" + rem);
                    myIntent.putExtra("sho", "" + sho);
                    myIntent.putExtra("no", "" + no);
                    myIntent.putExtra("name", "" + name);
                    myIntent.putExtra("mat", mat);
                    myIntent.putExtra("pdc", pdc);
                    myIntent.putExtra("deptname", deptname);
                    myIntent.putExtra("n", n);
                    startActivity(myIntent);
                }
            });
        }

        if(img3!=null) {
            img3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Log.i("came here wtf", "WHOHOWO");
                    Intent myIntent = new Intent(ShortageView.this, ShortageChange.class);
                    myIntent.putExtra("sid", "" + sid);
                    myIntent.putExtra("pid", "" + pid);
                    myIntent.putExtra("id", "" + id);
                    myIntent.putExtra("year", "" + year);
                    myIntent.putExtra("q", "" + q);
                    myIntent.putExtra("fv", "" + no);
                    myIntent.putExtra("fn", "no");
                    myIntent.putExtra("des", "" + des);
                    myIntent.putExtra("rem", "" + rem);
                    myIntent.putExtra("sho", "" + sho);
                    myIntent.putExtra("no", "" + no);
                    myIntent.putExtra("name", "" + name);
                    myIntent.putExtra("mat", mat);
                    myIntent.putExtra("pdc", pdc);
                    myIntent.putExtra("deptname", deptname);
                    myIntent.putExtra("n", n);
                    startActivity(myIntent);
                }
            });
        }

        if(img4!=null) {
            img4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Log.i("came here wtf", "WHOHOWO");
                    Intent myIntent = new Intent(ShortageView.this, ShortageChange.class);
                    myIntent.putExtra("sid", "" + sid);
                    myIntent.putExtra("pid", "" + pid);
                    myIntent.putExtra("id", "" + id);
                    myIntent.putExtra("year", "" + year);
                    myIntent.putExtra("q", "" + q);
                    myIntent.putExtra("fv", "" + sho);
                    myIntent.putExtra("fn", "sho");
                    myIntent.putExtra("des", "" + des);
                    myIntent.putExtra("rem", "" + rem);
                    myIntent.putExtra("sho", "" + sho);
                    myIntent.putExtra("no", "" + no);
                    myIntent.putExtra("name", "" + name);
                    myIntent.putExtra("mat", mat);
                    myIntent.putExtra("pdc", pdc);
                    myIntent.putExtra("deptname", deptname);
                    myIntent.putExtra("n", n);
                    startActivity(myIntent);
                }
            });
        }

        if(img5!=null) {
            img5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Log.i("came here wtf", "WHOHOWO");
                    Intent myIntent = new Intent(ShortageView.this, ShortageChange.class);
                    myIntent.putExtra("sid", "" + sid);
                    myIntent.putExtra("pid", "" + pid);
                    myIntent.putExtra("id", "" + id);
                    myIntent.putExtra("year", "" + year);
                    myIntent.putExtra("q", "" + q);
                    myIntent.putExtra("fv", "" + rem);
                    myIntent.putExtra("fn", "rem");
                    myIntent.putExtra("des", "" + des);
                    myIntent.putExtra("rem", "" + rem);
                    myIntent.putExtra("sho", "" + sho);
                    myIntent.putExtra("no", "" + no);
                    myIntent.putExtra("name", "" + name);
                    myIntent.putExtra("mat", mat);
                    myIntent.putExtra("pdc", pdc);
                    myIntent.putExtra("deptname", deptname);
                    myIntent.putExtra("n", n);
                    startActivity(myIntent);
                }
            });
        }

        if(img6!=null) {
            img6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Log.i("came here wtf", "WHOHOWO");
                    Intent myIntent = new Intent(ShortageView.this, ShortageChange.class);
                    myIntent.putExtra("sid", "" + sid);
                    myIntent.putExtra("pid", "" + pid);
                    myIntent.putExtra("id", "" + id);
                    myIntent.putExtra("year", "" + year);
                    myIntent.putExtra("q", "" + q);
                    myIntent.putExtra("fv", "" + pdc);
                    myIntent.putExtra("fn", "pdc");
                    myIntent.putExtra("des", "" + des);
                    myIntent.putExtra("rem", "" + rem);
                    myIntent.putExtra("sho", "" + sho);
                    myIntent.putExtra("no", "" + no);
                    myIntent.putExtra("name", "" + name);
                    myIntent.putExtra("mat", mat);
                    myIntent.putExtra("pdc", pdc);
                    myIntent.putExtra("deptname", deptname);
                    myIntent.putExtra("n", n);
                    startActivity(myIntent);
                }
            });
        }

    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(),Shortage.class);
        i.putExtra("id", id);
        i.putExtra("pid",pid );
        i.putExtra("name", name);
        i.putExtra("deptname", deptname);
        i.putExtra("n", n);
        startActivity(i);
    }
}