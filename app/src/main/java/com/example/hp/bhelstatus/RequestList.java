package com.example.hp.bhelstatus;

import android.app.VoiceInteractor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Adapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RequestList extends AppCompatActivity {

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_request);

        final List<UserRequest> plist = new ArrayList<>();
        final RequestAdapter mRequestAdapter = new RequestAdapter(this, R.layout.list_request, plist);
        listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(mRequestAdapter);
        listView.setEmptyView(findViewById(R.id.empty));

        DatabaseReference db = FirebaseDatabase.getInstance().getReference("bhel").child("userequest");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mRequestAdapter.clear();
                for(DataSnapshot childSnapshot: dataSnapshot.getChildren()){
                    UserRequest user = childSnapshot.getValue(UserRequest.class);
                    mRequestAdapter.add(user);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}