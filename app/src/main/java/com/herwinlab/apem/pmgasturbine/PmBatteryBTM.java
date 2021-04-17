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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.getbase.floatingactionbutton.FloatingActionButton;
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
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class PmBatteryBTM extends AppCompatActivity {

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
    public TextInputEditText No_31, No_32, No_33, No_34, No_35, No_36, No_37, No_38, No_39, No_40;
    public TextInputEditText No_41, No_42, No_43, No_44, No_45, No_46, No_47, No_48, No_49, No_50;
    public TextInputEditText No_51, No_52, No_53, No_54, No_55, No_56, No_57, No_58, No_59, No_60;
    public TextInputEditText No_61, No_62, No_63, No_64, No_65, No_66, No_67, No_68, No_69, No_70;
    public TextInputEditText No_71, No_72, No_73, No_74, No_75, No_76, No_77, No_78, No_79, No_80;
    public TextInputEditText No_81, No_82, No_83, No_84, No_85, No_86, No_87, No_88, No_89, No_90;
    public TextInputEditText No_91, No_92, No_93, No_94, No_95, No_96, No_97, No_98, No_99, No_100;
    public TextInputEditText No_101, No_102, No_103, No_104, No_105, No_106, No_107, No_108, Voltage;
    public TextInputEditText Nama_Unit;

    //Edit Text For Noted, Aux, Information
    public TextView JudulPmBatt, Hasil;
    public EditText Catatan1, Catatan2;
    public EditText orangPm1, orangPm2, orangPm3, Operator, NoWO, namaGT, namaPDF;

    private SharedPreferences prefs;
    private final String No_1KEY = "1", No_2KEY = "2", No_3KEY = "3", No_4KEY = "4", No_5KEY = "5", No_6KEY = "6", No_7KEY = "7", No_8KEY = "8", No_9KEY = "9", No_10KEY = "10";
    private final String No_11KEY = "11", No_12KEY = "12", No_13KEY = "13", No_14KEY = "14", No_15KEY = "15", No_16KEY = "16", No_17KEY = "17", No_18KEY = "18", No_19KEY = "19", No_20KEY = "20";
    private final String No_21KEY = "21", No_22KEY = "22", No_23KEY = "23", No_24KEY = "24", No_25KEY = "25", No_26KEY = "26", No_27KEY = "27", No_28KEY = "28", No_29KEY = "29", No_30KEY = "30";
    private final String No_31KEY = "31", No_32KEY = "32", No_33KEY = "33", No_34KEY = "34", No_35KEY = "35", No_36KEY = "36", No_37KEY = "37", No_38KEY = "38", No_39KEY = "39", No_40KEY = "40";
    private final String No_41KEY = "41", No_42KEY = "42", No_43KEY = "43", No_44KEY = "44", No_45KEY = "45", No_46KEY = "46", No_47KEY = "47", No_48KEY = "48", No_49KEY = "49", No_50KEY = "50";
    private final String No_51KEY = "51", No_52KEY = "52", No_53KEY = "53", No_54KEY = "54", No_55KEY = "55", No_56KEY = "56", No_57KEY = "57", No_58KEY = "58", No_59KEY = "59", No_60KEY = "60";
    private final String No_61KEY = "61", No_62KEY = "62", No_63KEY = "63", No_64KEY = "64", No_65KEY = "65", No_66KEY = "66", No_67KEY = "67", No_68KEY = "68", No_69KEY = "69", No_70KEY = "70";
    private final String No_71KEY = "71", No_72KEY = "72", No_73KEY = "73", No_74KEY = "74", No_75KEY = "75", No_76KEY = "76", No_77KEY = "77", No_78KEY = "78", No_79KEY = "79", No_80KEY = "80";
    private final String No_81KEY = "81", No_82KEY = "82", No_83KEY = "83", No_84KEY = "84", No_85KEY = "85", No_86KEY = "86", No_87KEY = "87", No_88KEY = "88", No_89KEY = "89", No_90KEY = "90";
    private final String No_91KEY = "91", No_92KEY = "92", No_93KEY = "93", No_94KEY = "94", No_95KEY = "95", No_96KEY = "96", No_97KEY = "97", No_98KEY = "98", No_99KEY = "99", No_100KEY = "100";
    private final String No_101KEY = "101", No_102KEY = "102", No_103KEY = "103", No_104KEY = "104", No_105KEY = "105", No_106KEY = "106", No_107KEY = "107", No_108KEY = "108";
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
        setContentView(R.layout.batterybtm_activity);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO); // Disable Dark Mode

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
        No_36 = findViewById(R.id.no_36); No_37 = findViewById(R.id.no_37); No_38 = findViewById(R.id.no_38); No_39 = findViewById(R.id.no_39); No_40 = findViewById(R.id.no_40);
        No_41 = findViewById(R.id.no_41); No_42 = findViewById(R.id.no_42); No_43 = findViewById(R.id.no_43); No_44 = findViewById(R.id.no_44); No_45 = findViewById(R.id.no_45);
        No_46 = findViewById(R.id.no_46); No_47 = findViewById(R.id.no_47); No_48 = findViewById(R.id.no_48); No_49 = findViewById(R.id.no_49); No_50 = findViewById(R.id.no_50);
        No_51 = findViewById(R.id.no_51); No_52 = findViewById(R.id.no_52); No_53 = findViewById(R.id.no_53); No_54 = findViewById(R.id.no_54); No_55 = findViewById(R.id.no_55);
        No_56 = findViewById(R.id.no_56); No_57 = findViewById(R.id.no_57); No_58 = findViewById(R.id.no_58); No_59 = findViewById(R.id.no_59); No_60 = findViewById(R.id.no_60);
        No_61 = findViewById(R.id.no_61); No_62 = findViewById(R.id.no_62); No_63 = findViewById(R.id.no_63); No_64 = findViewById(R.id.no_64); No_65 = findViewById(R.id.no_65);
        No_66 = findViewById(R.id.no_66); No_67 = findViewById(R.id.no_67); No_68 = findViewById(R.id.no_68); No_69 = findViewById(R.id.no_69); No_70 = findViewById(R.id.no_70);
        No_71 = findViewById(R.id.no_71); No_72 = findViewById(R.id.no_72); No_73 = findViewById(R.id.no_73); No_74 = findViewById(R.id.no_74); No_75 = findViewById(R.id.no_75);
        No_76 = findViewById(R.id.no_76); No_77 = findViewById(R.id.no_77); No_78 = findViewById(R.id.no_78); No_79 = findViewById(R.id.no_79); No_80 = findViewById(R.id.no_80);
        No_81 = findViewById(R.id.no_81); No_82 = findViewById(R.id.no_82); No_83 = findViewById(R.id.no_83); No_84 = findViewById(R.id.no_84); No_85 = findViewById(R.id.no_85);
        No_86 = findViewById(R.id.no_86); No_87 = findViewById(R.id.no_87); No_88 = findViewById(R.id.no_88); No_89 = findViewById(R.id.no_89); No_90 = findViewById(R.id.no_90);
        No_91 = findViewById(R.id.no_91); No_92 = findViewById(R.id.no_92); No_93 = findViewById(R.id.no_93); No_94 = findViewById(R.id.no_94); No_95 = findViewById(R.id.no_95);
        No_96 = findViewById(R.id.no_96); No_97 = findViewById(R.id.no_97); No_98 = findViewById(R.id.no_98); No_99 = findViewById(R.id.no_99); No_100 = findViewById(R.id.no_100);
        No_101 = findViewById(R.id.no_101); No_102 = findViewById(R.id.no_102); No_103 = findViewById(R.id.no_103); No_104 = findViewById(R.id.no_104); No_105 = findViewById(R.id.no_105);
        No_106 = findViewById(R.id.no_106); No_107 = findViewById(R.id.no_107); No_108 = findViewById(R.id.no_108);
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
        No_36.setText(prefs.getString(No_36KEY,"")); No_37.setText(prefs.getString(No_37KEY,"")); No_38.setText(prefs.getString(No_38KEY,"")); No_39.setText(prefs.getString(No_39KEY,"")); No_40.setText(prefs.getString(No_40KEY,""));
        No_41.setText(prefs.getString(No_41KEY,"")); No_42.setText(prefs.getString(No_42KEY,"")); No_43.setText(prefs.getString(No_43KEY,"")); No_44.setText(prefs.getString(No_44KEY,"")); No_45.setText(prefs.getString(No_45KEY,""));
        No_46.setText(prefs.getString(No_46KEY,"")); No_47.setText(prefs.getString(No_47KEY,"")); No_48.setText(prefs.getString(No_48KEY,"")); No_49.setText(prefs.getString(No_49KEY,"")); No_50.setText(prefs.getString(No_50KEY,""));
        No_51.setText(prefs.getString(No_51KEY,"")); No_52.setText(prefs.getString(No_52KEY,"")); No_53.setText(prefs.getString(No_53KEY,"")); No_54.setText(prefs.getString(No_54KEY,"")); No_55.setText(prefs.getString(No_55KEY,""));
        No_56.setText(prefs.getString(No_56KEY,"")); No_57.setText(prefs.getString(No_57KEY,"")); No_58.setText(prefs.getString(No_58KEY,"")); No_59.setText(prefs.getString(No_59KEY,"")); No_60.setText(prefs.getString(No_60KEY,""));
        No_61.setText(prefs.getString(No_61KEY,"")); No_62.setText(prefs.getString(No_62KEY,"")); No_63.setText(prefs.getString(No_63KEY,"")); No_64.setText(prefs.getString(No_64KEY,"")); No_65.setText(prefs.getString(No_65KEY,""));
        No_66.setText(prefs.getString(No_66KEY,"")); No_67.setText(prefs.getString(No_67KEY,"")); No_68.setText(prefs.getString(No_68KEY,"")); No_69.setText(prefs.getString(No_69KEY,"")); No_70.setText(prefs.getString(No_70KEY,""));
        No_71.setText(prefs.getString(No_71KEY,"")); No_72.setText(prefs.getString(No_72KEY,"")); No_73.setText(prefs.getString(No_73KEY,"")); No_74.setText(prefs.getString(No_74KEY,"")); No_75.setText(prefs.getString(No_75KEY,""));
        No_76.setText(prefs.getString(No_76KEY,"")); No_77.setText(prefs.getString(No_77KEY,"")); No_78.setText(prefs.getString(No_78KEY,"")); No_79.setText(prefs.getString(No_79KEY,"")); No_80.setText(prefs.getString(No_80KEY,""));
        No_81.setText(prefs.getString(No_81KEY,"")); No_82.setText(prefs.getString(No_82KEY,"")); No_83.setText(prefs.getString(No_83KEY,"")); No_84.setText(prefs.getString(No_84KEY,"")); No_85.setText(prefs.getString(No_85KEY,""));
        No_86.setText(prefs.getString(No_86KEY,"")); No_87.setText(prefs.getString(No_87KEY,"")); No_88.setText(prefs.getString(No_88KEY,"")); No_89.setText(prefs.getString(No_89KEY,"")); No_90.setText(prefs.getString(No_90KEY,""));
        No_91.setText(prefs.getString(No_91KEY,"")); No_92.setText(prefs.getString(No_92KEY,"")); No_93.setText(prefs.getString(No_93KEY,"")); No_94.setText(prefs.getString(No_94KEY,"")); No_95.setText(prefs.getString(No_95KEY,""));
        No_96.setText(prefs.getString(No_96KEY,"")); No_97.setText(prefs.getString(No_97KEY,"")); No_98.setText(prefs.getString(No_98KEY,"")); No_99.setText(prefs.getString(No_99KEY,"")); No_100.setText(prefs.getString(No_100KEY,""));
        No_101.setText(prefs.getString(No_101KEY,"")); No_102.setText(prefs.getString(No_102KEY,"")); No_103.setText(prefs.getString(No_103KEY,"")); No_104.setText(prefs.getString(No_104KEY,"")); No_105.setText(prefs.getString(No_105KEY,""));
        No_106.setText(prefs.getString(No_106KEY,"")); No_107.setText(prefs.getString(No_107KEY,"")); No_108.setText(prefs.getString(No_108KEY,""));
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

                String a1 = No_1.getText().toString(); String a11 = No_11.getText().toString(); String a21 = No_21.getText().toString(); String a31 = No_31.getText().toString(); String a41 = No_41.getText().toString();
                String a2 = No_2.getText().toString(); String a12 = No_12.getText().toString(); String a22 = No_22.getText().toString(); String a32 = No_32.getText().toString(); String a42 = No_42.getText().toString();
                String a3 = No_3.getText().toString(); String a13 = No_13.getText().toString(); String a23 = No_23.getText().toString(); String a33 = No_33.getText().toString(); String a43 = No_43.getText().toString();
                String a4 = No_4.getText().toString(); String a14 = No_14.getText().toString(); String a24 = No_24.getText().toString(); String a34 = No_34.getText().toString(); String a44 = No_44.getText().toString();
                String a5 = No_5.getText().toString(); String a15 = No_15.getText().toString(); String a25 = No_25.getText().toString(); String a35 = No_35.getText().toString(); String a45 = No_45.getText().toString();
                String a6 = No_6.getText().toString(); String a16 = No_16.getText().toString(); String a26 = No_26.getText().toString(); String a36 = No_36.getText().toString(); String a46 = No_46.getText().toString();
                String a7 = No_7.getText().toString(); String a17 = No_17.getText().toString(); String a27 = No_27.getText().toString(); String a37 = No_37.getText().toString(); String a47 = No_47.getText().toString();
                String a8 = No_8.getText().toString(); String a18 = No_18.getText().toString(); String a28 = No_28.getText().toString(); String a38 = No_38.getText().toString(); String a48 = No_48.getText().toString();
                String a9 = No_9.getText().toString(); String a19 = No_19.getText().toString(); String a29 = No_29.getText().toString(); String a39 = No_39.getText().toString(); String a49 = No_49.getText().toString();
                String a10 = No_10.getText().toString(); String a20 = No_20.getText().toString(); String a30 = No_30.getText().toString(); String a40 = No_40.getText().toString(); String a50 = No_50.getText().toString();

                String a51 = No_51.getText().toString(); String a61 = No_61.getText().toString(); String a71 = No_71.getText().toString(); String a81 = No_81.getText().toString(); String a91 = No_91.getText().toString();
                String a52 = No_52.getText().toString(); String a62 = No_62.getText().toString(); String a72 = No_72.getText().toString(); String a82 = No_82.getText().toString(); String a92 = No_92.getText().toString();
                String a53 = No_53.getText().toString(); String a63 = No_63.getText().toString(); String a73 = No_73.getText().toString(); String a83 = No_83.getText().toString(); String a93 = No_93.getText().toString();
                String a54 = No_54.getText().toString(); String a64 = No_64.getText().toString(); String a74 = No_74.getText().toString(); String a84 = No_84.getText().toString(); String a94 = No_94.getText().toString();
                String a55 = No_55.getText().toString(); String a65 = No_65.getText().toString(); String a75 = No_75.getText().toString(); String a85 = No_85.getText().toString(); String a95 = No_95.getText().toString();
                String a56 = No_56.getText().toString(); String a66 = No_66.getText().toString(); String a76 = No_76.getText().toString(); String a86 = No_86.getText().toString(); String a96 = No_96.getText().toString();
                String a57 = No_57.getText().toString(); String a67 = No_67.getText().toString(); String a77 = No_77.getText().toString(); String a87 = No_87.getText().toString(); String a97 = No_97.getText().toString();
                String a58 = No_58.getText().toString(); String a68 = No_68.getText().toString(); String a78 = No_78.getText().toString(); String a88 = No_88.getText().toString(); String a98 = No_98.getText().toString();
                String a59 = No_59.getText().toString(); String a69 = No_69.getText().toString(); String a79 = No_79.getText().toString(); String a89 = No_89.getText().toString(); String a99 = No_99.getText().toString();
                String a60 = No_60.getText().toString(); String a70 = No_70.getText().toString(); String a80 = No_80.getText().toString(); String a90 = No_90.getText().toString(); String a100= No_100.getText().toString();

                String a101 = No_101.getText().toString(); String a102 = No_102.getText().toString(); String a103 = No_103.getText().toString(); String a104 = No_104.getText().toString(); String a105 = No_105.getText().toString();
                String a106 = No_106.getText().toString(); String a107 = No_107.getText().toString(); String a108 = No_108.getText().toString();

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
                else if (a37.isEmpty()){No_37.setError("Field Empty!");No_37.requestFocus();}
                else if (a38.isEmpty()){No_38.setError("Field Empty!");No_38.requestFocus();}
                else if (a39.isEmpty()){No_39.setError("Field Empty!");No_39.requestFocus();}
                else if (a40.isEmpty()){No_40.setError("Field Empty!");No_40.requestFocus();}
                else if (a41.isEmpty()){No_41.setError("Field Empty!");No_41.requestFocus();}
                else if (a42.isEmpty()){No_42.setError("Field Empty!");No_42.requestFocus();}
                else if (a43.isEmpty()){No_43.setError("Field Empty!");No_43.requestFocus();}
                else if (a44.isEmpty()){No_44.setError("Field Empty!");No_44.requestFocus();}
                else if (a45.isEmpty()){No_45.setError("Field Empty!");No_45.requestFocus();}
                else if (a46.isEmpty()){No_46.setError("Field Empty!");No_46.requestFocus();}
                else if (a47.isEmpty()){No_47.setError("Field Empty!");No_47.requestFocus();}
                else if (a48.isEmpty()){No_48.setError("Field Empty!");No_48.requestFocus();}
                else if (a49.isEmpty()){No_49.setError("Field Empty!");No_49.requestFocus();}
                else if (a50.isEmpty()){No_50.setError("Field Empty!");No_50.requestFocus();}
                else if (a51.isEmpty()){No_51.setError("Field Empty!");No_51.requestFocus();}
                else if (a52.isEmpty()){No_52.setError("Field Empty!");No_52.requestFocus();}
                else if (a53.isEmpty()){No_53.setError("Field Empty!");No_53.requestFocus();}
                else if (a54.isEmpty()){No_54.setError("Field Empty!");No_54.requestFocus();}
                else if (a55.isEmpty()){No_55.setError("Field Empty!");No_55.requestFocus();}
                else if (a56.isEmpty()){No_56.setError("Field Empty!");No_56.requestFocus();}
                else if (a57.isEmpty()){No_57.setError("Field Empty!");No_57.requestFocus();}
                else if (a58.isEmpty()){No_58.setError("Field Empty!");No_58.requestFocus();}
                else if (a59.isEmpty()){No_59.setError("Field Empty!");No_59.requestFocus();}
                else if (a60.isEmpty()){No_60.setError("Field Empty!");No_60.requestFocus();}
                else if (a61.isEmpty()){No_61.setError("Field Empty!");No_61.requestFocus();}
                else if (a62.isEmpty()){No_62.setError("Field Empty!");No_62.requestFocus();}
                else if (a63.isEmpty()){No_63.setError("Field Empty!");No_63.requestFocus();}
                else if (a64.isEmpty()){No_64.setError("Field Empty!");No_64.requestFocus();}
                else if (a65.isEmpty()){No_65.setError("Field Empty!");No_65.requestFocus();}
                else if (a66.isEmpty()){No_66.setError("Field Empty!");No_66.requestFocus();}
                else if (a67.isEmpty()){No_67.setError("Field Empty!");No_67.requestFocus();}
                else if (a68.isEmpty()){No_68.setError("Field Empty!");No_68.requestFocus();}
                else if (a69.isEmpty()){No_69.setError("Field Empty!");No_69.requestFocus();}
                else if (a70.isEmpty()){No_70.setError("Field Empty!");No_70.requestFocus();}
                else if (a71.isEmpty()){No_71.setError("Field Empty!");No_71.requestFocus();}
                else if (a72.isEmpty()){No_72.setError("Field Empty!");No_72.requestFocus();}
                else if (a73.isEmpty()){No_73.setError("Field Empty!");No_73.requestFocus();}
                else if (a74.isEmpty()){No_74.setError("Field Empty!");No_74.requestFocus();}
                else if (a75.isEmpty()){No_75.setError("Field Empty!");No_75.requestFocus();}
                else if (a76.isEmpty()){No_76.setError("Field Empty!");No_76.requestFocus();}
                else if (a77.isEmpty()){No_77.setError("Field Empty!");No_77.requestFocus();}
                else if (a78.isEmpty()){No_78.setError("Field Empty!");No_78.requestFocus();}
                else if (a79.isEmpty()){No_79.setError("Field Empty!");No_79.requestFocus();}
                else if (a80.isEmpty()){No_80.setError("Field Empty!");No_80.requestFocus();}
                else if (a81.isEmpty()){No_81.setError("Field Empty!");No_81.requestFocus();}
                else if (a82.isEmpty()){No_82.setError("Field Empty!");No_82.requestFocus();}
                else if (a83.isEmpty()){No_83.setError("Field Empty!");No_83.requestFocus();}
                else if (a84.isEmpty()){No_84.setError("Field Empty!");No_84.requestFocus();}
                else if (a85.isEmpty()){No_85.setError("Field Empty!");No_85.requestFocus();}
                else if (a86.isEmpty()){No_86.setError("Field Empty!");No_86.requestFocus();}
                else if (a87.isEmpty()){No_87.setError("Field Empty!");No_87.requestFocus();}
                else if (a88.isEmpty()){No_88.setError("Field Empty!");No_88.requestFocus();}
                else if (a89.isEmpty()){No_89.setError("Field Empty!");No_89.requestFocus();}
                else if (a90.isEmpty()){No_90.setError("Field Empty!");No_90.requestFocus();}
                else if (a91.isEmpty()){No_91.setError("Field Empty!");No_91.requestFocus();}
                else if (a92.isEmpty()){No_92.setError("Field Empty!");No_92.requestFocus();}
                else if (a93.isEmpty()){No_93.setError("Field Empty!");No_93.requestFocus();}
                else if (a94.isEmpty()){No_94.setError("Field Empty!");No_94.requestFocus();}
                else if (a95.isEmpty()){No_95.setError("Field Empty!");No_95.requestFocus();}
                else if (a96.isEmpty()){No_96.setError("Field Empty!");No_96.requestFocus();}
                else if (a97.isEmpty()){No_97.setError("Field Empty!");No_97.requestFocus();}
                else if (a98.isEmpty()){No_98.setError("Field Empty!");No_98.requestFocus();}
                else if (a99.isEmpty()){No_99.setError("Field Empty!");No_99.requestFocus();}
                else if (a100.isEmpty()){No_100.setError("Field Empty!");No_100.requestFocus();}
                else if (a101.isEmpty()){No_101.setError("Field Empty!");No_101.requestFocus();}
                else if (a102.isEmpty()){No_102.setError("Field Empty!");No_102.requestFocus();}
                else if (a103.isEmpty()){No_103.setError("Field Empty!");No_103.requestFocus();}
                else if (a104.isEmpty()){No_104.setError("Field Empty!");No_104.requestFocus();}
                else if (a105.isEmpty()){No_105.setError("Field Empty!");No_105.requestFocus();}
                else if (a106.isEmpty()){No_106.setError("Field Empty!");No_106.requestFocus();}
                else if (a107.isEmpty()){No_107.setError("Field Empty!");No_107.requestFocus();}
                else if (a108.isEmpty()){No_108.setError("Field Empty!");No_108.requestFocus();}
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
                final AlertDialog.Builder builder = new AlertDialog.Builder(PmBatteryBTM.this, R.style.CustomAlertDialog);
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
                Intent intent = new Intent(PmBatteryBTM.this, CameraActivity.class);
                startActivity(intent);
            }
        });

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
        String a1 = No_1.getText().toString(); String a11 = No_11.getText().toString(); String a21 = No_21.getText().toString(); String a31 = No_31.getText().toString(); String a41 = No_41.getText().toString();
        String a2 = No_2.getText().toString(); String a12 = No_12.getText().toString(); String a22 = No_22.getText().toString(); String a32 = No_32.getText().toString(); String a42 = No_42.getText().toString();
        String a3 = No_3.getText().toString(); String a13 = No_13.getText().toString(); String a23 = No_23.getText().toString(); String a33 = No_33.getText().toString(); String a43 = No_43.getText().toString();
        String a4 = No_4.getText().toString(); String a14 = No_14.getText().toString(); String a24 = No_24.getText().toString(); String a34 = No_34.getText().toString(); String a44 = No_44.getText().toString();
        String a5 = No_5.getText().toString(); String a15 = No_15.getText().toString(); String a25 = No_25.getText().toString(); String a35 = No_35.getText().toString(); String a45 = No_45.getText().toString();
        String a6 = No_6.getText().toString(); String a16 = No_16.getText().toString(); String a26 = No_26.getText().toString(); String a36 = No_36.getText().toString(); String a46 = No_46.getText().toString();
        String a7 = No_7.getText().toString(); String a17 = No_17.getText().toString(); String a27 = No_27.getText().toString(); String a37 = No_37.getText().toString(); String a47 = No_47.getText().toString();
        String a8 = No_8.getText().toString(); String a18 = No_18.getText().toString(); String a28 = No_28.getText().toString(); String a38 = No_38.getText().toString(); String a48 = No_48.getText().toString();
        String a9 = No_9.getText().toString(); String a19 = No_19.getText().toString(); String a29 = No_29.getText().toString(); String a39 = No_39.getText().toString(); String a49 = No_49.getText().toString();
        String a10 = No_10.getText().toString(); String a20 = No_20.getText().toString(); String a30 = No_30.getText().toString(); String a40 = No_40.getText().toString(); String a50 = No_50.getText().toString();

        String a51 = No_51.getText().toString(); String a61 = No_61.getText().toString(); String a71 = No_71.getText().toString(); String a81 = No_81.getText().toString(); String a91 = No_91.getText().toString();
        String a52 = No_52.getText().toString(); String a62 = No_62.getText().toString(); String a72 = No_72.getText().toString(); String a82 = No_82.getText().toString(); String a92 = No_92.getText().toString();
        String a53 = No_53.getText().toString(); String a63 = No_63.getText().toString(); String a73 = No_73.getText().toString(); String a83 = No_83.getText().toString(); String a93 = No_93.getText().toString();
        String a54 = No_54.getText().toString(); String a64 = No_64.getText().toString(); String a74 = No_74.getText().toString(); String a84 = No_84.getText().toString(); String a94 = No_94.getText().toString();
        String a55 = No_55.getText().toString(); String a65 = No_65.getText().toString(); String a75 = No_75.getText().toString(); String a85 = No_85.getText().toString(); String a95 = No_95.getText().toString();
        String a56 = No_56.getText().toString(); String a66 = No_66.getText().toString(); String a76 = No_76.getText().toString(); String a86 = No_86.getText().toString(); String a96 = No_96.getText().toString();
        String a57 = No_57.getText().toString(); String a67 = No_67.getText().toString(); String a77 = No_77.getText().toString(); String a87 = No_87.getText().toString(); String a97 = No_97.getText().toString();
        String a58 = No_58.getText().toString(); String a68 = No_68.getText().toString(); String a78 = No_78.getText().toString(); String a88 = No_88.getText().toString(); String a98 = No_98.getText().toString();
        String a59 = No_59.getText().toString(); String a69 = No_69.getText().toString(); String a79 = No_79.getText().toString(); String a89 = No_89.getText().toString(); String a99 = No_99.getText().toString();
        String a60 = No_60.getText().toString(); String a70 = No_70.getText().toString(); String a80 = No_80.getText().toString(); String a90 = No_90.getText().toString(); String a100= No_100.getText().toString();

        String a101 = No_101.getText().toString(); String a102 = No_102.getText().toString(); String a103 = No_103.getText().toString(); String a104 = No_104.getText().toString(); String a105 = No_105.getText().toString();
        String a106 = No_106.getText().toString(); String a107 = No_107.getText().toString(); String a108 = No_108.getText().toString();

        float b1 = Float.parseFloat(a1); float b11 = Float.parseFloat(a11); float b21 = Float.parseFloat(a21); float b31 = Float.parseFloat(a31); float b41 = Float.parseFloat(a41);
        float b2 = Float.parseFloat(a2); float b12 = Float.parseFloat(a12); float b22 = Float.parseFloat(a22); float b32 = Float.parseFloat(a32); float b42 = Float.parseFloat(a42);
        float b3 = Float.parseFloat(a3); float b13 = Float.parseFloat(a13); float b23 = Float.parseFloat(a23); float b33 = Float.parseFloat(a33); float b43 = Float.parseFloat(a43);
        float b4 = Float.parseFloat(a4); float b14 = Float.parseFloat(a14); float b24 = Float.parseFloat(a24); float b34 = Float.parseFloat(a34); float b44 = Float.parseFloat(a44);
        float b5 = Float.parseFloat(a5); float b15 = Float.parseFloat(a15); float b25 = Float.parseFloat(a25); float b35 = Float.parseFloat(a35); float b45 = Float.parseFloat(a45);
        float b6 = Float.parseFloat(a6); float b16 = Float.parseFloat(a16); float b26 = Float.parseFloat(a26); float b36 = Float.parseFloat(a36); float b46 = Float.parseFloat(a46);
        float b7 = Float.parseFloat(a7); float b17 = Float.parseFloat(a17); float b27 = Float.parseFloat(a27); float b37 = Float.parseFloat(a37); float b47 = Float.parseFloat(a47);
        float b8 = Float.parseFloat(a8); float b18 = Float.parseFloat(a18); float b28 = Float.parseFloat(a28); float b38 = Float.parseFloat(a38); float b48 = Float.parseFloat(a48);
        float b9 = Float.parseFloat(a9); float b19 = Float.parseFloat(a19); float b29 = Float.parseFloat(a29); float b39 = Float.parseFloat(a39); float b49 = Float.parseFloat(a49);
        float b10 = Float.parseFloat(a10); float b20 = Float.parseFloat(a20); float b30 = Float.parseFloat(a30); float b40 = Float.parseFloat(a40); float b50 = Float.parseFloat(a50);

        float b51 = Float.parseFloat(a51); float b61 = Float.parseFloat(a61); float b71 = Float.parseFloat(a71); float b81 = Float.parseFloat(a81); float b91 = Float.parseFloat(a91);
        float b52 = Float.parseFloat(a52); float b62 = Float.parseFloat(a62); float b72 = Float.parseFloat(a72); float b82 = Float.parseFloat(a82); float b92 = Float.parseFloat(a92);
        float b53 = Float.parseFloat(a53); float b63 = Float.parseFloat(a63); float b73 = Float.parseFloat(a73); float b83 = Float.parseFloat(a83); float b93 = Float.parseFloat(a93);
        float b54 = Float.parseFloat(a54); float b64 = Float.parseFloat(a64); float b74 = Float.parseFloat(a74); float b84 = Float.parseFloat(a84); float b94 = Float.parseFloat(a94);
        float b55 = Float.parseFloat(a55); float b65 = Float.parseFloat(a65); float b75 = Float.parseFloat(a75); float b85 = Float.parseFloat(a85); float b95 = Float.parseFloat(a95);
        float b56 = Float.parseFloat(a56); float b66 = Float.parseFloat(a66); float b76 = Float.parseFloat(a76); float b86 = Float.parseFloat(a86); float b96 = Float.parseFloat(a96);
        float b57 = Float.parseFloat(a57); float b67 = Float.parseFloat(a67); float b77 = Float.parseFloat(a77); float b87 = Float.parseFloat(a87); float b97 = Float.parseFloat(a97);
        float b58 = Float.parseFloat(a58); float b68 = Float.parseFloat(a68); float b78 = Float.parseFloat(a78); float b88 = Float.parseFloat(a88); float b98 = Float.parseFloat(a98);
        float b59 = Float.parseFloat(a59); float b69 = Float.parseFloat(a69); float b79 = Float.parseFloat(a79); float b89 = Float.parseFloat(a89); float b99 = Float.parseFloat(a99);
        float b60 = Float.parseFloat(a60); float b70 = Float.parseFloat(a70); float b80 = Float.parseFloat(a80); float b90 = Float.parseFloat(a90); float b100 = Float.parseFloat(a100);

        float b101 = Float.parseFloat(a101); float b102 = Float.parseFloat(a102); float b103 = Float.parseFloat(a103); float b104 = Float.parseFloat(a104); float b105 = Float.parseFloat(a105);
        float b106 = Float.parseFloat(a106); float b107 = Float.parseFloat(a107); float b108 = Float.parseFloat(a108);

            float teganganBattery = b1 + b2 + b3 + b4 + b5 + b6 + b7 + b8 + b9 + b10 + b11 + b12 + b13 + b14 + b15 + b16 + b17 + b18 + b19 + b20 +
                    b21 + b22 + b23 + b24 + b25 + b26 + b27 + b28 + b29 + b30 + b31 + b32 + b33 + b34 + b35 + b36 + b37 + b38 + b39 + b40 +
                    b41 + b42 + b43 + b44 + b45 + b46 + b47 + b48 + b49 + b50 + b51 + b52 + b53 + b54 + b55 + b56 + b57 + b58 + b59 + b60 +
                    b61 + b62 + b63 + b64 + b65 + b66 + b67 + b68 + b69 + b70 + b71 + b72 + b73 + b74 + b75 + b76 + b77 + b78 + b79 + b80 +
                    b81 + b82 + b83 + b84 + b85 + b86 + b87 + b88 + b89 + b90 + b91 + b92 + b93 + b94 + b95 + b96 + b97 + b98 + b99 + b100 +
                    b101 + b102 + b103 + b104 + b105 + b106 + b107 + b108;
            //Volt = Float.toString(teganganBattery);
            Hasil.setText(String.format("%.2f", teganganBattery));

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
        editor.putString(No_36KEY, No_36.getText().toString()); editor.putString(No_37KEY, No_37.getText().toString()); editor.putString(No_38KEY, No_38.getText().toString()); editor.putString(No_39KEY, No_39.getText().toString()); editor.putString(No_40KEY, No_40.getText().toString());
        editor.putString(No_41KEY, No_41.getText().toString()); editor.putString(No_42KEY, No_42.getText().toString()); editor.putString(No_43KEY, No_43.getText().toString()); editor.putString(No_44KEY, No_44.getText().toString()); editor.putString(No_45KEY, No_45.getText().toString());
        editor.putString(No_46KEY, No_46.getText().toString()); editor.putString(No_47KEY, No_47.getText().toString()); editor.putString(No_48KEY, No_48.getText().toString()); editor.putString(No_49KEY, No_49.getText().toString()); editor.putString(No_50KEY, No_50.getText().toString());

        editor.putString(No_51KEY, No_51.getText().toString()); editor.putString(No_52KEY, No_52.getText().toString()); editor.putString(No_53KEY, No_53.getText().toString()); editor.putString(No_54KEY, No_54.getText().toString()); editor.putString(No_55KEY, No_55.getText().toString());
        editor.putString(No_56KEY, No_56.getText().toString()); editor.putString(No_57KEY, No_57.getText().toString()); editor.putString(No_58KEY, No_58.getText().toString()); editor.putString(No_59KEY, No_59.getText().toString()); editor.putString(No_60KEY, No_60.getText().toString());
        editor.putString(No_61KEY, No_61.getText().toString()); editor.putString(No_62KEY, No_62.getText().toString()); editor.putString(No_63KEY, No_63.getText().toString()); editor.putString(No_64KEY, No_64.getText().toString()); editor.putString(No_65KEY, No_65.getText().toString());
        editor.putString(No_66KEY, No_66.getText().toString()); editor.putString(No_67KEY, No_67.getText().toString()); editor.putString(No_68KEY, No_68.getText().toString()); editor.putString(No_69KEY, No_69.getText().toString()); editor.putString(No_70KEY, No_70.getText().toString());
        editor.putString(No_71KEY, No_71.getText().toString()); editor.putString(No_72KEY, No_72.getText().toString()); editor.putString(No_73KEY, No_73.getText().toString()); editor.putString(No_74KEY, No_74.getText().toString()); editor.putString(No_75KEY, No_75.getText().toString());

        editor.putString(No_76KEY, No_76.getText().toString()); editor.putString(No_77KEY, No_77.getText().toString()); editor.putString(No_78KEY, No_78.getText().toString()); editor.putString(No_79KEY, No_79.getText().toString()); editor.putString(No_80KEY, No_80.getText().toString());
        editor.putString(No_81KEY, No_81.getText().toString()); editor.putString(No_82KEY, No_82.getText().toString()); editor.putString(No_83KEY, No_83.getText().toString()); editor.putString(No_84KEY, No_84.getText().toString()); editor.putString(No_85KEY, No_85.getText().toString());
        editor.putString(No_86KEY, No_86.getText().toString()); editor.putString(No_87KEY, No_87.getText().toString()); editor.putString(No_88KEY, No_88.getText().toString()); editor.putString(No_89KEY, No_89.getText().toString()); editor.putString(No_90KEY, No_90.getText().toString());
        editor.putString(No_91KEY, No_91.getText().toString()); editor.putString(No_92KEY, No_92.getText().toString()); editor.putString(No_93KEY, No_93.getText().toString()); editor.putString(No_94KEY, No_94.getText().toString()); editor.putString(No_95KEY, No_95.getText().toString());
        editor.putString(No_96KEY, No_96.getText().toString()); editor.putString(No_97KEY, No_97.getText().toString()); editor.putString(No_98KEY, No_98.getText().toString()); editor.putString(No_99KEY, No_99.getText().toString()); editor.putString(No_100KEY, No_100.getText().toString());

        editor.putString(No_101KEY, No_101.getText().toString()); editor.putString(No_102KEY, No_102.getText().toString()); editor.putString(No_103KEY, No_103.getText().toString()); editor.putString(No_104KEY, No_104.getText().toString()); editor.putString(No_105KEY, No_105.getText().toString());
        editor.putString(No_106KEY, No_106.getText().toString()); editor.putString(No_107KEY, No_107.getText().toString()); editor.putString(No_108KEY, No_108.getText().toString());
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
        canvas.drawBitmap(accuImg, 480, 1240, paint);

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
        canvas.drawText("BATTERY BANK 220VDC GT", 1170 / 2, 195, titlePaint);

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
        canvas.drawText("17. ", 80, 1040, paint); canvas.drawText("37. ", 280, 1040, paint);
        canvas.drawText("18. ", 80, 1080, paint); canvas.drawText("38. ", 280, 1080, paint);
        canvas.drawText("19. ", 80, 1120, paint); canvas.drawText("39. ", 280, 1120, paint);
        canvas.drawText("20. ", 80, 1160, paint); canvas.drawText("40. ", 280, 1160, paint);

        canvas.drawText("41. ", 480, 400, paint); canvas.drawText("61. ", 680, 400, paint);
        canvas.drawText("42. ", 480, 440, paint); canvas.drawText("62. ", 680, 440, paint);
        canvas.drawText("43. ", 480, 480, paint); canvas.drawText("63. ", 680, 480, paint);
        canvas.drawText("44. ", 480, 520, paint); canvas.drawText("64. ", 680, 520, paint);
        canvas.drawText("45. ", 480, 560, paint); canvas.drawText("65. ", 680, 560, paint);
        canvas.drawText("46. ", 480, 600, paint); canvas.drawText("66. ", 680, 600, paint);
        canvas.drawText("47. ", 480, 640, paint); canvas.drawText("67. ", 680, 640, paint);
        canvas.drawText("48. ", 480, 680, paint); canvas.drawText("68. ", 680, 680, paint);
        canvas.drawText("49. ", 480, 720, paint); canvas.drawText("69. ", 680, 720, paint);
        canvas.drawText("50. ", 480, 760, paint); canvas.drawText("70. ", 680, 760, paint);
        canvas.drawText("51. ", 480, 800, paint); canvas.drawText("71. ", 680, 800, paint);
        canvas.drawText("52. ", 480, 840, paint); canvas.drawText("72. ", 680, 840, paint);
        canvas.drawText("53. ", 480, 880, paint); canvas.drawText("73. ", 680, 880, paint);
        canvas.drawText("54. ", 480, 920, paint); canvas.drawText("74. ", 680, 920, paint);
        canvas.drawText("55. ", 480, 960, paint); canvas.drawText("75. ", 680, 960, paint);
        canvas.drawText("56. ", 480, 1000, paint); canvas.drawText("76. ", 680, 1000, paint);
        canvas.drawText("57. ", 480, 1040, paint); canvas.drawText("77. ", 680, 1040, paint);
        canvas.drawText("58. ", 480, 1080, paint); canvas.drawText("78. ", 680, 1080, paint);
        canvas.drawText("59. ", 480, 1120, paint); canvas.drawText("79. ", 680, 1120, paint);
        canvas.drawText("60. ", 480, 1160, paint); canvas.drawText("80. ", 680, 1160, paint);

        canvas.drawText("81. ", 880, 400, paint); canvas.drawText("101. ", 80, 1240, paint);
        canvas.drawText("82. ", 880, 440, paint); canvas.drawText("102. ", 80, 1280, paint);
        canvas.drawText("83. ", 880, 480, paint); canvas.drawText("103. ", 80, 1320, paint);
        canvas.drawText("84. ", 880, 520, paint); canvas.drawText("104. ", 80, 1360, paint);
        canvas.drawText("85. ", 880, 560, paint); canvas.drawText("105. ", 80, 1400, paint);
        canvas.drawText("86. ", 880, 600, paint); canvas.drawText("106. ", 80, 1440, paint);
        canvas.drawText("87. ", 880, 640, paint); canvas.drawText("107. ", 80, 1480, paint);
        canvas.drawText("88. ", 880, 680, paint); canvas.drawText("108. ", 80, 1520, paint);
        canvas.drawText("89. ", 880, 720, paint);
        canvas.drawText("90. ", 880, 760, paint);
        canvas.drawText("91. ", 880, 800, paint);
        canvas.drawText("92. ", 880, 840, paint);
        canvas.drawText("93. ", 880, 880, paint);
        canvas.drawText("94. ", 880, 920, paint);
        canvas.drawText("95. ", 880, 960, paint);
        canvas.drawText("96. ", 880, 1000, paint);
        canvas.drawText("97. ", 880, 1040, paint);
        canvas.drawText("98. ", 880, 1080, paint);
        canvas.drawText("99. ", 880, 1120, paint);
        canvas.drawText("100. ", 880, 1160, paint);

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
        canvas.drawText(No_17.getText().toString()+" V", 140, 1040, paint); canvas.drawText(No_37.getText().toString()+" V", 340, 1040, paint);
        canvas.drawText(No_18.getText().toString()+" V", 140, 1080, paint); canvas.drawText(No_38.getText().toString()+" V", 340, 1080, paint);
        canvas.drawText(No_19.getText().toString()+" V", 140, 1120, paint); canvas.drawText(No_39.getText().toString()+" V", 340, 1120, paint);
        canvas.drawText(No_20.getText().toString()+" V", 140, 1160, paint); canvas.drawText(No_40.getText().toString()+" V", 340, 1160, paint);

        canvas.drawText(No_41.getText().toString()+" V", 540, 400, paint); canvas.drawText(No_61.getText().toString()+" V", 740, 400, paint);
        canvas.drawText(No_42.getText().toString()+" V", 540, 440, paint); canvas.drawText(No_62.getText().toString()+" V", 740, 440, paint);
        canvas.drawText(No_43.getText().toString()+" V", 540, 480, paint); canvas.drawText(No_63.getText().toString()+" V", 740, 480, paint);
        canvas.drawText(No_44.getText().toString()+" V", 540, 520, paint); canvas.drawText(No_64.getText().toString()+" V", 740, 520, paint);
        canvas.drawText(No_45.getText().toString()+" V", 540, 560, paint); canvas.drawText(No_65.getText().toString()+" V", 740, 560, paint);
        canvas.drawText(No_46.getText().toString()+" V", 540, 600, paint); canvas.drawText(No_66.getText().toString()+" V", 740, 600, paint);
        canvas.drawText(No_47.getText().toString()+" V", 540, 640, paint); canvas.drawText(No_67.getText().toString()+" V", 740, 640, paint);
        canvas.drawText(No_48.getText().toString()+" V", 540, 680, paint); canvas.drawText(No_68.getText().toString()+" V", 740, 680, paint);
        canvas.drawText(No_49.getText().toString()+" V", 540, 720, paint); canvas.drawText(No_69.getText().toString()+" V", 740, 720, paint);
        canvas.drawText(No_50.getText().toString()+" V", 540, 760, paint); canvas.drawText(No_70.getText().toString()+" V", 740, 760, paint);
        canvas.drawText(No_51.getText().toString()+" V", 540, 800, paint); canvas.drawText(No_71.getText().toString()+" V", 740, 800, paint);
        canvas.drawText(No_52.getText().toString()+" V", 540, 840, paint); canvas.drawText(No_72.getText().toString()+" V", 740, 840, paint);
        canvas.drawText(No_53.getText().toString()+" V", 540, 880, paint); canvas.drawText(No_73.getText().toString()+" V", 740, 880, paint);
        canvas.drawText(No_54.getText().toString()+" V", 540, 920, paint); canvas.drawText(No_74.getText().toString()+" V", 740, 920, paint);
        canvas.drawText(No_55.getText().toString()+" V", 540, 960, paint); canvas.drawText(No_75.getText().toString()+" V", 740, 960, paint);
        canvas.drawText(No_56.getText().toString()+" V", 540, 1000, paint); canvas.drawText(No_76.getText().toString()+" V", 740, 1000, paint);
        canvas.drawText(No_57.getText().toString()+" V", 540, 1040, paint); canvas.drawText(No_77.getText().toString()+" V", 740, 1040, paint);
        canvas.drawText(No_58.getText().toString()+" V", 540, 1080, paint); canvas.drawText(No_78.getText().toString()+" V", 740, 1080, paint);
        canvas.drawText(No_59.getText().toString()+" V", 540, 1120, paint); canvas.drawText(No_79.getText().toString()+" V", 740, 1120, paint);
        canvas.drawText(No_60.getText().toString()+" V", 540, 1160, paint); canvas.drawText(No_80.getText().toString()+" V", 740, 1160, paint);

        canvas.drawText(No_81.getText().toString()+" V", 940, 400, paint); canvas.drawText(No_101.getText().toString()+" V", 150, 1240, paint);
        canvas.drawText(No_82.getText().toString()+" V", 940, 440, paint); canvas.drawText(No_102.getText().toString()+" V", 150, 1280, paint);
        canvas.drawText(No_83.getText().toString()+" V", 940, 480, paint); canvas.drawText(No_103.getText().toString()+" V", 150, 1320, paint);
        canvas.drawText(No_84.getText().toString()+" V", 940, 520, paint); canvas.drawText(No_104.getText().toString()+" V", 150, 1360, paint);
        canvas.drawText(No_85.getText().toString()+" V", 940, 560, paint); canvas.drawText(No_105.getText().toString()+" V", 150, 1400, paint);
        canvas.drawText(No_86.getText().toString()+" V", 940, 600, paint); canvas.drawText(No_106.getText().toString()+" V", 150, 1440, paint);
        canvas.drawText(No_87.getText().toString()+" V", 940, 640, paint); canvas.drawText(No_107.getText().toString()+" V", 150, 1480, paint);
        canvas.drawText(No_88.getText().toString()+" V", 940, 680, paint); canvas.drawText(No_108.getText().toString()+" V", 150, 1520, paint);
        canvas.drawText(No_89.getText().toString()+" V", 940, 720, paint);
        canvas.drawText(No_90.getText().toString()+" V", 940, 760, paint);
        canvas.drawText(No_91.getText().toString()+" V", 940, 800, paint);
        canvas.drawText(No_92.getText().toString()+" V", 940, 840, paint);
        canvas.drawText(No_93.getText().toString()+" V", 940, 880, paint);
        canvas.drawText(No_94.getText().toString()+" V", 940, 920, paint);
        canvas.drawText(No_95.getText().toString()+" V", 940, 960, paint);
        canvas.drawText(No_96.getText().toString()+" V", 940, 1000, paint);
        canvas.drawText(No_97.getText().toString()+" V", 940, 1040, paint);
        canvas.drawText(No_98.getText().toString()+" V", 940, 1080, paint);
        canvas.drawText(No_99.getText().toString()+" V", 940, 1120, paint);
        canvas.drawText(No_100.getText().toString()+" V", 950, 1160, paint);

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
        Toast.makeText(PmBatteryBTM.this, "PDF has Created", Toast.LENGTH_LONG).show();


        dateTime = new Date();
    }

    public void signatureDigitalOPS()
    {
        dialogCustomTTD = new AlertDialog.Builder(PmBatteryBTM.this);
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
            MediaScannerConnection.scanFile(PmBatteryBTM.this,
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
        dialogCustomTTD = new AlertDialog.Builder(PmBatteryBTM.this);
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
            MediaScannerConnection.scanFile(PmBatteryBTM.this,
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
}
