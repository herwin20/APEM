package com.herwinlab.apem.pmgasturbine;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.ajts.androidmads.library.SQLiteToExcel;
import com.herwinlab.apem.R;
import com.herwinlab.apem.sqliteData.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class PmBattery extends AppCompatActivity {

    public DatabaseHelper myDb;
    public EditText namaGT, tegBatt, beratJenis, airBatt, nomorBattery;
    public LinearLayout btnSaved, btnDelete, btnView, btnUpdate, btnExport;
    public TextView namaBTM, tanggalBatt;

    // Excel
    SQLiteToExcel sqLiteToExcel;
    String directory_path = Environment.getExternalStorageDirectory().getPath() + "/Test/";

    // SharePrefs
    private SharedPreferences prefs;
    private final String TEGANGAN_KEY = "TEGANGAN";
    private final String BERATJENIS_KEY = "BERATJENIS";
    private final String AIRBATTERY_KEY = "AIRBATTERY";
    private final String NAMAGT_KEY = "NAMAGT";

    //Handler
    private final Handler handler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.battery_activity);

        // Untuk Tanggal
        handler.postDelayed(runnable, 1000);

        myDb = new DatabaseHelper(this);
        namaGT = findViewById(R.id.namaGTbatt);
        tegBatt =  findViewById(R.id.teg_batt);
        beratJenis = findViewById(R.id.berat_jenis);
        airBatt = findViewById(R.id.air_batt);
        nomorBattery = findViewById(R.id.nomor_batt);

        //Button Linear Layout
        btnSaved = findViewById(R.id.saved);
        btnDelete = findViewById(R.id.delete);
        btnView = findViewById(R.id.view);
        btnUpdate = findViewById(R.id.update);
        btnExport = findViewById(R.id.excel);

        // Text View
        namaBTM = findViewById(R.id.btm);
        tanggalBatt = findViewById(R.id.tanggalpmbatt);

        prefs = getPreferences(MODE_PRIVATE);
        namaGT.setText(prefs.getString(NAMAGT_KEY,""));
        tegBatt.setText(prefs.getString(TEGANGAN_KEY, ""));
        beratJenis.setText(prefs.getString(BERATJENIS_KEY, ""));
        airBatt.setText(prefs.getString(AIRBATTERY_KEY, ""));

        //Function DataBaseHelper
        AddData();
        viewAll();
        UpdateData();
        deleteData();

        btnExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createExcel();
            }
        });
    }

    public void createExcel()
    {
        sqLiteToExcel = new SQLiteToExcel(getApplicationContext(), DatabaseHelper.DATABASE_NAME, directory_path);
        sqLiteToExcel.exportAllTables("Battery.xls", new SQLiteToExcel.ExportListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onCompleted(String filePath) {
                Toast.makeText(PmBattery.this, "Export successful!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(PmBattery.this, "No Export", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Fungsi Untuk Jam dan Tanggal
    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Calendar c1 = Calendar.getInstance();
            SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/YYYY h:m:s a");
            String strdate1 = sdf1.format(c1.getTime());
            TextView txtdate1 = findViewById(R.id.tanggalpmbatt);
            txtdate1.setText(strdate1);

            handler.postDelayed(this, 1000);
        }
    };

    public void sharedPrefs()
    {
        prefs = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(NAMAGT_KEY, namaGT.getText().toString());
        editor.putString(TEGANGAN_KEY, tegBatt.getText().toString());
        editor.putString(BERATJENIS_KEY, beratJenis.getText().toString());
        editor.putString(AIRBATTERY_KEY, airBatt.getText().toString());
        editor.apply();
    }

    //delete Data
    public void deleteData()
    {
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPrefs();
                AlertDialog.Builder builder = new AlertDialog.Builder(PmBattery.this);
                builder.setCancelable(true);
                builder.setTitle("Warning");
                builder.setMessage("Delete data will make it disarray, better to update data");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Integer deletedRows = myDb.deleteData(nomorBattery.getText().toString());
                        if (deletedRows > 0)
                            Toast.makeText(PmBattery.this,"Data Deleted",Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(PmBattery.this,"Data Failed to Deleted!",Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                builder.show();
            }
        });

    }

    public void UpdateData() {
        btnUpdate.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sharedPrefs();
                        boolean isUpdate = myDb.updateData(nomorBattery.getText().toString(),
                                tanggalBatt.getText().toString(),
                                namaBTM.getText().toString(),
                                namaGT.getText().toString(),
                                tegBatt.getText().toString(),
                                beratJenis.getText().toString(),
                                airBatt.getText().toString() );
                        if(isUpdate == true)
                            Toast.makeText(PmBattery.this,"Data Updated",Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(PmBattery.this,"Data Failed to Update",Toast.LENGTH_LONG).show();
                    }
                }
        );

    }

    //fungsi tambah  //TANGGAL TEXT, BTMBTL TEXT, GT TEXT, TEGANGAN TEXT, BERAT_AIR TEXT, AIR_BATTERY TEXT
    public void AddData() {
        btnSaved.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sharedPrefs();
                        boolean checkInsertData = myDb.insertData (tanggalBatt.getText().toString(),
                                namaBTM.getText().toString(),
                                namaGT.getText().toString(),
                                tegBatt.getText().toString(),
                                beratJenis.getText().toString(),
                                airBatt.getText().toString() );
                        if(checkInsertData == true)
                            Toast.makeText(PmBattery.this,"Data Inserted",Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(PmBattery.this,"Data Not Inserted",Toast.LENGTH_LONG).show();
                    }
                }
        );
    }

    //fungsi menampilkan data
    public void viewAll() {
        btnView.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        sharedPrefs();
                        Cursor res = myDb.getAllData();
                        if(res.getCount() == 0) {
                            // show message
                            showMessage("Error","Noting Found");
                            return;
                        }
                        StringBuffer buffer = new StringBuffer();
                        while (res.moveToNext() ) {
                            buffer.append("Id : ").append(res.getString(0)).append("\n");
                            buffer.append("Tanggal : ").append(res.getString(1)).append("\n");
                            buffer.append("Posisi : ").append(res.getString(2)).append("\n");
                            buffer.append("Nama GT : ").append(res.getString(3)).append("\n");
                            buffer.append("Volt : ").append(res.getString(4)).append(" Volt").append("\n");
                            buffer.append("Berat Jenis : ").append(res.getString(5)).append(" Kg/cm").append("\n");
                            buffer.append("Air Battery : ").append(res.getString(6)).append(" %").append("\n\n");
                        }
                        // show all data
                        showMessage("Data",buffer.toString());
                    }
                }
        );
    }

    //membuat alert dialog
    public void showMessage(String title, String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }
}
