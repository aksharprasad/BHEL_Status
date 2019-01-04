package com.example.hp.bhelstatus;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.chilkatsoft.CkCsv;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Properties;

import javax.activation.CommandMap;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.MailcapCommandMap;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

public class Download extends AppCompatActivity {
    EditText edtYear;
    String ap,ae,id, deptname, key2,key, val,filename;
    int n;
    String csv_data,NAME;
    boolean s;
    Button update,send;
    int i=1,j=0,k=0,ab=0,a=0,b=0;
    Spinner year, quater;
    SharedPreferences.Editor editor;
    CkCsv csv;
    ProgressBar pb;
    ValueEventListener x,y;
    FileOutputStream out;
    private FirebaseAuth mAuth;
    byte []  data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.download);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        pb = (ProgressBar) findViewById(R.id.pb);

        csv = new CkCsv();

        csv.put_HasColumnNames(true);

        update = (Button) findViewById(R.id.update);
        send = (Button) findViewById(R.id.send);
        year = (Spinner) findViewById(R.id.year);
        quater = (Spinner) findViewById(R.id.quarter);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        deptname = intent.getStringExtra("name");
        n = intent.getIntExtra("n", 0);
        //ab = intent.getIntExtra("ab", 0);
        //Log.i("DOWNLOAD", ""+ab);

        DatabaseReference dbq = FirebaseDatabase.getInstance().getReference("bhel").child("fields").child(id).child("ab");
        dbq.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ab = Integer.parseInt(dataSnapshot.getValue(String.class));
                Log.i("TAG", "HEHEHE: "+ab);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Calendar calender;
        calender = Calendar.getInstance();

        data =  new ByteArrayOutputStream().toByteArray();;

        int yr = calender.get(Calendar.YEAR);
        ArrayList<String> years = new ArrayList<String>();
        for (i = yr + 3; i >= 2000; i--)
            years.add(Integer.toString(i)+ "-" + Integer.toString(i+1));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.view_spinner, years);
        year.setAdapter(adapter);

        ArrayList<String> q = new ArrayList<String>();
        q.add("Quarter 1");
        q.add("Quarter 2");
        q.add("Quarter 3");
        q.add("Quarter 4");
        ArrayAdapter<String> qadapter = new ArrayAdapter<String>(this, R.layout.view_spinner, q);
        quater.setAdapter(qadapter);

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

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pb.setVisibility(View.VISIBLE);
                csv.SetColumnName(0, "Projects");
                DatabaseReference dbp = FirebaseDatabase.getInstance().getReference("bhel").child("projects").child(id).child(year.getSelectedItem().toString().split("\\-")[0]).child(quater.getSelectedItem().toString());
                DatabaseReference dbf = FirebaseDatabase.getInstance().getReference("bhel").child("fields").child(id);

                dbf.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.i("Came", "here");
                        for (DataSnapshot fchildSnapshot : dataSnapshot.getChildren()) {
                            Log.i("key", fchildSnapshot.getKey());
                            Log.i("value", fchildSnapshot.getValue(String.class));
                            key = fchildSnapshot.getKey();
                            val = fchildSnapshot.getValue(String.class);
                            if(key.equals("ab")){
                                ab = Integer.parseInt(val);
                            }
                                if(key.startsWith("field")) {
                                    Log.i("TAGv", val);
                                    String[] kv = val.split("\\$");
                                    Log.i("TAG", "onDataChange: " + kv[0] + "||" + kv[1]);
                                    csv.SetColumnName(key.charAt(key.length()-1) - '0' + 1, kv[0]);
                            }
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });
                if(ab==0) {
                    dbp.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Log.i("HERE","HERE");
                            for (DataSnapshot id : dataSnapshot.getChildren()) {
                                for (DataSnapshot p : id.getChildren()) {
                                    Log.i("KEY", p.getKey());
                                    if (p.getKey().equals("name")) {
                                        csv.SetCell(k, 0, p.getValue(String.class));
                                        Log.i("NAME" + k, p.getValue(String.class));
                                    } else if (p.getKey().equals("fvalues")) {
                                        Log.i("FV","HERE");
                                        for (DataSnapshot pf : p.getChildren()) {
                                            key2 = pf.getKey();
                                            Log.i("FV",key2);
                                            csv.SetCell(k, key2.charAt(key2.length() - 1) - '0' + 1, pf.getValue(String.class));
                                            Log.i("FIELD" + k, pf.getValue(String.class));
                                        }
                                    }
                                }
                                k++;
                            }
                            pb.setVisibility(View.GONE);
                            send.setVisibility(View.VISIBLE);
                            NAME = deptname+"_"+year.getSelectedItem()+"_"+quater.getSelectedItem();
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                else if(ab==1) {

                    dbp.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Log.i("HERE","HERE");
                            for (DataSnapshot id : dataSnapshot.getChildren()) {
                                for (DataSnapshot p : id.getChildren()) {
                                    Log.i("KEY", p.getKey());
                                    if (p.getKey().equals("name")) {
                                        csv.SetCell(k, 0, p.getValue(String.class)+" A");
                                        csv.SetCell(k+1, 0, p.getValue(String.class)+" B");
                                        Log.i("NAME" + k, p.getValue(String.class));
                                    } else if (p.getKey().equals("a")) {
                                        Log.i("FV","HERE");
                                        for (DataSnapshot pf : p.child("fvalues").getChildren()) {
                                            key2 = pf.getKey();
                                            Log.i("FV",key2);
                                            csv.SetCell(k, key2.charAt(key2.length() - 1) - '0' + 1, pf.getValue(String.class));
                                            Log.i("FIELD" + k, pf.getValue(String.class));
                                        }
                                    }
                                    else if (p.getKey().equals("b")) {
                                        Log.i("FV","HERE");
                                        for (DataSnapshot pf : p.child("fvalues").getChildren()) {
                                            key2 = pf.getKey();
                                            Log.i("FV",key2);
                                            csv.SetCell(k+1, key2.charAt(key2.length() - 1) - '0' + 1, pf.getValue(String.class));
                                            Log.i("FIELD" + k, pf.getValue(String.class));
                                        }
                                    }
                                }
                                k=k+2;
                            }
                            pb.setVisibility(View.GONE);
                            send.setVisibility(View.VISIBLE);
                            NAME = deptname+"_"+year.getSelectedItem()+"_"+quater.getSelectedItem();
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

        });

        if(send!=null) {
            send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pb.setVisibility(View.VISIBLE);
                    csv_data = csv.saveToString();
                    Log.i("CSV", csv.saveToString());
                    //out = new FileOutputStream(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), deptname));
                    filename = "/storage/emulated/0/Download/"+NAME+".csv";
                    boolean success = csv.SaveFile(filename);
                    sendMail("Here is the file.", "Report");
                    if (success != true) {
                        Log.i("TAG", csv.lastErrorText());
                    }
                }
            });
        }
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

            MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
            mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
            mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
            mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
            mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
            mc.addMailcap("message/rfc822;; x-java-content- handler=com.sun.mail.handlers.message_rfc822");

            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.addRecipient(Message.RecipientType.TO,new InternetAddress(ae));
            Log.i("AE",""+ae);
            message.setSubject(subject);

            //3) create MimeBodyPart object and set your message text
            BodyPart messageBodyPart1 = new MimeBodyPart();
            messageBodyPart1.setText(body);

            //4) create new MimeBodyPart object and set DataHandler object to this object
            MimeBodyPart messageBodyPart2 = new MimeBodyPart();

            DataSource source = new ByteArrayDataSource(data,filename);
            Log.i("csv", "sendMail: "+csv_data);
            messageBodyPart2.setDataHandler(new DataHandler(new ByteArrayDataSource(csv_data.getBytes(),"text/csv")));
            messageBodyPart2.setFileName(NAME+".csv");
            Log.i("filename",filename);


            //5) create Multipart object and add MimeBodyPart objects to this object
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart1);
            multipart.addBodyPart(messageBodyPart2);

            //6) set the multiplart object to the message object
            message.setContent(multipart,"text/csv" );

            new Download.SendMailTask().execute(message);

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
                Thread.currentThread().setContextClassLoader( getClass().getClassLoader() );
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
                Log.i(":(", ""+e);
                return "error2";
            }

        }


        @Override
        protected void onPostExecute(String result) {
            pb.setVisibility(View.GONE);
            if(result.equals("Success"))
            {
                super.onPostExecute(result);
                // progressDialog.dismiss();
                Toast.makeText(Download.this, "Mail Sent Successfully", Toast.LENGTH_LONG).show();
            }
            else
            if(result.equals("error1"))
                Toast.makeText(Download.this, "Email Failure", Toast.LENGTH_LONG).show();
            else
            if(result.equals("error2"))
                Toast.makeText(Download.this, "Email Sent problem2", Toast.LENGTH_LONG).show();
        }
    }

    static {
        System.loadLibrary("chilkat");
    }
}
