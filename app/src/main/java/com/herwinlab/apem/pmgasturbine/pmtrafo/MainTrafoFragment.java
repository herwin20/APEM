package com.herwinlab.apem.pmgasturbine.pmtrafo;

import android.app.Notification;
import android.content.Context;
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
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.herwinlab.apem.R;
import com.herwinlab.apem.notification.NotificationUtils;
import com.herwinlab.apem.pmgasturbine.PmBattery12V;
import com.kyanogen.signatureview.SignatureView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;

public class MainTrafoFragment extends Fragment {

    public static final String MainTrafo = "maintrafo";

    //Notifikasi
    private NotificationUtils mNotificationUtils;

    //Custom Dialog
    AlertDialog.Builder dialogCustom, dialogCustomTTD, dialogCustomPDF;
    LayoutInflater inflater, inflaterTTD, inflaterPDF;
    View dialogView, dialogViewTTD, view, viewTTD, dialogViewPDF;

    //Digital Signature
    SignatureView signatureView;
    String path;
    private static final String IMAGE_DIRECTORY = "/Signature";

    //CheckBox
    public CheckBox ChkBoxSilicaGT11, ChkOilGT11, ChkFanGT11, ChkGroundGT11, ChkBushingGT11, ChkSumppumpGT11; // GT 11
    public CheckBox ChkBoxSilicaGT13, ChkOilGT13, ChkFanGT13, ChkGroundGT13, ChkBushingGT13, ChkSumppumpGT13; // GT 13
    public CheckBox ChkBoxSilicaGT21, ChkOilGT21, ChkFanGT21, ChkGroundGT21, ChkBushingGT21, ChkSumppumpGT21; // GT 13
    //TextInputEditText
    public TextInputEditText EditOilTempGT11, EditWindingGT11, EditOillevelGT11, EditKeteranganGT11;
    public TextInputEditText EditOilTempGT13, EditWindingGT13, EditOillevelGT13, EditKeteranganGT13;
    public TextInputEditText EditOilTempGT21, EditWindingGT21, EditOillevelGT21, EditKeteranganGT21;
    //TextView
    public TextView ketSilicaGT11, ketOilGT11, ketFanGT11, ketGroundGT11, ketBushingGT11, ketSumppumpGT11; // GT 11
    public TextView ketSilicaGT13, ketOilGT13, ketFanGT13, ketGroundGT13, ketBushingGT13, ketSumppumpGT13; // GT 13
    public TextView ketSilicaGT21, ketOilGT21, ketFanGT21, ketGroundGT21, ketBushingGT21, ketSumppumpGT21; // GT 21

    //LinearButton
    public LinearLayout savedButtonGT11, savedButtonGT13, savedButtonGT21;
    // Button Linear Layout
    public LinearLayout btnSignOps, btnCreatedPDF, dialogCreatedPDF, btnCalculation, btnSignSPV;
    //PDF
    Bitmap bitmap, scaleBitmap, PJBscale, IPJBway, accuImg, bitmapTTD, turbineImg;
    int pageWidth = 1200;
    Date dateTime;
    DateFormat dateFormat;

    //Edit Text For Noted, Aux, Information
    public TextView JudulPmBatt, Hasil;
    public EditText Catatan1, Catatan2;
    public EditText orangPm1, orangPm2, orangPm3, Operator, NoWO, namaGT, namaPDF;

    public SharedPreferences pref;
    //GT 11
    private final String OilTempGT11_KEY = "OilTempGT11";
    private final String WindingTempGT11_KEY = "WindingTempGT11";
    private final String OilLevel_KEY = "OilLevelGT11";
    private final String KeteranganGT11_KEY = "KeteranganGT11";
    private final String SilicaGelGT11_KEY = "SilicaGelGT11";
    private final String OilGT11_KEY = "OilGT11";
    private final String FanGT11_KEY = "FanGT11";
    private final String GroundGT11_KEY = "GroundGT11";
    private final String BushingGT11_KEY = "BushingGT11";
    private final String SumppumpGT11_KEY = "SumppumpGT11";
    //GT 13
    private final String OilTempGT13_KEY = "OilTempGT13";
    private final String WindingTempGT13_KEY = "WindingTempGT13";
    private final String OilLevel13_KEY = "OilLevelGT13";
    private final String KeteranganGT13_KEY = "KeteranganGT13";
    private final String SilicaGelGT13_KEY = "SilicaGelGT13";
    private final String OilGT13_KEY = "OilGT13";
    private final String FanGT13_KEY = "FanGT13";
    private final String GroundGT13_KEY = "GroundGT13";
    private final String BushingGT13_KEY = "BushingGT13";
    private final String SumppumpGT13_KEY = "SumppumpGT13";
    //GT 21
    private final String OilTempGT21_KEY = "OilTempGT21";
    private final String WindingTempGT21_KEY = "WindingTempGT21";
    private final String OilLevel21_KEY = "OilLevelGT21";
    private final String KeteranganGT21_KEY = "KeteranganGT21";
    private final String SilicaGelGT21_KEY = "SilicaGelGT21";
    private final String OilGT21_KEY = "OilGT21";
    private final String FanGT21_KEY = "FanGT21";
    private final String GroundGT21_KEY = "GroundGT21";
    private final String BushingGT21_KEY = "BushingGT21";
    private final String SumppumpGT21_KEY = "SumppumpGT21";

    private final String ORANG1_KEY = "ORANG1";
    private final String ORANG2_KEY = "ORANG2";
    private final String ORANG3_KEY = "ORANG3";
    private final String OPERATOR_KEY = "OPERATOR";

    //Button
    public Button mClear, mGetSign, mCancel;

    // Untuk Handler Tanggal dan Waktu
    private final Handler handler = new Handler();

    // Creating Separate Directory for Signature and Image
    String DIRECTORY = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Signature/";

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_maintrafo, container, false);

        //TextView
        ketSilicaGT11 = v.findViewById(R.id.silicaGelGT11);
        ketOilGT11 = v.findViewById(R.id.OilGlassGt11);
        ketFanGT11 = v.findViewById(R.id.fanGt11);
        ketGroundGT11 = v.findViewById(R.id.groundGt11);
        ketBushingGT11 = v.findViewById(R.id.bushingGt11);
        ketSumppumpGT11 = v.findViewById(R.id.sumpPumpGt11);
        //==================================================================================
        ketSilicaGT13 = v.findViewById(R.id.silicaGelGT13);
        ketOilGT13 = v.findViewById(R.id.OilGlassGt13);
        ketFanGT13 = v.findViewById(R.id.fanGt13);
        ketGroundGT13 = v.findViewById(R.id.groundGt13);
        ketBushingGT13 = v.findViewById(R.id.bushingGt13);
        ketSumppumpGT13 = v.findViewById(R.id.sumpPumpGt13);
        //==================================================================================
        ketSilicaGT21 = v.findViewById(R.id.silicaGelGT21);
        ketOilGT21 = v.findViewById(R.id.OilGlassGt21);
        ketFanGT21 = v.findViewById(R.id.fanGt21);
        ketGroundGT21 = v.findViewById(R.id.groundGt21);
        ketBushingGT21 = v.findViewById(R.id.bushingGt21);
        ketSumppumpGT21 = v.findViewById(R.id.sumpPumpGt21);
        //CheckBox
        ChkBoxSilicaGT11 = v.findViewById(R.id.checkSilicaGt11);
        ChkOilGT11 = v.findViewById(R.id.checkOilGt11);
        ChkFanGT11 = v.findViewById(R.id.checkFanGt11);
        ChkGroundGT11 = v.findViewById(R.id.checkGroundGt11);
        ChkBushingGT11 = v.findViewById(R.id.checkBushingGt11);
        ChkSumppumpGT11 = v.findViewById(R.id.checkSumppumpGt11);

        ChkBoxSilicaGT13 = v.findViewById(R.id.checkSilicaGt13);
        ChkOilGT13 = v.findViewById(R.id.checkOilGt13);
        ChkFanGT13 = v.findViewById(R.id.checkFanGt13);
        ChkGroundGT13 = v.findViewById(R.id.checkGroundGt13);
        ChkBushingGT13 = v.findViewById(R.id.checkBushingGt13);
        ChkSumppumpGT13 = v.findViewById(R.id.checkSumppumpGt13);

        ChkBoxSilicaGT21 = v.findViewById(R.id.checkSilicaGt21);
        ChkOilGT21 = v.findViewById(R.id.checkOilGt21);
        ChkFanGT21 = v.findViewById(R.id.checkFanGt21);
        ChkGroundGT21 = v.findViewById(R.id.checkGroundGt21);
        ChkBushingGT21 = v.findViewById(R.id.checkBushingGt21);
        ChkSumppumpGT21 = v.findViewById(R.id.checkSumppumpGt21);

        //TextEdit
        EditOilTempGT11 = v.findViewById(R.id.oiltemp);
        EditWindingGT11 = v.findViewById(R.id.winding);
        EditOillevelGT11 = v.findViewById(R.id.indikatorlevel);
        EditKeteranganGT11 = v.findViewById(R.id.keteranganGT11);

        EditOilTempGT13 = v.findViewById(R.id.oiltempgt13);
        EditWindingGT13 = v.findViewById(R.id.windinggt13);
        EditOillevelGT13 = v.findViewById(R.id.indikatorlevelgt13);
        EditKeteranganGT13 = v.findViewById(R.id.keteranganGT13);

        EditOilTempGT21 = v.findViewById(R.id.oiltempgt21);
        EditWindingGT21 = v.findViewById(R.id.windinggt21);
        EditOillevelGT21 = v.findViewById(R.id.indikatorlevelgt21);
        EditKeteranganGT21 = v.findViewById(R.id.keteranganGT21);
        //LinearButton
        savedButtonGT11 = v.findViewById(R.id.buttonSavedGT11);
        savedButtonGT13 = v.findViewById(R.id.buttonSavedGT13);
        savedButtonGT21 = v.findViewById(R.id.buttonSavedGT21);

        orangPm1 = v.findViewById(R.id.nama_pm1Trafo);
        orangPm2 = v.findViewById(R.id.nama_pm2Trafo);
        orangPm3 = v.findViewById(R.id.nama_pm3Trafo);
        Operator = v.findViewById(R.id.nama_operatorTrafo);

        //SharePreference TextVIew
        pref = this.getActivity().getSharedPreferences(MainTrafo, Context.MODE_PRIVATE);
        ketSilicaGT11.setText(pref.getString(SilicaGelGT11_KEY, ""));
        ketOilGT11.setText(pref.getString(OilGT11_KEY, ""));
        ketFanGT11.setText(pref.getString(FanGT11_KEY, ""));
        ketGroundGT11.setText(pref.getString(GroundGT11_KEY, ""));
        ketBushingGT11.setText(pref.getString(BushingGT11_KEY, ""));
        ketSumppumpGT11.setText(pref.getString(SumppumpGT11_KEY, ""));
        EditOilTempGT11.setText(pref.getString(OilTempGT11_KEY, ""));
        EditWindingGT11.setText(pref.getString(WindingTempGT11_KEY, ""));
        EditOillevelGT11.setText(pref.getString(OilLevel_KEY, ""));
        EditKeteranganGT11.setText(pref.getString(KeteranganGT11_KEY, ""));

        ketSilicaGT13.setText(pref.getString(SilicaGelGT13_KEY, ""));
        ketOilGT13.setText(pref.getString(OilGT13_KEY, ""));
        ketFanGT13.setText(pref.getString(FanGT13_KEY, ""));
        ketGroundGT13.setText(pref.getString(GroundGT13_KEY, ""));
        ketBushingGT13.setText(pref.getString(BushingGT13_KEY, ""));
        ketSumppumpGT13.setText(pref.getString(SumppumpGT13_KEY, ""));
        EditOilTempGT13.setText(pref.getString(OilTempGT13_KEY, ""));
        EditWindingGT13.setText(pref.getString(WindingTempGT13_KEY, ""));
        EditOillevelGT13.setText(pref.getString(OilLevel13_KEY, ""));
        EditKeteranganGT13.setText(pref.getString(KeteranganGT13_KEY, ""));

        ketSilicaGT21.setText(pref.getString(SilicaGelGT21_KEY, ""));
        ketOilGT21.setText(pref.getString(OilGT21_KEY, ""));
        ketFanGT21.setText(pref.getString(FanGT21_KEY, ""));
        ketGroundGT21.setText(pref.getString(GroundGT21_KEY, ""));
        ketBushingGT21.setText(pref.getString(BushingGT21_KEY, ""));
        ketSumppumpGT21.setText(pref.getString(SumppumpGT21_KEY, ""));
        EditOilTempGT21.setText(pref.getString(OilTempGT21_KEY, ""));
        EditWindingGT21.setText(pref.getString(WindingTempGT21_KEY, ""));
        EditOillevelGT21.setText(pref.getString(OilLevel21_KEY, ""));
        EditKeteranganGT21.setText(pref.getString(KeteranganGT21_KEY, ""));
        orangPm1.setText(pref.getString(ORANG1_KEY, ""));
        orangPm2.setText(pref.getString(ORANG2_KEY, ""));
        orangPm3.setText(pref.getString(ORANG3_KEY, ""));
        Operator.setText(pref.getString(OPERATOR_KEY, ""));

        mNotificationUtils = new NotificationUtils(getActivity());

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

        checkBoxSharedPref();

        savedButtonGT11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPref();
                Toast.makeText(getActivity(), "Saved", Toast.LENGTH_SHORT).show();
             /*   Intent intent = new Intent(getActivity(), MainPmTrafo.class);
                intent.putExtra("Test", EditOilTempGT11.getText().toString());
                startActivity(intent); */
            }
        });

        savedButtonGT13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPref();
                Toast.makeText(getActivity(), "Saved", Toast.LENGTH_SHORT).show();
            }
        });

        savedButtonGT21.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPref();
                Toast.makeText(getActivity(), "Saved", Toast.LENGTH_SHORT).show();
            }
        });

        btnSignSPV = v.findViewById(R.id.SignBtnSPVTrafo);
        btnSignSPV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signatureDigitalSPV();
                sharedPref();
                checkBoxSharedPref();
            }
        });

        btnSignOps = v.findViewById(R.id.btnSignOPSTrafo);
        btnSignOps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signatureDigitalOPS();
                sharedPref();
                checkBoxSharedPref();
            }
        });

        btnCreatedPDF = v.findViewById(R.id.btnCreatePDFTrafo);
        btnCreatedPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomAlertDialog);
                ViewGroup viewGroup = v.findViewById(R.id.content);
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
                        sharedPref();
                        checkBoxSharedPref();
                        createPDFTrafo();
                        Notification.Builder notifPDF = mNotificationUtils.getAndroidChannelNotification("PDF Notification", "PDF telah berhasil disimpan di Storage");
                        mNotificationUtils.getManager().notify(101, notifPDF.build());
                        alertDialog.dismiss();
                    }
                });

                alertDialog.show();
            }
        });

        return v;

    }

    public void sharedPref()
    {
        pref = this.getActivity().getSharedPreferences(MainTrafo, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(SilicaGelGT11_KEY, ketSilicaGT11.getText().toString());
        editor.putString(OilGT11_KEY, ketOilGT11.getText().toString());
        editor.putString(FanGT11_KEY, ketFanGT11.getText().toString());
        editor.putString(GroundGT11_KEY, ketGroundGT11.getText().toString());
        editor.putString(BushingGT11_KEY, ketBushingGT11.getText().toString());
        editor.putString(SumppumpGT11_KEY, ketSumppumpGT11.getText().toString());
        editor.putString(OilTempGT11_KEY, EditOilTempGT11.getText().toString());
        editor.putString(WindingTempGT11_KEY, EditWindingGT11.getText().toString());
        editor.putString(OilLevel_KEY, EditOillevelGT11.getText().toString());
        editor.putString(KeteranganGT11_KEY, EditKeteranganGT11.getText().toString());

        editor.putString(SilicaGelGT13_KEY, ketSilicaGT13.getText().toString());
        editor.putString(OilGT13_KEY, ketOilGT13.getText().toString());
        editor.putString(FanGT13_KEY, ketFanGT13.getText().toString());
        editor.putString(GroundGT13_KEY, ketGroundGT13.getText().toString());
        editor.putString(BushingGT13_KEY, ketBushingGT13.getText().toString());
        editor.putString(SumppumpGT13_KEY, ketSumppumpGT13.getText().toString());
        editor.putString(OilTempGT13_KEY, EditOilTempGT13.getText().toString());
        editor.putString(WindingTempGT13_KEY, EditWindingGT13.getText().toString());
        editor.putString(OilLevel13_KEY, EditOillevelGT13.getText().toString());
        editor.putString(KeteranganGT13_KEY, EditKeteranganGT13.getText().toString());

        editor.putString(SilicaGelGT21_KEY, ketSilicaGT21.getText().toString());
        editor.putString(OilGT21_KEY, ketOilGT21.getText().toString());
        editor.putString(FanGT21_KEY, ketFanGT21.getText().toString());
        editor.putString(GroundGT21_KEY, ketGroundGT21.getText().toString());
        editor.putString(BushingGT21_KEY, ketBushingGT21.getText().toString());
        editor.putString(SumppumpGT21_KEY, ketSumppumpGT21.getText().toString());
        editor.putString(OilTempGT21_KEY, EditOilTempGT21.getText().toString());
        editor.putString(WindingTempGT21_KEY, EditWindingGT21.getText().toString());
        editor.putString(OilLevel21_KEY, EditOillevelGT21.getText().toString());
        editor.putString(KeteranganGT21_KEY, EditKeteranganGT21.getText().toString());

        editor.putString(ORANG1_KEY, orangPm1.getText().toString());
        editor.putString(ORANG2_KEY, orangPm2.getText().toString());
        editor.putString(ORANG3_KEY, orangPm3.getText().toString());
        editor.putString(OPERATOR_KEY, Operator.getText().toString());
        editor.apply();
    }

    public void checkBoxSharedPref()
    {
        pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        final SharedPreferences.Editor editor = pref.edit();
        ChkBoxSilicaGT11.setChecked(pref.contains("checkedSilicaGT11") && pref.getBoolean("checkedSilicaGT11", false) == true);

        ChkOilGT11.setChecked(pref.contains("checkedOilGT11") && pref.getBoolean("checkedOilGT11", false) == true);

        ChkFanGT11.setChecked(pref.contains("checkedFanGT11") && pref.getBoolean("checkedFanGT11", false) == true);

        ChkGroundGT11.setChecked(pref.contains("checkedGroundGT11") && pref.getBoolean("checkedGroundGT11", false) == true);

        ChkBushingGT11.setChecked(pref.contains("checkedBushingGT11") && pref.getBoolean("checkedBushingGT11", false) == true);

        ChkSumppumpGT11.setChecked(pref.contains("checkedSumpGT11") && pref.getBoolean("checkedSumpGT11", false) == true);

        ChkBoxSilicaGT11.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (ChkBoxSilicaGT11.isChecked()) {
                    ketSilicaGT11.setText("OK");
                    editor.putBoolean("checkedSilicaGT11", true);
                    editor.apply();
                    sharedPref();
                } else {
                    ketSilicaGT11.setText("Not OK");
                    editor.putBoolean("checkedSilicaGT11", false);
                    editor.apply();
                    sharedPref();
                }
            }
        });

        ChkOilGT11.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (ChkOilGT11.isChecked()) {
                    ketOilGT11.setText("OK");
                    editor.putBoolean("checkedOilGT11", true);
                    editor.apply();
                    sharedPref();
                } else {
                    ketOilGT11.setText("Not OK");
                    editor.putBoolean("checkedOilGT11", false);
                    editor.apply();
                    sharedPref();
                }
            }
        });

        ChkFanGT11.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (ChkFanGT11.isChecked()) {
                    ketFanGT11.setText("OK");
                    editor.putBoolean("checkedFanGT11", true);
                    editor.apply();
                    sharedPref();
                } else {
                    ketFanGT11.setText("Not OK");
                    editor.putBoolean("checkedFanGT11", false);
                    editor.apply();
                    sharedPref();
                }
            }
        });

        ChkGroundGT11.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (ChkGroundGT11.isChecked()) {
                    ketGroundGT11.setText("OK");
                    editor.putBoolean("checkedGroundGT11", true);
                    editor.apply();
                    sharedPref();
                } else {
                    ketGroundGT11.setText("Not OK");
                    editor.putBoolean("checkedGroundGT11", false);
                    editor.apply();
                    sharedPref();
                }
            }
        });

        ChkBushingGT11.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (ChkBushingGT11.isChecked()) {
                    ketBushingGT11.setText("OK");
                    editor.putBoolean("checkedBushingGT11", true);
                    editor.apply();
                    sharedPref();
                } else {
                    ketBushingGT11.setText("Not OK");
                    editor.putBoolean("checkedBushingGT11", false);
                    editor.apply();
                    sharedPref();
                }
            }
        });

        ChkSumppumpGT11.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (ChkSumppumpGT11.isChecked()) {
                    ketSumppumpGT11.setText("OK");
                    editor.putBoolean("checkedSumpGT11", true);
                    editor.apply();
                    sharedPref();
                } else {
                    ketSumppumpGT11.setText("Not OK");
                    editor.putBoolean("checkedSumpGT11", false);
                    editor.apply();
                    sharedPref();
                }
            }
        });

        ChkBoxSilicaGT13.setChecked(pref.contains("checkedSilicaGT13") && pref.getBoolean("checkedSilicaGT13", false) == true);

        ChkOilGT13.setChecked(pref.contains("checkedOilGT13") && pref.getBoolean("checkedOilGT13", false) == true);

        ChkFanGT13.setChecked(pref.contains("checkedFanGT13") && pref.getBoolean("checkedFanGT13", false) == true);

        ChkGroundGT13.setChecked(pref.contains("checkedGroundGT13") && pref.getBoolean("checkedGroundGT13", false) == true);

        ChkBushingGT13.setChecked(pref.contains("checkedBushingGT13") && pref.getBoolean("checkedBushingGT13", false) == true);

        ChkSumppumpGT13.setChecked(pref.contains("checkedSumpGT13") && pref.getBoolean("checkedSumpGT13", false) == true);

        ChkBoxSilicaGT13.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (ChkBoxSilicaGT13.isChecked()) {
                    ketSilicaGT13.setText("OK");
                    editor.putBoolean("checkedSilicaGT13", true);
                    editor.apply();
                    sharedPref();
                } else {
                    ketSilicaGT13.setText("Not OK");
                    editor.putBoolean("checkedSilicaGT13", false);
                    editor.apply();
                    sharedPref();
                }
            }
        });

        ChkOilGT13.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (ChkOilGT13.isChecked()) {
                    ketOilGT13.setText("OK");
                    editor.putBoolean("checkedOilGT13", true);
                    editor.apply();
                    sharedPref();
                } else {
                    ketOilGT13.setText("Not OK");
                    editor.putBoolean("checkedOilGT13", false);
                    editor.apply();
                    sharedPref();
                }
            }
        });

        ChkFanGT13.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (ChkFanGT13.isChecked()) {
                    ketFanGT13.setText("OK");
                    editor.putBoolean("checkedFanGT13", true);
                    editor.apply();
                    sharedPref();
                } else {
                    ketFanGT13.setText("Not OK");
                    editor.putBoolean("checkedFanGT13", false);
                    editor.apply();
                    sharedPref();
                }
            }
        });

        ChkGroundGT13.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (ChkGroundGT13.isChecked()) {
                    ketGroundGT13.setText("OK");
                    editor.putBoolean("checkedGroundGT13", true);
                    editor.apply();
                    sharedPref();
                } else {
                    ketGroundGT13.setText("Not OK");
                    editor.putBoolean("checkedGroundGT13", false);
                    editor.apply();
                    sharedPref();
                }
            }
        });

        ChkBushingGT13.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (ChkBushingGT13.isChecked()) {
                    ketBushingGT13.setText("OK");
                    editor.putBoolean("checkedBushingGT13", true);
                    editor.apply();
                    sharedPref();
                } else {
                    ketBushingGT13.setText("Not OK");
                    editor.putBoolean("checkedBushingGT13", false);
                    editor.apply();
                    sharedPref();
                }
            }
        });

        ChkSumppumpGT13.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (ChkSumppumpGT13.isChecked()) {
                    ketSumppumpGT13.setText("OK");
                    editor.putBoolean("checkedSumpGT13", true);
                    editor.apply();
                    sharedPref();
                } else {
                    ketSumppumpGT13.setText("Not OK");
                    editor.putBoolean("checkedSumpGT13", false);
                    editor.apply();
                    sharedPref();
                }
            }
        });

        ChkBoxSilicaGT21.setChecked(pref.contains("checkedSilicaGT21") && pref.getBoolean("checkedSilicaGT21", false) == true);

        ChkOilGT21.setChecked(pref.contains("checkedOilGT21") && pref.getBoolean("checkedOilGT21", false) == true);

        ChkFanGT21.setChecked(pref.contains("checkedFanGT21") && pref.getBoolean("checkedFanGT21", false) == true);

        ChkGroundGT21.setChecked(pref.contains("checkedGroundGT21") && pref.getBoolean("checkedGroundGT21", false) == true);

        ChkBushingGT21.setChecked(pref.contains("checkedBushingGT21") && pref.getBoolean("checkedBushingGT21", false) == true);

        ChkSumppumpGT21.setChecked(pref.contains("checkedSumpGT21") && pref.getBoolean("checkedSumpGT21", false) == true);

        ChkBoxSilicaGT21.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (ChkBoxSilicaGT21.isChecked()) {
                    ketSilicaGT21.setText("OK");
                    editor.putBoolean("checkedSilicaGT21", true);
                    editor.apply();
                    sharedPref();
                } else {
                    ketSilicaGT21.setText("Not OK");
                    editor.putBoolean("checkedSilicaGT21", false);
                    editor.apply();
                    sharedPref();
                }
            }
        });

        ChkOilGT21.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (ChkOilGT21.isChecked()) {
                    ketOilGT21.setText("OK");
                    editor.putBoolean("checkedOilGT21", true);
                    editor.apply();
                    sharedPref();
                } else {
                    ketOilGT21.setText("Not OK");
                    editor.putBoolean("checkedOilGT21", false);
                    editor.apply();
                    sharedPref();
                }
            }
        });

        ChkFanGT21.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (ChkFanGT21.isChecked()) {
                    ketFanGT21.setText("OK");
                    editor.putBoolean("checkedFanGT21", true);
                    editor.apply();
                    sharedPref();
                } else {
                    ketFanGT21.setText("Not OK");
                    editor.putBoolean("checkedFanGT21", false);
                    editor.apply();
                    sharedPref();
                }
            }
        });

        ChkGroundGT21.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (ChkGroundGT21.isChecked()) {
                    ketGroundGT21.setText("OK");
                    editor.putBoolean("checkedGroundGT21", true);
                    editor.apply();
                    sharedPref();
                } else {
                    ketGroundGT21.setText("Not OK");
                    editor.putBoolean("checkedGroundGT21", false);
                    editor.apply();
                    sharedPref();
                }
            }
        });

        ChkBushingGT21.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (ChkBushingGT21.isChecked()) {
                    ketBushingGT21.setText("OK");
                    editor.putBoolean("checkedBushingGT21", true);
                    editor.apply();
                    sharedPref();
                } else {
                    ketBushingGT21.setText("Not OK");
                    editor.putBoolean("checkedBushingGT21", false);
                    editor.apply();
                    sharedPref();
                }
            }
        });

        ChkSumppumpGT21.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (ChkSumppumpGT21.isChecked()) {
                    ketSumppumpGT21.setText("OK");
                    editor.putBoolean("checkedSumpGT21", true);
                    editor.apply();
                    sharedPref();
                } else {
                    ketSumppumpGT21.setText("Not OK");
                    editor.putBoolean("checkedSumpGT21", false);
                    editor.apply();
                    sharedPref();
                }
            }
        });

    }

    public void createPDFTrafo()
    {
        //Image ttd tangan OPS
        String ImageFileOPS = DIRECTORY+"ttdOperatorPMtrafo.jpg";
        Bitmap bitmapImageOPS = BitmapFactory.decodeFile(ImageFileOPS);
        //Image ttd tangan SPV
        String ImageFileSPV = DIRECTORY+"ttdSPVPMgen.jpg";
        Bitmap bitmapImageSPV = BitmapFactory.decodeFile(ImageFileSPV);

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
        canvas.drawText("PEMELIHARAAN TRANSFORMER GT", 1170 / 2, 195, titlePaint);

        titlePaint.setTextSize(18f);
        titlePaint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("Nomor Dokumen : FMT-17.2.1", 912,65, titlePaint);
        canvas.drawText("Revisi : 00", 912,135, titlePaint);
        canvas.drawText("Tanggal Terbit : 20-08-2013", 912,195, titlePaint);

        // Nama Preventive GT dan Tanggal
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setColor(Color.BLACK);
        paint.setTextSize(30f);
        canvas.drawText("Main Transformer GT ", 40, 270, paint ); // Judul PM
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        paint.setColor(Color.BLACK);
        paint.setTextSize(30);
        dateFormat = new SimpleDateFormat("dd/MM/yy");
        canvas.drawText(dateFormat.format(dateTime), 1170 - 145, 270, paint);
        dateFormat = new SimpleDateFormat("HH:mm:ss");
        canvas.drawText(dateFormat.format(dateTime), 1170 - 145, 300, paint);

        /** Isi dari PDF */
        paint.setColor(Color.BLACK);
        paint.setTextSize(30f);

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

        /** Isi PDF */
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setColor(Color.BLACK);
        paint.setTextSize(30f);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        canvas.drawText("MAIN TRAFO GT 11", 1170/2, 380, paint );
        canvas.drawText("MAIN TRAFO GT 13", 1170/2, 780, paint );
        canvas.drawText("MAIN TRAFO GT 21", 1170/2, 1180, paint );

        /** GT 11 Main Trafo */
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setColor(Color.BLACK);
        paint.setTextSize(30f);
        canvas.drawText("- Winding Temp. : "+EditWindingGT11.getText().toString()+" \u2103", 70, 450, paint);
        canvas.drawText("- Oil Temp. : "+EditOilTempGT11.getText().toString()+" \u2103", 70, 500, paint);
        canvas.drawText("- Oil Level : "+EditOillevelGT11.getText().toString()+" \u2103", 70, 550, paint);

        canvas.drawText("- Silica Gel : "+ketSilicaGT11.getText().toString(), 450, 450, paint);
        canvas.drawText("- Glass Oil : "+ketOilGT11.getText().toString(), 450, 500, paint);
        canvas.drawText("- CO Fan Trafo : "+ketFanGT11.getText().toString(), 450, 550, paint);

        canvas.drawText("- Grounding : "+ketGroundGT11.getText().toString(), 830, 450, paint);
        canvas.drawText("- Oil Bushing : "+ketBushingGT11.getText().toString(), 830, 500, paint);
        canvas.drawText("- Sump Pump : "+ketSumppumpGT11.getText().toString(), 830, 550, paint);

        canvas.drawText("Keterangan : "+EditKeteranganGT11.getText().toString(), 70, 620, paint);

        // Border Table
        canvas.drawLine(50, 330, 1150, 330, paint); // Top
        canvas.drawLine(50, 400, 1150, 400, paint); // Top Bottom Header
        canvas.drawLine(50, 330, 50, 650, paint); // Left
        canvas.drawLine(420, 400, 420, 580, paint); // Left after Temp
        canvas.drawLine(1150, 330, 1150, 650, paint); // Right
        canvas.drawLine(800, 400, 800, 580, paint); // Right after Silica
        canvas.drawLine(50, 650, 1150, 650, paint); // Bottom
        canvas.drawLine(50, 580, 1150, 580, paint); // Bottom Upper Ket

        /** GT 13 Main Trafo */
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setColor(Color.BLACK);
        paint.setTextSize(30f);
        canvas.drawText("- Winding Temp. : "+EditWindingGT13.getText().toString()+" \u2103", 70, 850, paint);
        canvas.drawText("- Oil Temp. : "+EditOilTempGT13.getText().toString()+" \u2103", 70, 900, paint);
        canvas.drawText("- Oil Level : "+EditOillevelGT13.getText().toString()+" \u2103", 70, 950, paint);

        canvas.drawText("- Silica Gel : "+ketSilicaGT13.getText().toString(), 450, 850, paint);
        canvas.drawText("- Glass Oil : "+ketOilGT13.getText().toString(), 450, 900, paint);
        canvas.drawText("- CO Fan Trafo : "+ketFanGT13.getText().toString(), 450, 950, paint);

        canvas.drawText("- Grounding : "+ketGroundGT13.getText().toString(), 830, 850, paint);
        canvas.drawText("- Oil Bushing : "+ketBushingGT13.getText().toString(), 830, 900, paint);
        canvas.drawText("- Sump Pump : "+ketSumppumpGT13.getText().toString(), 830, 950, paint);

        canvas.drawText("Keterangan : "+EditKeteranganGT13.getText().toString(), 70, 1020, paint);

        // Border Table 780
        canvas.drawLine(50, 730, 1150, 730, paint); // Top
        canvas.drawLine(50, 800, 1150, 800, paint); // Top Bottom Header
        canvas.drawLine(50, 730, 50, 1050, paint); // Left
        canvas.drawLine(420, 800, 420, 980, paint); // Left after Temp
        canvas.drawLine(1150, 730, 1150, 1050, paint); // Right
        canvas.drawLine(800, 800, 800, 980, paint); // Right after Silica
        canvas.drawLine(50, 1050, 1150, 1050, paint); // Bottom
        canvas.drawLine(50, 980, 1150, 980, paint); // Bottom Upper Ket

        /** GT 21 Main Trafo 1180 */
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setColor(Color.BLACK);
        paint.setTextSize(30f);
        canvas.drawText("- Winding Temp. : "+EditWindingGT21.getText().toString()+" \u2103", 70, 1250, paint);
        canvas.drawText("- Oil Temp. : "+EditOilTempGT21.getText().toString()+" \u2103", 70, 1300, paint);
        canvas.drawText("- Oil Level : "+EditOillevelGT21.getText().toString()+" \u2103", 70, 1350, paint);

        canvas.drawText("- Silica Gel : "+ketSilicaGT21.getText().toString(), 450, 1250, paint);
        canvas.drawText("- Glass Oil : "+ketOilGT21.getText().toString(), 450, 1300, paint);
        canvas.drawText("- CO Fan Trafo : "+ketFanGT21.getText().toString(), 450, 1350, paint);

        canvas.drawText("- Grounding : "+ketGroundGT21.getText().toString(), 830, 1250, paint);
        canvas.drawText("- Oil Bushing : "+ketBushingGT21.getText().toString(), 830, 1300, paint);
        canvas.drawText("- Sump Pump : "+ketSumppumpGT21.getText().toString(), 830, 1350, paint);

        canvas.drawText("Keterangan : "+EditKeteranganGT21.getText().toString(), 70, 1420, paint);

        // Border Table 780 1180
        canvas.drawLine(50, 1130, 1150, 1130, paint); // Top
        canvas.drawLine(50, 1200, 1150, 1200, paint); // Top Bottom Header
        canvas.drawLine(50, 1130, 50, 1450, paint); // Left
        canvas.drawLine(420, 1200, 420, 1380, paint); // Left after Temp
        canvas.drawLine(1150, 1130, 1150, 1450, paint); // Right
        canvas.drawLine(800, 1200, 800, 1380, paint); // Right after Silica
        canvas.drawLine(50, 1450, 1150, 1450, paint); // Bottom
        canvas.drawLine(50, 1380, 1150, 1380, paint); // Bottom Upper Ket

        dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        pdfDocument.finishPage(page);
        File file = new File(Environment.getExternalStorageDirectory(), "/PM Main Trafo GT"+" "+dateFormat.format(dateTime)+".pdf");
        try {
            pdfDocument.writeTo(new FileOutputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }

        pdfDocument.close();
        Toast.makeText(getActivity(), "PDF has Created", Toast.LENGTH_LONG).show();


        dateTime = new Date();
    }

    public void signatureDigitalOPS()
    {
        dialogCustomTTD = new AlertDialog.Builder(getActivity());
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
            File f = new File(wallpaperDirectory,"ttdOperatorPMtrafo.jpg"); //Calendar.getInstance().getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(getActivity(),
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());
            Toast.makeText(getActivity(), "Saved Berhasil", Toast.LENGTH_SHORT).show();

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";

    }

    public void signatureDigitalSPV()
    {
        dialogCustomTTD = new AlertDialog.Builder(getActivity());
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
            MediaScannerConnection.scanFile(getActivity(),
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());
            Toast.makeText(getActivity(), "Saved Berhasil", Toast.LENGTH_SHORT).show();

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }

}
