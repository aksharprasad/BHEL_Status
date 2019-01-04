package com.example.hp.bhelstatus;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import java.util.ArrayList;
import java.util.List;

public class ListDepartments extends AppCompatActivity {


    String name, id, ns,pass, password = null;
    private TextView view;
    ListView listView;
    TextView mEmpty;
    ProgressBar progressBar;
    int n;
    int p = 0;
    private Boolean isFabOpen = false;
    FloatingActionButton fab,add;
    boolean isConnected;
    int admin = 0;
    ProgressBar pb;
    DatabaseReference flag;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private static final int PERMISSION_REQUEST_CODE = 200;
    private View pview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Projects");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dept_list);
        pb = (ProgressBar)findViewById(R.id.p);
/*
        mEmpty=(TextView) findViewById(R.id.empty_view);
        progressBar = (ProgressBar) findViewById(R.id.loading_spinner);

        ConnectivityManager cm = (ConnectivityManager)this.getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork!=null && activeNetwork.isConnectedOrConnecting();
*/

        Intent i = getIntent();
        if(i.hasExtra("p")) {
            password = i.getStringExtra("p");
            pb.setVisibility(View.VISIBLE);
        }
        if(password!=null)
            Log.i("TAG", password);
        final List<Department> dlist = new ArrayList<>();
        final DepartmentAdapter mDepartmentAdapter = new DepartmentAdapter(this, R.layout.dept_list, dlist);
        listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(mDepartmentAdapter);

        fab = (FloatingActionButton)findViewById(R.id.fab);
        add = (FloatingActionButton) findViewById(R.id.adduser);

        mFirebaseAuth = FirebaseAuth.getInstance();

        flag = FirebaseDatabase.getInstance().getReference("bhel");
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
                pb.setVisibility(View.GONE);
                listView.setEmptyView(findViewById(R.id.empty));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        String uid = "null";
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        if(currentFirebaseUser != null)
            uid = currentFirebaseUser.getUid();
        Log.i("uid at dl",uid);

        final DatabaseReference admindb = FirebaseDatabase.getInstance().getReference("bhel").child("users").child(uid);

        admindb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if(user!= null) {
                    admin = user.getAdmin();
                    pass = user.getPassword();
                    Log.i("TAG", "onDataChange: "+pass);
                    if(password!=null) {
                        if (pass != password)
                            admindb.child("password").setValue(password);
                    }
                    if(admin == 1) {
                        fab.setVisibility(View.VISIBLE);
                        //add.setVisibility(View.VISIBLE);
                    }
                    flag.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.hasChild("userequest")&admin==1)
                                add.setVisibility(View.VISIBLE);
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                    }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i("error at ui",":(");
            }
        });

        if(!checkPermission()) {
            Log.i("WEIRD AF NAME ********", "check");
            requestPermission();
        }

        if(fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(), DeptList.class);
                    startActivity(i);
                    //Toast.makeText(getBaseContext(),"FAB1 Click",Toast.LENGTH_SHORT).show();
                }
            });
        }
        if(add != null) {
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(), RequestList.class);
                    startActivity(i);
                    //Toast.makeText(getBaseContext(),"FAB1 Click",Toast.LENGTH_SHORT).show();
                }
            });
        }



    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);

        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE,READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean write = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean read = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (write && read)
                        Toast.makeText(this, "Permission granted.", Toast.LENGTH_SHORT).show();
                    else {

                        Toast.makeText(this, "Permission denied.", Toast.LENGTH_SHORT).show();

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)) {
                                showMessageOKCancel("You need to allow access to both the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE},
                                                            PERMISSION_REQUEST_CODE);
                                                }
                                            }
                                        });
                                return;
                            }
                        }

                    }
                }


                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(ListDepartments.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
/*
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        if(admin == 1)
            menu.add(0,1,0,"Add user");
*/  super.onCreateOptionsMenu(menu);

        menu.add(0, 0, 0, "Sign out");
        menu.add(0, 1, 0, "User Information");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case 0:

                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(this, MainActivity.class));
                break;
            case 1:

                startActivity(new Intent(this,UserInformation.class));
                break;
        }

        return true;
    }

    @Override
    public void onBackPressed() {
    }
}