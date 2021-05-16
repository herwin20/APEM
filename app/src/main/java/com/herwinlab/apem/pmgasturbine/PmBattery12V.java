package com.herwinlab.apem.pmgasturbine;

import android.app.Notification;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.media.MediaScannerConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.herwinlab.apem.R;
import com.herwinlab.apem.notification.NotificationUtils;
import com.herwinlab.apem.pmgasturbine.imagefunction.CameraActivity;
import com.kyanogen.signatureview.SignatureView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class PmBattery12V extends AppCompatActivity {

    //Notifikasi
    private NotificationUtils mNotificationUtils;

    // Coordinator Layout
    public CoordinatorLayout Coor_batt;

    //Digital Signature
    SignatureView signatureView;
    String path;
    private static final String IMAGE_DIRECTORY = "/Signature";

    //Custom Dialog
    AlertDialog.Builder dialogCustom, dialogCustomTTD, dialogCustomPDF;
    LayoutInflater inflater, inflaterTTD, inflaterPDF;
    View dialogView, dialogViewTTD, view, viewTTD, dialogViewPDF;

    //PDF
    Bitmap bitmap, scaleBitmap, PJBscale, IPJBway, accuImg, bitmapTTD, turbineImg;
    int pageWidth = 1200;
    Date dateTime;
    DateFormat dateFormat;

    public Button PhotoActivity;

    public TextInputEditText No_1, No_2, No_3, No_4, No_5, No_6, No_7, No_8, No_9, No_10;
    public TextInputEditText No_11, No_12, No_13, No_14, No_15, No_16, No_17, No_18, No_19, No_20;
    public TextInputEditText No_21, No_22, No_23, No_24, No_25, No_26, No_27, No_28, No_29, No_30;
    public TextInputEditText No_31, No_32, No_33, No_34, No_35, No_36;
    public TextInputEditText Nama_Unit, Voltage;

    //Edit Text For Noted, Aux, Information
    public TextView JudulPmBatt, Hasil;
    public EditText Catatan1, Catatan2;
    public EditText orangPm1, orangPm2, orangPm3, Operator, NoWO, namaGT, namaPDF;

    private SharedPreferences prefs;
    private final String No_1KEY = "1", No_2KEY = "2", No_3KEY = "3", No_4KEY = "4", No_5KEY = "5", No_6KEY = "6", No_7KEY = "7", No_8KEY = "8", No_9KEY = "9", No_10KEY = "10";
    private final String No_11KEY = "11", No_12KEY = "12", No_13KEY = "13", No_14KEY = "14", No_15KEY = "15", No_16KEY = "16", No_17KEY = "17", No_18KEY = "18", No_19KEY = "19", No_20KEY = "20";
    private final String No_21KEY = "21", No_22KEY = "22", No_23KEY = "23", No_24KEY = "24", No_25KEY = "25", No_26KEY = "26", No_27KEY = "27", No_28KEY = "28", No_29KEY = "29", No_30KEY = "30";
    private final String No_31KEY = "31", No_32KEY = "32", No_33KEY = "33", No_34KEY = "34", No_35KEY = "35", No_36KEY = "36";
    private final String VOLTAGE_KEY = "VOLTAGE";
    private final String CAT1_KEY = "CAT1";
    private final String CAT2_KEY = "CAT2";
    private final String ORANG1_KEY = "ORANG1";
    private final String ORANG2_KEY = "ORANG2";
    private final String ORANG3_KEY = "ORANG3";
    private final String OPERATOR_KEY = "OPERATOR";
    private final String NAMAGT_KEY = "NAMAGT";

    // Button Linear Layout
    LinearLayout btnSignOps, btnCreatedPDF, dialogCreatedPDF, btnCalculation, btnSignSPV;

    //Button
    public Button mClear, mGetSign, mCancel;

    // Untuk Handler Tanggal dan Waktu
    private final Handler handler = new Handler();

    // Creating Separate Directory for Signature and Image
    String DIRECTORY = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Signature/";

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.battery12_activity);

        transparentStatusAndNavigation();

        // Untuk Tanggal
        handler.postDelayed(runnable, 1000);

        //Initiate Voltage and Fill
        No_1 = findViewById(R.id.no_1); No_2 = findViewById(R.id.no_2); No_3 = findViewById(R.id.no_3); No_4 = findViewById(R.id.no_4); No_5 = findViewById(R.id.no_5);
        No_6 = findViewById(R.id.no_6); No_7 = findViewById(R.id.no_7); No_8 = findViewById(R.id.no_8); No_9 = findViewById(R.id.no_9); No_10 = findViewById(R.id.no_10);
        No_11 = findViewById(R.id.no_11); No_12 = findViewById(R.id.no_12); No_13 = findViewById(R.id.no_13); No_14 = findViewById(R.id.no_14); No_15 = findViewById(R.id.no_15);
        No_16 = findViewById(R.id.no_16); No_17 = findViewById(R.id.no_17); No_18 = findViewById(R.id.no_18); No_19 = findViewById(R.id.no_19); No_20 = findViewById(R.id.no_20);
        No_21 = findViewById(R.id.no_21); No_22 = findViewById(R.id.no_22); No_23 = findViewById(R.id.no_23); No_24 = findViewById(R.id.no_24); No_25 = findViewById(R.id.no_25);
        No_26 = findViewById(R.id.no_26); No_27 = findViewById(R.id.no_27); No_28 = findViewById(R.id.no_28); No_29 = findViewById(R.id.no_29); No_30 = findViewById(R.id.no_30);
        No_31 = findViewById(R.id.no_31); No_32 = findViewById(R.id.no_32); No_33 = findViewById(R.id.no_33); No_34 = findViewById(R.id.no_34); No_35 = findViewById(R.id.no_35);
        No_36 = findViewById(R.id.no_36);
        //Aux Initiated
        Catatan1 = findViewById(R.id.ket);
        Catatan2 = findViewById(R.id.ket2);
        orangPm1 = findViewById(R.id.nama_pm1);
        orangPm2 = findViewById(R.id.nama_pm2);
        orangPm3 = findViewById(R.id.nama_pm3);
        Operator = findViewById(R.id.nama_operator);
        Nama_Unit = findViewById(R.id.unit);
        Voltage = findViewById(R.id.voltage);
        // Judul PM
        JudulPmBatt = findViewById(R.id.judul_pmbatt);
        //Nama_unit
        Nama_Unit = findViewById(R.id.unit);
        // Tegangan Battery
        Hasil = findViewById(R.id.voltageCalculation);

        prefs = getPreferences(MODE_PRIVATE);
        No_1.setText(prefs.getString(No_1KEY,"")); No_2.setText(prefs.getString(No_2KEY,"")); No_3.setText(prefs.getString(No_3KEY,"")); No_4.setText(prefs.getString(No_4KEY,"")); No_5.setText(prefs.getString(No_5KEY,""));
        No_6.setText(prefs.getString(No_6KEY,"")); No_7.setText(prefs.getString(No_7KEY,"")); No_8.setText(prefs.getString(No_8KEY,"")); No_9.setText(prefs.getString(No_9KEY,"")); No_10.setText(prefs.getString(No_10KEY,""));
        No_11.setText(prefs.getString(No_11KEY,"")); No_12.setText(prefs.getString(No_12KEY,"")); No_13.setText(prefs.getString(No_13KEY,"")); No_14.setText(prefs.getString(No_14KEY,"")); No_15.setText(prefs.getString(No_15KEY,""));
        No_16.setText(prefs.getString(No_16KEY,"")); No_17.setText(prefs.getString(No_17KEY,"")); No_18.setText(prefs.getString(No_18KEY,"")); No_19.setText(prefs.getString(No_19KEY,"")); No_20.setText(prefs.getString(No_20KEY,""));
        No_21.setText(prefs.getString(No_21KEY,"")); No_22.setText(prefs.getString(No_22KEY,"")); No_23.setText(prefs.getString(No_23KEY,"")); No_24.setText(prefs.getString(No_24KEY,"")); No_25.setText(prefs.getString(No_25KEY,""));
        No_26.setText(prefs.getString(No_26KEY,"")); No_27.setText(prefs.getString(No_27KEY,"")); No_28.setText(prefs.getString(No_28KEY,"")); No_29.setText(prefs.getString(No_29KEY,"")); No_30.setText(prefs.getString(No_30KEY,""));
        No_31.setText(prefs.getString(No_31KEY,"")); No_32.setText(prefs.getString(No_32KEY,"")); No_33.setText(prefs.getString(No_33KEY,"")); No_34.setText(prefs.getString(No_34KEY,"")); No_35.setText(prefs.getString(No_35KEY,""));
        No_36.setText(prefs.getString(No_36KEY,""));
        Catatan1.setText(prefs.getString(CAT1_KEY, ""));
        Catatan2.setText(prefs.getString(CAT2_KEY, ""));
        orangPm1.setText(prefs.getString(ORANG1_KEY, ""));
        orangPm2.setText(prefs.getString(ORANG2_KEY, ""));
        orangPm3.setText(prefs.getString(ORANG3_KEY, ""));
        Operator.setText(prefs.getString(OPERATOR_KEY, ""));
        Nama_Unit.setText(prefs.getString(NAMAGT_KEY,""));
        Voltage.setText(prefs.getString(VOLTAGE_KEY, ""));

        // Koordinator Layout
        Coor_batt = findViewById(R.id.coor_batt);

        mNotificationUtils = new NotificationUtils(this);

        //Logo PJB
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.logo_pjb);
        PJBscale = Bitmap.createScaledBitmap(bitmap,200, 100, false);
        //Logo IPJB
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.logo_ipjb);
        IPJBway = Bitmap.createScaledBitmap(bitmap,260, 100, false);
        //Logo Turbine
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.turbine);
        turbineImg = Bitmap.createScaledBitmap(bitmap, 514, 500, false);
        //Logo Accu
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.accu);
        accuImg = Bitmap.createScaledBitmap(bitmap, 200, 180, false);

        btnCalculation = findViewById(R.id.buttonResult);
        btnCalculation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String a1 = No_1.getText().toString(); String a11 = No_11.getText().toString(); String a21 = No_21.getText().toString(); String a31 = No_31.getText().toString();
                String a2 = No_2.getText().toString(); String a12 = No_12.getText().toString(); String a22 = No_22.getText().toString(); String a32 = No_32.getText().toString();
                String a3 = No_3.getText().toString(); String a13 = No_13.getText().toString(); String a23 = No_23.getText().toString(); String a33 = No_33.getText().toString();
                String a4 = No_4.getText().toString(); String a14 = No_14.getText().toString(); String a24 = No_24.getText().toString(); String a34 = No_34.getText().toString();
                String a5 = No_5.getText().toString(); String a15 = No_15.getText().toString(); String a25 = No_25.getText().toString(); String a35 = No_35.getText().toString();
                String a6 = No_6.getText().toString(); String a16 = No_16.getText().toString(); String a26 = No_26.getText().toString(); String a36 = No_36.getText().toString();
                String a7 = No_7.getText().toString(); String a17 = No_17.getText().toString(); String a27 = No_27.getText().toString();
                String a8 = No_8.getText().toString(); String a18 = No_18.getText().toString(); String a28 = No_28.getText().toString();
                String a9 = No_9.getText().toString(); String a19 = No_19.getText().toString(); String a29 = No_29.getText().toString();
                String a10 = No_10.getText().toString(); String a20 = No_20.getText().toString(); String a30 = No_30.getText().toString();


                if (a1.isEmpty()){No_1.setError("Field Empty!");No_1.requestFocus();}
                else if (a2.isEmpty()){No_2.setError("Field Empty!");No_2.requestFocus();}
                else if (a3.isEmpty()){No_3.setError("Field Empty!");No_3.requestFocus();}
                else if (a4.isEmpty()){No_4.setError("Field Empty!");No_4.requestFocus();}
                else if (a5.isEmpty()){No_5.setError("Field Empty!");No_5.requestFocus();}
                else if (a6.isEmpty()){No_6.setError("Field Empty!");No_6.requestFocus();}
                else if (a7.isEmpty()){No_7.setError("Field Empty!");No_7.requestFocus();}
                else if (a8.isEmpty()){No_8.setError("Field Empty!");No_8.requestFocus();}
                else if (a9.isEmpty()){No_9.setError("Field Empty!");No_9.requestFocus();}
                else if (a10.isEmpty()){No_10.setError("Field Empty!");No_10.requestFocus();}
                else if (a11.isEmpty()){No_11.setError("Field Empty!");No_11.requestFocus();}
                else if (a12.isEmpty()){No_12.setError("Field Empty!");No_12.requestFocus();}
                else if (a13.isEmpty()){No_13.setError("Field Empty!");No_13.requestFocus();}
                else if (a14.isEmpty()){No_14.setError("Field Empty!");No_14.requestFocus();}
                else if (a15.isEmpty()){No_15.setError("Field Empty!");No_15.requestFocus();}
                else if (a16.isEmpty()){No_16.setError("Field Empty!");No_16.requestFocus();}
                else if (a17.isEmpty()){No_17.setError("Field Empty!");No_17.requestFocus();}
                else if (a18.isEmpty()){No_18.setError("Field Empty!");No_18.requestFocus();}
                else if (a19.isEmpty()){No_19.setError("Field Empty!");No_19.requestFocus();}
                else if (a20.isEmpty()){No_20.setError("Field Empty!");No_20.requestFocus();}
                else if (a21.isEmpty()){No_21.setError("Field Empty!");No_21.requestFocus();}
                else if (a22.isEmpty()){No_22.setError("Field Empty!");No_22.requestFocus();}
                else if (a23.isEmpty()){No_23.setError("Field Empty!");No_23.requestFocus();}
                else if (a24.isEmpty()){No_24.setError("Field Empty!");No_24.requestFocus();}
                else if (a25.isEmpty()){No_25.setError("Field Empty!");No_25.requestFocus();}
                else if (a26.isEmpty()){No_26.setError("Field Empty!");No_26.requestFocus();}
                else if (a27.isEmpty()){No_27.setError("Field Empty!");No_27.requestFocus();}
                else if (a28.isEmpty()){No_28.setError("Field Empty!");No_28.requestFocus();}
                else if (a29.isEmpty()){No_29.setError("Field Empty!");No_29.requestFocus();}
                else if (a30.isEmpty()){No_30.setError("Field Empty!");No_30.requestFocus();}
                else if (a31.isEmpty()){No_31.setError("Field Empty!");No_31.requestFocus();}
                else if (a32.isEmpty()){No_32.setError("Field Empty!");No_32.requestFocus();}
                else if (a33.isEmpty()){No_33.setError("Field Empty!");No_33.requestFocus();}
                else if (a34.isEmpty()){No_34.setError("Field Empty!");No_34.requestFocus();}
                else if (a35.isEmpty()){No_35.setError("Field Empty!");No_35.requestFocus();}
                else if (a36.isEmpty()){No_36.setError("Field Empty!");No_36.requestFocus();}
                else {
                    calVoltage();
                    savedPrefs();
                }
            }
        });

        btnSignSPV = findViewById(R.id.buttonSignatureSPVBatt);
        btnSignSPV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signatureDigitalSPV();
                savedPrefs();
            }
        });

        btnSignOps = findViewById(R.id.buttonSignatureOPSBatt);
        btnSignOps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signatureDigitalOPS();
                savedPrefs();
            }
        });

        btnCreatedPDF = findViewById(R.id.buttonCreatepdf);
        btnCreatedPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(PmBattery12V.this, R.style.CustomAlertDialog);
                ViewGroup viewGroup = findViewById(R.id.content);
                final View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.dialog_createdpdf, viewGroup, false);
                builder.setView(dialogView);
                final AlertDialog alertDialog = builder.create();
                //dialogCustomPDF.setIcon(R.mipmap.ic_launcher);
                //dialogCustomPDF.setTitle("WARNING !!!");

                dialogCreatedPDF = dialogView.findViewById(R.id.dialogCreatepdf);
                dialogCreatedPDF.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onClick(View v) {
                        savedPrefs();
                        calVoltage();
                        CreatedPDF();
                        Notification.Builder notifPDF = mNotificationUtils.getAndroidChannelNotification("PDF Notification", "PDF telah berhasil disimpan di Storage");
                        mNotificationUtils.getManager().notify(101, notifPDF.build());
                        alertDialog.dismiss();
                    }
                });

                alertDialog.show();
            }
        });

        //Button
        PhotoActivity = findViewById(R.id.takePhotoActivity);
        PhotoActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PmBattery12V.this, CameraActivity.class);
                startActivity(intent);
            }
        });
    }

    public void savedPrefs()
    {
        prefs = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(No_1KEY, No_1.getText().toString()); editor.putString(No_2KEY, No_2.getText().toString()); editor.putString(No_3KEY, No_3.getText().toString()); editor.putString(No_4KEY, No_4.getText().toString()); editor.putString(No_5KEY, No_5.getText().toString());
        editor.putString(No_6KEY, No_6.getText().toString()); editor.putString(No_7KEY, No_7.getText().toString()); editor.putString(No_8KEY, No_8.getText().toString()); editor.putString(No_9KEY, No_9.getText().toString()); editor.putString(No_10KEY, No_10.getText().toString());
        editor.putString(No_11KEY, No_11.getText().toString()); editor.putString(No_12KEY, No_12.getText().toString()); editor.putString(No_13KEY, No_13.getText().toString()); editor.putString(No_14KEY, No_14.getText().toString()); editor.putString(No_15KEY, No_15.getText().toString());
        editor.putString(No_16KEY, No_16.getText().toString()); editor.putString(No_17KEY, No_17.getText().toString()); editor.putString(No_18KEY, No_18.getText().toString()); editor.putString(No_19KEY, No_19.getText().toString()); editor.putString(No_20KEY, No_20.getText().toString());
        editor.putString(No_21KEY, No_21.getText().toString()); editor.putString(No_22KEY, No_22.getText().toString()); editor.putString(No_23KEY, No_23.getText().toString()); editor.putString(No_24KEY, No_24.getText().toString()); editor.putString(No_25KEY, No_25.getText().toString());

        editor.putString(No_26KEY, No_26.getText().toString()); editor.putString(No_27KEY, No_27.getText().toString()); editor.putString(No_28KEY, No_28.getText().toString()); editor.putString(No_29KEY, No_29.getText().toString()); editor.putString(No_30KEY, No_30.getText().toString());
        editor.putString(No_31KEY, No_31.getText().toString()); editor.putString(No_32KEY, No_32.getText().toString()); editor.putString(No_33KEY, No_33.getText().toString()); editor.putString(No_34KEY, No_34.getText().toString()); editor.putString(No_35KEY, No_35.getText().toString());
        editor.putString(No_36KEY, No_36.getText().toString());

        editor.putString(CAT1_KEY, Catatan1.getText().toString());
        editor.putString(CAT2_KEY, Catatan2.getText().toString());
        editor.putString(ORANG1_KEY, orangPm1.getText().toString());
        editor.putString(ORANG2_KEY, orangPm2.getText().toString());
        editor.putString(ORANG3_KEY, orangPm3.getText().toString());
        editor.putString(OPERATOR_KEY, Operator.getText().toString());
        editor.putString(NAMAGT_KEY, Nama_Unit.getText().toString());
        editor.putString(VOLTAGE_KEY, Voltage.getText().toString());

        editor.apply();
    }

    // Press Back Saved it !!
    @Override
    public void onBackPressed()
    {
        Snackbar snackbar = Snackbar.make(Coor_batt,"Before Exit you have to Saved it ??", Snackbar.LENGTH_LONG)
                .setAction("EXIT", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        savedPrefs();
                        finish();
                    }
                });
        snackbar.show();
    }

    //Fungsi Untuk Jam dan Tanggal
    private final Runnable runnable = new Runnable() {
        @RequiresApi(api = Build.VERSION_CODES.N)
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

    public void calVoltage()
    {
        String a1 = No_1.getText().toString(); String a11 = No_11.getText().toString(); String a21 = No_21.getText().toString(); String a31 = No_31.getText().toString();
        String a2 = No_2.getText().toString(); String a12 = No_12.getText().toString(); String a22 = No_22.getText().toString(); String a32 = No_32.getText().toString();
        String a3 = No_3.getText().toString(); String a13 = No_13.getText().toString(); String a23 = No_23.getText().toString(); String a33 = No_33.getText().toString();
        String a4 = No_4.getText().toString(); String a14 = No_14.getText().toString(); String a24 = No_24.getText().toString(); String a34 = No_34.getText().toString();
        String a5 = No_5.getText().toString(); String a15 = No_15.getText().toString(); String a25 = No_25.getText().toString(); String a35 = No_35.getText().toString();
        String a6 = No_6.getText().toString(); String a16 = No_16.getText().toString(); String a26 = No_26.getText().toString(); String a36 = No_36.getText().toString();
        String a7 = No_7.getText().toString(); String a17 = No_17.getText().toString(); String a27 = No_27.getText().toString();
        String a8 = No_8.getText().toString(); String a18 = No_18.getText().toString(); String a28 = No_28.getText().toString();
        String a9 = No_9.getText().toString(); String a19 = No_19.getText().toString(); String a29 = No_29.getText().toString();
        String a10 = No_10.getText().toString(); String a20 = No_20.getText().toString(); String a30 = No_30.getText().toString();

        float b1 = Float.parseFloat(a1); float b11 = Float.parseFloat(a11); float b21 = Float.parseFloat(a21); float b31 = Float.parseFloat(a31);
        float b2 = Float.parseFloat(a2); float b12 = Float.parseFloat(a12); float b22 = Float.parseFloat(a22); float b32 = Float.parseFloat(a32);
        float b3 = Float.parseFloat(a3); float b13 = Float.parseFloat(a13); float b23 = Float.parseFloat(a23); float b33 = Float.parseFloat(a33);
        float b4 = Float.parseFloat(a4); float b14 = Float.parseFloat(a14); float b24 = Float.parseFloat(a24); float b34 = Float.parseFloat(a34);
        float b5 = Float.parseFloat(a5); float b15 = Float.parseFloat(a15); float b25 = Float.parseFloat(a25); float b35 = Float.parseFloat(a35);
        float b6 = Float.parseFloat(a6); float b16 = Float.parseFloat(a16); float b26 = Float.parseFloat(a26); float b36 = Float.parseFloat(a36);
        float b7 = Float.parseFloat(a7); float b17 = Float.parseFloat(a17); float b27 = Float.parseFloat(a27);
        float b8 = Float.parseFloat(a8); float b18 = Float.parseFloat(a18); float b28 = Float.parseFloat(a28);
        float b9 = Float.parseFloat(a9); float b19 = Float.parseFloat(a19); float b29 = Float.parseFloat(a29);
        float b10 = Float.parseFloat(a10); float b20 = Float.parseFloat(a20); float b30 = Float.parseFloat(a30);

        float teganganBattery = b1 + b2 + b3 + b4 + b5 + b6 + b7 + b8 + b9 + b10 + b11 + b12 + b13 + b14 + b15 + b16 + b17 + b18 + b19 + b20 +
                b21 + b22 + b23 + b24 + b25 + b26 + b27 + b28 + b29 + b30 + b31 + b32 + b33 + b34 + b35 + b36;
        //Volt = Float.toString(teganganBattery);
        float hasilBatt = teganganBattery / 2;
        Hasil.setText(String.format("%.2f", hasilBatt));

    }

    public void CreatedPDF()
    {
        //Image ttd tangan OPS
        String ImageFileOPS = DIRECTORY+"ttdOperatorPMbatt.jpg";
        Bitmap bitmapImageOPS = BitmapFactory.decodeFile(ImageFileOPS);
        //Image ttd tangan SPV
        String ImageFileSPV = DIRECTORY+"ttdSPVPMgen.jpg";
        Bitmap bitmapImageSPV = BitmapFactory.decodeFile(ImageFileSPV);

        /*
        //Photo Before, Process, After
        String ImageBefore  = DIRECTORY_PHOTO+"PmGenerator_"+namaGT.getText().toString()+"_Before.jpeg";
        Bitmap bitmapImageBfr = BitmapFactory.decodeFile(ImageBefore);
        String ImageProcess = DIRECTORY_PHOTO+"PmGenerator_"+namaGT.getText().toString()+"_Process.jpeg";
        Bitmap bitmapImagePro = BitmapFactory.decodeFile(ImageProcess);
        String ImageAfter   = DIRECTORY_PHOTO+"PmGenerator_"+namaGT.getText().toString()+"_After.jpeg";
        Bitmap bitmapImageAft = BitmapFactory.decodeFile(ImageAfter); */

        dateTime = new Date();
        PdfDocument pdfDocument = new PdfDocument();
        Paint paint = new Paint();
        Paint titlePaint = new Paint(); // Untuk Judul Header

        PdfDocument.PageInfo pageInfo
                = new PdfDocument.PageInfo.Builder(1200, 2010, 1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);

        Canvas canvas = page.getCanvas();
        //canvas.drawBitmap(scaleBitmap, 0, 0, paint);
        canvas.drawBitmap(PJBscale,50,75, paint); // Logo PJB
        //canvas.drawBitmap(IPJBway,905,75, paint); // Logo IPJB
        canvas.drawBitmap(turbineImg,655, 1300, paint );
        //canvas.drawBitmap(accuImg, 480, 1240, paint);

        //TTD Digital
        canvas.drawBitmap(Bitmap.createScaledBitmap(bitmapImageOPS,250,200,false),400,1980-180,paint );
        canvas.drawBitmap(Bitmap.createScaledBitmap(bitmapImageSPV,250,200,false),800,1980-180, paint);

        // Garis tepi
        canvas.drawLine(30, 30, 1170, 30, paint); // Garis Atas
        canvas.drawLine(30, 30, 30, 1980, paint); // Garis Kiri
        canvas.drawLine(1170, 30, 1170, 1980, paint); // Garis Kanan
        canvas.drawLine(30, 1980, 1170, 1980, paint); // Garis Bawah

        //Garis Header
        canvas.drawLine(30, 220, 1170, 220, paint);
        canvas.drawLine(30, 230, 1170, 230, paint);
        canvas.drawLine(265, 30, 265, 220, paint); // Kiri
        canvas.drawLine(905, 30, 905, 220, paint); // Kanan
        //Bawah tanggal
        canvas.drawLine(30, 310, 1170, 310, paint);
        //diatas Catatan
        canvas.drawLine(30, 1545, 900, 1545, paint);


        // Garis NO Document
        canvas.drawLine(905, 90, 1170, 90, paint);
        canvas.drawLine(905, 160, 1170, 160, paint);
        canvas.drawLine(905, 230, 1170, 230, paint);

        // Judul Header
        titlePaint.setTextAlign(Paint.Align.CENTER);
        titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        titlePaint.setColor(Color.BLACK);
        titlePaint.setTextSize(25f);
        canvas.drawText("PT PEMBANGKITAN JAWA BALI UP MUARA TAWAR", 1170 / 2, 70, titlePaint);
        titlePaint.setTextSize(20f);
        canvas.drawText("PJB INTEGRATED MANAGEMENT SYSTEM", 1170 / 2, 110, titlePaint);
        titlePaint.setTextSize(25f);
        canvas.drawText("CEK LIST PREVENTIVE PEMELIHARAAN RUTIN ", 1170 / 2, 155, titlePaint);
        canvas.drawText("BATTERY BANK 12VDC GT", 1170 / 2, 195, titlePaint);

        titlePaint.setTextSize(18f);
        titlePaint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("Nomor Dokumen : FMT-17.2.1", 912,65, titlePaint);
        canvas.drawText("Revisi : 00", 912,135, titlePaint);
        canvas.drawText("Tanggal Terbit : 20-08-2013", 912,195, titlePaint);

        // Nama Preventive GT dan Tanggal
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setColor(Color.BLACK);
        paint.setTextSize(30f);
        canvas.drawText(JudulPmBatt.getText().toString(), 40, 270, paint ); // Judul PM
//        canvas.drawText(NoWO.getText().toString(),40,310, paint); // No WO
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setColor(Color.BLACK);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        paint.setTextSize(30);
        canvas.drawText(Nama_Unit.getText().toString(), 1170/2, 270, paint); // Nama GT
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        paint.setColor(Color.BLACK);
        paint.setTextSize(30);
        dateFormat = new SimpleDateFormat("dd/MM/yy");
        canvas.drawText(dateFormat.format(dateTime), 1170 - 145, 270, paint);
        dateFormat = new SimpleDateFormat("HH:mm:ss");
        canvas.drawText(dateFormat.format(dateTime), 1170 - 145, 300, paint);

        //Garis Footer
        canvas.drawLine(30, 1800, 1170, 1800, paint);
        canvas.drawLine(390, 1800, 390, 1980, paint);
        canvas.drawLine(780, 1800, 780, 1980, paint);

        // Tanda Tangan dan Nama Pelaksana
        paint.setTextSize(25);
        canvas.drawText("Nama Pelaksana :", 50, 1980-150, paint);
        canvas.drawText("1. "+orangPm1.getText().toString(), 70, 1980-110, paint);
        canvas.drawText("2. "+orangPm2.getText().toString(), 70, 1980-70, paint);
        canvas.drawText("3. "+orangPm3.getText().toString(), 70, 1980-30, paint);

        canvas.drawText("Regu Operasi", 420, 1980-150, paint );
        canvas.drawText(Operator.getText().toString(), 420, 1980-30, paint);

        canvas.drawText("SPV Listrik 1-2", 800, 1980-150, paint );
        canvas.drawText("Dammora W", 800, 1980-30, paint);

        // Content
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setColor(Color.BLACK);
        paint.setTextSize(35f);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText("- Voltage Battery", 50, 350, paint);
        canvas.drawText("- Catatan - catatan", 50, 1580, paint);
        canvas.drawText("Voltage Total :", 720, 1280, paint);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        canvas.drawText("1. "+Catatan1.getText().toString(),100, 1630, paint);
        canvas.drawText("2. "+Catatan2.getText().toString(), 100, 1730, paint);

        paint.setTextAlign(Paint.Align.LEFT);
        paint.setColor(Color.BLACK);
        paint.setTextSize(35f);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        canvas.drawText("1. ", 80, 400, paint); canvas.drawText("21. ", 280, 400, paint);
        canvas.drawText("2. ", 80, 440, paint); canvas.drawText("22. ", 280, 440, paint);
        canvas.drawText("3. ", 80, 480, paint); canvas.drawText("23. ", 280, 480, paint);
        canvas.drawText("4. ", 80, 520, paint); canvas.drawText("24. ", 280, 520, paint);
        canvas.drawText("5. ", 80, 560, paint); canvas.drawText("25. ", 280, 560, paint);
        canvas.drawText("6. ", 80, 600, paint); canvas.drawText("26. ", 280, 600, paint);
        canvas.drawText("7. ", 80, 640, paint); canvas.drawText("27. ", 280, 640, paint);
        canvas.drawText("8. ", 80, 680, paint); canvas.drawText("28. ", 280, 680, paint);
        canvas.drawText("9. ", 80, 720, paint); canvas.drawText("29. ", 280, 720, paint);
        canvas.drawText("10. ", 80, 760, paint); canvas.drawText("30. ", 280, 760, paint);
        canvas.drawText("11. ", 80, 800, paint); canvas.drawText("31. ", 280, 800, paint);
        canvas.drawText("12. ", 80, 840, paint); canvas.drawText("32. ", 280, 840, paint);
        canvas.drawText("13. ", 80, 880, paint); canvas.drawText("33. ", 280, 880, paint);
        canvas.drawText("14. ", 80, 920, paint); canvas.drawText("34. ", 280, 920, paint);
        canvas.drawText("15. ", 80, 960, paint); canvas.drawText("35. ", 280, 960, paint);
        canvas.drawText("16. ", 80, 1000, paint); canvas.drawText("36. ", 280, 1000, paint);
        canvas.drawText("17. ", 80, 1040, paint);
        canvas.drawText("18. ", 80, 1080, paint);
        canvas.drawText("19. ", 80, 1120, paint);
        canvas.drawText("20. ", 80, 1160, paint);

        //Tegangan Battery
        canvas.drawText(No_1.getText().toString()+" V", 140, 400, paint); canvas.drawText(No_21.getText().toString()+" V", 340, 400, paint);
        canvas.drawText(No_2.getText().toString()+" V", 140, 440, paint); canvas.drawText(No_22.getText().toString()+" V", 340, 440, paint);
        canvas.drawText(No_3.getText().toString()+" V", 140, 480, paint); canvas.drawText(No_23.getText().toString()+" V", 340, 480, paint);
        canvas.drawText(No_4.getText().toString()+" V", 140, 520, paint); canvas.drawText(No_24.getText().toString()+" V", 340, 520, paint);
        canvas.drawText(No_5.getText().toString()+" V", 140, 560, paint); canvas.drawText(No_25.getText().toString()+" V", 340, 560, paint);
        canvas.drawText(No_6.getText().toString()+" V", 140, 600, paint); canvas.drawText(No_26.getText().toString()+" V", 340, 600, paint);
        canvas.drawText(No_7.getText().toString()+" V", 140, 640, paint); canvas.drawText(No_27.getText().toString()+" V", 340, 640, paint);
        canvas.drawText(No_8.getText().toString()+" V", 140, 680, paint); canvas.drawText(No_28.getText().toString()+" V", 340, 680, paint);
        canvas.drawText(No_9.getText().toString()+" V", 140, 720, paint); canvas.drawText(No_29.getText().toString()+" V", 340, 720, paint);
        canvas.drawText(No_10.getText().toString()+" V", 140, 760, paint); canvas.drawText(No_30.getText().toString()+" V", 340, 760, paint);
        canvas.drawText(No_11.getText().toString()+" V", 140, 800, paint); canvas.drawText(No_31.getText().toString()+" V", 340, 800, paint);
        canvas.drawText(No_12.getText().toString()+" V", 140, 840, paint); canvas.drawText(No_32.getText().toString()+" V", 340, 840, paint);
        canvas.drawText(No_13.getText().toString()+" V", 140, 880, paint); canvas.drawText(No_33.getText().toString()+" V", 340, 880, paint);
        canvas.drawText(No_14.getText().toString()+" V", 140, 920, paint); canvas.drawText(No_34.getText().toString()+" V", 340, 920, paint);
        canvas.drawText(No_15.getText().toString()+" V", 140, 960, paint); canvas.drawText(No_35.getText().toString()+" V", 340, 960, paint);
        canvas.drawText(No_16.getText().toString()+" V", 140, 1000, paint); canvas.drawText(No_36.getText().toString()+" V", 340, 1000, paint);
        canvas.drawText(No_17.getText().toString()+" V", 140, 1040, paint);
        canvas.drawText(No_18.getText().toString()+" V", 140, 1080, paint);
        canvas.drawText(No_19.getText().toString()+" V", 140, 1120, paint);
        canvas.drawText(No_20.getText().toString()+" V", 140, 1160, paint);

        //Total Voltage
        paint.setColor(Color.RED);
        paint.setTextSize(40f);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText(Voltage.getText().toString()+" VDC (Voltmeter)", 720, 1350, paint);
        canvas.drawText(Hasil.getText().toString()+" VDC (Calc)", 720, 1400, paint);

        dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        pdfDocument.finishPage(page);
        File file = new File(Environment.getExternalStorageDirectory(), "/"+JudulPmBatt.getText().toString()+" "+Nama_Unit.getText().toString()+" "+dateFormat.format(dateTime)+".pdf");
        try {
            pdfDocument.writeTo(new FileOutputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }

        pdfDocument.close();
        Toast.makeText(PmBattery12V.this, "PDF has Created", Toast.LENGTH_LONG).show();


        dateTime = new Date();
    }

    public void signatureDigitalOPS()
    {
        dialogCustomTTD = new AlertDialog.Builder(PmBattery12V.this);
        inflaterTTD = getLayoutInflater();
        dialogViewTTD = inflaterTTD.inflate(R.layout.signature_activity, null);
        dialogCustomTTD.setView(dialogViewTTD);
        dialogCustomTTD.setCancelable(true);

        mClear = dialogViewTTD.findViewById(R.id.clear);
        mGetSign = dialogViewTTD.findViewById(R.id.getsign);
        //mGetSign.setEnabled(false);
        mCancel = dialogViewTTD.findViewById(R.id.cancel);
        signatureView = dialogViewTTD.findViewById(R.id.panelSignature);

        mClear.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.v("log_tag", "Panel Cleared");
                signatureView.clearCanvas();
            }
        });

        mGetSign.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.v("log_tag", "Panel Saved");
                bitmapTTD = signatureView.getSignatureBitmap();
                path = saveImageOPS(bitmapTTD);
                //Toast.makeText(PmGenerator.this, "Test", Toast.LENGTH_SHORT).show();
            }
        });
        mCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.v("log_tag", "Panel Canceled");

            }
        });
        dialogCustomTTD.show();
    }

    public String saveImageOPS(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY /*iDyme folder*/);
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
            Log.d("hhhhh",wallpaperDirectory.toString());
        }

        try {
            File f = new File(wallpaperDirectory,"ttdOperatorPMbatt.jpg"); //Calendar.getInstance().getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(PmBattery12V.this,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());
            Toast.makeText(this, "Saved Berhasil", Toast.LENGTH_SHORT).show();

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";

    }

    public void signatureDigitalSPV()
    {
        dialogCustomTTD = new AlertDialog.Builder(PmBattery12V.this);
        inflaterTTD = getLayoutInflater();
        dialogViewTTD = inflaterTTD.inflate(R.layout.signature_activity, null);
        dialogCustomTTD.setView(dialogViewTTD);
        dialogCustomTTD.setCancelable(true);

        mClear = dialogViewTTD.findViewById(R.id.clear);
        mGetSign = dialogViewTTD.findViewById(R.id.getsign);
        //mGetSign.setEnabled(false);
        mCancel = dialogViewTTD.findViewById(R.id.cancel);
        signatureView = dialogViewTTD.findViewById(R.id.panelSignature);

        mClear.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.v("log_tag", "Panel Cleared");
                signatureView.clearCanvas();
            }
        });

        mGetSign.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.v("log_tag", "Panel Saved");
                bitmapTTD = signatureView.getSignatureBitmap();
                path = saveImageSPV(bitmapTTD);
                //Toast.makeText(PmGenerator.this, "Test", Toast.LENGTH_SHORT).show();
            }
        });
        mCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.v("log_tag", "Panel Canceled");

            }
        });
        dialogCustomTTD.show();
    }

    public String saveImageSPV(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY /*iDyme folder*/);
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
            Log.d("hhhhh", wallpaperDirectory.toString());
        }

        try {
            File f = new File(wallpaperDirectory, "ttdSPVPMgen.jpg"); //Calendar.getInstance().getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(PmBattery12V.this,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());
            Toast.makeText(this, "Saved Berhasil", Toast.LENGTH_SHORT).show();

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }

    private void transparentStatusAndNavigation() {
        //make full transparent statusBar
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            );
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            getWindow().setNavigationBarColor(Color.TRANSPARENT);
        }
    }

    private void setWindowFlag(final int bits, boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
}
