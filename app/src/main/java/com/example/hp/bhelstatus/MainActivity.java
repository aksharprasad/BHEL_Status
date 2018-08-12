package com.example.hp.bhelstatus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    EditText vname;
    EditText vno;
    Button butt,go;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vname = (EditText) findViewById(R.id.name);
        vno = (EditText) findViewById(R.id.no);
        butt = (Button) findViewById(R.id.butt);
        butt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = vname.getText().toString().trim();
                String no = vno.getText().toString().trim();
                int n = Integer.parseInt(no);
                Intent myIntent = new Intent(getApplicationContext(), MainField.class);
                myIntent.putExtra("name", name);
                myIntent.putExtra("n", n);
                startActivity(myIntent);
            }
        });
        go = (Button) findViewById(R.id.go);
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getApplicationContext(), DeptList.class);
                startActivity(myIntent);
            }
        });
    }
}
