package com.example.hp.bhelstatus;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

public class getAB1 extends AppCompatActivity {
    EditText edtYear;
    String name,pid, id, deptname;
    int n;
    Button update;
    int i,c;
    Spinner year, quater;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ab);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        update = (Button) findViewById(R.id.update);
        year = (Spinner) findViewById(R.id.year);

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

        SharedPreferences pref = getApplicationContext().getSharedPreferences("AB", 0);
        editor = pref.edit();

        ArrayList<String> q = new ArrayList<String>();
        q.add("A");
        q.add("B");
        ArrayAdapter<String> qadapter = new ArrayAdapter<String>(this, R.layout.view_spinner, q);
        year.setAdapter(qadapter);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("c", year.getSelectedItem().toString().toLowerCase());
                editor.commit();
                Intent myIntent = new Intent(getApplicationContext(), datePick.class);
                myIntent.putExtra("name", name);
                myIntent.putExtra("id", id);
                myIntent.putExtra("pid", pid);
                myIntent.putExtra("n", n);
                myIntent.putExtra("deptname", deptname);
                myIntent.putExtra("c",c );
                startActivity(myIntent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent myIntent = new Intent(getApplicationContext(), datePick.class);
        myIntent.putExtra("name", name);
        myIntent.putExtra("id", id);
        myIntent.putExtra("pid", pid);
        myIntent.putExtra("n", n);
        myIntent.putExtra("deptname", deptname);
        myIntent.putExtra("c",c );
        startActivity(myIntent);
    }
}
