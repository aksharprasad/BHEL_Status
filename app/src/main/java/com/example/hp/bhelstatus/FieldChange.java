package com.example.hp.bhelstatus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FieldChange extends AppCompatActivity {

    EditText fieldvalue;
    TextView fieldname;
    String fn,fv,id,pid,ufv,projectName,deptName;
    int n,number;
    Button update;
    DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_field);

        Intent i = getIntent();
        fn = i.getStringExtra("fieldname");
        fv = i.getStringExtra("fieldvalue");
        n = i.getIntExtra("n",0 );
        id = i.getStringExtra("id");
        projectName = i.getStringExtra("projectName");
        deptName = i.getStringExtra("deptName");
        pid = i.getStringExtra("pid");
        number = i.getIntExtra("number",0 );

        Log.i("values",fn+"|"+fv+"|"+n+"|"+id+"|"+pid);

        setTitle(fn);

        db = FirebaseDatabase.getInstance().getReference("bhel").child("projects").child(id).child(pid).child("fvalues").child("field"+n);

        update = (Button) findViewById(R.id.add);
        fieldvalue= (EditText) findViewById(R.id.fieldvalue);
        fieldvalue.setHint(fv);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ufv = fieldvalue.getText().toString();
                if(!TextUtils.isEmpty(ufv)) {
                    db.setValue(ufv);

                    Toast.makeText(FieldChange.this, "Field updated successfully.", Toast.LENGTH_LONG).show();

                    Intent myIntent = new Intent(getApplicationContext(), ProjectView.class);
                    myIntent.putExtra("n", number);
                    myIntent.putExtra("id", id);
                    myIntent.putExtra("pid", pid);
                    myIntent.putExtra("deptname", deptName);
                    myIntent.putExtra("name", projectName);
                    startActivity(myIntent);

                }
                else
                    Toast.makeText(FieldChange.this, "Please enter valid data.", Toast.LENGTH_LONG).show();
            }
        });
    }
}
