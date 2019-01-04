package com.example.hp.bhelstatus;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserInformation extends AppCompatActivity {

    TextView vname,vemail,vuid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("User Information");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_user);

        String uid = "null";
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        if(currentFirebaseUser!=null)
            uid = currentFirebaseUser.getUid();
        Log.i("uid at ui",uid);

        vname = (TextView) findViewById(R.id.name);
        vemail = (TextView) findViewById(R.id.email);
        //vuid = (TextView) findViewById(R.id.uid);

        DatabaseReference db = FirebaseDatabase.getInstance().getReference("bhel").child("users").child(uid);

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if(user!= null) {
                    vname.setText(user.getUsername());
                    vemail.setText(user.getEmail());
                    //vuid.setText(user.getUid());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i("error at ui",":(");
            }
        });

    }
}
