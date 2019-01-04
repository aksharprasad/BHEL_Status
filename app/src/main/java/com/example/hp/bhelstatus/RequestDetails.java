package com.example.hp.bhelstatus;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class RequestDetails extends AppCompatActivity {

    String sname, seid, semail, sdob, scred, sid,ap = null,ae = null;
    TextView name, eid, email, dob, cred;
    private FirebaseAuth mAuth;
    DatabaseReference db;
    ProgressBar p;
    public static View view;
    String APPROVED_BODY;
    String APPROVED_SUBJECT;
    CheckBox ppc,test,gu;
    String REJECTED_BODY;
    String REJECTD_SUBJECT;
    int type = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_user);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        mAuth = FirebaseAuth.getInstance();

        Intent intent = getIntent();

        p = (ProgressBar) findViewById(R.id.prog);

        seid = intent.getStringExtra("eid");
        semail = intent.getStringExtra("email");
        sname = intent.getStringExtra("name");
        scred = intent.getStringExtra("cred");
        sdob = intent.getStringExtra("dob");
        sid = intent.getStringExtra("id");

        Log.i("eid",seid);
        Log.i("eid",sname);
        Log.i("eid",semail);
        Log.i("eid",scred);
        Log.i("eid",sdob);
        Log.i("eid",sid);

        APPROVED_BODY = "Dear "+sname+"\n\nYour BHEL Status account has been approved.\nYou can login with\n\nE-mail: "+semail+"\nPassword: "+sdob+"\n\nWith regards,\nAdministrator,BHEL";
        APPROVED_SUBJECT = "BHEL Status account approved";
        REJECTED_BODY = "Dear"+sname+"\n\nYour BHEL Status account has been rejected. Please contact the administrator for further queries.\n\nWith regards,\nAdministrator,BHEL";
        REJECTD_SUBJECT = "BHEL Status account rejected";

        TextView name = (TextView) findViewById(R.id.name);
        TextView email = (TextView) findViewById(R.id.email);
        TextView cred = (TextView) findViewById(R.id.cred);
        TextView eid = (TextView) findViewById(R.id.eid);
        TextView dob = (TextView) findViewById(R.id.dob);

        ppc = (CheckBox) findViewById(R.id.ppc);
        test = (CheckBox) findViewById(R.id.test);
        gu = (CheckBox) findViewById(R.id.gu);

        name.setText(sname);
        email.setText(semail);
        cred.setText(scred);
        eid.setText(seid);
        dob.setText(sdob);

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
                        ap = user.getPassword();
                        ae = user.getEmail();
                    }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i("error at ui",":(");
            }
        });

        db = FirebaseDatabase.getInstance().getReference("bhel").child("userequest").child(sid);

        Button reject = (Button) findViewById(R.id.no);
        reject.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                p.setVisibility(View.VISIBLE);
                db.removeValue();
                sendMail(REJECTED_BODY, REJECTD_SUBJECT);
            }
        });

        Button accept = (Button) findViewById(R.id.yes);
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!ppc.isChecked() && !test.isChecked() && !gu.isChecked())
                    Toast.makeText(getApplicationContext(), "Please select the type of user", Toast.LENGTH_LONG);
                    //new CustomToast().Show_Toast(getApplicationContext(), view, "Please select the type of user");
                else if((ppc.isChecked() && test.isChecked()) || (gu.isChecked() && test.isChecked()) || (ppc.isChecked() && gu.isChecked()) || (ppc.isChecked() && test.isChecked() && gu.isChecked())){
                    Toast.makeText(getApplicationContext(), "Please select only one class of user", Toast.LENGTH_LONG);
                }
                else {
                    p.setVisibility(View.VISIBLE);
                    if(ppc.isChecked())
                        type = 1;
                    else if(test.isChecked())
                        type = 2;
                    else
                        type = 0;
                    mAuth.createUserWithEmailAndPassword(semail, sdob).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                finish();
                                createNewUser(task.getResult().getUser());
                                //Toast.makeText(getApplicationContext(), "Done", Toast.LENGTH_SHORT).show();
                                sendMail(APPROVED_BODY, APPROVED_SUBJECT);
                                db.removeValue();
                            } else {

                                p.setVisibility(View.GONE);

                                if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                    Toast.makeText(getApplicationContext(), "This person is already registered", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                                db.removeValue();
                            }
                        }

                    });
                }

            }
        });

    }
    private void createNewUser(FirebaseUser userFromRegistration) {
        String email = userFromRegistration.getEmail();
        String userId = userFromRegistration.getUid();

        User user = new User(type,email,userId,sname,sdob);

        DatabaseReference db = FirebaseDatabase.getInstance().getReference("bhel");

        db.child("users").child(userId).setValue(user);
    }

    protected void sendMail(String body, String subject) {
        final String username = "bhelrvce@gmail.com";
        final String password = "bhel123rvce";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(semail));
            message.setSubject(subject);
            message.setText(body);
            new SendMailTask().execute(message);

        }catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
    private class SendMailTask extends AsyncTask<Message,String, String> {
        //private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //progressDialog = ProgressDialog.show(RequestDetails.this,null, "Sending mail", true, false);
        }

        @Override
        protected String doInBackground(Message... messages) {
            try {
                Transport.send(messages[0]);
                return "Success";
            }
            catch(SendFailedException ee)
            {
               // if(progressDialog.isShowing())
                 //   progressDialog.dismiss();
                return "error1";
            }catch (MessagingException e) {
                //if(progressDialog.isShowing())
                  //  progressDialog.dismiss();
                return "error2";
            }

        }


        @Override
        protected void onPostExecute(String result) {
            if(result.equals("Success"))
            {
                super.onPostExecute(result);
               // progressDialog.dismiss();
                Toast.makeText(RequestDetails.this, "Mail Sent Successfully", Toast.LENGTH_LONG).show();
            }
            else
            if(result.equals("error1"))
                Toast.makeText(RequestDetails.this, "Email Failure", Toast.LENGTH_LONG).show();
            else
            if(result.equals("error2"))
                Toast.makeText(RequestDetails.this, "Email Sent problem2", Toast.LENGTH_LONG).show();
            FirebaseAuth.getInstance().signOut();
            finish();
            mAuth.signInWithEmailAndPassword(ae, ap).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    p.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        finish();
                        Intent intent = new Intent(getApplicationContext(), RequestList.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "Please sign in again.", Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                    }
                }
            });

        }
    }
}
