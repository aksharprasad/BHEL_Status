package com.example.hp.bhelstatus;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;
import android.app.AlertDialog;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DepartmentAdapter extends ArrayAdapter<Department> {
    private Context mcontext;
    private List<Department> dlist;
    private int ab;
    DatabaseReference db;
    int admin = 0;
    View list_item;
    public DepartmentAdapter(Context context, int resource, List<Department> objects) {
        super(context, resource, objects);
        dlist = objects;
        mcontext = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @Nullable ViewGroup parent) {

        String name;
        list_item = convertView;
        if (list_item == null) {
            list_item = LayoutInflater.from(getContext()).inflate(R.layout.list_project, parent, false);
        }
        final Department current = getItem(position);



        TextView circle = (TextView) list_item.findViewById(R.id.circle);
        TextView stat = list_item.findViewById(R.id.img);
        TextView shor = list_item.findViewById(R.id.shor);
        ImageView  download = list_item.findViewById(R.id.download);
        LinearLayout bg = list_item.findViewById(R.id.bg);

        download.setImageResource(R.drawable.outline_cloud_download_black_18dp);

        shor.setText("Changes");
        stat.setText("List");

        String uid = "null";
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        if(currentFirebaseUser!=null)
            uid = currentFirebaseUser.getUid();
        Log.i("uid at dl",uid);

        final AlertDialog myQuittingDialogBox =new AlertDialog.Builder(getContext())
                //set message, title, and icon
                .setTitle("Delete")
                .setMessage("Do you want to Delete")
                .setIcon(R.drawable.baseline_person_add_white_18dp)

                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        db.child("fields").child(current.getID()).removeValue();
                        db.child("projects").child(current.getID()).removeValue();
                        dialog.dismiss();
                    }

                })



                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                })
                .create();



        DatabaseReference admindb = FirebaseDatabase.getInstance().getReference("bhel").child("users").child(uid);

        admindb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if(user!= null) {
                    admin = user.getAdmin();
                    Log.i("ADMIN","1");
                    if(admin == 1) {
                        Log.i("ADMIN","1");
                        list_item.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {
                                myQuittingDialogBox.show();
                                return false;
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i("error at ui",":(");
            }
        });


        /*
        GradientDrawable drawable = (GradientDrawable) circle.getBackground().mutate();

        switch (current.isP()) {
            case 0:
                drawable.setColor(0xffff4c4c);
                break;
            case 1:
                drawable.setColor(0xffffff66);
                break;
            case 2:
                drawable.setColor(0xff4c4cff);
                break;
            case 3:
                drawable.setColor(0xff6ef33a);
                break;
        }
*/
        list_item.setLongClickable(true);

        stat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent myIntent = new Intent(mcontext, ProjectList.class);
                myIntent.putExtra("name", current.getName());
                myIntent.putExtra("id", current.getID());
                myIntent.putExtra("n", current.getN());
                mcontext.startActivity(myIntent);
            }
        });
        shor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(mcontext, ViewChanges.class);
                myIntent.putExtra("name", current.getName());
                myIntent.putExtra("id", current.getID());
                myIntent.putExtra("n", current.getN());
                mcontext.startActivity(myIntent);
            }
        });
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(mcontext, Download.class);
                myIntent.putExtra("name", current.getName());
                myIntent.putExtra("id", current.getID());
                myIntent.putExtra("n", current.getN());
                //myIntent.putExtra("ab", ab);
                mcontext.startActivity(myIntent);
            }
        });

        db = FirebaseDatabase.getInstance().getReference("bhel");



        TextView d = (TextView) list_item.findViewById(R.id.textView);
        name = current.getName();
        d.setText(name);
        circle.setText(String.valueOf(name.charAt(0)));

        return list_item;
    }

    private AlertDialog AskOption()
    {
        AlertDialog myQuittingDialogBox =new AlertDialog.Builder(getContext())
                //set message, title, and icon
                .setTitle("Delete")
                .setMessage("Do you want to Delete")
                .setIcon(R.drawable.round_delete_black_18dp)

                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {

                        dialog.dismiss();
                    }

                })



                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                })
                .create();
        return myQuittingDialogBox;

    }

}
