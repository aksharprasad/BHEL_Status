package com.example.hp.bhelstatus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.CheckBox;
import android.widget.Toast;

public class DeptList extends AppCompatActivity {

    EditText vname;
    EditText vno;
    Button butt;
    private static View view;
    CheckBox ab;
    int c = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ab = (CheckBox)findViewById(R.id.ab);
        setTitle("Add Project");
        vname = (EditText) findViewById(R.id.name);
        vno = (EditText) findViewById(R.id.no);
        butt = (Button) findViewById(R.id.butt);
        butt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c = 0;
                String name = vname.getText().toString().trim();
                String no = vno.getText().toString().trim();
                if(ab.isChecked() == true)
                    c = 1;
                if(name == null ||  !isInteger(no)){
                    //new CustomToast().Show_Toast(getApplicationContext(), view, "Please enter valid data.");
                    Toast.makeText(DeptList.this, "Please enter valid data.", Toast.LENGTH_LONG).show();
                }
                else {
                    int n = Integer.parseInt(no);
                    Intent myIntent = new Intent(getApplicationContext(), MainField.class);
                    myIntent.putExtra("name", name);
                    myIntent.putExtra("n", n);
                    myIntent.putExtra("ab", c);
                    startActivity(myIntent);
                }
            }
        });
    }
    public static boolean isInteger(String s) {
        int t;
        try {
           t = Integer.parseInt(s);
        } catch(NumberFormatException e) {
            return false;
        } catch(NullPointerException e) {
            return false;
        }

        if(t<1||t>100)
            return false;
        return true;
    }
}