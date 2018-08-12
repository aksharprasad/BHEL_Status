package com.example.hp.bhelstatus;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DeptList extends AppCompatActivity {

    String name,id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dept_list);

        final List<Department> dlist = new ArrayList<>();
        final DepartmentAdapter mDepartmentAdapter = new DepartmentAdapter(this, R.layout.activity_main, dlist);
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(mDepartmentAdapter);

        DatabaseReference db = FirebaseDatabase.getInstance().getReference("bhel").child("fields");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mDepartmentAdapter.clear();
                for(DataSnapshot childSnapshot: dataSnapshot.getChildren()){
                        name = childSnapshot.child("name").getValue(String.class);
                        id = childSnapshot.child("id").getValue(String.class);
                        if(name!=null)
                            Log.i("name",name);
                        else
                            Log.i("name","NULL");
                        if(id!=null)
                            Log.i("id",id);
                        else
                            Log.i("name","NULL");
                        Department dept = new Department(name,id);
                        mDepartmentAdapter.add(dept);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
