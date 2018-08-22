package com.example.hp.bhelstatus;

import android.content.Intent;
import android.database.DataSetObserver;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import android.support.design.widget.FloatingActionButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    String name, id, ns;
    private TextView view;
    ListView listView;
    TextView mEmpty;
    ProgressBar progressBar;
    int n;
    int p = 0;
    private Boolean isFabOpen = false;
    FloatingActionButton fab,fab1,fab2;
    boolean isConnected;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Departments");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dept_list);
/*
        mEmpty=(TextView) findViewById(R.id.empty_view);
        progressBar = (ProgressBar) findViewById(R.id.loading_spinner);

        ConnectivityManager cm = (ConnectivityManager)this.getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork!=null && activeNetwork.isConnectedOrConnecting();
*/
        final List<Department> dlist = new ArrayList<>();
        final DepartmentAdapter mDepartmentAdapter = new DepartmentAdapter(this, R.layout.dept_list, dlist);
        listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(mDepartmentAdapter);

        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);

        mFirebaseAuth = FirebaseAuth.getInstance();

        DatabaseReference db = FirebaseDatabase.getInstance().getReference("bhel").child("fields");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mDepartmentAdapter.clear();
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    name = childSnapshot.child("name").getValue(String.class);
                    id = childSnapshot.child("id").getValue(String.class);
                    ns = childSnapshot.child("n").getValue(String.class);
                    n = Integer.parseInt(ns);
                    if (name != null)
                        Log.i("name", name);
                    else
                        Log.i("name", "NULL");
                    if (id != null)
                        Log.i("id", id);
                    else
                        Log.i("name", "NULL");
                    Department dept = new Department(name, id, n,(p%4));
                    mDepartmentAdapter.add(dept);
                    p++;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),DeptList.class);
                startActivity(i);
                //Toast.makeText(getBaseContext(),"FAB1 Click",Toast.LENGTH_SHORT).show();
            }
        });

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null ){

                }
                else{

                }
            }
        };

    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }
}