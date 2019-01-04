package com.example.hp.bhelstatus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ReferalRequest extends AppCompatActivity {

    EditText vname, vemail, vcred, vdob, veid;
    TextView request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request_referal);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        request = (TextView) findViewById(R.id.refCode);
        vname = (EditText) findViewById(R.id.name);
        vemail = (EditText) findViewById(R.id.email);
        veid = (EditText) findViewById(R.id.eid);
        vdob = (EditText) findViewById(R.id.dob);
        vcred = (EditText) findViewById(R.id.cred);

        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pushRequest();
            }
        });

    }

    public void pushRequest(){
        String email = vemail.getText().toString().trim();
        String cred = vcred.getText().toString().trim();
        String name = vname.getText().toString().trim();
        String eid = veid.getText().toString().trim();
        String dob = vdob.getText().toString().trim();

        if (name.isEmpty()) {
            vname.setError("Username is required");
            vname.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            vemail.setError("Email is required");
            vemail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            vemail.setError("Please enter a valid email");
            vemail.requestFocus();
            return;
        }

        if (eid.isEmpty()) {
            veid.setError("Employee ID is required");
            veid.requestFocus();
            return;
        }

        if (cred.isEmpty()) {
            vcred.setError("Credentials are required");
            vcred.requestFocus();
            return;
        }

        if (dob.isEmpty()) {
            vdob.setError("Password is required");
            vdob.requestFocus();
            return;
        }
        if (dob.length() < 6){
            vdob.setError("Minimum length of password should be 6");
            vdob.requestFocus();
            return;
        }

        DatabaseReference db = FirebaseDatabase.getInstance().getReference("bhel");
        String id = db.push().getKey();
        UserRequest user = new UserRequest(name,eid,email,cred,dob,id);
        db.child("userequest").child(id).setValue(user);
        Toast.makeText(getApplicationContext(),"Request has been made. Check your email for a confirmation" , Toast.LENGTH_LONG).show();
        startActivity(new Intent(getBaseContext(),MainActivity.class));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
        return true;
        //return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
        //super.onBackPressed();
    }
}