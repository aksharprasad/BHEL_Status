package com.example.hp.bhelstatus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProjectAdd extends AppCompatActivity {
    String name,pid;
    EditText vname,vid;
    DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.project_add);

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");

        vname = (EditText) findViewById(R.id.name);
        Button butt = (Button) findViewById(R.id.butt);
        db = FirebaseDatabase.getInstance().getReference("bhel").child("projects").child(id);

        butt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = vname.getText().toString().trim();
                pid = db.push().getKey();
                Project p = new Project(name,pid);
                db.child(pid).setValue(p);
                Intent i = new Intent(getApplicationContext(),DeptList.class);
                startActivity(i);
            }
        });
    }
}
