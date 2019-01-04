package com.example.hp.bhelstatus;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

public class Upload extends AppCompatActivity {
    String deptname,name, pid, id, year, q;
    int admin,n;
    Button but;
    public static View view;
    FileInputStream ip = null;
    XSSFWorkbook workbook = null;
    FileInputStream Fin;
    DatabaseReference db;
    EditText file;
    String up;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        Log.i("BEFORE1", "INTENT");
        Intent intent = getIntent();

        deptname = intent.getStringExtra("deptname");
        name = intent.getStringExtra("name");
        n = intent.getIntExtra("n", 0);
        pid = intent.getStringExtra("pid");
        Log.i("pid at PV",pid);
        id = intent.getStringExtra("id");
        Log.i("id at PV",id);

        SharedPreferences mPrefs = getSharedPreferences("YearPref",0);
        year = mPrefs.getString("year", "2018");
        q = mPrefs.getString("quarter", "Quarter 3" );

        file = (EditText) findViewById(R.id.file);
        db = FirebaseDatabase.getInstance().getReference("bhel").child("projects").child(id).child(year).child(q).child(pid).child("svalues");

        but = (Button) findViewById(R.id.but);

        // try {
        //   FileInputStream Fin = new FileInputStream(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/upload.xlsx");
        //}catch(Exception e){
        //  new CustomToast().Show_Toast(getApplicationContext(), view,""+e );
        //}

        //ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRI;TE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
          //      123);
        Log.i("W","tf");

        if(but != null) {
            but.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isReadStoragePermissionGranted() == true) {
                        try {
                            Log.i("", "LOL");
                            up = file.getText().toString();
                            try {
                                Log.i("", "Came here");
                                //ip = new FileInputStream(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/read.csv");
                                Fin = new FileInputStream(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), up));
                                //InputStream stream = new ByteArrayInputStream((Environment.DIRECTORY_DOWNLOADS + "/read.csv").getBytes(StandardCharsets.UTF_8));
                                Log.i("H", "" + Fin);
                                //Log.i("",""+ip);
                            } catch (Exception e) {
                                Log.i("", "NONO");
                            }
                            BufferedReader reader = new BufferedReader(
                                    new InputStreamReader(Fin, Charset.forName("UTF-8"))
                            );

                            // Initialization
                            String line = "";

                            // Initialization
                            try {
                                // Step over headers
                                reader.readLine();
                            } catch (Exception e) {
                            }

                            // If buffer is not empty
                            while ((line = reader.readLine()) != null) {
                                Log.d("MyActivity", "Line: " + line);
                                // use comma as separator columns of CSV
                                String[] tokens = line.split(",");
                                Log.i("V", "" + tokens[1]);
                                String sid = db.push().getKey();
                                Short s = new Short(tokens[0],tokens[1],tokens[2],tokens[3],tokens[4],tokens[5],sid,pid,id,year,q,name,deptname,n);
                                db.child(sid).setValue(s);
                            }
                            Log.i("DONE","DONE");
                            //new CustomToast().Show_Toast(getApplicationContext(),view , "Uploaded successfully.");
                            Intent myIntent = new Intent(Upload.this, Shortage.class);
                            myIntent.putExtra("name", name);
                            myIntent.putExtra("id",id);
                            myIntent.putExtra("pid", pid);
                            myIntent.putExtra("n", n);
                            myIntent.putExtra("deptname", deptname);
                            startActivity(myIntent);
                            Toast.makeText(Upload.this, "Uploaded successfully", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Log.e("TAG", "readExcelData: FileNotFoundException.");
                            Toast.makeText(Upload.this, "FileNotFound", Toast.LENGTH_SHORT).show();
                            //new CustomToast().Show_Toast(getApplicationContext(),view , "File not found.");
                        }
                    }
                    else{
                        Log.e("HEHE","FUCK");
                        Toast.makeText(Upload.this, "Permissioin to READ required", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    /*public void parseStringBuilder(StringBuilder mStringBuilder) {
        Log.d("", "parseStringBuilder: Started parsing.");

        // splits the sb into rows.
        String[] rows = mStringBuilder.toString().split(":");

        //Add to the ArrayList<XYValue> row by row
        for (int i = 0; i < rows.length; i++) {
            //Split the columns of the rows
            String[] columns = rows[i].split(",");

            //use try catch to make sure there are no "" that try to parse into doubles.
            try {
                String mat = columns[0];
                String des = columns[1];
                String sho = columns[2];
                String red = columns[3];
                String ppc = columns[4];

                String cellInfo = "(x,y):" + mat + "|" + des + "|" + sho + "|" + red + "|" + ppc;
                Log.d("", "ParseStringBuilder: Data from row: " + cellInfo);


                //add the the uploadData ArrayList
                //uploadData.add(new XYValue(x,y));

            } catch (NumberFormatException e) {

                Log.e("", "parseStringBuilder: NumberFormatException: " + e.getMessage());

            }
        }

        //printDataToLog();
    }

    private String getCellAsString(Row row, int c, FormulaEvaluator formulaEvaluator) {
        String value = "";
        try {
            Cell cell = row.getCell(c);
            CellValue cellValue = formulaEvaluator.evaluate(cell);
            switch (cellValue.getCellType()) {
                case Cell.CELL_TYPE_BOOLEAN:
                    value = "" + cellValue.getBooleanValue();
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    double numericValue = cellValue.getNumberValue();
                    if (HSSFDateUtil.isCellDateFormatted(cell)) {
                        double date = cellValue.getNumberValue();
                        SimpleDateFormat formatter =
                                new SimpleDateFormat("MM/dd/yy");
                        value = formatter.format(HSSFDateUtil.getJavaDate(date));
                    } else {
                        value = "" + numericValue;
                    }
                    break;
                case Cell.CELL_TYPE_STRING:
                    value = "" + cellValue.getStringValue();
                    break;
                default:
            }
        } catch (NullPointerException e) {

            Log.e("TAG", "getCellAsString: NullPointerException: " + e.getMessage());
        }
        return value;
    }*/
    public  boolean isReadStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG","Permission is granted1");
                return true;
            } else {

                Log.v("TAG","Permission is revoked1");
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 3);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG","Permission is granted1");
            return true;
        }
    }

    /*private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {//Can add more as per requirement

            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,android.Manifest.permission.READ_EXTERNAL_STORAGE},
                    123);

        } else {
            Log.i("TAG","Check permissions failed");
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 123: {


                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    try {
                        ip = new FileInputStream(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/upload.xlsx");
                    }catch (Exception e){
                        Log.e("HERE",""+e);
                    }
                    Log.i("",""+ip);
                } else {

                    checkPermissions();
                }
                return;
            }
        }
    }
    */
    @Override
    public void onBackPressed() {
        Intent myIntent = new Intent(Upload.this, Shortage.class);
        myIntent.putExtra("name", name);
        myIntent.putExtra("id",id);
        myIntent.putExtra("pid", pid);
        myIntent.putExtra("n", n);
        myIntent.putExtra("deptname", deptname);
        startActivity(myIntent);
        //super.onBackPressed();
    }
}

