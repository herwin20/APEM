package com.herwinlab.apem.pmgasturbine.batterybluetooth;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.media.MediaScannerConnection;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.snackbar.Snackbar;
import com.herwinlab.apem.R;
import com.herwinlab.apem.pmgasturbine.PmGasTurbine;
import com.kyanogen.signatureview.SignatureView;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

import me.aflak.bluetooth.Bluetooth;

public class BluetoothBattery extends AppCompatActivity implements Bluetooth.CommunicationCallback {

    public String name;
    public Bluetooth btActivity;
    TextView ReadVoltage, ConditionBattery, InternalRest, DeviceVolt;
    boolean registered=false;
    CardView cardCondition;

    //Digital Signature
    SignatureView signatureView;
    String path;
    private static final String IMAGE_DIRECTORY = "/Signature";

    //Custom Dialog
    AlertDialog.Builder dialogCustom, dialogCustomTTD, dialogCustomPDF;
    LayoutInflater inflater, inflaterTTD, inflaterPDF;
    View dialogView, dialogViewTTD, view, viewTTD, dialogViewPDF;

    //Button
    public Button mClear, mGetSign, mCancel;

    //Bitmap
    Bitmap bitmapTTD;

    // Creating Separate Directory for Signature and Image
    String DIRECTORY = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Signature/";

    //PDF
    Date dateTime;
    Bitmap bitmap, scaleBitmap, PJBscale, IPJBway, accuImg, turbineImg;
    DateFormat dateFormat;

    //Handler
    private final Handler handler = new Handler();

    /** Button Battery */
    public TextView Batt1, Batt2, Batt3, Batt4, Batt5, Batt6, Batt7, Batt8, Batt9, Batt10;
    public TextView Batt11, Batt12, Batt13, Batt14, Batt15, Batt16, Batt17, Batt18, Batt19, Batt20;
    public TextView Batt21, Batt22, Batt23, Batt24, Batt25, Batt26, Batt27, Batt28, Batt29, Batt30;
    public TextView Batt31, Batt32, Batt33, Batt34, Batt35, Batt36, Batt37, Batt38, Batt39, Batt40;
    public TextView Batt41, Batt42, Batt43, Batt44, Batt45, Batt46, Batt47, Batt48, Batt49, Batt50;
    public TextView Batt51, Batt52, Batt53, Batt54, Batt55, Batt56, Batt57, Batt58, Batt59, Batt60;
    public TextView Batt61, Batt62, Batt63, Batt64, Batt65, Batt66, Batt67, Batt68, Batt69, Batt70;
    public TextView Batt71, Batt72, Batt73, Batt74, Batt75, Batt76, Batt77, Batt78, Batt79, Batt80;
    public TextView Batt81, Batt82, Batt83, Batt84, Batt85, Batt86, Batt87, Batt88, Batt89, Batt90;
    public TextView Batt91, Batt92, Batt93, Batt94, Batt95, Batt96, Batt97, Batt98, Batt99, Batt100;
    public TextView Batt101, Batt102, Batt103, Batt104, Batt105, Batt106, Batt107, Batt108;
    public TextView TanggalNow, TotalVoltage;
    public float tegangan_total;
    public EditText namaGT, orangPm1, orangPm2, orangPm3, operator, Catatan1, Catatan2, unitBatt;

    public CardView graphVolage, clearAll;

    public LinearLayout buttonSPVttd, buttonOPSttd, buttonCreatedPDF;

    ArrayList<Entry> lineEntry;
    ArrayList<String> lineLabels;
    LineDataSet lineDataSet;
    LineData lineData;
    protected LineChart chartGraphBatt;

    private SharedPreferences prefs;
    private final String Batt1KEY = "1", Batt2KEY = "2", Batt3KEY = "3", Batt4KEY = "4", Batt5KEY = "5", Batt6KEY = "6", Batt7KEY = "7", Batt8KEY = "8", Batt9KEY = "9", Batt10KEY = "10";
    private final String Batt11KEY = "11", Batt12KEY = "12", Batt13KEY = "13", Batt14KEY = "14", Batt15KEY = "15", Batt16KEY = "16", Batt17KEY = "17", Batt18KEY = "18", Batt19KEY = "19", Batt20KEY = "20";
    private final String Batt21KEY = "21", Batt22KEY = "22", Batt23KEY = "23", Batt24KEY = "24", Batt25KEY = "25", Batt26KEY = "26", Batt27KEY = "27", Batt28KEY = "28", Batt29KEY = "29", Batt30KEY = "30";
    private final String Batt31KEY = "31", Batt32KEY = "32", Batt33KEY = "33", Batt34KEY = "34", Batt35KEY = "35", Batt36KEY = "36", Batt37KEY = "37", Batt38KEY = "38", Batt39KEY = "39", Batt40KEY = "40";
    private final String Batt41KEY = "41", Batt42KEY = "42", Batt43KEY = "43", Batt44KEY = "44", Batt45KEY = "45", Batt46KEY = "46", Batt47KEY = "47", Batt48KEY = "48", Batt49KEY = "49", Batt50KEY = "50";
    private final String Batt51KEY = "51", Batt52KEY = "52", Batt53KEY = "53", Batt54KEY = "54", Batt55KEY = "55", Batt56KEY = "56", Batt57KEY = "57", Batt58KEY = "58", Batt59KEY = "59", Batt60KEY = "60";
    private final String Batt61KEY = "61", Batt62KEY = "62", Batt63KEY = "63", Batt64KEY = "64", Batt65KEY = "65", Batt66KEY = "66", Batt67KEY = "67", Batt68KEY = "68", Batt69KEY = "69", Batt70KEY = "70";
    private final String Batt71KEY = "71", Batt72KEY = "72", Batt73KEY = "73", Batt74KEY = "74", Batt75KEY = "75", Batt76KEY = "76", Batt77KEY = "77", Batt78KEY = "78", Batt79KEY = "79", Batt80KEY = "80";
    private final String Batt81KEY = "81", Batt82KEY = "82", Batt83KEY = "83", Batt84KEY = "84", Batt85KEY = "85", Batt86KEY = "86", Batt87KEY = "87", Batt88KEY = "88", Batt89KEY = "89", Batt90KEY = "90";
    private final String Batt91KEY = "91", Batt92KEY = "92", Batt93KEY = "93", Batt94KEY = "94", Batt95KEY = "95", Batt96KEY = "96", Batt97KEY = "97", Batt98KEY = "98", Batt99KEY = "99", Batt100KEY = "100";
    private final String Batt101KEY = "101", Batt102KEY = "102", Batt103KEY = "103", Batt104KEY = "104", Batt105KEY = "105", Batt106KEY = "106", Batt107KEY = "107", Batt108KEY = "108";
    private final String VOLTAGE_KEY = "VOLTAGE";
    private final String CAT1_KEY = "CAT1";
    private final String CAT2_KEY = "CAT2";
    private final String ORANG1_KEY = "ORANG1";
    private final String ORANG2_KEY = "ORANG2";
    private final String ORANG3_KEY = "ORANG3";
    private final String OPERATOR_KEY = "OPERATOR";
    private final String NAMAGT_KEY = "NAMAGT";
    private final String NAMAUNIT_KEY = "NAMAUNIT";

    // Calibrated Function
    public ImageView calibratedImage;
    public EditText voltageAVO, voltageAlat;
    public TextView selesihVolt, errorVolt;
    public LinearLayout sendDataCalibrated, buttonZeroCalibrated;
    public Button calcButton;

    //Google SpreadSheet
    public String time, unit, nomor, tegangan, kondisi;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bluetooth_activity_battery);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);


        // Untuk Tanggal
        handler.postDelayed(runnable, 1000);

        transparentStatusAndNavigation();

        ReadVoltage = findViewById(R.id.read_voltage);
        ConditionBattery = findViewById(R.id.condition_batt);
        cardCondition = findViewById(R.id.condCard);
        InternalRest = findViewById(R.id.internal_ohm);
        DeviceVolt = findViewById(R.id.battery_alat);

        TotalVoltage = findViewById(R.id.totalvoltageble2);

        btActivity = new Bluetooth(this);
        btActivity.enableBluetooth();

        btActivity.setCommunicationCallback(this);
        int pos = getIntent().getExtras().getInt("pos");
        name = btActivity.getPairedDevices().get(pos).getName();

        btActivity.connectToDevice(btActivity.getPairedDevices().get(pos));

        //Nama
        namaGT = findViewById(R.id.namaGTble2);
        namaGT.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        unitBatt = findViewById(R.id.unitbattble2);
        unitBatt.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        orangPm1 = findViewById(R.id.nama_pm1);
        orangPm2 = findViewById(R.id.nama_pm2);
        orangPm3 = findViewById(R.id.nama_pm3);
        operator = findViewById(R.id.nama_operator);
        Catatan1 = findViewById(R.id.ket_BLE);
        Catatan2 = findViewById(R.id.ket2_BLE);

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

        buttonSPVttd = findViewById(R.id.buttonSignatureSPVBattBLE);
        buttonSPVttd.setOnClickListener(v -> {
            digitalSignatureSPV();

        });

        buttonOPSttd = findViewById(R.id.buttonSignatureOPSBattBLE);
        buttonOPSttd.setOnClickListener(v -> {
            digitalSignatureOPS();

        });

        buttonCreatedPDF = findViewById(R.id.buttonCreatepdfBLE);
        buttonCreatedPDF.setOnClickListener(v -> {
            createdPDF();
            createdExcel();
            savedPrefs();

        });

        // Main Program
        Measure_Battery1();
        Measure_Battery2();
        Measure_Battery3();
        Measure_Battery4();
        Measure_Battery5();
        Measure_Battery6();
        Measure_Battery7();
        Measure_Battery8();
        Measure_Battery9();
        Measure_Battery10();
        Measure_Battery11();

        //Click CardView and Clear All
        //Total_Clear();
        graphVolage =  findViewById(R.id.totalVoltage);
        graphVolage.setOnClickListener(view -> {
            savedPrefs();
            DialogGraphBatt();
        });

        // Shared Prefs
        prefs = getPreferences(MODE_PRIVATE);

        Batt1.setText(prefs.getString(Batt1KEY,"1")); Batt2.setText(prefs.getString(Batt2KEY,"2")); Batt3.setText(prefs.getString(Batt3KEY,"3")); Batt4.setText(prefs.getString(Batt4KEY,"4")); Batt5.setText(prefs.getString(Batt5KEY,"5"));
        Batt6.setText(prefs.getString(Batt6KEY,"6")); Batt7.setText(prefs.getString(Batt7KEY,"7")); Batt8.setText(prefs.getString(Batt8KEY,"8")); Batt9.setText(prefs.getString(Batt9KEY,"9")); Batt10.setText(prefs.getString(Batt10KEY,"10"));
        Batt11.setText(prefs.getString(Batt11KEY,"11")); Batt12.setText(prefs.getString(Batt12KEY,"12")); Batt13.setText(prefs.getString(Batt13KEY,"13")); Batt14.setText(prefs.getString(Batt14KEY,"14")); Batt15.setText(prefs.getString(Batt15KEY,"15"));
        Batt16.setText(prefs.getString(Batt16KEY,"16")); Batt17.setText(prefs.getString(Batt17KEY,"17")); Batt18.setText(prefs.getString(Batt18KEY,"18")); Batt19.setText(prefs.getString(Batt19KEY,"19")); Batt20.setText(prefs.getString(Batt20KEY,"20"));
        Batt21.setText(prefs.getString(Batt21KEY,"21")); Batt22.setText(prefs.getString(Batt22KEY,"22")); Batt23.setText(prefs.getString(Batt23KEY,"23")); Batt24.setText(prefs.getString(Batt24KEY,"24")); Batt25.setText(prefs.getString(Batt25KEY,"25"));
        Batt26.setText(prefs.getString(Batt26KEY,"26")); Batt27.setText(prefs.getString(Batt27KEY,"27")); Batt28.setText(prefs.getString(Batt28KEY,"28")); Batt29.setText(prefs.getString(Batt29KEY,"29")); Batt30.setText(prefs.getString(Batt30KEY,"30"));
        Batt31.setText(prefs.getString(Batt31KEY,"31")); Batt32.setText(prefs.getString(Batt32KEY,"32")); Batt33.setText(prefs.getString(Batt33KEY,"33")); Batt34.setText(prefs.getString(Batt34KEY,"34")); Batt35.setText(prefs.getString(Batt35KEY,"35"));
        Batt36.setText(prefs.getString(Batt36KEY,"36")); Batt37.setText(prefs.getString(Batt37KEY,"37")); Batt38.setText(prefs.getString(Batt38KEY,"38")); Batt39.setText(prefs.getString(Batt39KEY,"39")); Batt40.setText(prefs.getString(Batt40KEY,"40"));
        Batt41.setText(prefs.getString(Batt41KEY,"41")); Batt42.setText(prefs.getString(Batt42KEY,"42")); Batt43.setText(prefs.getString(Batt43KEY,"43")); Batt44.setText(prefs.getString(Batt44KEY,"44")); Batt45.setText(prefs.getString(Batt45KEY,"45"));
        Batt46.setText(prefs.getString(Batt46KEY,"46")); Batt47.setText(prefs.getString(Batt47KEY,"47")); Batt48.setText(prefs.getString(Batt48KEY,"48")); Batt49.setText(prefs.getString(Batt49KEY,"49")); Batt50.setText(prefs.getString(Batt50KEY,"50"));
        Batt51.setText(prefs.getString(Batt51KEY,"51")); Batt52.setText(prefs.getString(Batt52KEY,"52")); Batt53.setText(prefs.getString(Batt53KEY,"53")); Batt54.setText(prefs.getString(Batt54KEY,"54")); Batt55.setText(prefs.getString(Batt55KEY,"55"));
        Batt56.setText(prefs.getString(Batt56KEY,"56")); Batt57.setText(prefs.getString(Batt57KEY,"57")); Batt58.setText(prefs.getString(Batt58KEY,"58")); Batt59.setText(prefs.getString(Batt59KEY,"59")); Batt60.setText(prefs.getString(Batt60KEY,"60"));
        Batt61.setText(prefs.getString(Batt61KEY,"61")); Batt62.setText(prefs.getString(Batt62KEY,"62")); Batt63.setText(prefs.getString(Batt63KEY,"63")); Batt64.setText(prefs.getString(Batt64KEY,"64")); Batt65.setText(prefs.getString(Batt65KEY,"65"));
        Batt66.setText(prefs.getString(Batt66KEY,"66")); Batt67.setText(prefs.getString(Batt67KEY,"67")); Batt68.setText(prefs.getString(Batt68KEY,"68")); Batt69.setText(prefs.getString(Batt69KEY,"69")); Batt70.setText(prefs.getString(Batt70KEY,"70"));
        Batt71.setText(prefs.getString(Batt71KEY,"71")); Batt72.setText(prefs.getString(Batt72KEY,"72")); Batt73.setText(prefs.getString(Batt73KEY,"73")); Batt74.setText(prefs.getString(Batt74KEY,"74")); Batt75.setText(prefs.getString(Batt75KEY,"75"));
        Batt76.setText(prefs.getString(Batt76KEY,"76")); Batt77.setText(prefs.getString(Batt77KEY,"77")); Batt78.setText(prefs.getString(Batt78KEY,"78")); Batt79.setText(prefs.getString(Batt79KEY,"79")); Batt80.setText(prefs.getString(Batt80KEY,"80"));
        Batt81.setText(prefs.getString(Batt81KEY,"81")); Batt82.setText(prefs.getString(Batt82KEY,"82")); Batt83.setText(prefs.getString(Batt83KEY,"83")); Batt84.setText(prefs.getString(Batt84KEY,"84")); Batt85.setText(prefs.getString(Batt85KEY,"85"));
        Batt86.setText(prefs.getString(Batt86KEY,"86")); Batt87.setText(prefs.getString(Batt87KEY,"87")); Batt88.setText(prefs.getString(Batt88KEY,"88")); Batt89.setText(prefs.getString(Batt89KEY,"89")); Batt90.setText(prefs.getString(Batt90KEY,"90"));
        Batt91.setText(prefs.getString(Batt91KEY,"91")); Batt92.setText(prefs.getString(Batt92KEY,"92")); Batt93.setText(prefs.getString(Batt93KEY,"93")); Batt94.setText(prefs.getString(Batt94KEY,"94")); Batt95.setText(prefs.getString(Batt95KEY,"95"));
        Batt96.setText(prefs.getString(Batt96KEY,"96")); Batt97.setText(prefs.getString(Batt97KEY,"97")); Batt98.setText(prefs.getString(Batt98KEY,"98")); Batt99.setText(prefs.getString(Batt99KEY,"99")); Batt100.setText(prefs.getString(Batt100KEY,"100"));
        Batt101.setText(prefs.getString(Batt101KEY,"101")); Batt102.setText(prefs.getString(Batt102KEY,"102")); Batt103.setText(prefs.getString(Batt103KEY,"103")); Batt104.setText(prefs.getString(Batt104KEY,"104")); Batt105.setText(prefs.getString(Batt105KEY,"105"));
        Batt106.setText(prefs.getString(Batt106KEY,"106")); Batt107.setText(prefs.getString(Batt107KEY,"107")); Batt108.setText(prefs.getString(Batt108KEY,"108"));

        Catatan1.setText(prefs.getString(CAT1_KEY, ""));
        Catatan2.setText(prefs.getString(CAT2_KEY, ""));
        orangPm1.setText(prefs.getString(ORANG1_KEY, ""));
        orangPm2.setText(prefs.getString(ORANG2_KEY, ""));
        orangPm3.setText(prefs.getString(ORANG3_KEY, ""));
        operator.setText(prefs.getString(OPERATOR_KEY, ""));
        namaGT.setText(prefs.getString(NAMAGT_KEY,""));
        unitBatt.setText(prefs.getString(NAMAUNIT_KEY, ""));

        calibratedImage = findViewById(R.id.calimage);
        calibratedImage.setOnClickListener(view ->{
            dialogCalibrated();
        });
    }

    public void savedPrefs()
    {
        prefs = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Batt1KEY, Batt1.getText().toString()); editor.putString(Batt2KEY, Batt2.getText().toString()); editor.putString(Batt3KEY, Batt3.getText().toString()); editor.putString(Batt4KEY, Batt4.getText().toString()); editor.putString(Batt5KEY, Batt5.getText().toString());
        editor.putString(Batt6KEY, Batt6.getText().toString()); editor.putString(Batt7KEY, Batt7.getText().toString()); editor.putString(Batt8KEY, Batt8.getText().toString()); editor.putString(Batt9KEY, Batt9.getText().toString()); editor.putString(Batt10KEY, Batt10.getText().toString());
        editor.putString(Batt11KEY, Batt11.getText().toString()); editor.putString(Batt12KEY, Batt12.getText().toString()); editor.putString(Batt13KEY, Batt13.getText().toString()); editor.putString(Batt14KEY, Batt14.getText().toString()); editor.putString(Batt15KEY, Batt15.getText().toString());
        editor.putString(Batt16KEY, Batt16.getText().toString()); editor.putString(Batt17KEY, Batt17.getText().toString()); editor.putString(Batt18KEY, Batt18.getText().toString()); editor.putString(Batt19KEY, Batt19.getText().toString()); editor.putString(Batt20KEY, Batt20.getText().toString());
        editor.putString(Batt21KEY, Batt21.getText().toString()); editor.putString(Batt22KEY, Batt22.getText().toString()); editor.putString(Batt23KEY, Batt23.getText().toString()); editor.putString(Batt24KEY, Batt24.getText().toString()); editor.putString(Batt25KEY, Batt25.getText().toString());

        editor.putString(Batt26KEY, Batt26.getText().toString()); editor.putString(Batt27KEY, Batt27.getText().toString()); editor.putString(Batt28KEY, Batt28.getText().toString()); editor.putString(Batt29KEY, Batt29.getText().toString()); editor.putString(Batt30KEY, Batt30.getText().toString());
        editor.putString(Batt31KEY, Batt31.getText().toString()); editor.putString(Batt32KEY, Batt32.getText().toString()); editor.putString(Batt33KEY, Batt33.getText().toString()); editor.putString(Batt34KEY, Batt34.getText().toString()); editor.putString(Batt35KEY, Batt35.getText().toString());
        editor.putString(Batt36KEY, Batt36.getText().toString()); editor.putString(Batt37KEY, Batt37.getText().toString()); editor.putString(Batt38KEY, Batt38.getText().toString()); editor.putString(Batt39KEY, Batt39.getText().toString()); editor.putString(Batt40KEY, Batt40.getText().toString());
        editor.putString(Batt41KEY, Batt41.getText().toString()); editor.putString(Batt42KEY, Batt42.getText().toString()); editor.putString(Batt43KEY, Batt43.getText().toString()); editor.putString(Batt44KEY, Batt44.getText().toString()); editor.putString(Batt45KEY, Batt45.getText().toString());
        editor.putString(Batt46KEY, Batt46.getText().toString()); editor.putString(Batt47KEY, Batt47.getText().toString()); editor.putString(Batt48KEY, Batt48.getText().toString()); editor.putString(Batt49KEY, Batt49.getText().toString()); editor.putString(Batt50KEY, Batt50.getText().toString());

        editor.putString(Batt51KEY, Batt51.getText().toString()); editor.putString(Batt52KEY, Batt52.getText().toString()); editor.putString(Batt53KEY, Batt53.getText().toString()); editor.putString(Batt54KEY, Batt54.getText().toString()); editor.putString(Batt55KEY, Batt55.getText().toString());
        editor.putString(Batt56KEY, Batt56.getText().toString()); editor.putString(Batt57KEY, Batt57.getText().toString()); editor.putString(Batt58KEY, Batt58.getText().toString()); editor.putString(Batt59KEY, Batt59.getText().toString()); editor.putString(Batt60KEY, Batt60.getText().toString());
        editor.putString(Batt61KEY, Batt61.getText().toString()); editor.putString(Batt62KEY, Batt62.getText().toString()); editor.putString(Batt63KEY, Batt63.getText().toString()); editor.putString(Batt64KEY, Batt64.getText().toString()); editor.putString(Batt65KEY, Batt65.getText().toString());
        editor.putString(Batt66KEY, Batt66.getText().toString()); editor.putString(Batt67KEY, Batt67.getText().toString()); editor.putString(Batt68KEY, Batt68.getText().toString()); editor.putString(Batt69KEY, Batt69.getText().toString()); editor.putString(Batt70KEY, Batt70.getText().toString());
        editor.putString(Batt71KEY, Batt71.getText().toString()); editor.putString(Batt72KEY, Batt72.getText().toString()); editor.putString(Batt73KEY, Batt73.getText().toString()); editor.putString(Batt74KEY, Batt74.getText().toString()); editor.putString(Batt75KEY, Batt75.getText().toString());

        editor.putString(Batt76KEY, Batt76.getText().toString()); editor.putString(Batt77KEY, Batt77.getText().toString()); editor.putString(Batt78KEY, Batt78.getText().toString()); editor.putString(Batt79KEY, Batt79.getText().toString()); editor.putString(Batt80KEY, Batt80.getText().toString());
        editor.putString(Batt81KEY, Batt81.getText().toString()); editor.putString(Batt82KEY, Batt82.getText().toString()); editor.putString(Batt83KEY, Batt83.getText().toString()); editor.putString(Batt84KEY, Batt84.getText().toString()); editor.putString(Batt85KEY, Batt85.getText().toString());
        editor.putString(Batt86KEY, Batt86.getText().toString()); editor.putString(Batt87KEY, Batt87.getText().toString()); editor.putString(Batt88KEY, Batt88.getText().toString()); editor.putString(Batt89KEY, Batt89.getText().toString()); editor.putString(Batt90KEY, Batt90.getText().toString());
        editor.putString(Batt91KEY, Batt91.getText().toString()); editor.putString(Batt92KEY, Batt92.getText().toString()); editor.putString(Batt93KEY, Batt93.getText().toString()); editor.putString(Batt94KEY, Batt94.getText().toString()); editor.putString(Batt95KEY, Batt95.getText().toString());
        editor.putString(Batt96KEY, Batt96.getText().toString()); editor.putString(Batt97KEY, Batt97.getText().toString()); editor.putString(Batt98KEY, Batt98.getText().toString()); editor.putString(Batt99KEY, Batt99.getText().toString()); editor.putString(Batt100KEY, Batt100.getText().toString());

        editor.putString(Batt101KEY, Batt101.getText().toString()); editor.putString(Batt102KEY, Batt102.getText().toString()); editor.putString(Batt103KEY, Batt103.getText().toString()); editor.putString(Batt104KEY, Batt104.getText().toString()); editor.putString(Batt105KEY, Batt105.getText().toString());
        editor.putString(Batt106KEY, Batt106.getText().toString()); editor.putString(Batt107KEY, Batt107.getText().toString()); editor.putString(Batt108KEY, Batt108.getText().toString());
        editor.putString(CAT1_KEY, Catatan1.getText().toString());
        editor.putString(CAT2_KEY, Catatan2.getText().toString());
        editor.putString(ORANG1_KEY, orangPm1.getText().toString());
        editor.putString(ORANG2_KEY, orangPm2.getText().toString());
        editor.putString(ORANG3_KEY, orangPm3.getText().toString());
        editor.putString(OPERATOR_KEY, operator.getText().toString());
        editor.putString(NAMAGT_KEY, namaGT.getText().toString());
        editor.putString(NAMAUNIT_KEY, unitBatt.getText().toString());

        editor.apply();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (registered) {
            unregisterReceiver(mReceiver);
            registered=false;
        }
    }

    // Fungsi untuk menampilkan data BLE
    public void Display (final String data) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String data_masuk = data;
                String[] separated = data_masuk.split(",");
                // separated[0] -> Tegangan
                // separated[1] -> hambatan internal
                // separated[2] -> teg. alatnya
                ReadVoltage.setText(separated[0]);
                InternalRest.setText(separated[1]);
                DeviceVolt.setText(separated[2]);

                float Voltage = Float.parseFloat(separated[0]);
                if (Voltage == 0) {
                    ConditionBattery.setText("Not Connect!");
                }

                else if (Voltage < 2.17) {
                    ConditionBattery.setText("Bad !");
                    cardCondition.setCardBackgroundColor(Color.RED);
                }

                else if (Voltage >= 2.19 && Voltage <= 2.5) {
                    ConditionBattery.setText("Good !");
                    cardCondition.setCardBackgroundColor(Color.rgb(21, 142, 8));
                }

                else if (Voltage > 2.6) {
                    ConditionBattery.setText("Over !");
                    cardCondition.setCardBackgroundColor(Color.rgb(204, 102, 0));
                }

                else {ConditionBattery.setText("");}
            }
        });
    }

    //Fungsi ketika bluetooth Connect Devices
    public void onConnect(BluetoothDevice device) {
        //Display("Connected to "+device.getName()+" - "+device.getAddress());
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //   send.setEnabled(true);
            }
        });
    }

    public void onDisconnect(BluetoothDevice device, String message) {
        //Display("Disconnected!");
        //Display("Connecting again...");
        btActivity.connectToDevice(device);
    }

    public void onMessage(String message) {
        Display(message);
    }

    public void onError(String message) {
        Display("Error: "+message);
    }

    public void onConnectError(final BluetoothDevice device, String message) {
        //Display("Error: "+message);
        //Display("Trying again in 3 sec.");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        btActivity.connectToDevice(device);
                    }
                }, 2000);
                btActivity.removeCommunicationCallback();
                btActivity.disconnect();
                btActivity.disableBluetooth(); // untuk turn OFF Bluetooth
                Toast.makeText(BluetoothBattery.this, "Bluetooth Not Connected (Error)", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(BluetoothBattery.this, SelectBLEbatt.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                Intent intent1 = new Intent(BluetoothBattery.this, SelectBLEbatt.class);

                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        if(registered) {
                            unregisterReceiver(mReceiver);
                            registered=false;
                        }
                        startActivity(intent1);
                        finish();
                        break;
                }
            }
        }
    };

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

    private void Measure_Battery1() {

        dateTime = new Date();
        dateFormat = new SimpleDateFormat("dd-MM-yy");

        Batt1 = findViewById(R.id.batt1);
        Batt1.setOnClickListener(v -> {
            String batt1 = ReadVoltage.getText().toString();// + "," + "\n" + InternalRest.getText().toString() +" \u2126";
            Batt1.setText(batt1);
            // Send to SpreadSheet
            time = dateFormat.format(dateTime);
            unit = namaGT.getText().toString();
            nomor = "1";
            tegangan = ReadVoltage.getText().toString();
            kondisi = ConditionBattery.getText().toString();
            new SendRequest().execute();
        });

        Batt2 = findViewById(R.id.batt2);
        Batt2.setOnClickListener(v -> {
            String batt2 = ReadVoltage.getText().toString();// + "," + "\n" + InternalRest.getText().toString() +" \u2126";
            Batt2.setText(batt2);
            // Send to SpreadSheet
            time = dateFormat.format(dateTime);
            unit = namaGT.getText().toString();
            nomor = "2";
            tegangan = ReadVoltage.getText().toString();
            new SendRequest().execute();
        });

        Batt3 = findViewById(R.id.batt3);
        Batt3.setOnClickListener(v -> {
            String batt3 = ReadVoltage.getText().toString();// + "," + "\n" + InternalRest.getText().toString() +" \u2126";
            Batt3.setText(batt3);
            // Send to SpreadSheet
            time = dateFormat.format(dateTime);
            unit = namaGT.getText().toString();
            nomor = "3";
            tegangan = ReadVoltage.getText().toString();
            new SendRequest().execute();
        });

        Batt4 = findViewById(R.id.batt4);
        Batt4.setOnClickListener(v -> {
            String batt4 = ReadVoltage.getText().toString();// + "," + "\n" + InternalRest.getText().toString() +" \u2126";
            Batt4.setText(batt4);
            // Send to SpreadSheet
            time = dateFormat.format(dateTime);
            unit = namaGT.getText().toString();
            nomor = "4";
            tegangan = ReadVoltage.getText().toString();
            new SendRequest().execute();
        });

        Batt5 = findViewById(R.id.batt5);
        Batt5.setOnClickListener(v -> {
            String batt5 = ReadVoltage.getText().toString();// + "," + "\n" + InternalRest.getText().toString() +" \u2126";
            Batt5.setText(batt5);
            // Send to SpreadSheet
            time = dateFormat.format(dateTime);
            unit = namaGT.getText().toString();
            nomor = "5";
            tegangan = ReadVoltage.getText().toString();
            new SendRequest().execute();
        });

        Batt6 = findViewById(R.id.batt7);
        Batt6.setOnClickListener(v -> {
            String batt6 = ReadVoltage.getText().toString();
            Batt6.setText(batt6);
            // Send to SpreadSheet
            time = dateFormat.format(dateTime);
            unit = namaGT.getText().toString();
            nomor = "6";
            tegangan = ReadVoltage.getText().toString();
            new SendRequest().execute();
        });

        Batt7 = findViewById(R.id.batt8);
        Batt7.setOnClickListener(v -> {
            String batt7 = ReadVoltage.getText().toString();
            Batt7.setText(batt7);
            // Send to SpreadSheet
            time = dateFormat.format(dateTime);
            unit = namaGT.getText().toString();
            nomor = "7";
            tegangan = ReadVoltage.getText().toString();
            new SendRequest().execute();
        });

        Batt8 = findViewById(R.id.batt9);
        Batt8.setOnClickListener(v -> {
            String batt8 = ReadVoltage.getText().toString();
            Batt8.setText(batt8);
            // Send to SpreadSheet
            time = dateFormat.format(dateTime);
            unit = namaGT.getText().toString();
            nomor = "8";
            tegangan = ReadVoltage.getText().toString();
            new SendRequest().execute();
        });

        Batt9 = findViewById(R.id.batt10);
        Batt9.setOnClickListener(v -> {
            String batt9 = ReadVoltage.getText().toString();
            Batt9.setText(batt9);
            // Send to SpreadSheet
            time = dateFormat.format(dateTime);
            unit = namaGT.getText().toString();
            nomor = "9";
            tegangan = ReadVoltage.getText().toString();
            new SendRequest().execute();
        });

        Batt10 = findViewById(R.id.batt11);
        Batt10.setOnClickListener(v -> {
            String batt10 = ReadVoltage.getText().toString();
            Batt10.setText(batt10);
            // Send to SpreadSheet
            time = dateFormat.format(dateTime);
            unit = namaGT.getText().toString();
            nomor = "10";
            tegangan = ReadVoltage.getText().toString();
            new SendRequest().execute();
        });
    }
    private void Measure_Battery2() {

        Batt11 = findViewById(R.id.batt13);
        Batt11.setOnClickListener(v -> {
            String batt11 = ReadVoltage.getText().toString();
            Batt11.setText(batt11);
            // Send to SpreadSheet
            time = dateFormat.format(dateTime);
            unit = namaGT.getText().toString();
            nomor = "11";
            tegangan = ReadVoltage.getText().toString();
            new SendRequest().execute();
        });

        Batt12 = findViewById(R.id.batt14);
        Batt12.setOnClickListener(v -> {
            String batt12 = ReadVoltage.getText().toString();
            Batt12.setText(batt12);
            // Send to SpreadSheet
            time = dateFormat.format(dateTime);
            unit = namaGT.getText().toString();
            nomor = "12";
            tegangan = ReadVoltage.getText().toString();
            new SendRequest().execute();
        });

        Batt13 = findViewById(R.id.batt15);
        Batt13.setOnClickListener(v -> {
            String batt13 = ReadVoltage.getText().toString();
            Batt13.setText(batt13);
            // Send to SpreadSheet
            time = dateFormat.format(dateTime);
            unit = namaGT.getText().toString();
            nomor = "13";
            tegangan = ReadVoltage.getText().toString();
            new SendRequest().execute();
        });

        Batt14 = findViewById(R.id.batt16);
        Batt14.setOnClickListener(v -> {
            String batt14 = ReadVoltage.getText().toString();
            Batt14.setText(batt14);
            // Send to SpreadSheet
            time = dateFormat.format(dateTime);
            unit = namaGT.getText().toString();
            nomor = "14";
            tegangan = ReadVoltage.getText().toString();
            new SendRequest().execute();
        });

        Batt15 = findViewById(R.id.batt17);
        Batt15.setOnClickListener(v -> {
            String batt15 = ReadVoltage.getText().toString();
            Batt15.setText(batt15);
            // Send to SpreadSheet
            time = dateFormat.format(dateTime);
            unit = namaGT.getText().toString();
            nomor = "15";
            tegangan = ReadVoltage.getText().toString();
            new SendRequest().execute();
        });

        Batt16 = findViewById(R.id.batt19);
        Batt16.setOnClickListener(v -> {
            String batt16 = ReadVoltage.getText().toString();
            Batt16.setText(batt16);
            // Send to SpreadSheet
            time = dateFormat.format(dateTime);
            unit = namaGT.getText().toString();
            nomor = "16";
            tegangan = ReadVoltage.getText().toString();
            new SendRequest().execute();
        });

        Batt17 = findViewById(R.id.batt20);
        Batt17.setOnClickListener(v -> {
            String batt17 = ReadVoltage.getText().toString();
            Batt17.setText(batt17);
            // Send to SpreadSheet
            time = dateFormat.format(dateTime);
            unit = namaGT.getText().toString();
            nomor = "17";
            tegangan = ReadVoltage.getText().toString();
            new SendRequest().execute();
        });

        Batt18 = findViewById(R.id.batt21);
        Batt18.setOnClickListener(v -> {
            String batt18 = ReadVoltage.getText().toString();
            Batt18.setText(batt18);
            // Send to SpreadSheet
            time = dateFormat.format(dateTime);
            unit = namaGT.getText().toString();
            nomor = "18";
            tegangan = ReadVoltage.getText().toString();
            new SendRequest().execute();
        });

        Batt19 = findViewById(R.id.batt22);
        Batt19.setOnClickListener(v -> {
            String batt19 = ReadVoltage.getText().toString();
            Batt19.setText(batt19);
            // Send to SpreadSheet
            time = dateFormat.format(dateTime);
            unit = namaGT.getText().toString();
            nomor = "19";
            tegangan = ReadVoltage.getText().toString();
            new SendRequest().execute();
        });

        Batt20 = findViewById(R.id.batt23);
        Batt20.setOnClickListener(v -> {
            String batt20 = ReadVoltage.getText().toString();
            Batt20.setText(batt20);
            // Send to SpreadSheet
            time = dateFormat.format(dateTime);
            unit = namaGT.getText().toString();
            nomor = "20";
            tegangan = ReadVoltage.getText().toString();
            new SendRequest().execute();
        });
    }
    private void Measure_Battery3() {

        Batt21 = findViewById(R.id.batt25);
        Batt21.setOnClickListener(v -> {
            String batt21 = ReadVoltage.getText().toString();
            Batt21.setText(batt21);
            // Send to SpreadSheet
            time = dateFormat.format(dateTime);
            unit = namaGT.getText().toString();
            nomor = "21";
            tegangan = ReadVoltage.getText().toString();
            new SendRequest().execute();
        });

        Batt22 = findViewById(R.id.batt26);
        Batt22.setOnClickListener(v -> {
            String batt22 = ReadVoltage.getText().toString();
            Batt22.setText(batt22);
            // Send to SpreadSheet
            time = dateFormat.format(dateTime);
            unit = namaGT.getText().toString();
            nomor = "22";
            tegangan = ReadVoltage.getText().toString();
            new SendRequest().execute();
        });

        Batt23 = findViewById(R.id.batt27);
        Batt23.setOnClickListener(v -> {
            String batt23 = ReadVoltage.getText().toString();
            Batt23.setText(batt23);
            // Send to SpreadSheet
            time = dateFormat.format(dateTime);
            unit = namaGT.getText().toString();
            nomor = "23";
            tegangan = ReadVoltage.getText().toString();
            new SendRequest().execute();
        });

        Batt24 = findViewById(R.id.batt28);
        Batt24.setOnClickListener(v -> {
            String batt24 = ReadVoltage.getText().toString();
            Batt24.setText(batt24);
            // Send to SpreadSheet
            time = dateFormat.format(dateTime);
            unit = namaGT.getText().toString();
            nomor = "24";
            tegangan = ReadVoltage.getText().toString();
            new SendRequest().execute();
        });

        Batt25 = findViewById(R.id.batt29);
        Batt25.setOnClickListener(v -> {
            String batt25 = ReadVoltage.getText().toString();
            Batt25.setText(batt25);
            // Send to SpreadSheet
            time = dateFormat.format(dateTime);
            unit = namaGT.getText().toString();
            nomor = "25";
            tegangan = ReadVoltage.getText().toString();
            new SendRequest().execute();
        });

        Batt26 = findViewById(R.id.batt31);
        Batt26.setOnClickListener(v -> {
            String batt26 = ReadVoltage.getText().toString();
            Batt26.setText(batt26);
            // Send to SpreadSheet
            time = dateFormat.format(dateTime);
            unit = namaGT.getText().toString();
            nomor = "26";
            tegangan = ReadVoltage.getText().toString();
            new SendRequest().execute();
        });

        Batt27 = findViewById(R.id.batt32);
        Batt27.setOnClickListener(v -> {
            String batt27 = ReadVoltage.getText().toString();
            Batt27.setText(batt27);
            // Send to SpreadSheet
            time = dateFormat.format(dateTime);
            unit = namaGT.getText().toString();
            nomor = "27";
            tegangan = ReadVoltage.getText().toString();
            new SendRequest().execute();
        });

        Batt28 = findViewById(R.id.batt33);
        Batt28.setOnClickListener(v -> {
            String batt28 = ReadVoltage.getText().toString();
            Batt28.setText(batt28);
            // Send to SpreadSheet
            time = dateFormat.format(dateTime);
            unit = namaGT.getText().toString();
            nomor = "28";
            tegangan = ReadVoltage.getText().toString();
            new SendRequest().execute();
        });

        Batt29 = findViewById(R.id.batt34);
        Batt29.setOnClickListener(v -> {
            String batt29 = ReadVoltage.getText().toString();
            Batt29.setText(batt29);
            // Send to SpreadSheet
            time = dateFormat.format(dateTime);
            unit = namaGT.getText().toString();
            nomor = "29";
            tegangan = ReadVoltage.getText().toString();
            new SendRequest().execute();
        });

        Batt30 = findViewById(R.id.batt35);
        Batt30.setOnClickListener(v -> {
            String batt30 = ReadVoltage.getText().toString();
            Batt30.setText(batt30);
            // Send to SpreadSheet
            time = dateFormat.format(dateTime);
            unit = namaGT.getText().toString();
            nomor = "30";
            tegangan = ReadVoltage.getText().toString();
            new SendRequest().execute();
        });
    }
    private void Measure_Battery4() {

        Batt31 = findViewById(R.id.batt36);
        Batt31.setOnClickListener(v -> {
            String batt31 = ReadVoltage.getText().toString();
            Batt31.setText(batt31);
            // Send to SpreadSheet
            time = dateFormat.format(dateTime);
            unit = namaGT.getText().toString();
            nomor = "31";
            tegangan = ReadVoltage.getText().toString();
            new SendRequest().execute();
        });

        Batt32 = findViewById(R.id.batt37);
        Batt32.setOnClickListener(v -> {
            String batt32 = ReadVoltage.getText().toString();
            Batt32.setText(batt32);
            // Send to SpreadSheet
            time = dateFormat.format(dateTime);
            unit = namaGT.getText().toString();
            nomor = "32";
            tegangan = ReadVoltage.getText().toString();
            new SendRequest().execute();
        });

        Batt33 = findViewById(R.id.batt38);
        Batt33.setOnClickListener(v -> {
            String batt33 = ReadVoltage.getText().toString();
            Batt33.setText(batt33);
            // Send to SpreadSheet
            time = dateFormat.format(dateTime);
            unit = namaGT.getText().toString();
            nomor = "33";
            tegangan = ReadVoltage.getText().toString();
            new SendRequest().execute();
        });

        Batt34 = findViewById(R.id.batt39);
        Batt34.setOnClickListener(v -> {
            String batt34 = ReadVoltage.getText().toString();
            Batt34.setText(batt34);
            // Send to SpreadSheet
            time = dateFormat.format(dateTime);
            unit = namaGT.getText().toString();
            nomor = "34";
            tegangan = ReadVoltage.getText().toString();
            new SendRequest().execute();
        });

        Batt35 = findViewById(R.id.batt40);
        Batt35.setOnClickListener(v -> {
            String batt35 = ReadVoltage.getText().toString();
            Batt35.setText(batt35);
            // Send to SpreadSheet
            time = dateFormat.format(dateTime);
            unit = namaGT.getText().toString();
            nomor = "35";
            tegangan = ReadVoltage.getText().toString();
            new SendRequest().execute();
        });

        Batt36 = findViewById(R.id.batt41);
        Batt36.setOnClickListener(v -> {
            String batt36 = ReadVoltage.getText().toString();
            Batt36.setText(batt36);
            // Send to SpreadSheet
            time = dateFormat.format(dateTime);
            unit = namaGT.getText().toString();
            nomor = "36";
            tegangan = ReadVoltage.getText().toString();
            new SendRequest().execute();
        });

        Batt37 = findViewById(R.id.batt42);
        Batt37.setOnClickListener(v -> {
            String batt37 = ReadVoltage.getText().toString();
            Batt37.setText(batt37);
            // Send to SpreadSheet
            time = dateFormat.format(dateTime);
            unit = namaGT.getText().toString();
            nomor = "37";
            tegangan = ReadVoltage.getText().toString();
            new SendRequest().execute();
        });

        Batt38 = findViewById(R.id.batt43);
        Batt38.setOnClickListener(v -> {
            String batt38 = ReadVoltage.getText().toString();
            Batt38.setText(batt38);
            // Send to SpreadSheet
            time = dateFormat.format(dateTime);
            unit = namaGT.getText().toString();
            nomor = "38";
            tegangan = ReadVoltage.getText().toString();
            new SendRequest().execute();
        });

        Batt39 = findViewById(R.id.batt44);
        Batt39.setOnClickListener(v -> {
            String batt39 = ReadVoltage.getText().toString();
            Batt39.setText(batt39);
            // Send to SpreadSheet
            time = dateFormat.format(dateTime);
            unit = namaGT.getText().toString();
            nomor = "39";
            tegangan = ReadVoltage.getText().toString();
            new SendRequest().execute();
        });

        Batt40 = findViewById(R.id.batt45);
        Batt40.setOnClickListener(v -> {
            String batt40 = ReadVoltage.getText().toString();
            Batt40.setText(batt40);
            // Send to SpreadSheet
            time = dateFormat.format(dateTime);
            unit = namaGT.getText().toString();
            nomor = "40";
            tegangan = ReadVoltage.getText().toString();
            new SendRequest().execute();
        });
    }
    private void Measure_Battery5() {

        Batt41 = findViewById(R.id.batt46);
        Batt41.setOnClickListener(v -> {
            String batt41 = ReadVoltage.getText().toString();
            Batt41.setText(batt41);
            // Send to SpreadSheet
            time = dateFormat.format(dateTime);
            unit = namaGT.getText().toString();
            nomor = "41";
            tegangan = ReadVoltage.getText().toString();
            new SendRequest().execute();
        });

        Batt42 = findViewById(R.id.batt47);
        Batt42.setOnClickListener(v -> {
            String batt42 = ReadVoltage.getText().toString();
            Batt42.setText(batt42);
            // Send to SpreadSheet
            time = dateFormat.format(dateTime);
            unit = namaGT.getText().toString();
            nomor = "42";
            tegangan = ReadVoltage.getText().toString();
            new SendRequest().execute();
        });

        Batt43 = findViewById(R.id.batt48);
        Batt43.setOnClickListener(v -> {
            String batt43 = ReadVoltage.getText().toString();
            Batt43.setText(batt43);
            // Send to SpreadSheet
            time = dateFormat.format(dateTime);
            unit = namaGT.getText().toString();
            nomor = "43";
            tegangan = ReadVoltage.getText().toString();
            new SendRequest().execute();
        });

        Batt44 = findViewById(R.id.batt49);
        Batt44.setOnClickListener(v -> {
            String batt44 = ReadVoltage.getText().toString();
            Batt44.setText(batt44);
            // Send to SpreadSheet
            time = dateFormat.format(dateTime);
            unit = namaGT.getText().toString();
            nomor = "44";
            tegangan = ReadVoltage.getText().toString();
            new SendRequest().execute();
        });

        Batt45 = findViewById(R.id.batt50);
        Batt45.setOnClickListener(v -> {
            String batt45 = ReadVoltage.getText().toString();
            Batt45.setText(batt45);
            // Send to SpreadSheet
            time = dateFormat.format(dateTime);
            unit = namaGT.getText().toString();
            nomor = "45";
            tegangan = ReadVoltage.getText().toString();
            new SendRequest().execute();
        });

        Batt46 = findViewById(R.id.batt51);
        Batt46.setOnClickListener(v -> {
            String batt46 = ReadVoltage.getText().toString();
            Batt46.setText(batt46);
            // Send to SpreadSheet
            time = dateFormat.format(dateTime);
            unit = namaGT.getText().toString();
            nomor = "46";
            tegangan = ReadVoltage.getText().toString();
            new SendRequest().execute();
        });

        Batt47 = findViewById(R.id.batt52);
        Batt47.setOnClickListener(v -> {
            String batt47 = ReadVoltage.getText().toString();
            Batt47.setText(batt47);
            // Send to SpreadSheet
            time = dateFormat.format(dateTime);
            unit = namaGT.getText().toString();
            nomor = "47";
            tegangan = ReadVoltage.getText().toString();
            new SendRequest().execute();
        });

        Batt48 = findViewById(R.id.batt53);
        Batt48.setOnClickListener(v -> {
            String batt48 = ReadVoltage.getText().toString();
            Batt48.setText(batt48);
            // Send to SpreadSheet
            time = dateFormat.format(dateTime);
            unit = namaGT.getText().toString();
            nomor = "48";
            tegangan = ReadVoltage.getText().toString();
            new SendRequest().execute();
        });

        Batt49 = findViewById(R.id.batt54);
        Batt49.setOnClickListener(v -> {
            String batt49 = ReadVoltage.getText().toString();
            Batt49.setText(batt49);
            // Send to SpreadSheet
            time = dateFormat.format(dateTime);
            unit = namaGT.getText().toString();
            nomor = "49";
            tegangan = ReadVoltage.getText().toString();
            new SendRequest().execute();
        });

        Batt50 = findViewById(R.id.batt55);
        Batt50.setOnClickListener(v -> {
            String batt50 = ReadVoltage.getText().toString();
            Batt50.setText(batt50);
            // Send to SpreadSheet
            time = dateFormat.format(dateTime);
            unit = namaGT.getText().toString();
            nomor = "50";
            tegangan = ReadVoltage.getText().toString();
            new SendRequest().execute();
        });
    }
    private void Measure_Battery6() {

        Batt51 = findViewById(R.id.batt56);
        Batt51.setOnClickListener(v -> {
            String batt51 = ReadVoltage.getText().toString();
            Batt51.setText(batt51);
            // Send to SpreadSheet
            time = dateFormat.format(dateTime);
            unit = namaGT.getText().toString();
            nomor = "51";
            tegangan = ReadVoltage.getText().toString();
            new SendRequest().execute();
        });

        Batt52 = findViewById(R.id.batt57);
        Batt52.setOnClickListener(v -> {
            String batt52 = ReadVoltage.getText().toString();
            Batt52.setText(batt52);
            // Send to SpreadSheet
            time = dateFormat.format(dateTime);
            unit = namaGT.getText().toString();
            nomor = "52";
            tegangan = ReadVoltage.getText().toString();
            new SendRequest().execute();
        });

        Batt53 = findViewById(R.id.batt58);
        Batt53.setOnClickListener(v -> {
            String batt53 = ReadVoltage.getText().toString();
            Batt53.setText(batt53);
            // Send to SpreadSheet
            time = dateFormat.format(dateTime);
            unit = namaGT.getText().toString();
            nomor = "53";
            tegangan = ReadVoltage.getText().toString();
            new SendRequest().execute();
        });

        Batt54 = findViewById(R.id.batt59);
        Batt54.setOnClickListener(v -> {
            String batt54 = ReadVoltage.getText().toString();
            Batt54.setText(batt54);
            // Send to SpreadSheet
            time = dateFormat.format(dateTime);
            unit = namaGT.getText().toString();
            nomor = "54";
            tegangan = ReadVoltage.getText().toString();
            new SendRequest().execute();
        });

        Batt55 = findViewById(R.id.batt60);
        Batt55.setOnClickListener(v -> {
            String batt55 = ReadVoltage.getText().toString();
            Batt55.setText(batt55);
            // Send to SpreadSheet
            time = dateFormat.format(dateTime);
            unit = namaGT.getText().toString();
            nomor = "55";
            tegangan = ReadVoltage.getText().toString();
            new SendRequest().execute();
        });

        Batt56 = findViewById(R.id.batt61);
        Batt56.setOnClickListener(v -> {
            String batt56 = ReadVoltage.getText().toString();
            Batt56.setText(batt56);
            // Send to SpreadSheet
            time = dateFormat.format(dateTime);
            unit = namaGT.getText().toString();
            nomor = "56";
            tegangan = ReadVoltage.getText().toString();
            new SendRequest().execute();
        });

        Batt57 = findViewById(R.id.batt62);
        Batt57.setOnClickListener(v -> {
            String batt57 = ReadVoltage.getText().toString();
            Batt57.setText(batt57);
            // Send to SpreadSheet
            time = dateFormat.format(dateTime);
            unit = namaGT.getText().toString();
            nomor = "57";
            tegangan = ReadVoltage.getText().toString();
            new SendRequest().execute();
        });

        Batt58 = findViewById(R.id.batt63);
        Batt58.setOnClickListener(v -> {
            String batt58 = ReadVoltage.getText().toString();
            Batt58.setText(batt58);
            // Send to SpreadSheet
            time = dateFormat.format(dateTime);
            unit = namaGT.getText().toString();
            nomor = "58";
            tegangan = ReadVoltage.getText().toString();
            new SendRequest().execute();
        });

        Batt59 = findViewById(R.id.batt64);
        Batt59.setOnClickListener(v -> {
            String batt59 = ReadVoltage.getText().toString();
            Batt59.setText(batt59);
            // Send to SpreadSheet
            time = dateFormat.format(dateTime);
            unit = namaGT.getText().toString();
            nomor = "59";
            tegangan = ReadVoltage.getText().toString();
            new SendRequest().execute();
        });

        Batt60 = findViewById(R.id.batt65);
        Batt60.setOnClickListener(v -> {
            String batt60 = ReadVoltage.getText().toString();
            Batt60.setText(batt60);
            // Send to SpreadSheet
            time = dateFormat.format(dateTime);
            unit = namaGT.getText().toString();
            nomor = "60";
            tegangan = ReadVoltage.getText().toString();
            new SendRequest().execute();
        });
    }
    private void Measure_Battery7() {

        Batt61 = findViewById(R.id.batt66);
        Batt61.setOnClickListener(v -> {
            String batt61 = ReadVoltage.getText().toString();
            Batt61.setText(batt61);
            // Send to SpreadSheet
            time = dateFormat.format(dateTime);
            unit = namaGT.getText().toString();
            nomor = "61";
            tegangan = ReadVoltage.getText().toString();
            new SendRequest().execute();
        });

        Batt62 = findViewById(R.id.batt67);
        Batt62.setOnClickListener(v -> {
            String batt62 = ReadVoltage.getText().toString();
            Batt62.setText(batt62);
            // Send to SpreadSheet
            time = dateFormat.format(dateTime);
            unit = namaGT.getText().toString();
            nomor = "62";
            tegangan = ReadVoltage.getText().toString();
            new SendRequest().execute();
        });

        Batt63 = findViewById(R.id.batt68);
        Batt63.setOnClickListener(v -> {
            String batt63 = ReadVoltage.getText().toString();
            Batt63.setText(batt63);
            // Send to SpreadSheet
            time = dateFormat.format(dateTime);
            unit = namaGT.getText().toString();
            nomor = "63";
            tegangan = ReadVoltage.getText().toString();
            new SendRequest().execute();
        });

        Batt64 = findViewById(R.id.batt69);
        Batt64.setOnClickListener(v -> {
            String batt64 = ReadVoltage.getText().toString();
            Batt64.setText(batt64);
            // Send to SpreadSheet
            time = dateFormat.format(dateTime);
            unit = namaGT.getText().toString();
            nomor = "64";
            tegangan = ReadVoltage.getText().toString();
            new SendRequest().execute();
        });

        Batt65 = findViewById(R.id.batt70);
        Batt65.setOnClickListener(v -> {
            String batt65 = ReadVoltage.getText().toString();
            Batt65.setText(batt65);
            // Send to SpreadSheet
            time = dateFormat.format(dateTime);
            unit = namaGT.getText().toString();
            nomor = "65";
            tegangan = ReadVoltage.getText().toString();
            new SendRequest().execute();
        });

        Batt66 = findViewById(R.id.batt71);
        Batt66.setOnClickListener(v -> {
            String batt66 = ReadVoltage.getText().toString();
            Batt66.setText(batt66);
            // Send to SpreadSheet
            time = dateFormat.format(dateTime);
            unit = namaGT.getText().toString();
            nomor = "66";
            tegangan = ReadVoltage.getText().toString();
            new SendRequest().execute();
        });

        Batt67 = findViewById(R.id.batt72);
        Batt67.setOnClickListener(v -> {
            String batt67 = ReadVoltage.getText().toString();
            Batt67.setText(batt67);
            // Send to SpreadSheet
            time = dateFormat.format(dateTime);
            unit = namaGT.getText().toString();
            nomor = "67";
            tegangan = ReadVoltage.getText().toString();
            new SendRequest().execute();
        });

        Batt68 = findViewById(R.id.batt73);
        Batt68.setOnClickListener(v -> {
            String batt68 = ReadVoltage.getText().toString();
            Batt68.setText(batt68);
            // Send to SpreadSheet
            time = dateFormat.format(dateTime);
            unit = namaGT.getText().toString();
            nomor = "68";
            tegangan = ReadVoltage.getText().toString();
            new SendRequest().execute();
        });

        Batt69 = findViewById(R.id.batt74);
        Batt69.setOnClickListener(v -> {
            String batt69 = ReadVoltage.getText().toString();
            Batt69.setText(batt69);
            // Send to SpreadSheet
            time = dateFormat.format(dateTime);
            unit = namaGT.getText().toString();
            nomor = "69";
            tegangan = ReadVoltage.getText().toString();
            new SendRequest().execute();
        });

        Batt70 = findViewById(R.id.batt75);
        Batt70.setOnClickListener(v -> {
            String batt70 = ReadVoltage.getText().toString();
            Batt70.setText(batt70);
            // Send to SpreadSheet
            time = dateFormat.format(dateTime);
            unit = namaGT.getText().toString();
            nomor = "70";
            tegangan = ReadVoltage.getText().toString();
            new SendRequest().execute();
        });
    }
    private void Measure_Battery8() {

        Batt71 = findViewById(R.id.batt76);
        Batt71.setOnClickListener(v -> {
            String batt71 = ReadVoltage.getText().toString();
            Batt71.setText(batt71);
            // Send to SpreadSheet
            time = dateFormat.format(dateTime);
            unit = namaGT.getText().toString();
            nomor = "71";
            tegangan = ReadVoltage.getText().toString();
            new SendRequest().execute();
        });

        Batt72 = findViewById(R.id.batt77);
        Batt72.setOnClickListener(v -> {
            String batt72 = ReadVoltage.getText().toString();
            Batt72.setText(batt72);
            // Send to SpreadSheet
            time = dateFormat.format(dateTime);
            unit = namaGT.getText().toString();
            nomor = "72";
            tegangan = ReadVoltage.getText().toString();
            new SendRequest().execute();
        });

        Batt73 = findViewById(R.id.batt78);
        Batt73.setOnClickListener(v -> {
            String batt73 = ReadVoltage.getText().toString();
            Batt73.setText(batt73);
            // Send to SpreadSheet
            time = dateFormat.format(dateTime);
            unit = namaGT.getText().toString();
            nomor = "73";
            tegangan = ReadVoltage.getText().toString();
            new SendRequest().execute();
        });

        Batt74 = findViewById(R.id.batt79);
        Batt74.setOnClickListener(v -> {
            String batt74 = ReadVoltage.getText().toString();
            Batt74.setText(batt74);
            // Send to SpreadSheet
            time = dateFormat.format(dateTime);
            unit = namaGT.getText().toString();
            nomor = "74";
            tegangan = ReadVoltage.getText().toString();
            new SendRequest().execute();
        });

        Batt75 = findViewById(R.id.batt80);
        Batt75.setOnClickListener(v -> {
            String batt75 = ReadVoltage.getText().toString();
            Batt75.setText(batt75);
            // Send to SpreadSheet
            time = dateFormat.format(dateTime);
            unit = namaGT.getText().toString();
            nomor = "75";
            tegangan = ReadVoltage.getText().toString();
            new SendRequest().execute();
        });

        Batt76 = findViewById(R.id.batt81);
        Batt76.setOnClickListener(v -> {
            String batt76 = ReadVoltage.getText().toString();
            Batt76.setText(batt76);
            // Send to SpreadSheet
            time = dateFormat.format(dateTime);
            unit = namaGT.getText().toString();
            nomor = "76";
            tegangan = ReadVoltage.getText().toString();
            new SendRequest().execute();
        });

        Batt77 = findViewById(R.id.batt82);
        Batt77.setOnClickListener(v -> {
            String batt77 = ReadVoltage.getText().toString();
            Batt77.setText(batt77);
            // Send to SpreadSheet
            time = dateFormat.format(dateTime);
            unit = namaGT.getText().toString();
            nomor = "77";
            tegangan = ReadVoltage.getText().toString();
            new SendRequest().execute();
        });

        Batt78 = findViewById(R.id.batt83);
        Batt78.setOnClickListener(v -> {
            String batt78 = ReadVoltage.getText().toString();
            Batt78.setText(batt78);
            // Send to SpreadSheet
            time = dateFormat.format(dateTime);
            unit = namaGT.getText().toString();
            nomor = "78";
            tegangan = ReadVoltage.getText().toString();
            new SendRequest().execute();
        });

        Batt79 = findViewById(R.id.batt84);
        Batt79.setOnClickListener(v -> {
            String batt79 = ReadVoltage.getText().toString();
            Batt79.setText(batt79);
            // Send to SpreadSheet
            time = dateFormat.format(dateTime);
            unit = namaGT.getText().toString();
            nomor = "79";
            tegangan = ReadVoltage.getText().toString();
            new SendRequest().execute();
        });

        Batt80 = findViewById(R.id.batt85);
        Batt80.setOnClickListener(v -> {
            String batt80 = ReadVoltage.getText().toString();
            Batt80.setText(batt80);
            // Send to SpreadSheet
            time = dateFormat.format(dateTime);
            unit = namaGT.getText().toString();
            nomor = "80";
            tegangan = ReadVoltage.getText().toString();
            new SendRequest().execute();
        });
    }
    private void Measure_Battery9() {

        Batt81 = findViewById(R.id.batt86);
        Batt81.setOnClickListener(v -> {
            String batt81 = ReadVoltage.getText().toString();
            Batt81.setText(batt81);
            // Send to SpreadSheet
            time = dateFormat.format(dateTime);
            unit = namaGT.getText().toString();
            nomor = "81";
            tegangan = ReadVoltage.getText().toString();
            new SendRequest().execute();
        });

        Batt82 = findViewById(R.id.batt87);
        Batt82.setOnClickListener(v -> {
            String batt82 = ReadVoltage.getText().toString();
            Batt82.setText(batt82);
            // Send to SpreadSheet
            time = dateFormat.format(dateTime);
            unit = namaGT.getText().toString();
            nomor = "82";
            tegangan = ReadVoltage.getText().toString();
            new SendRequest().execute();
        });

        Batt83 = findViewById(R.id.batt88);
        Batt83.setOnClickListener(v -> {
            String batt83 = ReadVoltage.getText().toString();
            Batt83.setText(batt83);
            // Send to SpreadSheet
            time = dateFormat.format(dateTime);
            unit = namaGT.getText().toString();
            nomor = "83";
            tegangan = ReadVoltage.getText().toString();
            new SendRequest().execute();
        });

        Batt84 = findViewById(R.id.batt89);
        Batt84.setOnClickListener(v -> {
            String batt84 = ReadVoltage.getText().toString();
            Batt84.setText(batt84);
            // Send to SpreadSheet
            time = dateFormat.format(dateTime);
            unit = namaGT.getText().toString();
            nomor = "84";
            tegangan = ReadVoltage.getText().toString();
            new SendRequest().execute();
        });

        Batt85 = findViewById(R.id.batt90);
        Batt85.setOnClickListener(v -> {
            String batt85 = ReadVoltage.getText().toString();
            Batt85.setText(batt85);
            // Send to SpreadSheet
            time = dateFormat.format(dateTime);
            unit = namaGT.getText().toString();
            nomor = "85";
            tegangan = ReadVoltage.getText().toString();
            new SendRequest().execute();
        });

        Batt86 = findViewById(R.id.batt91);
        Batt86.setOnClickListener(v -> {
            String batt86 = ReadVoltage.getText().toString();
            Batt86.setText(batt86);
            // Send to SpreadSheet
            time = dateFormat.format(dateTime);
            unit = namaGT.getText().toString();
            nomor = "86";
            tegangan = ReadVoltage.getText().toString();
            new SendRequest().execute();
        });

        Batt87 = findViewById(R.id.batt92);
        Batt87.setOnClickListener(v -> {
            String batt87 = ReadVoltage.getText().toString();
            Batt87.setText(batt87);
            // Send to SpreadSheet
            time = dateFormat.format(dateTime);
            unit = namaGT.getText().toString();
            nomor = "87";
            tegangan = ReadVoltage.getText().toString();
            new SendRequest().execute();
        });

        Batt88 = findViewById(R.id.batt93);
        Batt88.setOnClickListener(v -> {
            String batt88 = ReadVoltage.getText().toString();
            Batt88.setText(batt88);
            // Send to SpreadSheet
            time = dateFormat.format(dateTime);
            unit = namaGT.getText().toString();
            nomor = "88";
            tegangan = ReadVoltage.getText().toString();
            new SendRequest().execute();
        });

        Batt89 = findViewById(R.id.batt94);
        Batt89.setOnClickListener(v -> {
            String batt89 = ReadVoltage.getText().toString();
            Batt89.setText(batt89);
            // Send to SpreadSheet
            time = dateFormat.format(dateTime);
            unit = namaGT.getText().toString();
            nomor = "89";
            tegangan = ReadVoltage.getText().toString();
            new SendRequest().execute();
        });

        Batt90 = findViewById(R.id.batt95);
        Batt90.setOnClickListener(v -> {
            String batt90 = ReadVoltage.getText().toString();
            Batt90.setText(batt90);
            // Send to SpreadSheet
            time = dateFormat.format(dateTime);
            unit = namaGT.getText().toString();
            nomor = "90";
            tegangan = ReadVoltage.getText().toString();
            new SendRequest().execute();
        });
    }
    private void Measure_Battery10() {

        Batt91 = findViewById(R.id.batt96);
        Batt91.setOnClickListener(v -> {
            String batt91 = ReadVoltage.getText().toString();
            Batt91.setText(batt91);
            // Send to SpreadSheet
            time = dateFormat.format(dateTime);
            unit = namaGT.getText().toString();
            nomor = "91";
            tegangan = ReadVoltage.getText().toString();
            new SendRequest().execute();
        });

        Batt92 = findViewById(R.id.batt97);
        Batt92.setOnClickListener(v -> {
            String batt92 = ReadVoltage.getText().toString();
            Batt92.setText(batt92);
            // Send to SpreadSheet
            time = dateFormat.format(dateTime);
            unit = namaGT.getText().toString();
            nomor = "92";
            tegangan = ReadVoltage.getText().toString();
            new SendRequest().execute();
        });

        Batt93 = findViewById(R.id.batt98);
        Batt93.setOnClickListener(v -> {
            String batt93 = ReadVoltage.getText().toString();
            Batt93.setText(batt93);
            // Send to SpreadSheet
            time = dateFormat.format(dateTime);
            unit = namaGT.getText().toString();
            nomor = "93";
            tegangan = ReadVoltage.getText().toString();
            new SendRequest().execute();
        });

        Batt94 = findViewById(R.id.batt99);
        Batt94.setOnClickListener(v -> {
            String batt94 = ReadVoltage.getText().toString();
            Batt94.setText(batt94);
            // Send to SpreadSheet
            time = dateFormat.format(dateTime);
            unit = namaGT.getText().toString();
            nomor = "94";
            tegangan = ReadVoltage.getText().toString();
            new SendRequest().execute();
        });

        Batt95 = findViewById(R.id.batt100);
        Batt95.setOnClickListener(v -> {
            String batt95 = ReadVoltage.getText().toString();
            Batt95.setText(batt95);
            // Send to SpreadSheet
            time = dateFormat.format(dateTime);
            unit = namaGT.getText().toString();
            nomor = "95";
            tegangan = ReadVoltage.getText().toString();
            new SendRequest().execute();
        });

        Batt96 = findViewById(R.id.batt101);
        Batt96.setOnClickListener(v -> {
            String batt96 = ReadVoltage.getText().toString();
            Batt96.setText(batt96);
            // Send to SpreadSheet
            time = dateFormat.format(dateTime);
            unit = namaGT.getText().toString();
            nomor = "96";
            tegangan = ReadVoltage.getText().toString();
            new SendRequest().execute();
        });

        Batt97 = findViewById(R.id.batt102);
        Batt97.setOnClickListener(v -> {
            String batt97 = ReadVoltage.getText().toString();
            Batt97.setText(batt97);
            // Send to SpreadSheet
            time = dateFormat.format(dateTime);
            unit = namaGT.getText().toString();
            nomor = "97";
            tegangan = ReadVoltage.getText().toString();
            new SendRequest().execute();
        });

        Batt98 = findViewById(R.id.batt103);
        Batt98.setOnClickListener(v -> {
            String batt98 = ReadVoltage.getText().toString();
            Batt98.setText(batt98);
            // Send to SpreadSheet
            time = dateFormat.format(dateTime);
            unit = namaGT.getText().toString();
            nomor = "98";
            tegangan = ReadVoltage.getText().toString();
            new SendRequest().execute();
        });

        Batt99 = findViewById(R.id.batt104);
        Batt99.setOnClickListener(v -> {
            String batt99 = ReadVoltage.getText().toString();
            Batt99.setText(batt99);
            // Send to SpreadSheet
            time = dateFormat.format(dateTime);
            unit = namaGT.getText().toString();
            nomor = "99";
            tegangan = ReadVoltage.getText().toString();
            new SendRequest().execute();
        });

        Batt100 = findViewById(R.id.batt105);
        Batt100.setOnClickListener(v -> {
            String batt100 = ReadVoltage.getText().toString();
            Batt100.setText(batt100);
            // Send to SpreadSheet
            time = dateFormat.format(dateTime);
            unit = namaGT.getText().toString();
            nomor = "100";
            tegangan = ReadVoltage.getText().toString();
            new SendRequest().execute();
        });
    }
    private void Measure_Battery11() {

        Batt101 = findViewById(R.id.batt106);
        Batt101.setOnClickListener(v -> {
            String batt101 = ReadVoltage.getText().toString();
            Batt101.setText(batt101);
            // Send to SpreadSheet
            time = dateFormat.format(dateTime);
            unit = namaGT.getText().toString();
            nomor = "101";
            tegangan = ReadVoltage.getText().toString();
            new SendRequest().execute();
        });

        Batt102 = findViewById(R.id.batt107);
        Batt102.setOnClickListener(v -> {
            String batt102 = ReadVoltage.getText().toString();
            Batt102.setText(batt102);
            // Send to SpreadSheet
            time = dateFormat.format(dateTime);
            unit = namaGT.getText().toString();
            nomor = "102";
            tegangan = ReadVoltage.getText().toString();
            new SendRequest().execute();
        });

        Batt103 = findViewById(R.id.batt108);
        Batt103.setOnClickListener(v -> {
            String batt103 = ReadVoltage.getText().toString();
            Batt103.setText(batt103);
            // Send to SpreadSheet
            time = dateFormat.format(dateTime);
            unit = namaGT.getText().toString();
            nomor = "103";
            tegangan = ReadVoltage.getText().toString();
            new SendRequest().execute();
        });

        Batt104 = findViewById(R.id.batt109);
        Batt104.setOnClickListener(v -> {
            String batt104 = ReadVoltage.getText().toString();
            Batt104.setText(batt104);
            // Send to SpreadSheet
            time = dateFormat.format(dateTime);
            unit = namaGT.getText().toString();
            nomor = "104";
            tegangan = ReadVoltage.getText().toString();
            new SendRequest().execute();
        });

        Batt105 = findViewById(R.id.batt110);
        Batt105.setOnClickListener(v -> {
            String batt105 = ReadVoltage.getText().toString();
            Batt105.setText(batt105);
            // Send to SpreadSheet
            time = dateFormat.format(dateTime);
            unit = namaGT.getText().toString();
            nomor = "105";
            tegangan = ReadVoltage.getText().toString();
            new SendRequest().execute();
        });

        Batt106 = findViewById(R.id.batt111);
        Batt106.setOnClickListener(v -> {
            String batt106 = ReadVoltage.getText().toString();
            Batt106.setText(batt106);
            // Send to SpreadSheet
            time = dateFormat.format(dateTime);
            unit = namaGT.getText().toString();
            nomor = "107";
            tegangan = ReadVoltage.getText().toString();
            new SendRequest().execute();
        });

        Batt107 = findViewById(R.id.batt112);
        Batt107.setOnClickListener(v -> {
            String batt107 = ReadVoltage.getText().toString();
            Batt107.setText(batt107);
            // Send to SpreadSheet
            time = dateFormat.format(dateTime);
            unit = namaGT.getText().toString();
            nomor = "107";
            tegangan = ReadVoltage.getText().toString();
            new SendRequest().execute();
        });

        Batt108 = findViewById(R.id.batt113);
        Batt108.setOnClickListener(v -> {
            String batt108 = ReadVoltage.getText().toString();
            Batt108.setText(batt108);
            // Send to SpreadSheet
            time = dateFormat.format(dateTime);
            unit = namaGT.getText().toString();
            nomor = "108";
            tegangan = ReadVoltage.getText().toString();
            new SendRequest().execute();
        });
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    private void Total_Clear() {

        /*String dataVoltage1 = Batt1.getText().toString();
        String[] pjb1 = dataVoltage1.split(",");
        String dataVoltage2 = Batt1.getText().toString();
        String[] pjb2 = dataVoltage2.split(",");
        String dataVoltage3 = Batt1.getText().toString();
        String[] pjb3 = dataVoltage3.split(",");
        String dataVoltage4 = Batt1.getText().toString();
        String[] pjb4 = dataVoltage4.split(",");
        String dataVoltage5 = Batt1.getText().toString();
        String[] pjb5 = dataVoltage5.split(",");*/

        float a1 = Float.parseFloat(Batt1.getText().toString());
        float a2 = Float.parseFloat(Batt2.getText().toString());
        float a3 = Float.parseFloat(Batt3.getText().toString());
        float a4 = Float.parseFloat(Batt4.getText().toString());
        float a5 = Float.parseFloat(Batt5.getText().toString());
        float a6 = Float.parseFloat(Batt6.getText().toString());
        float a7 = Float.parseFloat(Batt7.getText().toString());
        float a8 = Float.parseFloat(Batt8.getText().toString());
        float a9 = Float.parseFloat(Batt9.getText().toString());
        float a10 = Float.parseFloat(Batt10.getText().toString());

        float a11 = Float.parseFloat(Batt11.getText().toString());
        float a12 = Float.parseFloat(Batt12.getText().toString());
        float a13 = Float.parseFloat(Batt13.getText().toString());
        float a14 = Float.parseFloat(Batt14.getText().toString());
        float a15 = Float.parseFloat(Batt15.getText().toString());
        float a16 = Float.parseFloat(Batt16.getText().toString());
        float a17 = Float.parseFloat(Batt17.getText().toString());
        float a18 = Float.parseFloat(Batt18.getText().toString());
        float a19 = Float.parseFloat(Batt19.getText().toString());
        float a20 = Float.parseFloat(Batt20.getText().toString());

        float a21 = Float.parseFloat(Batt21.getText().toString());
        float a22 = Float.parseFloat(Batt22.getText().toString());
        float a23 = Float.parseFloat(Batt23.getText().toString());
        float a24 = Float.parseFloat(Batt24.getText().toString());
        float a25 = Float.parseFloat(Batt25.getText().toString());
        float a26 = Float.parseFloat(Batt26.getText().toString());
        float a27 = Float.parseFloat(Batt27.getText().toString());
        float a28 = Float.parseFloat(Batt28.getText().toString());
        float a29 = Float.parseFloat(Batt29.getText().toString());
        float a30 = Float.parseFloat(Batt30.getText().toString());

        float a31 = Float.parseFloat(Batt31.getText().toString());
        float a32 = Float.parseFloat(Batt32.getText().toString());
        float a33 = Float.parseFloat(Batt33.getText().toString());
        float a34 = Float.parseFloat(Batt34.getText().toString());
        float a35 = Float.parseFloat(Batt35.getText().toString());
        float a36 = Float.parseFloat(Batt36.getText().toString());
        float a37 = Float.parseFloat(Batt37.getText().toString());
        float a38 = Float.parseFloat(Batt38.getText().toString());
        float a39 = Float.parseFloat(Batt39.getText().toString());
        float a40 = Float.parseFloat(Batt40.getText().toString());

        float a41 = Float.parseFloat(Batt41.getText().toString());
        float a42 = Float.parseFloat(Batt42.getText().toString());
        float a43 = Float.parseFloat(Batt43.getText().toString());
        float a44 = Float.parseFloat(Batt44.getText().toString());
        float a45 = Float.parseFloat(Batt45.getText().toString());
        float a46 = Float.parseFloat(Batt46.getText().toString());
        float a47 = Float.parseFloat(Batt47.getText().toString());
        float a48 = Float.parseFloat(Batt48.getText().toString());
        float a49 = Float.parseFloat(Batt49.getText().toString());
        float a50 = Float.parseFloat(Batt50.getText().toString());

        float a51 = Float.parseFloat(Batt51.getText().toString());
        float a52 = Float.parseFloat(Batt52.getText().toString());
        float a53 = Float.parseFloat(Batt53.getText().toString());
        float a54 = Float.parseFloat(Batt54.getText().toString());
        float a55 = Float.parseFloat(Batt55.getText().toString());
        float a56 = Float.parseFloat(Batt56.getText().toString());
        float a57 = Float.parseFloat(Batt57.getText().toString());
        float a58 = Float.parseFloat(Batt58.getText().toString());
        float a59 = Float.parseFloat(Batt59.getText().toString());
        float a60 = Float.parseFloat(Batt60.getText().toString());

        float a61 = Float.parseFloat(Batt61.getText().toString());
        float a62 = Float.parseFloat(Batt62.getText().toString());
        float a63 = Float.parseFloat(Batt63.getText().toString());
        float a64 = Float.parseFloat(Batt64.getText().toString());
        float a65 = Float.parseFloat(Batt65.getText().toString());
        float a66 = Float.parseFloat(Batt66.getText().toString());
        float a67 = Float.parseFloat(Batt67.getText().toString());
        float a68 = Float.parseFloat(Batt68.getText().toString());
        float a69 = Float.parseFloat(Batt69.getText().toString());
        float a70 = Float.parseFloat(Batt70.getText().toString());

        float a71 = Float.parseFloat(Batt71.getText().toString());
        float a72 = Float.parseFloat(Batt72.getText().toString());
        float a73 = Float.parseFloat(Batt73.getText().toString());
        float a74 = Float.parseFloat(Batt74.getText().toString());
        float a75 = Float.parseFloat(Batt75.getText().toString());
        float a76 = Float.parseFloat(Batt76.getText().toString());
        float a77 = Float.parseFloat(Batt77.getText().toString());
        float a78 = Float.parseFloat(Batt78.getText().toString());
        float a79 = Float.parseFloat(Batt79.getText().toString());
        float a80 = Float.parseFloat(Batt80.getText().toString());

        float a81 = Float.parseFloat(Batt81.getText().toString());
        float a82 = Float.parseFloat(Batt82.getText().toString());
        float a83 = Float.parseFloat(Batt83.getText().toString());
        float a84 = Float.parseFloat(Batt84.getText().toString());
        float a85 = Float.parseFloat(Batt85.getText().toString());
        float a86 = Float.parseFloat(Batt86.getText().toString());
        float a87 = Float.parseFloat(Batt87.getText().toString());
        float a88 = Float.parseFloat(Batt88.getText().toString());
        float a89 = Float.parseFloat(Batt89.getText().toString());
        float a90 = Float.parseFloat(Batt90.getText().toString());

        float a91 = Float.parseFloat(Batt91.getText().toString());
        float a92 = Float.parseFloat(Batt92.getText().toString());
        float a93 = Float.parseFloat(Batt93.getText().toString());
        float a94 = Float.parseFloat(Batt94.getText().toString());
        float a95 = Float.parseFloat(Batt95.getText().toString());
        float a96 = Float.parseFloat(Batt96.getText().toString());
        float a97 = Float.parseFloat(Batt97.getText().toString());
        float a98 = Float.parseFloat(Batt98.getText().toString());
        float a99 = Float.parseFloat(Batt99.getText().toString());
        float a100 = Float.parseFloat(Batt100.getText().toString());

        float a101 = Float.parseFloat(Batt101.getText().toString());
        float a102 = Float.parseFloat(Batt102.getText().toString());
        float a103 = Float.parseFloat(Batt103.getText().toString());
        float a104 = Float.parseFloat(Batt104.getText().toString());
        float a105 = Float.parseFloat(Batt105.getText().toString());
        float a106 = Float.parseFloat(Batt106.getText().toString());
        float a107 = Float.parseFloat(Batt107.getText().toString());
        float a108 = Float.parseFloat(Batt108.getText().toString());

        tegangan_total = a1 + a2 + a3 + a4 + a5 + a6 + a7 + a8 + a9 + a10 +
                a11 + a12 + a13 + a14 + a15 + a16 + a17 + a18 + a19 + a20 +
                a21 + a22 + a23 + a24 + a25 + a26 + a27 + a28 + a29 + a30 +
                a31 + a32 + a33 + a34 + a35 + a36 + a37 + a38 + a39 + a40 +
                a41 + a42 + a43 + a44 + a45 + a46 + a47 + a48 + a49 + a50 +
                a51 + a52 + a53 + a54 + a55 + a56 + a57 + a58 + a59 + a60 +
                a61 + a62 + a63 + a64 + a65 + a66 + a67 + a68 + a69 + a70 +
                a71 + a72 + a73 + a74 + a75 + a76 + a77 + a78 + a79 + a80 +
                a81 + a82 + a83 + a84 + a85 + a86 + a87 + a88 + a89 + a90 +
                a91 + a92 + a93 + a94 + a95 + a96 + a97 + a98 + a99 + a100 +
                a101 + a102 + a103 + a104 + a105 + a106 + a107 + a108;


        CardView ClearAll =  findViewById(R.id.clearAll);
        ClearAll.setOnClickListener(v -> {
            Batt1.setText("0.00");
            Batt2.setText("0.00");
            Batt3.setText("0.00");
            Batt4.setText("0.00");
            Batt5.setText("0.00");
            Batt6.setText("0.00");
            Batt7.setText("0.00");
            Batt8.setText("0.00");
            Batt9.setText("0.00");
            Batt10.setText("0.00");

            Batt11.setText("0.00");
            Batt12.setText("0.00");
            Batt13.setText("0.00");
            Batt14.setText("0.00");
            Batt15.setText("0.00");
            Batt16.setText("0.00");
            Batt17.setText("0.00");
            Batt18.setText("0.00");
            Batt19.setText("0.00");
            Batt20.setText("0.00");

            Batt21.setText("0.00");
            Batt22.setText("0.00");
            Batt23.setText("0.00");
            Batt24.setText("0.00");
            Batt25.setText("0.00");
            Batt26.setText("0.00");
            Batt27.setText("0.00");
            Batt28.setText("0.00");
            Batt29.setText("0.00");
            Batt30.setText("0.00");

            Batt31.setText("0.00");
            Batt32.setText("0.00");
            Batt33.setText("0.00");
            Batt34.setText("0.00");
            Batt35.setText("0.00");
            Batt36.setText("0.00");
            Batt37.setText("0.00");
            Batt38.setText("0.00");
            Batt39.setText("0.00");
            Batt40.setText("0.00");

            Batt41.setText("0.00");
            Batt42.setText("0.00");
            Batt43.setText("0.00");
            Batt44.setText("0.00");
            Batt45.setText("0.00");
            Batt46.setText("0.00");
            Batt47.setText("0.00");
            Batt48.setText("0.00");
            Batt49.setText("0.00");
            Batt50.setText("0.00");

            Batt51.setText("0.00");
            Batt52.setText("0.00");
            Batt53.setText("0.00");
            Batt54.setText("0.00");
            Batt55.setText("0.00");
            Batt56.setText("0.00");
            Batt57.setText("0.00");
            Batt58.setText("0.00");
            Batt59.setText("0.00");
            Batt60.setText("0.00");

            Batt61.setText("0.00");
            Batt62.setText("0.00");
            Batt63.setText("0.00");
            Batt64.setText("0.00");
            Batt65.setText("0.00");
            Batt66.setText("0.00");
            Batt67.setText("0.00");
            Batt68.setText("0.00");
            Batt69.setText("0.00");
            Batt70.setText("0.00");

            Batt71.setText("0.00");
            Batt72.setText("0.00");
            Batt73.setText("0.00");
            Batt74.setText("0.00");
            Batt75.setText("0.00");
            Batt76.setText("0.00");
            Batt77.setText("0.00");
            Batt78.setText("0.00");
            Batt79.setText("0.00");
            Batt80.setText("0.00");

            Batt81.setText("0.00");
            Batt82.setText("0.00");
            Batt83.setText("0.00");
            Batt84.setText("0.00");
            Batt85.setText("0.00");
            Batt86.setText("0.00");
            Batt87.setText("0.00");
            Batt88.setText("0.00");
            Batt89.setText("0.00");
            Batt90.setText("0.00");

            Batt91.setText("0.00");
            Batt92.setText("0.00");
            Batt93.setText("0.00");
            Batt94.setText("0.00");
            Batt95.setText("0.00");
            Batt96.setText("0.00");
            Batt97.setText("0.00");
            Batt98.setText("0.00");
            Batt99.setText("0.00");
            Batt100.setText("0.00");

            Batt101.setText("0.00");
            Batt102.setText("0.00");
            Batt103.setText("0.00");
            Batt104.setText("0.00");
            Batt105.setText("0.00");
            Batt106.setText("0.00");
            Batt107.setText("0.00");
            Batt108.setText("0.00");

        });

    }

    public void digitalSignatureSPV() {
        dialogCustomTTD = new AlertDialog.Builder(BluetoothBattery.this);
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
            Log.d("Success", wallpaperDirectory.toString());
        }

        try {
            File f = new File(wallpaperDirectory, "ttdSPVPMbatteryBLE.jpg"); //Calendar.getInstance().getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(BluetoothBattery.this,
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

    public void digitalSignatureOPS() {
        dialogCustomTTD = new AlertDialog.Builder(BluetoothBattery.this);
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
            File f = new File(wallpaperDirectory,"ttdOperatorPMbattBLE.jpg"); //Calendar.getInstance().getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(BluetoothBattery.this,
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

    public void createdPDF() {

        String judul = "Pemeliharaan Battery Gas Turbine";
        //Image ttd tangan OPS
        String ImageFileOPS = DIRECTORY+"ttdOperatorPMbattBLE.jpg";
        Bitmap bitmapImageOPS = BitmapFactory.decodeFile(ImageFileOPS);
        //Image ttd tangan SPV
        String ImageFileSPV = DIRECTORY+"ttdSPVPMbatteryBLE.jpg";
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
        canvas.drawText(judul, 40, 270, paint ); // Judul PM
//        canvas.drawText(NoWO.getText().toString(),40,310, paint); // No WO
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setColor(Color.BLACK);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        paint.setTextSize(30);
        canvas.drawText(namaGT.getText().toString(), 1170/2, 270, paint); // Nama GT
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        paint.setColor(Color.BLACK);
        paint.setTextSize(30);
        dateFormat = new SimpleDateFormat("dd-MM-yy");
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
        canvas.drawText(operator.getText().toString(), 420, 1980-30, paint);

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

        /*if (ConditionBattery.getText().toString().equals("Good !"))
        {
            paint.setColor(Color.BLACK);
        }
        else if (ConditionBattery.getText().toString().equals("Bad !"))
        {
            paint.setColor(Color.RED);
        }
        else if (ConditionBattery.getText().toString().equals("Over !"))
        {
            paint.setColor(Color.YELLOW);
        }
        else {
            paint.setColor(Color.BLACK);
        } */

        //Tegangan Battery
        canvas.drawText(Batt1.getText().toString()+" V", 140, 400, paint); canvas.drawText(Batt21.getText().toString()+" V", 340, 400, paint);
        canvas.drawText(Batt2.getText().toString()+" V", 140, 440, paint); canvas.drawText(Batt22.getText().toString()+" V", 340, 440, paint);
        canvas.drawText(Batt3.getText().toString()+" V", 140, 480, paint); canvas.drawText(Batt23.getText().toString()+" V", 340, 480, paint);
        canvas.drawText(Batt4.getText().toString()+" V", 140, 520, paint); canvas.drawText(Batt24.getText().toString()+" V", 340, 520, paint);
        canvas.drawText(Batt5.getText().toString()+" V", 140, 560, paint); canvas.drawText(Batt25.getText().toString()+" V", 340, 560, paint);
        canvas.drawText(Batt6.getText().toString()+" V", 140, 600, paint); canvas.drawText(Batt26.getText().toString()+" V", 340, 600, paint);
        canvas.drawText(Batt7.getText().toString()+" V", 140, 640, paint); canvas.drawText(Batt27.getText().toString()+" V", 340, 640, paint);
        canvas.drawText(Batt8.getText().toString()+" V", 140, 680, paint); canvas.drawText(Batt28.getText().toString()+" V", 340, 680, paint);
        canvas.drawText(Batt9.getText().toString()+" V", 140, 720, paint); canvas.drawText(Batt29.getText().toString()+" V", 340, 720, paint);
        canvas.drawText(Batt10.getText().toString()+" V", 140, 760, paint); canvas.drawText(Batt30.getText().toString()+" V", 340, 760, paint);
        canvas.drawText(Batt11.getText().toString()+" V", 140, 800, paint); canvas.drawText(Batt31.getText().toString()+" V", 340, 800, paint);
        canvas.drawText(Batt12.getText().toString()+" V", 140, 840, paint); canvas.drawText(Batt32.getText().toString()+" V", 340, 840, paint);
        canvas.drawText(Batt13.getText().toString()+" V", 140, 880, paint); canvas.drawText(Batt33.getText().toString()+" V", 340, 880, paint);
        canvas.drawText(Batt14.getText().toString()+" V", 140, 920, paint); canvas.drawText(Batt34.getText().toString()+" V", 340, 920, paint);
        canvas.drawText(Batt15.getText().toString()+" V", 140, 960, paint); canvas.drawText(Batt35.getText().toString()+" V", 340, 960, paint);
        canvas.drawText(Batt16.getText().toString()+" V", 140, 1000, paint); canvas.drawText(Batt36.getText().toString()+" V", 340, 1000, paint);
        canvas.drawText(Batt17.getText().toString()+" V", 140, 1040, paint); canvas.drawText(Batt37.getText().toString()+" V", 340, 1040, paint);
        canvas.drawText(Batt18.getText().toString()+" V", 140, 1080, paint); canvas.drawText(Batt38.getText().toString()+" V", 340, 1080, paint);
        canvas.drawText(Batt19.getText().toString()+" V", 140, 1120, paint); canvas.drawText(Batt39.getText().toString()+" V", 340, 1120, paint);
        canvas.drawText(Batt20.getText().toString()+" V", 140, 1160, paint); canvas.drawText(Batt40.getText().toString()+" V", 340, 1160, paint);

        canvas.drawText(Batt41.getText().toString()+" V", 540, 400, paint); canvas.drawText(Batt61.getText().toString()+" V", 740, 400, paint);
        canvas.drawText(Batt42.getText().toString()+" V", 540, 440, paint); canvas.drawText(Batt62.getText().toString()+" V", 740, 440, paint);
        canvas.drawText(Batt43.getText().toString()+" V", 540, 480, paint); canvas.drawText(Batt63.getText().toString()+" V", 740, 480, paint);
        canvas.drawText(Batt44.getText().toString()+" V", 540, 520, paint); canvas.drawText(Batt64.getText().toString()+" V", 740, 520, paint);
        canvas.drawText(Batt45.getText().toString()+" V", 540, 560, paint); canvas.drawText(Batt65.getText().toString()+" V", 740, 560, paint);
        canvas.drawText(Batt46.getText().toString()+" V", 540, 600, paint); canvas.drawText(Batt66.getText().toString()+" V", 740, 600, paint);
        canvas.drawText(Batt47.getText().toString()+" V", 540, 640, paint); canvas.drawText(Batt67.getText().toString()+" V", 740, 640, paint);
        canvas.drawText(Batt48.getText().toString()+" V", 540, 680, paint); canvas.drawText(Batt68.getText().toString()+" V", 740, 680, paint);
        canvas.drawText(Batt49.getText().toString()+" V", 540, 720, paint); canvas.drawText(Batt69.getText().toString()+" V", 740, 720, paint);
        canvas.drawText(Batt50.getText().toString()+" V", 540, 760, paint); canvas.drawText(Batt70.getText().toString()+" V", 740, 760, paint);
        canvas.drawText(Batt51.getText().toString()+" V", 540, 800, paint); canvas.drawText(Batt71.getText().toString()+" V", 740, 800, paint);
        canvas.drawText(Batt52.getText().toString()+" V", 540, 840, paint); canvas.drawText(Batt72.getText().toString()+" V", 740, 840, paint);
        canvas.drawText(Batt53.getText().toString()+" V", 540, 880, paint); canvas.drawText(Batt73.getText().toString()+" V", 740, 880, paint);
        canvas.drawText(Batt54.getText().toString()+" V", 540, 920, paint); canvas.drawText(Batt74.getText().toString()+" V", 740, 920, paint);
        canvas.drawText(Batt55.getText().toString()+" V", 540, 960, paint); canvas.drawText(Batt75.getText().toString()+" V", 740, 960, paint);
        canvas.drawText(Batt56.getText().toString()+" V", 540, 1000, paint); canvas.drawText(Batt76.getText().toString()+" V", 740, 1000, paint);
        canvas.drawText(Batt57.getText().toString()+" V", 540, 1040, paint); canvas.drawText(Batt77.getText().toString()+" V", 740, 1040, paint);
        canvas.drawText(Batt58.getText().toString()+" V", 540, 1080, paint); canvas.drawText(Batt78.getText().toString()+" V", 740, 1080, paint);
        canvas.drawText(Batt59.getText().toString()+" V", 540, 1120, paint); canvas.drawText(Batt79.getText().toString()+" V", 740, 1120, paint);
        canvas.drawText(Batt60.getText().toString()+" V", 540, 1160, paint); canvas.drawText(Batt80.getText().toString()+" V", 740, 1160, paint);

        canvas.drawText(Batt81.getText().toString()+" V", 940, 400, paint); canvas.drawText(Batt101.getText().toString()+" V", 160, 1240, paint);
        canvas.drawText(Batt82.getText().toString()+" V", 940, 440, paint); canvas.drawText(Batt102.getText().toString()+" V", 160, 1280, paint);
        canvas.drawText(Batt83.getText().toString()+" V", 940, 480, paint); canvas.drawText(Batt103.getText().toString()+" V", 160, 1320, paint);
        canvas.drawText(Batt84.getText().toString()+" V", 940, 520, paint); canvas.drawText(Batt104.getText().toString()+" V", 160, 1360, paint);
        canvas.drawText(Batt85.getText().toString()+" V", 940, 560, paint); canvas.drawText(Batt105.getText().toString()+" V", 160, 1400, paint);
        canvas.drawText(Batt86.getText().toString()+" V", 940, 600, paint); canvas.drawText(Batt106.getText().toString()+" V", 160, 1440, paint);
        canvas.drawText(Batt87.getText().toString()+" V", 940, 640, paint); canvas.drawText(Batt107.getText().toString()+" V", 160, 1480, paint);
        canvas.drawText(Batt88.getText().toString()+" V", 940, 680, paint); canvas.drawText(Batt108.getText().toString()+" V", 160, 1520, paint);
        canvas.drawText(Batt89.getText().toString()+" V", 940, 720, paint);
        canvas.drawText(Batt90.getText().toString()+" V", 940, 760, paint);
        canvas.drawText(Batt91.getText().toString()+" V", 940, 800, paint);
        canvas.drawText(Batt92.getText().toString()+" V", 940, 840, paint);
        canvas.drawText(Batt93.getText().toString()+" V", 940, 880, paint);
        canvas.drawText(Batt94.getText().toString()+" V", 940, 920, paint);
        canvas.drawText(Batt95.getText().toString()+" V", 940, 960, paint);
        canvas.drawText(Batt96.getText().toString()+" V", 940, 1000, paint);
        canvas.drawText(Batt97.getText().toString()+" V", 940, 1040, paint);
        canvas.drawText(Batt98.getText().toString()+" V", 940, 1080, paint);
        canvas.drawText(Batt99.getText().toString()+" V", 940, 1120, paint);
        canvas.drawText(Batt100.getText().toString()+" V", 950, 1160, paint);


        //Total Voltage
        paint.setColor(Color.RED);
        paint.setTextSize(40f);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText(TotalVoltage.getText().toString(), 720, 1350, paint);
        //canvas.drawText(Hasil.getText().toString()+" VDC (Calc)", 720, 1400, paint);

        dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        pdfDocument.finishPage(page);

        //Pages 2 for Graph battery
        PdfDocument.PageInfo pageInfo2
                = new PdfDocument.PageInfo.Builder(1200, 2010, 1).create();
        PdfDocument.Page page2 = pdfDocument.startPage(pageInfo2);
        Canvas canvas2 = page2.getCanvas();

        canvas2.drawBitmap(PJBscale,50,75, paint); // Logo PJB
        //warna awal
        paint.setColor(Color.BLACK);
        // Garis tepi
        canvas2.drawLine(30, 30, 1170, 30, paint); // Garis Atas
        canvas2.drawLine(30, 30, 30, 1980, paint); // Garis Kiri
        canvas2.drawLine(1170, 30, 1170, 1980, paint); // Garis Kanan
        canvas2.drawLine(30, 1980, 1170, 1980, paint); // Garis Bawah

        //Garis Header
        canvas2.drawLine(30, 220, 1170, 220, paint);
        canvas2.drawLine(30, 230, 1170, 230, paint);
        canvas2.drawLine(265, 30, 265, 220, paint); // Kiri
        canvas2.drawLine(905, 30, 905, 220, paint); // Kanan

        // Garis NO Document
        canvas2.drawLine(905, 90, 1170, 90, paint);
        canvas2.drawLine(905, 160, 1170, 160, paint);
        canvas2.drawLine(905, 230, 1170, 230, paint);

        // Judul Header
        titlePaint.setTextAlign(Paint.Align.CENTER);
        titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        titlePaint.setColor(Color.BLACK);
        titlePaint.setTextSize(25f);
        canvas2.drawText("PT PEMBANGKITAN JAWA BALI UP MUARA TAWAR", 1170 / 2, 70, titlePaint);
        titlePaint.setTextSize(20f);
        canvas2.drawText("PJB INTEGRATED MANAGEMENT SYSTEM", 1170 / 2, 110, titlePaint);
        titlePaint.setTextSize(25f);
        canvas2.drawText("CEK LIST PREVENTIVE PEMELIHARAAN RUTIN ", 1170 / 2, 155, titlePaint);
        canvas2.drawText("BATTERY BANK 220VDC GT", 1170 / 2, 195, titlePaint);

        titlePaint.setTextSize(50f);
        titlePaint.setTextAlign(Paint.Align.LEFT);
        canvas2.save();
        canvas2.rotate(90f, 0, 1000);
        canvas2.drawText("Grafik Tegangan Battery " + namaGT.getText().toString(), -200, 0, titlePaint);
        canvas2.restore();

        titlePaint.setTextSize(18f);
        titlePaint.setTextAlign(Paint.Align.LEFT);
        canvas2.drawText("Nomor Dokumen : FMT-17.2.1", 912,65, titlePaint);
        canvas2.drawText("Revisi : 00", 912,135, titlePaint);
        canvas2.drawText("Tanggal Terbit : 20-08-2013", 912,195, titlePaint);

        String ImageGraph = DIRECTORY+"Graph Battery Bank "+namaGT.getText().toString() + " " + unitBatt.getText().toString()+" "+dateFormat.format(dateTime)+".png";
        Bitmap bitmapImageGraph = BitmapFactory.decodeFile(ImageGraph);

        Matrix matrix = new Matrix();
        matrix.setRotate(90);
        Bitmap bitmapRotate = Bitmap.createBitmap(bitmapImageGraph, 0, 0, bitmapImageGraph.getWidth(), bitmapImageGraph.getHeight(), matrix, true);

        canvas2.drawBitmap(Bitmap.createScaledBitmap(bitmapRotate,700,1700,false),200,250,paint );

        pdfDocument.finishPage(page2);


        File file = new File(Environment.getExternalStorageDirectory(), "/"+judul+" "+namaGT.getText().toString()+" "+dateFormat.format(dateTime)+".pdf");
        try {
            pdfDocument.writeTo(new FileOutputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }

        pdfDocument.close();
        Toast.makeText(BluetoothBattery.this, "PDF has Created", Toast.LENGTH_LONG).show();


        dateTime = new Date();
    }

    public void createdExcel() {
        // Untuk Tanggal
     /*   dateTime = new Date();
        dateFormat = new SimpleDateFormat("dd-MM-yy");

        // Main Core Excel
        Workbook workbook= new HSSFWorkbook();
        Sheet sheet = workbook.createSheet(namaGT.getText().toString()+" "+unitBatt.getText().toString()+" "+dateFormat.format(dateTime));

        // Header Judul
        Row row0 = sheet.createRow(0);
        Row row1 = sheet.createRow(0);
        Cell cell0 = row0.createCell(0);
        Cell cell1 = row1.createCell(1);
        cell0.setCellValue("No Battery");
        cell1.setCellValue("Tegangan Battery");

        //Main Content
        Row row1A = sheet.createRow(1);
        Row row1B = sheet.createRow(1);
        Cell cell1A = row1A.createCell(0);
        Cell cell1B = row1B.createCell(1);
        cell1A.setCellValue("1");
        cell1B.setCellValue(Batt1.getText().toString());

        Row row2A = sheet.createRow(2);
        Row row2B = sheet.createRow(2);
        Cell cell2A = row2A.createCell(0);
        Cell cell2B = row2B.createCell(1);
        cell2A.setCellValue("2");
        cell2B.setCellValue(Batt2.getText().toString());

        Row row3A = sheet.createRow(3);
        Row row3B = sheet.createRow(3);
        Cell cell3A = row3A.createCell(0);
        Cell cell3B = row3B.createCell(1);
        cell3A.setCellValue("3");
        cell3B.setCellValue(Batt3.getText().toString());

        Row row4A = sheet.createRow(4);
        Row row4B = sheet.createRow(4);
        Cell cell4A = row4A.createCell(0);
        Cell cell4B = row4B.createCell(1);
        cell4A.setCellValue("4");
        cell4B.setCellValue(Batt4.getText().toString());

        Row row5A = sheet.createRow(5);
        Row row5B = sheet.createRow(5);
        Cell cell5A = row5A.createCell(0);
        Cell cell5B = row5B.createCell(1);
        cell5A.setCellValue("5");
        cell5B.setCellValue(Batt5.getText().toString());

        Row row6A = sheet.createRow(6);
        Row row6B = sheet.createRow(6);
        Cell cell6A = row6A.createCell(0);
        Cell cell6B = row6B.createCell(1);
        cell6A.setCellValue("6");
        cell6B.setCellValue(Batt6.getText().toString());

        Row row7A = sheet.createRow(7);
        Row row7B = sheet.createRow(7);
        Cell cell7A = row7A.createCell(0);
        Cell cell7B = row7B.createCell(1);
        cell7A.setCellValue("7");
        cell7B.setCellValue(Batt7.getText().toString());

        Row row8A = sheet.createRow(8);
        Row row8B = sheet.createRow(8);
        Cell cell8A = row8A.createCell(0);
        Cell cell8B = row8B.createCell(1);
        cell8A.setCellValue("8");
        cell8B.setCellValue(Batt8.getText().toString());

        Row row9A = sheet.createRow(9);
        Row row9B = sheet.createRow(9);
        Cell cell9A = row9A.createCell(0);
        Cell cell9B = row9B.createCell(1);
        cell9A.setCellValue("9");
        cell9B.setCellValue(Batt9.getText().toString());

        Row row10A = sheet.createRow(10);
        Row row10B = sheet.createRow(10);
        Cell cell10A = row10A.createCell(0);
        Cell cell10B = row10B.createCell(1);
        cell10A.setCellValue("10");
        cell10B.setCellValue(Batt10.getText().toString());

        Row row11A = sheet.createRow(11);
        Row row11B = sheet.createRow(11);
        Cell cell11A = row11A.createCell(0);
        Cell cell11B = row11B.createCell(1);
        cell11A.setCellValue("11");
        cell11B.setCellValue(Batt11.getText().toString());

        Row row12A = sheet.createRow(12);
        Row row12B = sheet.createRow(12);
        Cell cell12A = row12A.createCell(0);
        Cell cell12B = row12B.createCell(1);
        cell12A.setCellValue("12");
        cell12B.setCellValue(Batt12.getText().toString());

        Row row13A = sheet.createRow(13);
        Row row13B = sheet.createRow(13);
        Cell cell13A = row13A.createCell(0);
        Cell cell13B = row13B.createCell(1);
        cell13A.setCellValue("13");
        cell13B.setCellValue(Batt13.getText().toString());

        Row row14A = sheet.createRow(14);
        Row row14B = sheet.createRow(14);
        Cell cell14A = row14A.createCell(0);
        Cell cell14B = row14B.createCell(1);
        cell14A.setCellValue("14");
        cell14B.setCellValue(Batt14.getText().toString());

        Row row15A = sheet.createRow(15);
        Row row15B = sheet.createRow(15);
        Cell cell15A = row15A.createCell(0);
        Cell cell15B = row15B.createCell(1);
        cell15A.setCellValue("15");
        cell15B.setCellValue(Batt15.getText().toString());

        Row row16A = sheet.createRow(16);
        Row row16B = sheet.createRow(16);
        Cell cell16A = row16A.createCell(0);
        Cell cell16B = row16B.createCell(1);
        cell16A.setCellValue("16");
        cell16B.setCellValue(Batt16.getText().toString());

        Row row17A = sheet.createRow(17);
        Row row17B = sheet.createRow(17);
        Cell cell17A = row17A.createCell(0);
        Cell cell17B = row17B.createCell(1);
        cell17A.setCellValue("17");
        cell17B.setCellValue(Batt17.getText().toString());

        Row row18A = sheet.createRow(18);
        Row row18B = sheet.createRow(18);
        Cell cell18A = row18A.createCell(0);
        Cell cell18B = row18B.createCell(1);
        cell18A.setCellValue("18");
        cell18B.setCellValue(Batt18.getText().toString());

        Row row19A = sheet.createRow(19);
        Row row19B = sheet.createRow(19);
        Cell cell19A = row19A.createCell(0);
        Cell cell19B = row19B.createCell(1);
        cell19A.setCellValue("19");
        cell19B.setCellValue(Batt19.getText().toString());

        Row row20A = sheet.createRow(20);
        Row row20B = sheet.createRow(20);
        Cell cell20A = row20A.createCell(0);
        Cell cell20B = row20B.createCell(1);
        cell20A.setCellValue("20");
        cell20B.setCellValue(Batt20.getText().toString());

        Row row21A = sheet.createRow(21);
        Row row21B = sheet.createRow(21);
        Cell cell21A = row21A.createCell(0);
        Cell cell21B = row21B.createCell(1);
        cell21A.setCellValue("21");
        cell21B.setCellValue(Batt21.getText().toString());

        Row row22A = sheet.createRow(22);
        Row row22B = sheet.createRow(22);
        Cell cell22A = row22A.createCell(0);
        Cell cell22B = row22B.createCell(1);
        cell22A.setCellValue("22");
        cell22B.setCellValue(Batt22.getText().toString());

        Row row23A = sheet.createRow(23);
        Row row23B = sheet.createRow(23);
        Cell cell23A = row23A.createCell(0);
        Cell cell23B = row23B.createCell(1);
        cell23A.setCellValue("23");
        cell23B.setCellValue(Batt23.getText().toString());

        Row row24A = sheet.createRow(24);
        Row row24B = sheet.createRow(24);
        Cell cell24A = row24A.createCell(0);
        Cell cell24B = row24B.createCell(1);
        cell24A.setCellValue("24");
        cell24B.setCellValue(Batt24.getText().toString());

        Row row25A = sheet.createRow(25);
        Row row25B = sheet.createRow(25);
        Cell cell25A = row25A.createCell(0);
        Cell cell25B = row25B.createCell(1);
        cell25A.setCellValue("25");
        cell25B.setCellValue(Batt25.getText().toString());

        Row row26A = sheet.createRow(26);
        Row row26B = sheet.createRow(26);
        Cell cell26A = row26A.createCell(0);
        Cell cell26B = row26B.createCell(1);
        cell26A.setCellValue("26");
        cell26B.setCellValue(Batt26.getText().toString());

        Row row27A = sheet.createRow(27);
        Row row27B = sheet.createRow(27);
        Cell cell27A = row27A.createCell(0);
        Cell cell27B = row27B.createCell(1);
        cell27A.setCellValue("27");
        cell27B.setCellValue(Batt27.getText().toString());

        Row row28A = sheet.createRow(28);
        Row row28B = sheet.createRow(28);
        Cell cell28A = row28A.createCell(0);
        Cell cell28B = row28B.createCell(1);
        cell28A.setCellValue("28");
        cell28B.setCellValue(Batt28.getText().toString());

        Row row29A = sheet.createRow(29);
        Row row29B = sheet.createRow(29);
        Cell cell29A = row29A.createCell(0);
        Cell cell29B = row29B.createCell(1);
        cell29A.setCellValue("29");
        cell29B.setCellValue(Batt29.getText().toString());

        Row row30A = sheet.createRow(30);
        Row row30B = sheet.createRow(30);
        Cell cell30A = row30A.createCell(0);
        Cell cell30B = row30B.createCell(1);
        cell30A.setCellValue("30");
        cell30B.setCellValue(Batt30.getText().toString());

        File file = new File(Environment.getExternalStorageDirectory(), "battery.xls");
        try {
            if (!file.exists()){
                file.createNewFile();
            }

            FileOutputStream fileOutputStream= new FileOutputStream(file);
            workbook.write(fileOutputStream);

            if (fileOutputStream!=null){
                fileOutputStream.flush();
                fileOutputStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

*/
    }

    // Custom Dialog Chart
    public void DialogGraphBatt()
    {
        final String GT = namaGT.getText().toString();
        final String Unit = unitBatt.getText().toString();
        if (GT.isEmpty()){
            namaGT.setError("Harus di isi ya");
            namaGT.requestFocus();
        }
        else if (Unit.isEmpty()) {
            unitBatt.setError("Harus di isi ya");
            unitBatt.requestFocus();
        }
        else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            dialogCustom = new AlertDialog.Builder(BluetoothBattery.this);
            inflater = getLayoutInflater();
            dialogView = inflater.inflate(R.layout.dialog_graph_ble, null);
            dialogCustom.setView(dialogView);
            dialogCustom.setCancelable(false);
            dialogCustom.setIcon(R.mipmap.ic_launcher);
            dialogCustom.setTitle("APEM GRAPH SYSTEMS");

            dateTime = new Date();
            dateFormat = new SimpleDateFormat("dd-MM-yyyy");

            chartBattery();

            dialogCustom.setNegativeButton("EXIT", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }
            });

            dialogCustom.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //chartGraphBatt.saveToGallery("Graph Battery Bank "+ unitBatt.getText().toString()+ " " + dateFormat.format(dateTime), "/ApemGraphSystem/" + namaGT.getText().toString() + "/", "TEST", Bitmap.CompressFormat.PNG, 90);
                    chartGraphBatt.saveToPath("Graph Battery Bank "+ namaGT.getText().toString() + " " + unitBatt.getText().toString()+ " " + dateFormat.format(dateTime), "/Signature/");
                    chartGraphBatt.setSaveEnabled(true);
                    Toast.makeText(BluetoothBattery.this, "Save Successfully", Toast.LENGTH_SHORT).show();
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // Default Potrait
                }
            });

            dialogCustom.show();
        }
    }

    public void chartBattery() {
        chartGraphBatt = dialogView.findViewById(R.id.batteryGraph);

        final String batt1 = Batt1.getText().toString();
        final String batt2 = Batt2.getText().toString();
        final String batt3 = Batt3.getText().toString();
        final String batt4 = Batt4.getText().toString();
        final String batt5 = Batt5.getText().toString();
        final String batt6 = Batt6.getText().toString();
        final String batt7 = Batt7.getText().toString();
        final String batt8 = Batt8.getText().toString();
        final String batt9 = Batt9.getText().toString();
        final String batt10 = Batt10.getText().toString();
        final String batt11 = Batt11.getText().toString();
        final String batt12 = Batt12.getText().toString();
        final String batt13 = Batt13.getText().toString();
        final String batt14 = Batt14.getText().toString();
        final String batt15 = Batt15.getText().toString();
        final String batt16 = Batt16.getText().toString();
        final String batt17 = Batt17.getText().toString();
        final String batt18 = Batt18.getText().toString();
        final String batt19 = Batt19.getText().toString();
        final String batt20 = Batt20.getText().toString();
        final String batt21 = Batt21.getText().toString();
        final String batt22 = Batt22.getText().toString();
        final String batt23 = Batt23.getText().toString();
        final String batt24 = Batt24.getText().toString();
        final String batt25 = Batt25.getText().toString();
        final String batt26 = Batt26.getText().toString();
        final String batt27 = Batt27.getText().toString();
        final String batt28 = Batt28.getText().toString();
        final String batt29 = Batt29.getText().toString();
        final String batt30 = Batt30.getText().toString();
        final String batt31 = Batt31.getText().toString();
        final String batt32 = Batt32.getText().toString();
        final String batt33 = Batt33.getText().toString();
        final String batt34 = Batt34.getText().toString();
        final String batt35 = Batt35.getText().toString();
        final String batt36 = Batt36.getText().toString();
        final String batt37 = Batt37.getText().toString();
        final String batt38 = Batt38.getText().toString();
        final String batt39 = Batt39.getText().toString();
        final String batt40 = Batt40.getText().toString();
        final String batt41 = Batt41.getText().toString();
        final String batt42 = Batt42.getText().toString();
        final String batt43 = Batt43.getText().toString();
        final String batt44 = Batt44.getText().toString();
        final String batt45 = Batt45.getText().toString();
        final String batt46 = Batt46.getText().toString();
        final String batt47 = Batt47.getText().toString();
        final String batt48 = Batt48.getText().toString();
        final String batt49 = Batt49.getText().toString();
        final String batt50 = Batt50.getText().toString();
        final String batt51 = Batt51.getText().toString();
        final String batt52 = Batt52.getText().toString();
        final String batt53 = Batt53.getText().toString();
        final String batt54 = Batt54.getText().toString();
        final String batt55 = Batt55.getText().toString();
        final String batt56 = Batt56.getText().toString();
        final String batt57 = Batt57.getText().toString();
        final String batt58 = Batt58.getText().toString();
        final String batt59 = Batt59.getText().toString();
        final String batt60 = Batt60.getText().toString();
        final String batt61 = Batt61.getText().toString();
        final String batt62 = Batt62.getText().toString();
        final String batt63 = Batt63.getText().toString();
        final String batt64 = Batt64.getText().toString();
        final String batt65 = Batt65.getText().toString();
        final String batt66 = Batt66.getText().toString();
        final String batt67 = Batt67.getText().toString();
        final String batt68 = Batt68.getText().toString();
        final String batt69 = Batt69.getText().toString();
        final String batt70 = Batt70.getText().toString();
        final String batt71 = Batt71.getText().toString();
        final String batt72 = Batt72.getText().toString();
        final String batt73 = Batt73.getText().toString();
        final String batt74 = Batt74.getText().toString();
        final String batt75 = Batt75.getText().toString();
        final String batt76 = Batt76.getText().toString();
        final String batt77 = Batt77.getText().toString();
        final String batt78 = Batt78.getText().toString();
        final String batt79 = Batt79.getText().toString();
        final String batt80 = Batt80.getText().toString();
        final String batt81 = Batt81.getText().toString();
        final String batt82 = Batt82.getText().toString();
        final String batt83 = Batt83.getText().toString();
        final String batt84 = Batt84.getText().toString();
        final String batt85 = Batt85.getText().toString();
        final String batt86 = Batt86.getText().toString();
        final String batt87 = Batt87.getText().toString();
        final String batt88 = Batt88.getText().toString();
        final String batt89 = Batt89.getText().toString();
        final String batt90 = Batt90.getText().toString();
        final String batt91 = Batt91.getText().toString();
        final String batt92 = Batt92.getText().toString();
        final String batt93 = Batt93.getText().toString();
        final String batt94 = Batt94.getText().toString();
        final String batt95 = Batt95.getText().toString();
        final String batt96 = Batt96.getText().toString();
        final String batt97 = Batt97.getText().toString();
        final String batt98 = Batt98.getText().toString();
        final String batt99 = Batt99.getText().toString();
        final String batt100 = Batt100.getText().toString();
        final String batt101 = Batt101.getText().toString();
        final String batt102 = Batt102.getText().toString();
        final String batt103 = Batt103.getText().toString();
        final String batt104 = Batt104.getText().toString();
        final String batt105 = Batt105.getText().toString();
        final String batt106 = Batt106.getText().toString();
        final String batt107 = Batt107.getText().toString();
        final String batt108 = Batt108.getText().toString();

        Batt1.setText(batt1);
        Batt2.setText(batt2);
        Batt3.setText(batt3);
        Batt4.setText(batt4);
        Batt5.setText(batt5);
        Batt6.setText(batt6);
        Batt7.setText(batt7);
        Batt8.setText(batt8);
        Batt9.setText(batt9);
        Batt10.setText(batt10);
        Batt11.setText(batt11);
        Batt12.setText(batt12);
        Batt13.setText(batt13);
        Batt14.setText(batt14);
        Batt15.setText(batt15);
        Batt16.setText(batt16);
        Batt17.setText(batt17);
        Batt18.setText(batt18);
        Batt19.setText(batt19);
        Batt20.setText(batt20);
        Batt21.setText(batt21);
        Batt22.setText(batt22);
        Batt23.setText(batt23);
        Batt24.setText(batt24);
        Batt25.setText(batt25);
        Batt26.setText(batt26);
        Batt27.setText(batt27);
        Batt28.setText(batt28);
        Batt29.setText(batt29);
        Batt30.setText(batt30);
        Batt31.setText(batt31);
        Batt32.setText(batt32);
        Batt33.setText(batt33);
        Batt34.setText(batt34);
        Batt35.setText(batt35);
        Batt36.setText(batt36);
        Batt37.setText(batt37);
        Batt38.setText(batt38);
        Batt39.setText(batt39);
        Batt40.setText(batt40);
        Batt41.setText(batt41);
        Batt42.setText(batt42);
        Batt43.setText(batt43);
        Batt44.setText(batt44);
        Batt45.setText(batt45);
        Batt46.setText(batt46);
        Batt47.setText(batt47);
        Batt48.setText(batt48);
        Batt49.setText(batt49);
        Batt50.setText(batt50);
        Batt51.setText(batt51);
        Batt52.setText(batt52);
        Batt53.setText(batt53);
        Batt54.setText(batt54);
        Batt55.setText(batt55);
        Batt56.setText(batt56);
        Batt57.setText(batt57);
        Batt58.setText(batt58);
        Batt59.setText(batt59);
        Batt60.setText(batt60);
        Batt61.setText(batt61);
        Batt62.setText(batt62);
        Batt63.setText(batt63);
        Batt64.setText(batt64);
        Batt65.setText(batt65);
        Batt66.setText(batt66);
        Batt67.setText(batt67);
        Batt68.setText(batt68);
        Batt69.setText(batt69);
        Batt70.setText(batt70);
        Batt71.setText(batt71);
        Batt72.setText(batt72);
        Batt73.setText(batt73);
        Batt74.setText(batt74);
        Batt75.setText(batt75);
        Batt76.setText(batt76);
        Batt77.setText(batt77);
        Batt78.setText(batt78);
        Batt79.setText(batt79);
        Batt80.setText(batt80);
        Batt81.setText(batt81);
        Batt82.setText(batt82);
        Batt83.setText(batt83);
        Batt84.setText(batt84);
        Batt85.setText(batt85);
        Batt86.setText(batt86);
        Batt87.setText(batt87);
        Batt88.setText(batt88);
        Batt89.setText(batt89);
        Batt90.setText(batt90);
        Batt91.setText(batt91);
        Batt92.setText(batt92);
        Batt93.setText(batt93);
        Batt94.setText(batt94);
        Batt95.setText(batt95);
        Batt96.setText(batt96);
        Batt97.setText(batt97);
        Batt98.setText(batt98);
        Batt99.setText(batt99);
        Batt100.setText(batt100);
        Batt101.setText(batt101);
        Batt102.setText(batt102);
        Batt103.setText(batt103);
        Batt104.setText(batt104);
        Batt105.setText(batt105);
        Batt106.setText(batt106);
        Batt107.setText(batt107);
        Batt108.setText(batt108);

        float ya1 = Float.parseFloat(Batt1.getText().toString());
        float ya2 = Float.parseFloat(Batt2.getText().toString());
        float ya3 = Float.parseFloat(Batt3.getText().toString());
        float ya4 = Float.parseFloat(Batt4.getText().toString());
        float ya5 = Float.parseFloat(Batt5.getText().toString());
        float ya6 = Float.parseFloat(Batt6.getText().toString());
        float ya7 = Float.parseFloat(Batt7.getText().toString());
        float ya8 = Float.parseFloat(Batt8.getText().toString());
        float ya9 = Float.parseFloat(Batt9.getText().toString());
        float ya10 = Float.parseFloat(Batt10.getText().toString());
        float yb1 = Float.parseFloat(Batt11.getText().toString());
        float yb2 = Float.parseFloat(Batt12.getText().toString());
        float yb3 = Float.parseFloat(Batt13.getText().toString());
        float yb4 = Float.parseFloat(Batt14.getText().toString());
        float yb5 = Float.parseFloat(Batt15.getText().toString());
        float yb6 = Float.parseFloat(Batt16.getText().toString());
        float yb7 = Float.parseFloat(Batt17.getText().toString());
        float yb8 = Float.parseFloat(Batt18.getText().toString());
        float yb9 = Float.parseFloat(Batt19.getText().toString());
        float yb10 = Float.parseFloat(Batt20.getText().toString());
        float yc1 = Float.parseFloat(Batt21.getText().toString());
        float yc2 = Float.parseFloat(Batt22.getText().toString());
        float yc3 = Float.parseFloat(Batt23.getText().toString());
        float yc4 = Float.parseFloat(Batt24.getText().toString());
        float yc5 = Float.parseFloat(Batt25.getText().toString());
        float yc6 = Float.parseFloat(Batt26.getText().toString());
        float yc7 = Float.parseFloat(Batt27.getText().toString());
        float yc8 = Float.parseFloat(Batt28.getText().toString());
        float yc9 = Float.parseFloat(Batt29.getText().toString());
        float yc10 = Float.parseFloat(Batt30.getText().toString());
        float yd1 = Float.parseFloat(Batt31.getText().toString());
        float yd2 = Float.parseFloat(Batt32.getText().toString());
        float yd3 = Float.parseFloat(Batt33.getText().toString());
        float yd4 = Float.parseFloat(Batt34.getText().toString());
        float yd5 = Float.parseFloat(Batt35.getText().toString());
        float yd6 = Float.parseFloat(Batt36.getText().toString());
        float yd7 = Float.parseFloat(Batt37.getText().toString());
        float yd8 = Float.parseFloat(Batt38.getText().toString());
        float yd9 = Float.parseFloat(Batt39.getText().toString());
        float yd10 = Float.parseFloat(Batt40.getText().toString());
        float ye1 = Float.parseFloat(Batt41.getText().toString());
        float ye2 = Float.parseFloat(Batt42.getText().toString());
        float ye3 = Float.parseFloat(Batt43.getText().toString());
        float ye4 = Float.parseFloat(Batt44.getText().toString());
        float ye5 = Float.parseFloat(Batt45.getText().toString());
        float ye6 = Float.parseFloat(Batt46.getText().toString());
        float ye7 = Float.parseFloat(Batt47.getText().toString());
        float ye8 = Float.parseFloat(Batt48.getText().toString());
        float ye9 = Float.parseFloat(Batt49.getText().toString());
        float ye10 = Float.parseFloat(Batt50.getText().toString());
        float yf1 = Float.parseFloat(Batt51.getText().toString());
        float yf2 = Float.parseFloat(Batt52.getText().toString());
        float yf3 = Float.parseFloat(Batt53.getText().toString());
        float yf4 = Float.parseFloat(Batt54.getText().toString());
        float yf5 = Float.parseFloat(Batt55.getText().toString());
        float yf6 = Float.parseFloat(Batt56.getText().toString());
        float yf7 = Float.parseFloat(Batt57.getText().toString());
        float yf8 = Float.parseFloat(Batt58.getText().toString());
        float yf9 = Float.parseFloat(Batt59.getText().toString());
        float yf10 = Float.parseFloat(Batt60.getText().toString());
        float yg1 = Float.parseFloat(Batt61.getText().toString());
        float yg2 = Float.parseFloat(Batt62.getText().toString());
        float yg3 = Float.parseFloat(Batt63.getText().toString());
        float yg4 = Float.parseFloat(Batt64.getText().toString());
        float yg5 = Float.parseFloat(Batt65.getText().toString());
        float yg6 = Float.parseFloat(Batt66.getText().toString());
        float yg7 = Float.parseFloat(Batt67.getText().toString());
        float yg8 = Float.parseFloat(Batt68.getText().toString());
        float yg9 = Float.parseFloat(Batt69.getText().toString());
        float yg10 = Float.parseFloat(Batt70.getText().toString());
        float yh1 = Float.parseFloat(Batt71.getText().toString());
        float yh2 = Float.parseFloat(Batt72.getText().toString());
        float yh3 = Float.parseFloat(Batt73.getText().toString());
        float yh4 = Float.parseFloat(Batt74.getText().toString());
        float yh5 = Float.parseFloat(Batt75.getText().toString());
        float yh6 = Float.parseFloat(Batt76.getText().toString());
        float yh7 = Float.parseFloat(Batt77.getText().toString());
        float yh8 = Float.parseFloat(Batt78.getText().toString());
        float yh9 = Float.parseFloat(Batt79.getText().toString());
        float yh10 = Float.parseFloat(Batt80.getText().toString());
        float yi1 = Float.parseFloat(Batt81.getText().toString());
        float yi2 = Float.parseFloat(Batt82.getText().toString());
        float yi3 = Float.parseFloat(Batt83.getText().toString());
        float yi4 = Float.parseFloat(Batt84.getText().toString());
        float yi5 = Float.parseFloat(Batt85.getText().toString());
        float yi6 = Float.parseFloat(Batt86.getText().toString());
        float yi7 = Float.parseFloat(Batt87.getText().toString());
        float yi8 = Float.parseFloat(Batt88.getText().toString());
        float yi9 = Float.parseFloat(Batt89.getText().toString());
        float yi10 = Float.parseFloat(Batt90.getText().toString());
        float yj1 = Float.parseFloat(Batt91.getText().toString());
        float yj2 = Float.parseFloat(Batt92.getText().toString());
        float yj3 = Float.parseFloat(Batt93.getText().toString());
        float yj4 = Float.parseFloat(Batt94.getText().toString());
        float yj5 = Float.parseFloat(Batt95.getText().toString());
        float yj6 = Float.parseFloat(Batt96.getText().toString());
        float yj7 = Float.parseFloat(Batt97.getText().toString());
        float yj8 = Float.parseFloat(Batt98.getText().toString());
        float yj9 = Float.parseFloat(Batt99.getText().toString());
        float yj10 = Float.parseFloat(Batt100.getText().toString());
        float yk1 = Float.parseFloat(Batt101.getText().toString());
        float yk2 = Float.parseFloat(Batt102.getText().toString());
        float yk3 = Float.parseFloat(Batt103.getText().toString());
        float yk4 = Float.parseFloat(Batt104.getText().toString());
        float yk5 = Float.parseFloat(Batt105.getText().toString());
        float yk6 = Float.parseFloat(Batt106.getText().toString());
        float yk7 = Float.parseFloat(Batt107.getText().toString());
        float yk8 = Float.parseFloat(Batt108.getText().toString());

        lineEntry = new ArrayList<Entry>();
        lineLabels = new ArrayList<String>();

        lineLabels.add(""); // Biarkan Kosong Index 0;
        // Main Chart
        lineEntry.add(new Entry(1, ya1));
        lineLabels.add("No1");
        lineEntry.add(new Entry(2, ya2));
        lineLabels.add("No2");
        lineEntry.add(new Entry(3, ya3));
        lineLabels.add("No3");
        lineEntry.add(new Entry(4, ya4));
        lineLabels.add("No4");
        lineEntry.add(new Entry(5, ya5));
        lineLabels.add("No5");
        lineEntry.add(new Entry(6, ya6));
        lineLabels.add("No6");
        lineEntry.add(new Entry(7, ya7));
        lineLabels.add("No7");
        lineEntry.add(new Entry(8, ya8));
        lineLabels.add("No8");
        lineEntry.add(new Entry(9, ya9));
        lineLabels.add("No9");
        lineEntry.add(new Entry(10, ya10));
        lineLabels.add("No10");
        // Main Chart
        lineEntry.add(new Entry(11, yb1));
        lineLabels.add("No11");
        lineEntry.add(new Entry(12, yb2));
        lineLabels.add("No12");
        lineEntry.add(new Entry(13, yb3));
        lineLabels.add("No13");
        lineEntry.add(new Entry(14, yb4));
        lineLabels.add("No14");
        lineEntry.add(new Entry(15, yb5));
        lineLabels.add("No15");
        lineEntry.add(new Entry(16, yb6));
        lineLabels.add("No16");
        lineEntry.add(new Entry(17, yb7));
        lineLabels.add("No17");
        lineEntry.add(new Entry(18, yb8));
        lineLabels.add("No18");
        lineEntry.add(new Entry(19, yb9));
        lineLabels.add("No19");
        lineEntry.add(new Entry(20, yb10));
        lineLabels.add("No20");
        // Main Chart
        lineEntry.add(new Entry(21, yc1));
        lineLabels.add("No21");
        lineEntry.add(new Entry(22, yc2));
        lineLabels.add("No22");
        lineEntry.add(new Entry(23, yc3));
        lineLabels.add("No23");
        lineEntry.add(new Entry(24, yc4));
        lineLabels.add("No24");
        lineEntry.add(new Entry(25, yc5));
        lineLabels.add("No25");
        lineEntry.add(new Entry(26, yc6));
        lineLabels.add("No26");
        lineEntry.add(new Entry(27, yc7));
        lineLabels.add("No27");
        lineEntry.add(new Entry(28, yc8));
        lineLabels.add("No28");
        lineEntry.add(new Entry(29, yc9));
        lineLabels.add("No29");
        lineEntry.add(new Entry(30, yc10));
        lineLabels.add("No30");
        // Main Chart
        lineEntry.add(new Entry(31, yd1));
        lineLabels.add("No31");
        lineEntry.add(new Entry(32, yd2));
        lineLabels.add("No32");
        lineEntry.add(new Entry(33, yd3));
        lineLabels.add("No33");
        lineEntry.add(new Entry(34, yd4));
        lineLabels.add("No34");
        lineEntry.add(new Entry(35, yd5));
        lineLabels.add("No35");
        lineEntry.add(new Entry(36, yd6));
        lineLabels.add("No36");
        lineEntry.add(new Entry(37, yd7));
        lineLabels.add("No37");
        lineEntry.add(new Entry(38, yd8));
        lineLabels.add("No38");
        lineEntry.add(new Entry(39, yd9));
        lineLabels.add("No39");
        lineEntry.add(new Entry(40, yd10));
        lineLabels.add("No40");
        // Main Chart
        lineEntry.add(new Entry(41, ye1));
        lineLabels.add("No41");
        lineEntry.add(new Entry(42, ye2));
        lineLabels.add("No42");
        lineEntry.add(new Entry(43, ye3));
        lineLabels.add("No43");
        lineEntry.add(new Entry(44, ye4));
        lineLabels.add("No44");
        lineEntry.add(new Entry(45, ye5));
        lineLabels.add("No45");
        lineEntry.add(new Entry(46, ye6));
        lineLabels.add("No46");
        lineEntry.add(new Entry(47, ye7));
        lineLabels.add("No47");
        lineEntry.add(new Entry(48, ye8));
        lineLabels.add("No48");
        lineEntry.add(new Entry(49, ye9));
        lineLabels.add("No49");
        lineEntry.add(new Entry(50, ye10));
        lineLabels.add("No50");
        // Main Chart
        lineEntry.add(new Entry(51, yf1));
        lineLabels.add("No51");
        lineEntry.add(new Entry(52, yf2));
        lineLabels.add("No52");
        lineEntry.add(new Entry(53, yf3));
        lineLabels.add("No53");
        lineEntry.add(new Entry(54, yf4));
        lineLabels.add("No54");
        lineEntry.add(new Entry(55, yf5));
        lineLabels.add("No55");
        lineEntry.add(new Entry(56, yf6));
        lineLabels.add("No56");
        lineEntry.add(new Entry(57, yf7));
        lineLabels.add("No57");
        lineEntry.add(new Entry(58, yf8));
        lineLabels.add("No58");
        lineEntry.add(new Entry(59, yf9));
        lineLabels.add("No59");
        lineEntry.add(new Entry(60, yf10));
        lineLabels.add("No60");
        // Main Chart
        lineEntry.add(new Entry(61, yg1));
        lineLabels.add("No61");
        lineEntry.add(new Entry(62, yg2));
        lineLabels.add("No62");
        lineEntry.add(new Entry(63, yg3));
        lineLabels.add("No63");
        lineEntry.add(new Entry(64, yg4));
        lineLabels.add("No64");
        lineEntry.add(new Entry(65, yg5));
        lineLabels.add("No65");
        lineEntry.add(new Entry(66, yg6));
        lineLabels.add("No66");
        lineEntry.add(new Entry(67, yg7));
        lineLabels.add("No67");
        lineEntry.add(new Entry(68, yg8));
        lineLabels.add("No68");
        lineEntry.add(new Entry(69, yg9));
        lineLabels.add("No69");
        lineEntry.add(new Entry(70, yg10));
        lineLabels.add("No70");
        // Main Chart
        lineEntry.add(new Entry(71, yh1));
        lineLabels.add("No71");
        lineEntry.add(new Entry(72, yh2));
        lineLabels.add("No72");
        lineEntry.add(new Entry(73, yh3));
        lineLabels.add("No73");
        lineEntry.add(new Entry(74, yh4));
        lineLabels.add("No74");
        lineEntry.add(new Entry(75, yh5));
        lineLabels.add("No75");
        lineEntry.add(new Entry(76, yh6));
        lineLabels.add("No76");
        lineEntry.add(new Entry(77, yh7));
        lineLabels.add("No77");
        lineEntry.add(new Entry(78, yh8));
        lineLabels.add("No78");
        lineEntry.add(new Entry(79, yh9));
        lineLabels.add("No79");
        lineEntry.add(new Entry(80, yh10));
        lineLabels.add("No80");
        // Main Chart
        lineEntry.add(new Entry(81, yi1));
        lineLabels.add("No81");
        lineEntry.add(new Entry(82, yi2));
        lineLabels.add("No82");
        lineEntry.add(new Entry(83, yi3));
        lineLabels.add("No83");
        lineEntry.add(new Entry(84, yi4));
        lineLabels.add("No84");
        lineEntry.add(new Entry(85, yi5));
        lineLabels.add("No85");
        lineEntry.add(new Entry(86, yi6));
        lineLabels.add("No86");
        lineEntry.add(new Entry(87, yi7));
        lineLabels.add("No87");
        lineEntry.add(new Entry(88, yi8));
        lineLabels.add("No88");
        lineEntry.add(new Entry(89, yi9));
        lineLabels.add("No89");
        lineEntry.add(new Entry(90, yi10));
        lineLabels.add("No90");
        // Main Chart
        lineEntry.add(new Entry(91, yj1));
        lineLabels.add("No91");
        lineEntry.add(new Entry(92, yj2));
        lineLabels.add("No92");
        lineEntry.add(new Entry(93, yj3));
        lineLabels.add("No93");
        lineEntry.add(new Entry(94, yj4));
        lineLabels.add("No94");
        lineEntry.add(new Entry(95, yj5));
        lineLabels.add("No95");
        lineEntry.add(new Entry(96, yj6));
        lineLabels.add("No96");
        lineEntry.add(new Entry(97, yj7));
        lineLabels.add("No97");
        lineEntry.add(new Entry(98, yj8));
        lineLabels.add("No98");
        lineEntry.add(new Entry(99, yj9));
        lineLabels.add("No99");
        lineEntry.add(new Entry(100, yj10));
        lineLabels.add("No100");
        // Main Chart
        lineEntry.add(new Entry(101, yk1));
        lineLabels.add("No101");
        lineEntry.add(new Entry(102, yk2));
        lineLabels.add("No102");
        lineEntry.add(new Entry(103, yk3));
        lineLabels.add("No103");
        lineEntry.add(new Entry(104, yk4));
        lineLabels.add("No104");
        lineEntry.add(new Entry(105, yk5));
        lineLabels.add("No105");
        lineEntry.add(new Entry(106, yk6));
        lineLabels.add("No106");
        lineEntry.add(new Entry(107, yk7));
        lineLabels.add("No107");
        lineEntry.add(new Entry(108, yk8));
        lineLabels.add("No108");

        lineDataSet = new LineDataSet(lineEntry, "Voltage Level");
        lineData = new LineData(lineDataSet);
        chartGraphBatt.animateY(1000);
        chartGraphBatt.getXAxis().setValueFormatter(
                new IndexAxisValueFormatter(lineLabels));
        YAxis yAxis = chartGraphBatt.getAxisLeft();
        yAxis.setAxisMinimum(1.5f); // start at zero
        yAxis.setAxisMaximum(2.8f); // the axis maximum is 3 Volt
        LimitLine limitA = new LimitLine(1.8f, "Minimum Voltage"); //garis batas
        limitA.setLineWidth(1f);
        limitA.setTextSize(5f);
        limitA.setLabelPosition(LimitLine.LimitLabelPosition.LEFT_BOTTOM);
        yAxis.addLimitLine(limitA);

        chartGraphBatt.getAxisLeft().setDrawGridLines(false);
        chartGraphBatt.getAxisRight().setDrawGridLines(false);
        chartGraphBatt.getAxisRight().setEnabled(false);
        chartGraphBatt.getAxisLeft().setEnabled(true);
        chartGraphBatt.getXAxis().setDrawGridLines(false);
        chartGraphBatt.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);

        chartGraphBatt.getDescription().setEnabled(false);

        lineDataSet.setColors(ColorTemplate.getHoloBlue());
        chartGraphBatt.setData(lineData);
    }

    public void dialogCalibrated() {
        dialogCustom = new AlertDialog.Builder(BluetoothBattery.this);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.dialog_calibrated_ble, null);
        dialogCustom.setView(dialogView);
        dialogCustom.setCancelable(false);
        dialogCustom.setIcon(R.mipmap.ic_launcher);

        voltageAVO = dialogView.findViewById(R.id.tegAVO);
        voltageAlat = dialogView.findViewById(R.id.tegAlat);
        selesihVolt = dialogView.findViewById(R.id.selisihVolt);
        errorVolt = dialogView.findViewById(R.id.errorCal);
        sendDataCalibrated = dialogView.findViewById(R.id.buttonSendCalibrated);
        buttonZeroCalibrated = dialogView.findViewById(R.id.buttonZeroCalibrated);
        calcButton = dialogView.findViewById(R.id.buttonCalculated);

        calcButton.setOnClickListener(view ->{
            String AVO = voltageAVO.getText().toString();
            String BLE = voltageAlat.getText().toString();
            if (AVO.isEmpty()){
                voltageAVO.setError("Field Empty!");voltageAVO.requestFocus();
            }
            else if (BLE.isEmpty()) {
                voltageAlat.setError("Field Empty");voltageAlat.requestFocus();
            }
            else {
                float AVO1 = Float.parseFloat(voltageAVO.getText().toString());
                float BLE1 = Float.parseFloat(voltageAlat.getText().toString());
                //float Selisih1 = Float.parseFloat(Selisih);
                //float Error1 = Float.parseFloat(Error);

                float selisih1 = AVO1 - BLE1;
                float percentError = ((AVO1 - BLE1) / AVO1) * 100;
                selesihVolt.setText(String.format("%.2f", selisih1));
                errorVolt.setText(String.format("%.2f", Math.abs(percentError)));
            }
        });

        sendDataCalibrated.setOnClickListener(view ->{
            //String sendData = selesihVolt.getText().toString();
            btActivity.send(selesihVolt.getText().toString());
            Toast.makeText(this, "Data calibrated has sent", Toast.LENGTH_SHORT).show();
        });

        buttonZeroCalibrated.setOnClickListener(view->{
            btActivity.send("0.00");
            Toast.makeText(this, "Zero calibrated has sent", Toast.LENGTH_SHORT).show();
        });


        dialogCustom.setNegativeButton("EXIT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        dialogCustom.show();
    }

    //Fungsi Untuk Jam dan Tanggal
    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Calendar c1 = Calendar.getInstance();
            SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy h:m:s a");
            String strdate1 = sdf1.format(c1.getTime());
            TextView txtdate1 = findViewById(R.id.tanggalmccac);
            txtdate1.setText(strdate1);

            //Continue Running
            Total_Clear();
            TotalVoltage.setText(String.format("%.2f", tegangan_total)+" V");
            handler.postDelayed(this, 1000);
        }
    };

    // Press Back Saved it !!
    @Override
    public void onBackPressed()
    {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Keluar dari PM Battery");
        // set pesan dari dialog
        alertDialogBuilder
                .setMessage("Klik Ya untuk keluar! Bluetooth Turn Off Auto")
                .setCancelable(false)
                .setPositiveButton("Ya",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // jika tombol diklik, maka akan menutup activity ini
                        savedPrefs();
                        Intent intent = new Intent(BluetoothBattery.this, SelectBLEbatt.class);
                        startActivity(intent);
                        btActivity.disconnect();
                        btActivity.disableBluetooth();
                        BluetoothBattery.this.finish();
                    }
                })
                .setNegativeButton("Tidak",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // jika tombol ini diklik, akan menutup dialog
                        // dan tidak terjadi apa2
                        dialog.cancel();
                    }
                });

        // membuat alert dialog dari builder
        AlertDialog alertDialog = alertDialogBuilder.create();

        // menampilkan alert dialog
        alertDialog.show();
    }

    public class SendRequest extends AsyncTask<String, Void, String> {

        protected void onPreExecute(){}
        protected String doInBackground(String... arg0) {
            try{
                URL url = new URL("https://script.google.com/macros/s/AKfycbzxH41GfnBkml4u8ZDSi7v20wTuRY0BFwBHfRe0qM6sIJnNEs90iOoXVkjYs7DgmFCf/exec");
                JSONObject postDataParams = new JSONObject();
                String id= "1sjVe5hv_zxTz3iOk__718kItXSOPD51dSE2-aafLnAA";

                postDataParams.put("time",time);
                postDataParams.put("unit",unit);
                postDataParams.put("nomor",nomor);
                postDataParams.put("tegangan",tegangan);
                postDataParams.put("kondisi",kondisi);
                postDataParams.put("id",id);

                Log.e("params",postDataParams.toString());

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));

                writer.flush();
                writer.close();
                os.close();

                int responseCode=conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuffer sb = new StringBuffer("");
                    String line = "";

                    while ((line = in.readLine()) != null) {

                        sb.append(line);
                        break;
                    }
                    in.close();
                    return sb.toString();
                }
                else {
                    return new String("false : "+responseCode);
                }
            }
            catch(Exception e){
                return new String("Exception: " + e.getMessage());
            }
        }
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getApplicationContext(), result,
                    Toast.LENGTH_SHORT).show();
        }
    }

    public String getPostDataString(JSONObject params) throws Exception {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        Iterator<String> itr = params.keys();
        while(itr.hasNext()){
            String key= itr.next();
            Object value = params.get(key);
            if (first)
                first = false;
            else
                result.append("&");
            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));
        }
        return result.toString();
    }
}
