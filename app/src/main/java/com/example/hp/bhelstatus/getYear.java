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
import java.util.Calendar;

public class getYear extends AppCompatActivity {
    EditText edtYear;
    String id, deptname;
    int n;
    Button update;
    int i;
    Spinner year, quater;
    SharedPreferences.Editor editor;
    String oldy,oldq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.year);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        update = (Button) findViewById(R.id.update);
        year = (Spinner) findViewById(R.id.year);
        quater = (Spinner) findViewById(R.id.quarter);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        deptname = intent.getStringExtra("name");
        n = intent.getIntExtra("n", 0);

        SharedPreferences mPrefs = getSharedPreferences("YearPref",0);
        oldy = mPrefs.getString("year", "2018");
        oldq = mPrefs.getString("quarter", "Quarter 3" );

        SharedPreferences pref = getApplicationContext().getSharedPreferences("YearPref", 0);
        editor = pref.edit();

        Calendar calender;
        calender = Calendar.getInstance();

        int yr = calender.get(Calendar.YEAR);
        ArrayList<String> years = new ArrayList<String>();
        for (i = yr + 3; i >= 2000; i--)
            years.add(Integer.toString(i) + "-" + Integer.toString(i+1));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.view_spinner, years);
        year.setAdapter(adapter);

        int yp = adapter.getPosition(oldy+ "-" + Integer.toString(Integer.parseInt(oldy)+1));
        year.setSelection(yp);

        ArrayList<String> q = new ArrayList<String>();
        q.add("Quarter 1");
        q.add("Quarter 2");
        q.add("Quarter 3");
        q.add("Quarter 4");
        //q.add("All quarters");
        ArrayAdapter<String> qadapter = new ArrayAdapter<String>(this, R.layout.view_spinner, q);
        quater.setAdapter(qadapter);

        int qp = qadapter.getPosition(oldq);
        quater.setSelection(qp);

        final String year0;

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] kv = year.getSelectedItem().toString().split("\\-");
                editor.putString("year", kv[0]);
                Log.i(kv[0],kv[1]);
                editor.putString("quarter", quater.getSelectedItem().toString());
                editor.commit();
                Intent myIntent = new Intent(getApplicationContext(), ProjectList.class);
                myIntent.putExtra("id", id);
                myIntent.putExtra("n", n);
                myIntent.putExtra("name", deptname);
                startActivity(myIntent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent myIntent = new Intent(getApplicationContext(), ProjectList.class);
        myIntent.putExtra("id", id);
        myIntent.putExtra("n", n);
        myIntent.putExtra("name", deptname);
        startActivity(myIntent);
    }
}
