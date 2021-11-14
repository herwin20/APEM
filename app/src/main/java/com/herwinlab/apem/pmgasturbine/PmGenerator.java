package com.herwinlab.apem.pmgasturbine;

import android.Manifest;
import android.app.Dialog;
import android.app.Notification;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.pdf.PdfDocument;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.PermissionRequest;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.snackbar.Snackbar;
import com.herwinlab.apem.MainActivity;
import com.herwinlab.apem.R;
import com.herwinlab.apem.notification.NotificationUtils;
import com.herwinlab.apem.pmgasturbine.imagefunction.CameraActivity;
import com.herwinlab.apem.pmgasturbine.imagefunction.CameraUtils;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.kyanogen.signatureview.SignatureView;
import com.zigis.materialtextfield.MaterialTextField;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

public class PmGenerator extends AppCompatActivity implements LocationListener {

    LocationManager locationManager;

    //Notifikasi
    private NotificationUtils mNotificationUtils;

    // Activity request codes
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;

    // key to store image path in savedInstance state
    public static final String KEY_IMAGE_STORAGE_PATH = "image_path";

    public static final int EVIDENCE_BEFORE = 1;
    public static final int EVIDENCE_PROCESS = 2;
    public static final int EVIDENCE_AFTER = 3;
    //public static final int MEDIA_TYPE_VIDEO = 2;
    //public static final int MEDIA_TYPE_IMAGE = 2;

    // Bitmap sampling size
    public static final int BITMAP_SAMPLE_SIZE = 8;

    // Gallery directory name to store the images or videos
    public static final String GALLERY_DIRECTORY_NAME = "EVIDENCEPM/GT/GENERATOR";

    // Image and Video file extensions
    public static final String IMAGE_EXTENSION = "jpg";
    public static final String VIDEO_EXTENSION = "mp4";

    private static String imageStoragePath;

    // Edit Text For Carbon Brush
    public EditText A1_nde, A2_nde, A3_nde;     //holder A nde
    public EditText A1_de, A2_de, A3_de;        // holder A de
    public EditText B1_nde, B2_nde, B3_nde;     //holder B nde
    public EditText B1_de, B2_de, B3_de;        // holder B de
    public EditText C1_nde, C2_nde, C3_nde;     //holder C nde
    public EditText C1_de, C2_de, C3_de;        // holder C de
    public EditText D1_nde, D2_nde, D3_nde;     //holder D nde
    public EditText D1_de, D2_de, D3_de;        // holder D de
    public EditText E1_nde, E2_nde, E3_nde;     //holder E nde
    public EditText E1_de, E2_de, E3_de;        // holder E de

    //Edit Text For Noted, Aux, Information
    public EditText Catatan1, Catatan2, InletFilter, OutletFilter, LampuMati, BrushGear, CecerMinyak, FilterSarang, Kebersihan;
    public EditText orangPm1, orangPm2, orangPm3, Operator;
    public EditText NoWO, namaGT;

    //TextView
    public TextView tanggal_pm, judulPM, gpS, ttdOperator;

    //Handler
    private final Handler handler = new Handler();

    //Button
    public Button photoBefore, photoProcess, photoAfter, mClear, mGetSign, mCancel;

    //Image View
    public ImageView imgBefore, imgProcess, imgAfter;

    // Coordinator Layout
    CoordinatorLayout Coor_Pm;

    //PDF
    Bitmap bitmap, scaleBitmap, PJBscale, IPJBway, TTDpakdam, bitmapTTD, turbineImg;
    int pageWidth = 1200;
    Date dateTime;
    DateFormat dateFormat;

    // Graph
    protected float ValueA1, ValueA2, ValueA3; //Holder A1 De
    protected float ValueA11, ValueA12, ValueA13; //Holder A1 Nde
    protected float ValueB1, ValueB2, ValueB3; //Holder B1 De
    protected float ValueB11, ValueB12, ValueB13; //Holder B1 Nde
    protected float ValueC1, ValueC2, ValueC3; //Holder C1 De
    protected float ValueC11, ValueC12, ValueC13; //Holder C1 Nde
    protected float ValueD1, ValueD2, ValueD3; //Holder D1 De
    protected float ValueD11, ValueD12, ValueD13; //Holder D1 Nde
    protected float ValueE1, ValueE2, ValueE3; //Holder E1 De
    protected float ValueE11, ValueE12, ValueE13; //Holder E1 Nde
    ArrayList<BarEntry> barEntries;
    ArrayList<String> barLabels;
    BarDataSet barDataSet;
    BarData barData;
    protected BarChart chartHolderA1_nde;
    protected BarChart chartHolderA1_de;

    //Shared Prefs
    private SharedPreferences prefs;
    private final String A1NDE_KEY = "A1NDE";
    private final String A2NDE_KEY = "A2NDE";
    private final String A3NDE_KEY = "A3NDE";
    private final String A1DE_KEY = "A1DE";
    private final String A2DE_KEY = "A2DE";
    private final String A3DE_KEY = "A3DE";
    private final String B1NDE_KEY = "B1NDE";
    private final String B2NDE_KEY = "B2NDE";
    private final String B3NDE_KEY = "B3NDE";
    private final String B1DE_KEY = "B1DE";
    private final String B2DE_KEY = "B2DE";
    private final String B3DE_KEY = "B3DE";
    private final String C1NDE_KEY = "C1NDE";
    private final String C2NDE_KEY = "C2NDE";
    private final String C3NDE_KEY = "C3NDE";
    private final String C1DE_KEY = "C1DE";
    private final String C2DE_KEY = "C2DE";
    private final String C3DE_KEY = "C3DE";
    private final String D1NDE_KEY = "D1NDE";
    private final String D2NDE_KEY = "D2NDE";
    private final String D3NDE_KEY = "D3NDE";
    private final String D1DE_KEY = "D1DE";
    private final String D2DE_KEY = "D2DE";
    private final String D3DE_KEY = "D3DE";
    private final String E1NDE_KEY = "E1NDE";
    private final String E2NDE_KEY = "E2NDE";
    private final String E3NDE_KEY = "E3NDE";
    private final String E1DE_KEY = "E1DE";
    private final String E2DE_KEY = "E2DE";
    private final String E3DE_KEY = "E3DE";
    private final String CAT1_KEY = "CAT1";
    private final String CAT2_KEY = "CAT2";
    private final String INFIL_KEY = "INFIL";
    private final String OUTFIL_KEY = "OUTFIL";
    private final String LAMPU_KEY = "LAMPU";
    private final String BRUSH_KEY = "BRUSH";
    private final String MINYAK_KEY = "MINYAK";
    private final String FILTER_KEY = "FILTER";
    private final String KEBERSIHAN_KEY = "KEBERSIHAN";
    private final String ORANG1_KEY = "ORANG1";
    private final String ORANG2_KEY = "ORANG2";
    private final String ORANG3_KEY = "ORANG3";
    private final String OPERATOR_KEY = "OPERATOR";
    private final String NOWO_KEY = "NOWO";
    private final String NAMAGT_KEY = "NAMAGT";

    Context mContext;

    //Custom Dialog
    AlertDialog.Builder dialogCustom, dialogCustomTTD, dialogCustomPDF;
    LayoutInflater inflater, inflaterTTD, inflaterPDF;
    View dialogView, dialogViewTTD, view, viewTTD, dialogViewPDF;
    File file;
    Dialog dialog, dialogTTD;
    //signature mSignature;

    //Digital Signature
    SignatureView signatureView;
    String path;
    private static final String IMAGE_DIRECTORY = "/Signature";

    // Creating Separate Directory for Signature and Image
    String DIRECTORY = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Signature/";
    String DIRECTORY_PHOTO = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Preventive/GT/Generator/";

    // Creating Camera Code
    private static final int CAMERA_REQUEST_CODE_BEFORE = 7777;
    private static final int CAMERA_REQUEST_CODE_PROCESS = 7778;
    private static final int CAMERA_REQUEST_CODE_AFTER = 7779;

    //Linear Layout Click
    LinearLayout LinHolderA, LinHolderB, LinHolderC, LinHolderD, LinHolderE, buttonCreatePDF, buttonSignSPV, buttonSignOPS, dialogCreatedPDF;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.pmgenerator_activity);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        transparentStatusAndNavigation();

        // Untuk Tanggal
        handler.postDelayed(runnable, 1000);
        //Text Untuk Tanggal
        tanggal_pm = findViewById(R.id.tanggalpm);
        judulPM = findViewById(R.id.judul_pm);

        // Koordinator Layout
        Coor_Pm = findViewById(R.id.coor_pm);

        // Nama GT
        namaGT = findViewById(R.id.namaGT);
        //Gps di PM
        gpS = findViewById(R.id.gps);

        // Dialog Graph Function
       // dialog = new Dialog(PmGenerator.this);
        // Removing the features of Normal Dialogs
     //   dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
     //   dialog.setContentView(R.layout.signature_activity);
      //  dialog.setCancelable(true);

        //Untuk ttd Operator
        buttonSignOPS = findViewById(R.id.buttonSignatureOPS);
        buttonSignOPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signatureDigitalOPS();
            }
        });

        // TTD SPV
        buttonSignSPV = findViewById(R.id.buttonSignatureSPV);
        buttonSignSPV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signatureDigitalSPV();
            }
        });

        // Holder Initiated
        A1_nde = findViewById(R.id.a1_nde);
        A2_nde = findViewById(R.id.a2_nde);
        A3_nde = findViewById(R.id.a3_nde);
        A1_de = findViewById(R.id.a1_de);
        A2_de = findViewById(R.id.a2_de);
        A3_de = findViewById(R.id.a3_de);
        B1_nde = findViewById(R.id.b1_nde);
        B2_nde = findViewById(R.id.b2_nde);
        B3_nde = findViewById(R.id.b3_nde);
        B1_de = findViewById(R.id.b1_de);
        B2_de = findViewById(R.id.b2_de);
        B3_de = findViewById(R.id.b3_de);
        C1_nde = findViewById(R.id.c1_nde);
        C2_nde = findViewById(R.id.c2_nde);
        C3_nde = findViewById(R.id.c3_nde);
        C1_de = findViewById(R.id.c1_de);
        C2_de = findViewById(R.id.c2_de);
        C3_de = findViewById(R.id.c3_de);
        D1_nde = findViewById(R.id.d1_nde);
        D2_nde = findViewById(R.id.d2_nde);
        D3_nde = findViewById(R.id.d3_nde);
        D1_de = findViewById(R.id.d1_de);
        D2_de = findViewById(R.id.d2_de);
        D3_de = findViewById(R.id.d3_de);
        E1_nde = findViewById(R.id.e1_nde);
        E2_nde = findViewById(R.id.e2_nde);
        E3_nde = findViewById(R.id.e3_nde);
        E1_de = findViewById(R.id.e1_de);
        E2_de = findViewById(R.id.e2_de);
        E3_de = findViewById(R.id.e3_de);

        //Aux Initiated
        Catatan1 = findViewById(R.id.ket);
        Catatan2 = findViewById(R.id.ket2);
        InletFilter = findViewById(R.id.inlet);
        OutletFilter = findViewById(R.id.oulet);
        LampuMati = findViewById(R.id.lampu);
        BrushGear = findViewById(R.id.shaftbrush);
        CecerMinyak = findViewById(R.id.minyak);
        FilterSarang = findViewById(R.id.sarang);
        Kebersihan = findViewById(R.id.kebersihan);
        orangPm1 = findViewById(R.id.nama_pm1);
        orangPm2 = findViewById(R.id.nama_pm2);
        orangPm3 = findViewById(R.id.nama_pm3);
        Operator = findViewById(R.id.nama_operator);
        NoWO = findViewById(R.id.no_wo);

        // Shared Prefs
        prefs = getPreferences(MODE_PRIVATE);
        A1_nde.setText(prefs.getString(A1NDE_KEY, ""));
        A2_nde.setText(prefs.getString(A2NDE_KEY, ""));
        A3_nde.setText(prefs.getString(A3NDE_KEY, ""));
        A1_de.setText(prefs.getString(A1DE_KEY, ""));
        A2_de.setText(prefs.getString(A2DE_KEY, ""));
        A3_de.setText(prefs.getString(A3DE_KEY, ""));
        B1_nde.setText(prefs.getString(B1NDE_KEY, ""));
        B2_nde.setText(prefs.getString(B2NDE_KEY, ""));
        B3_nde.setText(prefs.getString(B3NDE_KEY, ""));
        B1_de.setText(prefs.getString(B1DE_KEY, ""));
        B2_de.setText(prefs.getString(B2DE_KEY, ""));
        B3_de.setText(prefs.getString(B3DE_KEY, ""));
        C1_nde.setText(prefs.getString(C1NDE_KEY, ""));
        C2_nde.setText(prefs.getString(C2NDE_KEY, ""));
        C3_nde.setText(prefs.getString(C3NDE_KEY, ""));
        C1_de.setText(prefs.getString(C1DE_KEY, ""));
        C2_de.setText(prefs.getString(C2DE_KEY, ""));
        C3_de.setText(prefs.getString(C3DE_KEY, ""));
        D1_nde.setText(prefs.getString(D1NDE_KEY, ""));
        D2_nde.setText(prefs.getString(D2NDE_KEY, ""));
        D3_nde.setText(prefs.getString(D3NDE_KEY, ""));
        D1_de.setText(prefs.getString(D1DE_KEY, ""));
        D2_de.setText(prefs.getString(D2DE_KEY, ""));
        D3_de.setText(prefs.getString(D3DE_KEY, ""));
        E1_nde.setText(prefs.getString(E1NDE_KEY, ""));
        E2_nde.setText(prefs.getString(E2NDE_KEY, ""));
        E3_nde.setText(prefs.getString(E3NDE_KEY, ""));
        E1_de.setText(prefs.getString(E1DE_KEY, ""));
        E2_de.setText(prefs.getString(E2DE_KEY, ""));
        E3_de.setText(prefs.getString(E3DE_KEY, ""));

        Catatan1.setText(prefs.getString(CAT1_KEY, ""));
        Catatan2.setText(prefs.getString(CAT2_KEY, ""));
        InletFilter.setText(prefs.getString(INFIL_KEY, ""));
        OutletFilter.setText(prefs.getString(OUTFIL_KEY, ""));
        LampuMati.setText(prefs.getString(LAMPU_KEY, ""));
        BrushGear.setText(prefs.getString(BRUSH_KEY, ""));
        CecerMinyak.setText(prefs.getString(MINYAK_KEY, ""));
        FilterSarang.setText(prefs.getString(FILTER_KEY, ""));
        Kebersihan.setText(prefs.getString(KEBERSIHAN_KEY, ""));
        orangPm1.setText(prefs.getString(ORANG1_KEY, ""));
        orangPm2.setText(prefs.getString(ORANG2_KEY, ""));
        orangPm3.setText(prefs.getString(ORANG3_KEY, ""));
        Operator.setText(prefs.getString(OPERATOR_KEY, ""));
        NoWO.setText(prefs.getString(NOWO_KEY, ""));
        namaGT.setText(prefs.getString(NAMAGT_KEY,""));

        //Linear Layout Click
        LinHolderA = findViewById(R.id.linear_a);
        LinHolderA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savedPrefs();
                final String graphA1 = A1_nde.getText().toString();
                final String graphA2 = A2_nde.getText().toString();
                final String graphA3 = A3_nde.getText().toString();
                final String graphAD1 = A1_de.getText().toString();
                final String graphAD2 = A2_de.getText().toString();
                final String graphAD3 = A3_de.getText().toString();

                if (graphA1.isEmpty()) {
                    A1_nde.setError("Isi A");
                    A1_nde.requestFocus();
                } else if (graphA2.isEmpty()) {
                    A2_nde.setError("Isi B");
                    A2_nde.requestFocus();
                } else if (graphA3.isEmpty()) {
                    A3_nde.setError("Isi C");
                    A3_nde.requestFocus();
                } else if (graphAD1.isEmpty()) {
                    A1_de.setError("Isi A");
                    A1_de.requestFocus();
                } else if (graphAD2.isEmpty()) {
                    A2_de.setError("Isi B");
                    A2_de.requestFocus();
                } else if (graphAD3.isEmpty()) {
                    A3_de.setError("Isi C");
                    A3_de.requestFocus();
                } else {
                    DialogCustomA();
                }
            }
        });

        LinHolderB = findViewById(R.id.linear_b);
        LinHolderB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savedPrefs();
                final String graphA1 = B1_nde.getText().toString();
                final String graphA2 = B2_nde.getText().toString();
                final String graphA3 = B3_nde.getText().toString();
                final String graphAD1 = B1_de.getText().toString();
                final String graphAD2 = B2_de.getText().toString();
                final String graphAD3 = B3_de.getText().toString();

                if (graphA1.isEmpty()) {
                    B1_nde.setError("Isi A");
                    B1_nde.requestFocus();
                } else if (graphA2.isEmpty()) {
                    B2_nde.setError("Isi B");
                    B2_nde.requestFocus();
                } else if (graphA3.isEmpty()) {
                    B3_nde.setError("Isi C");
                    B3_nde.requestFocus();
                } else if (graphAD1.isEmpty()) {
                    B1_de.setError("Isi A");
                    B1_de.requestFocus();
                } else if (graphAD2.isEmpty()) {
                    B2_de.setError("Isi B");
                    B2_de.requestFocus();
                } else if (graphAD3.isEmpty()) {
                    B3_de.setError("Isi C");
                    B3_de.requestFocus();
                } else {
                    DialogCustomB();
                }
            }
        });

        LinHolderC = findViewById(R.id.linear_c);
        LinHolderC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                savedPrefs();
                final String graphA1 = C1_nde.getText().toString();
                final String graphA2 = C2_nde.getText().toString();
                final String graphA3 = C3_nde.getText().toString();
                final String graphAD1 = C1_de.getText().toString();
                final String graphAD2 = C2_de.getText().toString();
                final String graphAD3 = C3_de.getText().toString();

                if (graphA1.isEmpty()) {
                    C1_nde.setError("Isi A");
                    C1_nde.requestFocus();
                } else if (graphA2.isEmpty()) {
                    C2_nde.setError("Isi B");
                    C2_nde.requestFocus();
                } else if (graphA3.isEmpty()) {
                    C3_nde.setError("Isi C");
                    C3_nde.requestFocus();
                } else if (graphAD1.isEmpty()) {
                    C1_de.setError("Isi A");
                    C1_de.requestFocus();
                } else if (graphAD2.isEmpty()) {
                    C2_de.setError("Isi B");
                    C2_de.requestFocus();
                } else if (graphAD3.isEmpty()) {
                    C3_de.setError("Isi C");
                    C3_de.requestFocus();
                } else {
                    DialogCustomC();
                }

            }
        });

        LinHolderD = findViewById(R.id.linear_d);
        LinHolderD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savedPrefs();
                final String graphA1 = D1_nde.getText().toString();
                final String graphA2 = D2_nde.getText().toString();
                final String graphA3 = D3_nde.getText().toString();
                final String graphAD1 = D1_de.getText().toString();
                final String graphAD2 = D2_de.getText().toString();
                final String graphAD3 = D3_de.getText().toString();

                if (graphA1.isEmpty()) {
                    D1_nde.setError("Isi A");
                    D1_nde.requestFocus();
                } else if (graphA2.isEmpty()) {
                    D2_nde.setError("Isi B");
                    D2_nde.requestFocus();
                } else if (graphA3.isEmpty()) {
                    D3_nde.setError("Isi C");
                    D3_nde.requestFocus();
                } else if (graphAD1.isEmpty()) {
                    D1_de.setError("Isi A");
                    D1_de.requestFocus();
                } else if (graphAD2.isEmpty()) {
                    D2_de.setError("Isi B");
                    D2_de.requestFocus();
                } else if (graphAD3.isEmpty()) {
                    D3_de.setError("Isi C");
                    D3_de.requestFocus();
                } else {
                    DialogCustomD();
                }
            }
        });

        LinHolderE = findViewById(R.id.linear_e);
        LinHolderE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savedPrefs();
                final String graphA1 = E1_nde.getText().toString();
                final String graphA2 = E2_nde.getText().toString();
                final String graphA3 = E3_nde.getText().toString();
                final String graphAD1 = E1_de.getText().toString();
                final String graphAD2 = E2_de.getText().toString();
                final String graphAD3 = E3_de.getText().toString();

                if (graphA1.isEmpty()) {
                    E1_nde.setError("Isi A");
                    E1_nde.requestFocus();
                } else if (graphA2.isEmpty()) {
                    E2_nde.setError("Isi B");
                    E2_nde.requestFocus();
                } else if (graphA3.isEmpty()) {
                    E3_nde.setError("Isi C");
                    E3_nde.requestFocus();
                } else if (graphAD1.isEmpty()) {
                    E1_de.setError("Isi A");
                    E1_de.requestFocus();
                } else if (graphAD2.isEmpty()) {
                    E2_de.setError("Isi B");
                    E2_de.requestFocus();
                } else if (graphAD3.isEmpty()) {
                    E3_de.setError("Isi C");
                    E3_de.requestFocus();
                } else {
                    DialogCustomE();
                }
            }
        });

        // PDF Bitmap
        //Logo PJB
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.logo_pjb);
        PJBscale = Bitmap.createScaledBitmap(bitmap,200, 100, false);

        //Logo PJB
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.logo_ipjb);
        IPJBway = Bitmap.createScaledBitmap(bitmap,260, 100, false);

        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ttd);
        TTDpakdam = Bitmap.createScaledBitmap(bitmap,390, 180, false);

        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.turbine);
        turbineImg = Bitmap.createScaledBitmap(bitmap, 514, 500, false);

        photoProcess = findViewById(R.id.btn_process);
        photoProcess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savedPrefs();
                Intent intent = new Intent(PmGenerator.this, CameraActivity.class);
                startActivity(intent);
            }
        });

        imgBefore = findViewById(R.id.temuan1_pic);
        imgProcess = findViewById(R.id.temuan2_pic);
        imgAfter = findViewById(R.id.temuan3_pic);

        //permission
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        mNotificationUtils = new NotificationUtils(this);

        buttonCreatePDF = findViewById(R.id.buttonCreatepdf);
        buttonCreatePDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(PmGenerator.this, R.style.CustomAlertDialog);
                ViewGroup viewGroup = findViewById(R.id.content);
                final View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.dialog_createdpdf, viewGroup, false);
                builder.setView(dialogView);
                final AlertDialog alertDialog = builder.create();
                //dialogCustomPDF.setIcon(R.mipmap.ic_launcher);
                //dialogCustomPDF.setTitle("WARNING !!!");

                dialogCreatedPDF = dialogView.findViewById(R.id.dialogCreatepdf);
                dialogCreatedPDF.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        savedPrefs();
                        CreatedPDF();
                        Notification.Builder notifPDF = mNotificationUtils.getAndroidChannelNotification("PDF Notification", "PDF telah berhasil disimpan di Storage");
                        mNotificationUtils.getManager().notify(101, notifPDF.build());
                        alertDialog.dismiss();
                    }
                });

                alertDialog.show();
            }
        });

        getLocation();
        restoreFromBundle(savedInstanceState);

    }

    public void savedPrefs(){
        // Shared Prefs
        prefs = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(A1NDE_KEY, A1_nde.getText().toString());
        editor.putString(A2NDE_KEY, A2_nde.getText().toString());
        editor.putString(A3NDE_KEY, A3_nde.getText().toString());
        editor.putString(A1DE_KEY, A1_de.getText().toString());
        editor.putString(A2DE_KEY, A2_de.getText().toString());
        editor.putString(A3DE_KEY, A3_de.getText().toString());
        editor.putString(B1NDE_KEY, B1_nde.getText().toString());
        editor.putString(B2NDE_KEY, B2_nde.getText().toString());
        editor.putString(B3NDE_KEY, B3_nde.getText().toString());
        editor.putString(B1DE_KEY, B1_de.getText().toString());
        editor.putString(B2DE_KEY, B2_de.getText().toString());
        editor.putString(B3DE_KEY, B3_de.getText().toString());
        editor.putString(C1NDE_KEY, C1_nde.getText().toString());
        editor.putString(C2NDE_KEY, C2_nde.getText().toString());
        editor.putString(C3NDE_KEY, C3_nde.getText().toString());
        editor.putString(C1DE_KEY, C1_de.getText().toString());
        editor.putString(C2DE_KEY, C2_de.getText().toString());
        editor.putString(C3DE_KEY, C3_de.getText().toString());
        editor.putString(D1NDE_KEY, D1_nde.getText().toString());
        editor.putString(D2NDE_KEY, D2_nde.getText().toString());
        editor.putString(D3NDE_KEY, D3_nde.getText().toString());
        editor.putString(D1DE_KEY, D1_de.getText().toString());
        editor.putString(D2DE_KEY, D2_de.getText().toString());
        editor.putString(D3DE_KEY, D3_de.getText().toString());
        editor.putString(E1NDE_KEY, E1_nde.getText().toString());
        editor.putString(E2NDE_KEY, E2_nde.getText().toString());
        editor.putString(E3NDE_KEY, E3_nde.getText().toString());
        editor.putString(E1DE_KEY, E1_de.getText().toString());
        editor.putString(E2DE_KEY, E2_de.getText().toString());
        editor.putString(E3DE_KEY, E3_de.getText().toString());
        editor.putString(CAT1_KEY, Catatan1.getText().toString());
        editor.putString(CAT2_KEY, Catatan2.getText().toString());
        editor.putString(INFIL_KEY, InletFilter.getText().toString());
        editor.putString(OUTFIL_KEY, OutletFilter.getText().toString());
        editor.putString(LAMPU_KEY, LampuMati.getText().toString());
        editor.putString(BRUSH_KEY, BrushGear.getText().toString());
        editor.putString(MINYAK_KEY, CecerMinyak.getText().toString());
        editor.putString(FILTER_KEY, FilterSarang.getText().toString());
        editor.putString(KEBERSIHAN_KEY, Kebersihan.getText().toString());
        editor.putString(ORANG1_KEY, orangPm1.getText().toString());
        editor.putString(ORANG2_KEY, orangPm2.getText().toString());
        editor.putString(ORANG3_KEY, orangPm3.getText().toString());
        editor.putString(OPERATOR_KEY, Operator.getText().toString());
        editor.putString(NOWO_KEY, NoWO.getText().toString());
        editor.putString(NAMAGT_KEY, namaGT.getText().toString());

        editor.apply();
    }

    //Fungsi Untuk Jam dan Tanggal
    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Calendar c1 = Calendar.getInstance();
            SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/YYYY h:m:s a");
            String strdate1 = sdf1.format(c1.getTime());
            TextView txtdate1 = findViewById(R.id.tanggalpm);
            txtdate1.setText(strdate1);

            handler.postDelayed(this, 1000);
        }
    };

    // Custom Dialog Chart
    public void DialogCustomA()
    {
        dialogCustom = new AlertDialog.Builder(PmGenerator.this);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.dialog_grapha, null);
        dialogCustom.setView(dialogView);
        dialogCustom.setCancelable(true);
        dialogCustom.setIcon(R.mipmap.ic_launcher);
        dialogCustom.setTitle("Graph Holder A");

        chartHolderANDE();
        chartHolderADE();

        dialogCustom.setPositiveButton("EXIT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        dialogCustom.show();
    }

    public void DialogCustomB()
    {
        dialogCustom = new AlertDialog.Builder(PmGenerator.this);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.dialog_grapha, null);
        dialogCustom.setView(dialogView);
        dialogCustom.setCancelable(true);
        dialogCustom.setIcon(R.mipmap.ic_launcher);
        dialogCustom.setTitle("Graph Holder B");

        chartHolderBNDE();
        chartHolderBDE();

        dialogCustom.setPositiveButton("EXIT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        dialogCustom.show();
    }

    public void DialogCustomC()
    {
        dialogCustom = new AlertDialog.Builder(PmGenerator.this);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.dialog_grapha, null);
        dialogCustom.setView(dialogView);
        dialogCustom.setCancelable(true);
        dialogCustom.setIcon(R.mipmap.ic_launcher);
        dialogCustom.setTitle("Graph Holder C");

        chartHolderCNDE();
        chartHolderCDE();

        dialogCustom.setPositiveButton("EXIT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        dialogCustom.show();
    }

    public void DialogCustomD()
    {
        dialogCustom = new AlertDialog.Builder(PmGenerator.this);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.dialog_grapha, null);
        dialogCustom.setView(dialogView);
        dialogCustom.setCancelable(true);
        dialogCustom.setIcon(R.mipmap.ic_launcher);
        dialogCustom.setTitle("Graph Holder D");

        chartHolderDNDE();
        chartHolderDDE();

        dialogCustom.setPositiveButton("EXIT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        dialogCustom.show();
    }

    public void DialogCustomE()
    {
        dialogCustom = new AlertDialog.Builder(PmGenerator.this);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.dialog_grapha, null);
        dialogCustom.setView(dialogView);
        dialogCustom.setCancelable(true);
        dialogCustom.setIcon(R.mipmap.ic_launcher);
        dialogCustom.setTitle("Graph Holder E");

        chartHolderENDE();
        chartHolderEDE();

        dialogCustom.setPositiveButton("EXIT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        dialogCustom.show();
    }

    // Press Back Saved it !!
    @Override
    public void onBackPressed()
    {
        Snackbar snackbar = Snackbar.make(Coor_Pm,"Before Exit you have to Saved it ??", Snackbar.LENGTH_LONG)
                .setAction("EXIT", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        savedPrefs();
                        finish();
                    }
                });
        snackbar.show();
    }

    public void  chartHolderANDE()
    {
        // BarChart
        chartHolderA1_nde = dialogView.findViewById(R.id.holder_A_nde);

        final String graphA1 = A1_nde.getText().toString();
        final String graphA2 = A2_nde.getText().toString();
        final String graphA3 = A3_nde.getText().toString();

            A1_nde.setText(graphA1);
            A2_nde.setText(graphA2);
            A3_nde.setText(graphA3);
            ValueA1 = Float.valueOf(A1_nde.getText().toString());
            ValueA2 = Float.valueOf(A2_nde.getText().toString());
            ValueA3 = Float.valueOf(A3_nde.getText().toString());
            barEntries = new ArrayList<BarEntry>();
            barLabels = new ArrayList<String>();
            barLabels.add("");// index 0 kosongkan saja
            barEntries.add(new BarEntry(1, ValueA1));
            barLabels.add("A1");
            barEntries.add(new BarEntry(2, ValueA2));
            barLabels.add("A2");
            barEntries.add(new BarEntry(3, ValueA3));
            barLabels.add("A3");
            barDataSet = new BarDataSet(barEntries, "Holder A NDE");
            barData = new BarData(barDataSet);
            chartHolderA1_nde.animateY(1000);
            chartHolderA1_nde.getXAxis().setValueFormatter(
                    new IndexAxisValueFormatter(barLabels));
            YAxis yAxis = chartHolderA1_nde.getAxisLeft();
            yAxis.setAxisMinimum(0f); // start at zero
            yAxis.setAxisMaximum(60f); // the axis maximum is 60
            LimitLine limitA = new LimitLine(15f, "Minimum Carbon"); //garis batas
            limitA.setLineWidth(1f);
            limitA.setTextSize(5f);
            limitA.setLabelPosition(LimitLine.LimitLabelPosition.LEFT_BOTTOM);
            yAxis.addLimitLine(limitA);
            barDataSet.setColors(ColorTemplate.getHoloBlue());
            chartHolderA1_nde.setData(barData);

            //btnDisable.setVisibility(View.GONE);
            //setSave.setVisibility(View.VISIBLE);
            //Toast.makeText(CarbonBrush.this, "Berhasil Menampilkan Data", Toast.LENGTH_SHORT).show();

        //Untuk Warna ketika
        int A1nde = Integer.parseInt(graphA1);
        int A2nde = Integer.parseInt(graphA2);
        int A3nde = Integer.parseInt(graphA3);

        //Merah
        if(A1nde < 23)
        {
            A1_nde.setTextColor(Color.RED);
        }
        else {A1_nde.setTextColor(Color.BLACK);}

        if(A2nde < 23)
        {
            A2_nde.setTextColor(Color.RED);
        }
        else {A2_nde.setTextColor(Color.BLACK);}

        if(A3nde < 23)
        {
            A3_nde.setTextColor(Color.RED);
        }
        else {A3_nde.setTextColor(Color.BLACK);}
    }

    public void chartHolderADE()
    {
        chartHolderA1_de = dialogView.findViewById(R.id.holder_A_de);

        final String graphA1 = A1_de.getText().toString();
        final String graphA2 = A2_de.getText().toString();
        final String graphA3 = A3_de.getText().toString();

        if (graphA1.isEmpty()) {
            A1_de.setError("Isi A");
            A1_de.requestFocus();
        } else if (graphA2.isEmpty()) {
            A2_de.setError("Isi B");
            A2_de.requestFocus();
        } else if (graphA3.isEmpty()) {
            A3_de.setError("Isi C");
            A3_de.requestFocus();
        } else {

            A1_de.setText(graphA1);
            A2_de.setText(graphA2);
            A3_de.setText(graphA3);
            ValueA11 = Float.valueOf(A1_de.getText().toString());
            ValueA12 = Float.valueOf(A2_de.getText().toString());
            ValueA13 = Float.valueOf(A3_de.getText().toString());
            barEntries = new ArrayList<BarEntry>();
            barLabels = new ArrayList<String>();
            barLabels.add("");// index 0 kosongkan saja
            barEntries.add(new BarEntry(1, ValueA11));
            barLabels.add("A1");
            barEntries.add(new BarEntry(2, ValueA12));
            barLabels.add("A2");
            barEntries.add(new BarEntry(3, ValueA13));
            barLabels.add("A3");
            barDataSet = new BarDataSet(barEntries, "Holder A DE");
            barData = new BarData(barDataSet);
            chartHolderA1_de.animateY(1000);
            chartHolderA1_de.getXAxis().setValueFormatter(
                    new IndexAxisValueFormatter(barLabels));
            YAxis yAxis = chartHolderA1_de.getAxisLeft();
            yAxis.setAxisMinimum(0f); // start at zero
            yAxis.setAxisMaximum(60f); // the axis maximum is 60
            LimitLine limitA = new LimitLine(15f, "Minimum Carbon"); //garis batas
            limitA.setLineWidth(1f);
            limitA.setTextSize(5f);
            limitA.setLabelPosition(LimitLine.LimitLabelPosition.LEFT_BOTTOM);
            yAxis.addLimitLine(limitA);
            barDataSet.setColors(ColorTemplate.getHoloBlue());
            chartHolderA1_de.setData(barData);

            //btnDisable.setVisibility(View.GONE);
            //setSave.setVisibility(View.VISIBLE);
            //Toast.makeText(CarbonBrush.this, "Berhasil Menampilkan Data", Toast.LENGTH_SHORT).show();
        }

        //Untuk Warna ketika
        int A1de = Integer.parseInt(graphA1);
        int A2de = Integer.parseInt(graphA2);
        int A3de = Integer.parseInt(graphA3);

        //Merah
        if(A1de < 23)
        {
            A1_de.setTextColor(Color.RED);
        }
        else {A1_de.setTextColor(Color.BLACK);}

        if(A2de < 23)
        {
            A2_de.setTextColor(Color.RED);
        }
        else {A2_de.setTextColor(Color.BLACK);}

        if(A3de < 23)
        {
            A3_de.setTextColor(Color.RED);
        }
        else {A3_de.setTextColor(Color.BLACK);}
    }

    public void chartHolderBNDE()
    {
        chartHolderA1_nde = dialogView.findViewById(R.id.holder_A_nde);
        final String graphA1 = B1_nde.getText().toString();
        final String graphA2 = B2_nde.getText().toString();
        final String graphA3 = B3_nde.getText().toString();

        if (graphA1.isEmpty()) {
            B1_nde.setError("Isi A");
            B1_nde.requestFocus();
        } else if (graphA2.isEmpty()) {
            B2_nde.setError("Isi B");
            B2_nde.requestFocus();
        } else if (graphA3.isEmpty()) {
            B3_nde.setError("Isi C");
            B3_nde.requestFocus();
        } else {

            B1_nde.setText(graphA1);
            B2_nde.setText(graphA2);
            B3_nde.setText(graphA3);
            ValueB1 = Float.valueOf(B1_nde.getText().toString());
            ValueB2 = Float.valueOf(B2_nde.getText().toString());
            ValueB3 = Float.valueOf(B3_nde.getText().toString());
            barEntries = new ArrayList<BarEntry>();
            barLabels = new ArrayList<String>();
            barLabels.add("");// index 0 kosongkan saja
            barEntries.add(new BarEntry(1, ValueB1));
            barLabels.add("B1");
            barEntries.add(new BarEntry(2, ValueB2));
            barLabels.add("B2");
            barEntries.add(new BarEntry(3, ValueB3));
            barLabels.add("B3");
            barDataSet = new BarDataSet(barEntries, "Holder B NDE");
            barData = new BarData(barDataSet);
            chartHolderA1_nde.animateY(1000);
            chartHolderA1_nde.getXAxis().setValueFormatter(
                    new IndexAxisValueFormatter(barLabels));
            YAxis yAxis = chartHolderA1_nde.getAxisLeft();
            yAxis.setAxisMinimum(0f); // start at zero
            yAxis.setAxisMaximum(60f); // the axis maximum is 60
            LimitLine limitA = new LimitLine(15f, "Minimum Carbon"); //garis batas
            limitA.setLineWidth(1f);
            limitA.setTextSize(5f);
            limitA.setLabelPosition(LimitLine.LimitLabelPosition.LEFT_BOTTOM);
            yAxis.addLimitLine(limitA);
            barDataSet.setColors(ColorTemplate.getHoloBlue());
            chartHolderA1_nde.setData(barData);

            //btnDisable.setVisibility(View.GONE);
            //setSave.setVisibility(View.VISIBLE);
            //Toast.makeText(CarbonBrush.this, "Berhasil Menampilkan Data", Toast.LENGTH_SHORT).show();

        }

        //Untuk Warna ketika
        int B1nde = Integer.parseInt(graphA1);
        int B2nde = Integer.parseInt(graphA2);
        int B3nde = Integer.parseInt(graphA3);

        //Merah
        if(B1nde < 23)
        {
            B1_nde.setTextColor(Color.RED);
        }
        else {B1_nde.setTextColor(Color.BLACK);}

        if(B2nde < 23)
        {
            B2_nde.setTextColor(Color.RED);
        }
        else {B2_nde.setTextColor(Color.BLACK);}

        if(B3nde < 23)
        {
            B3_nde.setTextColor(Color.RED);
        }
        else {B3_nde.setTextColor(Color.BLACK);}
    }

    public void chartHolderBDE()
    {
        chartHolderA1_de = dialogView.findViewById(R.id.holder_A_de);
        final String graphA1 = B1_de.getText().toString();
        final String graphA2 = B2_de.getText().toString();
        final String graphA3 = B3_de.getText().toString();

        if (graphA1.isEmpty()) {
            B1_de.setError("Isi A");
            B1_de.requestFocus();
        } else if (graphA2.isEmpty()) {
            B2_de.setError("Isi B");
            B2_de.requestFocus();
        } else if (graphA3.isEmpty()) {
            B3_de.setError("Isi C");
            B3_de.requestFocus();
        } else {

            B1_de.setText(graphA1);
            B2_de.setText(graphA2);
            B3_de.setText(graphA3);
            ValueB11 = Float.valueOf(B1_de.getText().toString());
            ValueB12 = Float.valueOf(B2_de.getText().toString());
            ValueB13 = Float.valueOf(B3_de.getText().toString());
            barEntries = new ArrayList<BarEntry>();
            barLabels = new ArrayList<String>();
            barLabels.add("");// index 0 kosongkan saja
            barEntries.add(new BarEntry(1, ValueB11));
            barLabels.add("B1");
            barEntries.add(new BarEntry(2, ValueB12));
            barLabels.add("B2");
            barEntries.add(new BarEntry(3, ValueB13));
            barLabels.add("B3");
            barDataSet = new BarDataSet(barEntries, "Holder B DE");
            barData = new BarData(barDataSet);
            chartHolderA1_de.animateY(1000);
            chartHolderA1_de.getXAxis().setValueFormatter(
                    new IndexAxisValueFormatter(barLabels));
            YAxis yAxis = chartHolderA1_de.getAxisLeft();
            yAxis.setAxisMinimum(0f); // start at zero
            yAxis.setAxisMaximum(60f); // the axis maximum is 60
            LimitLine limitA = new LimitLine(15f, "Minimum Carbon"); //garis batas
            limitA.setLineWidth(1f);
            limitA.setTextSize(5f);
            limitA.setLabelPosition(LimitLine.LimitLabelPosition.LEFT_BOTTOM);
            yAxis.addLimitLine(limitA);
            barDataSet.setColors(ColorTemplate.getHoloBlue());
            chartHolderA1_de.setData(barData);

            //btnDisable.setVisibility(View.GONE);
            //setSave.setVisibility(View.VISIBLE);
            //Toast.makeText(CarbonBrush.this, "Berhasil Menampilkan Data", Toast.LENGTH_SHORT).show();
        }

        //Untuk Warna ketika
        int B1de = Integer.parseInt(graphA1);
        int B2de = Integer.parseInt(graphA2);
        int B3de = Integer.parseInt(graphA3);

        //Merah
        if(B1de < 23)
        {
            B1_de.setTextColor(Color.RED);
        }
        else {B1_de.setTextColor(Color.BLACK);}

        if(B2de < 23)
        {
            B2_de.setTextColor(Color.RED);
        }
        else {B2_de.setTextColor(Color.BLACK);}

        if(B3de < 23)
        {
            B3_de.setTextColor(Color.RED);
        }
        else {B3_de.setTextColor(Color.BLACK);}
    }

    public void chartHolderCNDE()
    {
        chartHolderA1_nde = dialogView.findViewById(R.id.holder_A_nde);
        final String graphA1 = C1_nde.getText().toString();
        final String graphA2 = C2_nde.getText().toString();
        final String graphA3 = C3_nde.getText().toString();

        if (graphA1.isEmpty()) {
            C1_nde.setError("Isi A");
            C1_nde.requestFocus();
        } else if (graphA2.isEmpty()) {
            C2_nde.setError("Isi B");
            C2_nde.requestFocus();
        } else if (graphA3.isEmpty()) {
            C3_nde.setError("Isi C");
            C3_nde.requestFocus();
        } else {

            C1_nde.setText(graphA1);
            C2_nde.setText(graphA2);
            C3_nde.setText(graphA3);
            ValueC1 = Float.valueOf(C1_nde.getText().toString());
            ValueC2 = Float.valueOf(C2_nde.getText().toString());
            ValueC3 = Float.valueOf(C3_nde.getText().toString());
            barEntries = new ArrayList<BarEntry>();
            barLabels = new ArrayList<String>();
            barLabels.add("");// index 0 kosongkan saja
            barEntries.add(new BarEntry(1, ValueC1));
            barLabels.add("C1");
            barEntries.add(new BarEntry(2, ValueC2));
            barLabels.add("C2");
            barEntries.add(new BarEntry(3, ValueC3));
            barLabels.add("C3");
            barDataSet = new BarDataSet(barEntries, "Holder C NDE");
            barData = new BarData(barDataSet);
            chartHolderA1_nde.animateY(1000);
            chartHolderA1_nde.getXAxis().setValueFormatter(
                    new IndexAxisValueFormatter(barLabels));
            YAxis yAxis = chartHolderA1_nde.getAxisLeft();
            yAxis.setAxisMinimum(0f); // start at zero
            yAxis.setAxisMaximum(60f); // the axis maximum is 60
            LimitLine limitA = new LimitLine(15f, "Minimum Carbon"); //garis batas
            limitA.setLineWidth(1f);
            limitA.setTextSize(5f);
            limitA.setLabelPosition(LimitLine.LimitLabelPosition.LEFT_BOTTOM);
            yAxis.addLimitLine(limitA);
            barDataSet.setColors(ColorTemplate.getHoloBlue());
            chartHolderA1_nde.setData(barData);

            //btnDisable.setVisibility(View.GONE);
            //setSave.setVisibility(View.VISIBLE);
            //Toast.makeText(CarbonBrush.this, "Berhasil Menampilkan Data", Toast.LENGTH_SHORT).show();
        }

        //Untuk Warna ketika
        int C1nde = Integer.parseInt(graphA1);
        int C2nde = Integer.parseInt(graphA2);
        int C3nde = Integer.parseInt(graphA3);

        //Merah
        if(C1nde < 23)
        {
            C1_nde.setTextColor(Color.RED);
        }
        else {C1_nde.setTextColor(Color.BLACK);}

        if(C2nde < 23)
        {
            C2_nde.setTextColor(Color.RED);
        }
        else {C2_nde.setTextColor(Color.BLACK);}

        if(C3nde < 23)
        {
            C3_nde.setTextColor(Color.RED);
        }
        else {C3_nde.setTextColor(Color.BLACK);}
    }

    public void chartHolderCDE()
    {
        chartHolderA1_de = dialogView.findViewById(R.id.holder_A_de);
        final String graphA1 = C1_de.getText().toString();
        final String graphA2 = C2_de.getText().toString();
        final String graphA3 = C3_de.getText().toString();

        if (graphA1.isEmpty()) {
            C1_de.setError("Isi A");
            C1_de.requestFocus();
        } else if (graphA2.isEmpty()) {
            C2_de.setError("Isi B");
            C2_de.requestFocus();
        } else if (graphA3.isEmpty()) {
            C3_de.setError("Isi C");
            C3_de.requestFocus();
        } else {

            C1_de.setText(graphA1);
            C2_de.setText(graphA2);
            C3_de.setText(graphA3);
            ValueC11 = Float.valueOf(C1_de.getText().toString());
            ValueC12 = Float.valueOf(C2_de.getText().toString());
            ValueC13 = Float.valueOf(C3_de.getText().toString());
            barEntries = new ArrayList<BarEntry>();
            barLabels = new ArrayList<String>();
            barLabels.add("");// index 0 kosongkan saja
            barEntries.add(new BarEntry(1, ValueC11));
            barLabels.add("C1");
            barEntries.add(new BarEntry(2, ValueC12));
            barLabels.add("C2");
            barEntries.add(new BarEntry(3, ValueC13));
            barLabels.add("C3");
            barDataSet = new BarDataSet(barEntries, "Holder C DE");
            barData = new BarData(barDataSet);
            chartHolderA1_de.animateY(1000);
            chartHolderA1_de.getXAxis().setValueFormatter(
                    new IndexAxisValueFormatter(barLabels));
            YAxis yAxis = chartHolderA1_de.getAxisLeft();
            yAxis.setAxisMinimum(0f); // start at zero
            yAxis.setAxisMaximum(60f); // the axis maximum is 60
            LimitLine limitA = new LimitLine(15f, "Minimum Carbon"); //garis batas
            limitA.setLineWidth(1f);
            limitA.setTextSize(5f);
            limitA.setLabelPosition(LimitLine.LimitLabelPosition.LEFT_BOTTOM);
            yAxis.addLimitLine(limitA);
            barDataSet.setColors(ColorTemplate.getHoloBlue());
            chartHolderA1_de.setData(barData);

            //btnDisable.setVisibility(View.GONE);
            //setSave.setVisibility(View.VISIBLE);
            //Toast.makeText(CarbonBrush.this, "Berhasil Menampilkan Data", Toast.LENGTH_SHORT).show();
        }

        //Untuk Warna ketika
        int C1de = Integer.parseInt(graphA1);
        int C2de = Integer.parseInt(graphA2);
        int C3de = Integer.parseInt(graphA3);

        //Merah
        if(C1de < 23)
        {
            C1_de.setTextColor(Color.RED);
        }
        else {C1_de.setTextColor(Color.BLACK);}

        if(C2de < 23)
        {
            C2_de.setTextColor(Color.RED);
        }
        else {C2_de.setTextColor(Color.BLACK);}

        if(C3de < 23)
        {
            C3_de.setTextColor(Color.RED);
        }
        else {C3_de.setTextColor(Color.BLACK);}
    }

    public void chartHolderDNDE()
    {
        chartHolderA1_nde = dialogView.findViewById(R.id.holder_A_nde);
        final String graphA1 = D1_nde.getText().toString();
        final String graphA2 = D2_nde.getText().toString();
        final String graphA3 = D3_nde.getText().toString();

        if (graphA1.isEmpty()) {
            D1_nde.setError("Isi A");
            D1_nde.requestFocus();
        } else if (graphA2.isEmpty()) {
            D2_nde.setError("Isi B");
            D2_nde.requestFocus();
        } else if (graphA3.isEmpty()) {
            D3_nde.setError("Isi C");
            D3_nde.requestFocus();
        } else {

            D1_nde.setText(graphA1);
            D2_nde.setText(graphA2);
            D3_nde.setText(graphA3);
            ValueD1 = Float.valueOf(D1_nde.getText().toString());
            ValueD2 = Float.valueOf(D2_nde.getText().toString());
            ValueD3 = Float.valueOf(D3_nde.getText().toString());
            barEntries = new ArrayList<BarEntry>();
            barLabels = new ArrayList<String>();
            barLabels.add("");// index 0 kosongkan saja
            barEntries.add(new BarEntry(1, ValueD1));
            barLabels.add("D1");
            barEntries.add(new BarEntry(2, ValueD2));
            barLabels.add("D2");
            barEntries.add(new BarEntry(3, ValueD3));
            barLabels.add("D3");
            barDataSet = new BarDataSet(barEntries, "Holder D NDE");
            barData = new BarData(barDataSet);
            chartHolderA1_nde.animateY(1000);
            chartHolderA1_nde.getXAxis().setValueFormatter(
                    new IndexAxisValueFormatter(barLabels));
            YAxis yAxis = chartHolderA1_nde.getAxisLeft();
            yAxis.setAxisMinimum(0f); // start at zero
            yAxis.setAxisMaximum(60f); // the axis maximum is 60
            LimitLine limitA = new LimitLine(15f, "Minimum Carbon"); //garis batas
            limitA.setLineWidth(1f);
            limitA.setTextSize(5f);
            limitA.setLabelPosition(LimitLine.LimitLabelPosition.LEFT_BOTTOM);
            yAxis.addLimitLine(limitA);
            barDataSet.setColors(ColorTemplate.getHoloBlue());
            chartHolderA1_nde.setData(barData);

            //btnDisable.setVisibility(View.GONE);
            //setSave.setVisibility(View.VISIBLE);
            //Toast.makeText(CarbonBrush.this, "Berhasil Menampilkan Data", Toast.LENGTH_SHORT).show();

            prefs = getPreferences(MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(D1NDE_KEY, D1_nde.getText().toString());
            editor.putString(D2NDE_KEY, D2_nde.getText().toString());
            editor.putString(D3NDE_KEY, D3_nde.getText().toString());
            editor.commit();
        }

        //Untuk Warna ketika
        int D1nde = Integer.parseInt(graphA1);
        int D2nde = Integer.parseInt(graphA2);
        int D3nde = Integer.parseInt(graphA3);

        //Merah
        if(D1nde < 23)
        {
            D1_nde.setTextColor(Color.RED);
        }
        else {D1_nde.setTextColor(Color.BLACK);}

        if(D2nde < 23)
        {
            D2_nde.setTextColor(Color.RED);
        }
        else {D2_nde.setTextColor(Color.BLACK);}

        if(D3nde < 23)
        {
            D3_nde.setTextColor(Color.RED);
        }
        else {D3_nde.setTextColor(Color.BLACK);}
    }

    public void chartHolderDDE()
    {
        chartHolderA1_de = dialogView.findViewById(R.id.holder_A_de);
        final String graphA1 = D1_de.getText().toString();
        final String graphA2 = D2_de.getText().toString();
        final String graphA3 = D3_de.getText().toString();

        if (graphA1.isEmpty()) {
            D1_de.setError("Isi A");
            D1_de.requestFocus();
        } else if (graphA2.isEmpty()) {
            D2_de.setError("Isi B");
            D2_de.requestFocus();
        } else if (graphA3.isEmpty()) {
            D3_de.setError("Isi C");
            D3_de.requestFocus();
        } else {

            D1_de.setText(graphA1);
            D2_de.setText(graphA2);
            D3_de.setText(graphA3);
            ValueD11 = Float.valueOf(D1_de.getText().toString());
            ValueD12 = Float.valueOf(D2_de.getText().toString());
            ValueD13 = Float.valueOf(D3_de.getText().toString());
            barEntries = new ArrayList<BarEntry>();
            barLabels = new ArrayList<String>();
            barLabels.add("");// index 0 kosongkan saja
            barEntries.add(new BarEntry(1, ValueD11));
            barLabels.add("D1");
            barEntries.add(new BarEntry(2, ValueD12));
            barLabels.add("D2");
            barEntries.add(new BarEntry(3, ValueD13));
            barLabels.add("D3");
            barDataSet = new BarDataSet(barEntries, "Holder D DE");
            barData = new BarData(barDataSet);
            chartHolderA1_de.animateY(1000);
            chartHolderA1_de.getXAxis().setValueFormatter(
                    new IndexAxisValueFormatter(barLabels));
            YAxis yAxis = chartHolderA1_de.getAxisLeft();
            yAxis.setAxisMinimum(0f); // start at zero
            yAxis.setAxisMaximum(60f); // the axis maximum is 60
            LimitLine limitA = new LimitLine(15f, "Minimum Carbon"); //garis batas
            limitA.setLineWidth(1f);
            limitA.setTextSize(5f);
            limitA.setLabelPosition(LimitLine.LimitLabelPosition.LEFT_BOTTOM);
            yAxis.addLimitLine(limitA);
            barDataSet.setColors(ColorTemplate.getHoloBlue());
            chartHolderA1_de.setData(barData);

            //btnDisable.setVisibility(View.GONE);
            //setSave.setVisibility(View.VISIBLE);
            //Toast.makeText(CarbonBrush.this, "Berhasil Menampilkan Data", Toast.LENGTH_SHORT).show();

        }

        //Untuk Warna ketika
        int D1de = Integer.parseInt(graphA1);
        int D2de = Integer.parseInt(graphA2);
        int D3de = Integer.parseInt(graphA3);

        //Merah
        if(D1de < 23)
        {
            D1_de.setTextColor(Color.RED);
        }
        else {D1_de.setTextColor(Color.BLACK);}

        if(D2de < 23)
        {
            D2_de.setTextColor(Color.RED);
        }
        else {D2_de.setTextColor(Color.BLACK);}

        if(D3de < 23)
        {
            D3_de.setTextColor(Color.RED);
        }
        else {D3_de.setTextColor(Color.BLACK);}
    }

    public void chartHolderENDE()
    {
        chartHolderA1_nde = dialogView.findViewById(R.id.holder_A_nde);
        final String graphA1 = E1_nde.getText().toString();
        final String graphA2 = E2_nde.getText().toString();
        final String graphA3 = E3_nde.getText().toString();

        if (graphA1.isEmpty()) {
            E1_nde.setError("Isi A");
            E1_nde.requestFocus();
        } else if (graphA2.isEmpty()) {
            E2_nde.setError("Isi B");
            E2_nde.requestFocus();
        } else if (graphA3.isEmpty()) {
            E3_nde.setError("Isi C");
            E3_nde.requestFocus();
        } else {

            E1_nde.setText(graphA1);
            E2_nde.setText(graphA2);
            E3_nde.setText(graphA3);
            ValueE1 = Float.valueOf(E1_nde.getText().toString());
            ValueE2 = Float.valueOf(E2_nde.getText().toString());
            ValueE3 = Float.valueOf(E3_nde.getText().toString());
            barEntries = new ArrayList<BarEntry>();
            barLabels = new ArrayList<String>();
            barLabels.add("");// index 0 kosongkan saja
            barEntries.add(new BarEntry(1, ValueE1));
            barLabels.add("E1");
            barEntries.add(new BarEntry(2, ValueE2));
            barLabels.add("E2");
            barEntries.add(new BarEntry(3, ValueE3));
            barLabels.add("E3");
            barDataSet = new BarDataSet(barEntries, "Holder E NDE");
            barData = new BarData(barDataSet);
            chartHolderA1_nde.animateY(1000);
            chartHolderA1_nde.getXAxis().setValueFormatter(
                    new IndexAxisValueFormatter(barLabels));
            YAxis yAxis = chartHolderA1_nde.getAxisLeft();
            yAxis.setAxisMinimum(0f); // start at zero
            yAxis.setAxisMaximum(60f); // the axis maximum is 60
            LimitLine limitA = new LimitLine(15f, "Minimum Carbon"); //garis batas
            limitA.setLineWidth(1f);
            limitA.setTextSize(5f);
            limitA.setLabelPosition(LimitLine.LimitLabelPosition.LEFT_BOTTOM);
            yAxis.addLimitLine(limitA);
            barDataSet.setColors(ColorTemplate.getHoloBlue());
            chartHolderA1_nde.setData(barData);

            //btnDisable.setVisibility(View.GONE);
            //setSave.setVisibility(View.VISIBLE);
            //Toast.makeText(CarbonBrush.this, "Berhasil Menampilkan Data", Toast.LENGTH_SHORT).show();
        }

        //Untuk Warna ketika
        int E1nde = Integer.parseInt(graphA1);
        int E2nde = Integer.parseInt(graphA2);
        int E3nde = Integer.parseInt(graphA3);

        //Merah
        if(E1nde < 23)
        {
            E1_nde.setTextColor(Color.RED);
        }
        else {E1_nde.setTextColor(Color.BLACK);}

        if(E2nde < 23)
        {
            E2_nde.setTextColor(Color.RED);
        }
        else {E2_nde.setTextColor(Color.BLACK);}

        if(E3nde < 23)
        {
            E3_nde.setTextColor(Color.RED);
        }
        else {E3_nde.setTextColor(Color.BLACK);}
    }

    public void chartHolderEDE()
    {
        chartHolderA1_de = dialogView.findViewById(R.id.holder_A_de);
        final String graphA1 = E1_de.getText().toString();
        final String graphA2 = E2_de.getText().toString();
        final String graphA3 = E3_de.getText().toString();

        if (graphA1.isEmpty()) {
            E1_de.setError("Isi A");
            E1_de.requestFocus();
        } else if (graphA2.isEmpty()) {
            E2_de.setError("Isi B");
            E2_de.requestFocus();
        } else if (graphA3.isEmpty()) {
            E3_de.setError("Isi C");
            E3_de.requestFocus();
        } else {

            E1_de.setText(graphA1);
            E2_de.setText(graphA2);
            E3_de.setText(graphA3);
            ValueE11 = Float.valueOf(E1_de.getText().toString());
            ValueE12 = Float.valueOf(E2_de.getText().toString());
            ValueE13 = Float.valueOf(E3_de.getText().toString());
            barEntries = new ArrayList<BarEntry>();
            barLabels = new ArrayList<String>();
            barLabels.add("");// index 0 kosongkan saja
            barEntries.add(new BarEntry(1, ValueE11));
            barLabels.add("E1");
            barEntries.add(new BarEntry(2, ValueE12));
            barLabels.add("E2");
            barEntries.add(new BarEntry(3, ValueE13));
            barLabels.add("E3");
            barDataSet = new BarDataSet(barEntries, "Holder E DE");
            barData = new BarData(barDataSet);
            chartHolderA1_de.animateY(1000);
            chartHolderA1_de.getXAxis().setValueFormatter(
                    new IndexAxisValueFormatter(barLabels));
            YAxis yAxis = chartHolderA1_de.getAxisLeft();
            yAxis.setAxisMinimum(0f); // start at zero
            yAxis.setAxisMaximum(60f); // the axis maximum is 60
            LimitLine limitA = new LimitLine(15f, "Minimum Carbon"); //garis batas
            limitA.setLineWidth(1f);
            limitA.setTextSize(5f);
            limitA.setLabelPosition(LimitLine.LimitLabelPosition.LEFT_BOTTOM);
            yAxis.addLimitLine(limitA);
            barDataSet.setColors(ColorTemplate.getHoloBlue());
            chartHolderA1_de.setData(barData);

            //btnDisable.setVisibility(View.GONE);
            //setSave.setVisibility(View.VISIBLE);
            //Toast.makeText(CarbonBrush.this, "Berhasil Menampilkan Data", Toast.LENGTH_SHORT).show();
        }

        //Untuk Warna ketika
        int E1de = Integer.parseInt(graphA1);
        int E2de = Integer.parseInt(graphA2);
        int E3de = Integer.parseInt(graphA3);

        //Merah
        if(E1de < 23)
        {
            E1_de.setTextColor(Color.RED);
        }
        else {E1_de.setTextColor(Color.BLACK);}

        if(E2de < 23)
        {
            E2_de.setTextColor(Color.RED);
        }
        else {E2_de.setTextColor(Color.BLACK);}

        if(E3de < 23)
        {
            E3_de.setTextColor(Color.RED);
        }
        else {E3_de.setTextColor(Color.BLACK);}
    }

    public void CreatedPDF()
    {
        //Image ttd tangan OPS
        String ImageFileOPS = DIRECTORY+"ttdOperatorPMgen.jpg";
        Bitmap bitmapImageOPS = BitmapFactory.decodeFile(ImageFileOPS);
        //Image ttd tangan SPV
        String ImageFileSPV = DIRECTORY+"ttdSPVPMgen.jpg";
        Bitmap bitmapImageSPV = BitmapFactory.decodeFile(ImageFileSPV);

        //Photo Before, Process, After
        String ImageBefore  = DIRECTORY_PHOTO+"PmGenerator_"+namaGT.getText().toString()+"_Before.jpeg";
        Bitmap bitmapImageBfr = BitmapFactory.decodeFile(ImageBefore);
        String ImageProcess = DIRECTORY_PHOTO+"PmGenerator_"+namaGT.getText().toString()+"_Process.jpeg";
        Bitmap bitmapImagePro = BitmapFactory.decodeFile(ImageProcess);
        String ImageAfter   = DIRECTORY_PHOTO+"PmGenerator_"+namaGT.getText().toString()+"_After.jpeg";
        Bitmap bitmapImageAft = BitmapFactory.decodeFile(ImageAfter);

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

        // Garis NO Document
        canvas.drawLine(905, 90, 1170, 90, paint);
        canvas.drawLine(905, 160, 1170, 160, paint);
        canvas.drawLine(905, 230, 1170, 230, paint);

        //Holder A
        canvas.drawLine(150, 420, 380, 420, paint);
        canvas.drawLine(265,420, 265, 600, paint);
        paint.setColor(Color.BLACK);
        paint.setTextSize(30f);
        canvas.drawText("NDE",180, 450, paint);
        canvas.drawText(A1_nde.getText().toString(),190, 490, paint);
        canvas.drawText(A2_nde.getText().toString(),190, 530, paint);
        canvas.drawText(A3_nde.getText().toString(),190, 570, paint);
        canvas.drawText("DE",300, 450, paint);
        canvas.drawText(A1_de.getText().toString(),300, 490, paint);
        canvas.drawText(A2_de.getText().toString(),300, 530, paint);
        canvas.drawText(A3_de.getText().toString(),300, 570, paint);

        //Holder B
        canvas.drawLine(490, 420, 720, 420, paint);
        canvas.drawLine(605,420, 605, 600, paint);
        canvas.drawText("NDE",520, 450, paint);
        canvas.drawText(B1_nde.getText().toString(),530, 490, paint);
        canvas.drawText(B2_nde.getText().toString(),530, 530, paint);
        canvas.drawText(B3_nde.getText().toString(),530, 570, paint);
        canvas.drawText("DE",640, 450, paint);
        canvas.drawText(B1_de.getText().toString(),640, 490, paint);
        canvas.drawText(B2_de.getText().toString(),640, 530, paint);
        canvas.drawText(B3_de.getText().toString(),640, 570, paint);

        //Holder C
        canvas.drawLine(835, 420, 1065, 420, paint);
        canvas.drawLine(950,420, 950, 600, paint);
        canvas.drawText("NDE",865, 450, paint);
        canvas.drawText(C1_nde.getText().toString(),875, 490, paint);
        canvas.drawText(C2_nde.getText().toString(),875, 530, paint);
        canvas.drawText(C3_nde.getText().toString(),875, 570, paint);
        canvas.drawText("DE",985, 450, paint);
        canvas.drawText(C1_de.getText().toString(),985, 490, paint);
        canvas.drawText(C2_de.getText().toString(),985, 530, paint);
        canvas.drawText(C3_de.getText().toString(),985, 570, paint);

        //Holder D
        canvas.drawLine(150, 670, 380, 670, paint);
        canvas.drawLine(265,670, 265, 850, paint);
        canvas.drawText("NDE",180, 700, paint);
        canvas.drawText(D1_nde.getText().toString(),190, 740, paint);
        canvas.drawText(D2_nde.getText().toString(),190, 780, paint);
        canvas.drawText(D3_nde.getText().toString(),190, 820, paint);
        canvas.drawText("DE",300, 700, paint);
        canvas.drawText(D1_de.getText().toString(),300, 740, paint);
        canvas.drawText(D2_de.getText().toString(),300, 780, paint);
        canvas.drawText(D3_de.getText().toString(),300, 820, paint);

        //Holder E
        canvas.drawLine(490, 670, 720, 670, paint);
        canvas.drawLine(605,670, 605, 850, paint);
        canvas.drawText("NDE",520, 700, paint);
        canvas.drawText(E1_nde.getText().toString(),530, 740, paint);
        canvas.drawText(E2_nde.getText().toString(),530, 780, paint);
        canvas.drawText(E3_nde.getText().toString(),530, 820, paint);
        canvas.drawText("DE",640, 700, paint);
        canvas.drawText(E1_de.getText().toString(),640, 740, paint);
        canvas.drawText(E2_de.getText().toString(),640, 780, paint);
        canvas.drawText(E3_de.getText().toString(),640, 820, paint);

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
        canvas.drawText("PEMELIHARAAN GENERATOR GT", 1170 / 2, 195, titlePaint);

        titlePaint.setTextSize(18f);
        titlePaint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("Nomor Dokumen : FMT-17.2.1", 912,65, titlePaint);
        canvas.drawText("Revisi : 00", 912,135, titlePaint);
        canvas.drawText("Tanggal Terbit : 20-08-2013", 912,195, titlePaint);

        // Nama Preventive GT dan Tanggal
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setColor(Color.BLACK);
        paint.setTextSize(30f);
        canvas.drawText(judulPM.getText().toString(), 40, 270, paint ); // Judul PM
        canvas.drawText(NoWO.getText().toString(),40,300, paint); // No WO
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setColor(Color.BLACK);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        paint.setTextSize(30);
        canvas.drawText(namaGT.getText().toString(), 1170/2, 270, paint); // Nama GT
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
        canvas.drawText("- Carbon Brush", 50, 350, paint);
        canvas.drawText("- Auxiliary", 50 ,900, paint);
        canvas.drawText("- Catatan - catatan", 50, 1500, paint);
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("Holder A", 200, 400, paint);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("Holder B", 610, 400, paint);
        paint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText("Holder C", 1170-150, 400, paint);
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("Holder D", 200, 650, paint);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("Holder E", 610, 650, paint);

        paint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("1. Kebersihan Filter",100,950,paint);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        canvas.drawText("- Filter Inlet",150,1000,paint);
        canvas.drawText("- Filter Outlet",150,1050,paint);
        canvas.drawText("- Filter Sarang",150,1100,paint);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText("2. Shaft Ground Brush",100,1150,paint);
        canvas.drawText("3. Penerangan",100,1200,paint);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        canvas.drawText("- Jumlah lampu mati",150,1250,paint);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText("4. Lain-lain",100,1300,paint);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        canvas.drawText("- Bocoran Minyak",150,1350,paint);
        canvas.drawText("- Kebersihan",150,1400,paint);

        // Titik 2
        canvas.drawText(":",500,1000,paint);
        canvas.drawText(":",500,1050,paint);
        canvas.drawText(":",500,1100,paint);
        canvas.drawText(":",500,1150,paint);
        canvas.drawText(":",500,1250,paint);
        canvas.drawText(":",500,1350,paint);
        canvas.drawText(":",500,1400,paint);

        // Isi Aux
        canvas.drawText(InletFilter.getText().toString(),550,1000,paint);
        canvas.drawText(OutletFilter.getText().toString(),550,1050,paint);
        canvas.drawText(FilterSarang.getText().toString(),550,1100,paint);
        canvas.drawText(BrushGear.getText().toString(),550,1150,paint);
        canvas.drawText(LampuMati.getText().toString(),550,1250,paint);
        canvas.drawText(CecerMinyak.getText().toString(),550,1350,paint);
        canvas.drawText(Kebersihan.getText().toString(),550,1400,paint);

        canvas.drawText("1. "+Catatan1.getText().toString(),100, 1550, paint);
        canvas.drawText("2. "+Catatan2.getText().toString(), 100, 1650, paint);



        dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        pdfDocument.finishPage(page);
        File file = new File(Environment.getExternalStorageDirectory(), "/"+judulPM.getText().toString()+" "+namaGT.getText().toString()+" "+dateFormat.format(dateTime)+".pdf");
        try {
            pdfDocument.writeTo(new FileOutputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }

        pdfDocument.close();
        Toast.makeText(PmGenerator.this, "PDF has Created", Toast.LENGTH_LONG).show();


        dateTime = new Date();
    }

    public void signatureDigitalOPS()
    {
        dialogCustomTTD = new AlertDialog.Builder(PmGenerator.this);
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
            File f = new File(wallpaperDirectory,"ttdOperatorPMgen.jpg"); //Calendar.getInstance().getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(PmGenerator.this,
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
        dialogCustomTTD = new AlertDialog.Builder(PmGenerator.this);
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
            MediaScannerConnection.scanFile(PmGenerator.this,
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

 /*   @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1 :
                if(resultCode == Activity.RESULT_OK)
                {
                    // result code sama, save gambar ke bitmap
                    Bitmap bitmapBefore;
                    bitmapBefore = (Bitmap) data.getExtras().get("data");
                    imgBefore.setImageBitmap(bitmapBefore);
                    saveImageBefore(bitmapBefore);
                    Toast.makeText(this, "Saved Successfully", Toast.LENGTH_SHORT).show();
                }
            break;

            case 2 :
                if(requestCode == Activity.RESULT_OK)
                {
                    // result code sama, save gambar ke bitmap
                    Bitmap bitmapProcess;
                    bitmapProcess = (Bitmap) data.getExtras().get("data");
                    imgProcess.setImageBitmap(bitmapProcess);
                    saveImageProcess(bitmapProcess);
                    Toast.makeText(this, "Saved Successfully", Toast.LENGTH_SHORT).show();

                }
             break;

            case 3 :
                if(requestCode == Activity.RESULT_OK)
                {
                    // result code sama, save gambar ke bitmap
                    Bitmap bitmapAfter;
                    bitmapAfter = (Bitmap) data.getExtras().get("data");
                    imgAfter.setImageBitmap(bitmapAfter);
                    saveImageAfter(bitmapAfter);
                    Toast.makeText(this, "Saved Successfully", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void saveImageBefore(Bitmap finalBitmap)
    {
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/Preventive/");
        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "ImageBefore.jpg";
        File file = new File (myDir, fname);
        if (file.exists ()) file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveImageProcess(Bitmap finalBitmap)
    {
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/Preventive/");
        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "ImageProcess.jpg";
        File file = new File (myDir, fname);
        if (file.exists ()) file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveImageAfter(Bitmap finalBitmap)
    {
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/Preventive/");
        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "ImageAfter.jpg";
        File file = new File (myDir, fname);
        if (file.exists ()) file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    } */

    private void restoreFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(KEY_IMAGE_STORAGE_PATH)) {
                imageStoragePath = savedInstanceState.getString(KEY_IMAGE_STORAGE_PATH);
                if (!TextUtils.isEmpty(imageStoragePath)) {
                    if (imageStoragePath.substring(imageStoragePath.lastIndexOf(".")).equals("." + IMAGE_EXTENSION)) {
                        previewCapturedImageBefore();
                    }
                    else if (imageStoragePath.substring(imageStoragePath.lastIndexOf(".")).equals("." + IMAGE_EXTENSION)) {
                        previewCapturedImageProcess();
                    }
                    else if (imageStoragePath.substring(imageStoragePath.lastIndexOf(".")).equals("." + IMAGE_EXTENSION)) {
                        previewCapturedImageAfter();
                    }
                }
            }
        }
    }

    private void requestCameraPermission(final int type) {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.RECORD_AUDIO)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {

                            if (type == EVIDENCE_BEFORE) {
                                // capture picture
                                captureImageBefore();
                            }
                             else if (type == EVIDENCE_PROCESS) {
                                // capture picture
                                captureImageProcess();
                            }
                            else if (type == EVIDENCE_AFTER) {
                                // capture picture
                                captureImageAfter();
                            }
                        } else if (report.isAnyPermissionPermanentlyDenied()) {
                            showPermissionsAlert();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<com.karumi.dexter.listener.PermissionRequest> permissions, PermissionToken token) {

                    }
                }).check();
    }

    private void captureImageBefore() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File file = CameraUtils.getOutputMediaFile(EVIDENCE_BEFORE);
        if (file != null) {
            imageStoragePath = file.getAbsolutePath();
        }

        Uri fileUri = CameraUtils.getOutputMediaFileUri(getApplicationContext(), file);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // start the image capture Intent
        startActivityForResult(intent, CAMERA_REQUEST_CODE_BEFORE);
    }

    private void captureImageProcess() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File file = CameraUtils.getOutputMediaFile(EVIDENCE_PROCESS);
        if (file != null) {
            imageStoragePath = file.getAbsolutePath();
        }

        Uri fileUri = CameraUtils.getOutputMediaFileUri(getApplicationContext(), file);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // start the image capture Intent
        startActivityForResult(intent, CAMERA_REQUEST_CODE_PROCESS);
    }

    private void captureImageAfter() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File file = CameraUtils.getOutputMediaFile(EVIDENCE_AFTER);
        if (file != null) {
            imageStoragePath = file.getAbsolutePath();
        }

        Uri fileUri = CameraUtils.getOutputMediaFileUri(getApplicationContext(), file);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // start the image capture Intent
        startActivityForResult(intent, CAMERA_REQUEST_CODE_AFTER);
    }

    /**
     * Saving stored image path to saved instance state
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save file url in bundle as it will be null on screen orientation
        // changes
        outState.putString(KEY_IMAGE_STORAGE_PATH, imageStoragePath);
    }

    /**
     * Restoring image path from saved instance state
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // get the file url
        imageStoragePath = savedInstanceState.getString(KEY_IMAGE_STORAGE_PATH);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE_BEFORE) {
            if (resultCode == RESULT_OK) {
                // Refreshing the gallery
                CameraUtils.refreshGallery(getApplicationContext(), imageStoragePath);
                Toast.makeText(this, "Berhasil", Toast.LENGTH_SHORT).show();
                // successfully captured the image
                // display it in image view
                previewCapturedImageBefore();
            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled Image capture
                Toast.makeText(getApplicationContext(),
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();
            } else {
                // failed to capture image
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }
        } else if (requestCode == CAMERA_REQUEST_CODE_PROCESS) {
            if (resultCode == RESULT_OK) {
                // Refreshing the gallery
                CameraUtils.refreshGallery(getApplicationContext(), imageStoragePath);
                Toast.makeText(this, "Berhasil", Toast.LENGTH_SHORT).show();
                // successfully captured the image
                // display it in image view
                previewCapturedImageProcess();
            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled Image capture
                Toast.makeText(getApplicationContext(),
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();
            } else {
                // failed to capture image
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }
        } else if (requestCode == CAMERA_REQUEST_CODE_AFTER) {
            if (resultCode == RESULT_OK) {
                // Refreshing the gallery
                CameraUtils.refreshGallery(getApplicationContext(), imageStoragePath);
                Toast.makeText(this, "Berhasil", Toast.LENGTH_SHORT).show();
                // successfully captured the image
                // display it in image view
                previewCapturedImageAfter();
            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled Image capture
                Toast.makeText(getApplicationContext(),
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();
            } else {
                // failed to capture image
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

        private void previewCapturedImageBefore() {
            try {
                // hide video preview

                imgBefore.setVisibility(View.VISIBLE);

                Bitmap bitmap = CameraUtils.optimizeBitmap(BITMAP_SAMPLE_SIZE, imageStoragePath);

                imgBefore.setImageBitmap(bitmap);

            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }

    private void previewCapturedImageProcess() {
        try {
            // hide video preview

            imgProcess.setVisibility(View.VISIBLE);

            Bitmap bitmap = CameraUtils.optimizeBitmap(BITMAP_SAMPLE_SIZE, imageStoragePath);

            imgProcess.setImageBitmap(bitmap);

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void previewCapturedImageAfter() {
        try {
            // hide video preview

            imgAfter.setVisibility(View.VISIBLE);

            Bitmap bitmap = CameraUtils.optimizeBitmap(BITMAP_SAMPLE_SIZE, imageStoragePath);

            imgAfter.setImageBitmap(bitmap);

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void showPermissionsAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissions required!")
                .setMessage("Camera needs few permissions to work properly. Grant them in settings.")
                .setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        CameraUtils.openSettings(PmGenerator.this);
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }

    public void getLocation()
    {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,5000,5,this);
        }
        catch(SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        gpS.setText("Latitude: " + location.getLatitude() + "\nLongitude: " + location.getLongitude());

        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            gpS.setText(gpS.getText() + "\n"+addresses.get(0).getAddressLine(0)+", "+
                    addresses.get(0).getAddressLine(1)+", "+addresses.get(0).getAddressLine(2));
        }catch(Exception e)
        {

        }

    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(PmGenerator.this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

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
