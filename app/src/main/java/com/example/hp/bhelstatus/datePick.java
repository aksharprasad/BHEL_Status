package com.example.hp.bhelstatus;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
        import android.content.res.Resources;
        import android.support.v4.app.DialogFragment;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Button;
        import android.widget.DatePicker;
        import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class datePick extends AppCompatActivity implements View.OnClickListener{

    Button sub, btnDatePicker, btnMonthYear;
    private static View views;
    String ab = "", pid, id, name, key, val, vkey, vval,deptname,year,q;
    int c,n;
    TextView txtDate, txtMonthYear;
    private int mYear, mMonth, mDay, ff = 0, tf = 0, from[], to[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.date_pick);

        btnDatePicker = (Button) findViewById(R.id.btnDatePicker);
        btnMonthYear = (Button) findViewById(R.id.btnMonthYear);
        sub = (Button) findViewById(R.id.sub);
        txtDate = (TextView) findViewById(R.id.txtDate);
        txtMonthYear = (TextView) findViewById(R.id.txtMonthYear);

        from = new int[3];
        to = new int[3];

        for(int i = 0; i < from.length ; i++ ){
            from[i] = to[i] = 0;
        }

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


        btnDatePicker.setOnClickListener(this);
        btnMonthYear.setOnClickListener(this);
        sub.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view == btnDatePicker){

            Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, AlertDialog.THEME_HOLO_DARK,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            ff = 1;
                            from[0] = dayOfMonth;
                            from[1] = monthOfYear + 1;
                            from[2] = year;
                            txtDate.setText(from[0] + "/" + from[1] + "/" + from[2]);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }else if(view == btnMonthYear){
            Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, AlertDialog.THEME_HOLO_DARK,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            tf = 1;
                            to[0] = dayOfMonth;
                            to[1] = monthOfYear + 1;
                            to[2] = year;
                            txtMonthYear.setText(to[0] + "/" + to[1] + "/" + to[2]);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }else if(view == sub){
            if(tf != 1 || ff != 1)
                Toast.makeText(datePick.this, "Set both the dates.", Toast.LENGTH_LONG).show();
            else{
                //Toast.makeText(datePick.this, "Dope.", Toast.LENGTH_LONG).show();
                Intent myIntent = new Intent(getApplicationContext(), progressView.class);
                myIntent.putExtra("name", name);
                myIntent.putExtra("id", id);
                myIntent.putExtra("pid", pid);
                myIntent.putExtra("n", n);
                myIntent.putExtra("deptname", deptname);
                myIntent.putExtra("c",c );
                myIntent.putExtra("fday", from[0]);
                myIntent.putExtra("fmonth", from[1]);
                myIntent.putExtra("fyear", from[2]);
                myIntent.putExtra("tday", to[0]);
                myIntent.putExtra("tmonth", to[1]);
                myIntent.putExtra("tyear", to[2]);
                startActivity(myIntent);
            }
        }
    }


    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState){
            //Use the current date as the default date in the date picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);



            DatePickerDialog dpd = new DatePickerDialog(getActivity(), AlertDialog.THEME_HOLO_DARK,this,year, month, day){
                //DatePickerDialog dpd = new DatePickerDialog(getActivity(),AlertDialog.THEME_HOLO_LIGHT,this,year, month, day){
                // DatePickerDialog dpd = new DatePickerDialog(getActivity(), AlertDialog.THEME_TRADITIONAL,this,year, month, day){
                @Override
                protected void onCreate(Bundle savedInstanceState)
                {
                    super.onCreate(savedInstanceState);
                    int day = getContext().getResources().getIdentifier("android:id/day", null, null);
                    if(day != 0){
                        View dayPicker = findViewById(day);
                        if(dayPicker != null){
                            //Set Day view visibility Off/Gone
                            dayPicker.setVisibility(View.GONE);
                        }
                    }
                }
            };
            return dpd;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            //Set the Month & Year to TextView which chosen by the user
            TextView tv = (TextView) getActivity().findViewById(R.id.txtMonthYear);

            String stringOfDate = month + "/" + year;
            tv.setText(stringOfDate);
        }
    }
}