package com.herwinlab.apem.pmgasturbine.batterybluetooth;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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
import com.herwinlab.apem.R;
import com.kyanogen.signatureview.SignatureView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import me.aflak.bluetooth.Bluetooth;

public class BluetoothBattery extends AppCompatActivity implements Bluetooth.CommunicationCallback {

    public String name;
    public Bluetooth btActivity;
    TextView ReadVoltage, ConditionBattery;
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

    public CardView graphVolage;

    public LinearLayout buttonSPVttd, buttonOPSttd, buttonCreatedPDF;

    ArrayList<Entry> lineEntry;
    ArrayList<String> lineLabels;
    LineDataSet lineDataSet;
    LineData lineData;
    protected LineChart chartGraphBatt;

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
            DialogGraphBatt();
        });

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
                ReadVoltage.setText(data);

                float Voltage = Float.parseFloat(data);
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
                //btActivity.disableBluetooth(); // untuk turn OFF Bluetooth
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

        Batt1 = findViewById(R.id.batt1);
        Batt1.setOnClickListener(v -> {
            String batt1 = ReadVoltage.getText().toString();
            Batt1.setText(batt1);
        });

        Batt2 = findViewById(R.id.batt2);
        Batt2.setOnClickListener(v -> {
            String batt2 = ReadVoltage.getText().toString();
            Batt2.setText(batt2);
        });

        Batt3 = findViewById(R.id.batt3);
        Batt3.setOnClickListener(v -> {
            String batt3 = ReadVoltage.getText().toString();
            Batt3.setText(batt3);
        });

        Batt4 = findViewById(R.id.batt4);
        Batt4.setOnClickListener(v -> {
            String batt4 = ReadVoltage.getText().toString();
            Batt4.setText(batt4);
        });

        Batt5 = findViewById(R.id.batt5);
        Batt5.setOnClickListener(v -> {
            String batt5 = ReadVoltage.getText().toString();
            Batt5.setText(batt5);
        });

        Batt6 = findViewById(R.id.batt7);
        Batt6.setOnClickListener(v -> {
            String batt6 = ReadVoltage.getText().toString();
            Batt6.setText(batt6);
        });

        Batt7 = findViewById(R.id.batt8);
        Batt7.setOnClickListener(v -> {
            String batt7 = ReadVoltage.getText().toString();
            Batt7.setText(batt7);
        });

        Batt8 = findViewById(R.id.batt9);
        Batt8.setOnClickListener(v -> {
            String batt8 = ReadVoltage.getText().toString();
            Batt8.setText(batt8);
        });

        Batt9 = findViewById(R.id.batt10);
        Batt9.setOnClickListener(v -> {
            String batt9 = ReadVoltage.getText().toString();
            Batt9.setText(batt9);
        });

        Batt10 = findViewById(R.id.batt11);
        Batt10.setOnClickListener(v -> {
            String batt10 = ReadVoltage.getText().toString();
            Batt10.setText(batt10);
        });
    }
    private void Measure_Battery2() {

        Batt11 = findViewById(R.id.batt13);
        Batt11.setOnClickListener(v -> {
            String batt11 = ReadVoltage.getText().toString();
            Batt11.setText(batt11);
        });

        Batt12 = findViewById(R.id.batt14);
        Batt12.setOnClickListener(v -> {
            String batt12 = ReadVoltage.getText().toString();
            Batt12.setText(batt12);
        });

        Batt13 = findViewById(R.id.batt15);
        Batt13.setOnClickListener(v -> {
            String batt13 = ReadVoltage.getText().toString();
            Batt13.setText(batt13);
        });

        Batt14 = findViewById(R.id.batt16);
        Batt14.setOnClickListener(v -> {
            String batt14 = ReadVoltage.getText().toString();
            Batt14.setText(batt14);
        });

        Batt15 = findViewById(R.id.batt17);
        Batt15.setOnClickListener(v -> {
            String batt15 = ReadVoltage.getText().toString();
            Batt15.setText(batt15);
        });

        Batt16 = findViewById(R.id.batt19);
        Batt16.setOnClickListener(v -> {
            String batt16 = ReadVoltage.getText().toString();
            Batt16.setText(batt16);
        });

        Batt17 = findViewById(R.id.batt20);
        Batt17.setOnClickListener(v -> {
            String batt17 = ReadVoltage.getText().toString();
            Batt17.setText(batt17);
        });

        Batt18 = findViewById(R.id.batt21);
        Batt18.setOnClickListener(v -> {
            String batt18 = ReadVoltage.getText().toString();
            Batt18.setText(batt18);
        });

        Batt19 = findViewById(R.id.batt22);
        Batt19.setOnClickListener(v -> {
            String batt19 = ReadVoltage.getText().toString();
            Batt19.setText(batt19);
        });

        Batt20 = findViewById(R.id.batt23);
        Batt20.setOnClickListener(v -> {
            String batt20 = ReadVoltage.getText().toString();
            Batt20.setText(batt20);
        });
    }
    private void Measure_Battery3() {

        Batt21 = findViewById(R.id.batt25);
        Batt21.setOnClickListener(v -> {
            String batt21 = ReadVoltage.getText().toString();
            Batt21.setText(batt21);
        });

        Batt22 = findViewById(R.id.batt26);
        Batt22.setOnClickListener(v -> {
            String batt22 = ReadVoltage.getText().toString();
            Batt22.setText(batt22);
        });

        Batt23 = findViewById(R.id.batt27);
        Batt23.setOnClickListener(v -> {
            String batt13 = ReadVoltage.getText().toString();
            Batt23.setText(batt13);
        });

        Batt24 = findViewById(R.id.batt28);
        Batt24.setOnClickListener(v -> {
            String batt24 = ReadVoltage.getText().toString();
            Batt24.setText(batt24);
        });

        Batt25 = findViewById(R.id.batt29);
        Batt25.setOnClickListener(v -> {
            String batt25 = ReadVoltage.getText().toString();
            Batt25.setText(batt25);
        });

        Batt26 = findViewById(R.id.batt31);
        Batt26.setOnClickListener(v -> {
            String batt26 = ReadVoltage.getText().toString();
            Batt26.setText(batt26);
        });

        Batt27 = findViewById(R.id.batt32);
        Batt27.setOnClickListener(v -> {
            String batt27 = ReadVoltage.getText().toString();
            Batt27.setText(batt27);
        });

        Batt28 = findViewById(R.id.batt33);
        Batt28.setOnClickListener(v -> {
            String batt28 = ReadVoltage.getText().toString();
            Batt28.setText(batt28);
        });

        Batt29 = findViewById(R.id.batt34);
        Batt29.setOnClickListener(v -> {
            String batt29 = ReadVoltage.getText().toString();
            Batt29.setText(batt29);
        });

        Batt30 = findViewById(R.id.batt35);
        Batt30.setOnClickListener(v -> {
            String batt30 = ReadVoltage.getText().toString();
            Batt30.setText(batt30);
        });
    }
    private void Measure_Battery4() {

        Batt31 = findViewById(R.id.batt36);
        Batt31.setOnClickListener(v -> {
            String batt31 = ReadVoltage.getText().toString();
            Batt31.setText(batt31);
        });

        Batt32 = findViewById(R.id.batt37);
        Batt32.setOnClickListener(v -> {
            String batt32 = ReadVoltage.getText().toString();
            Batt32.setText(batt32);
        });

        Batt33 = findViewById(R.id.batt38);
        Batt33.setOnClickListener(v -> {
            String batt33 = ReadVoltage.getText().toString();
            Batt33.setText(batt33);
        });

        Batt34 = findViewById(R.id.batt39);
        Batt34.setOnClickListener(v -> {
            String batt34 = ReadVoltage.getText().toString();
            Batt34.setText(batt34);
        });

        Batt35 = findViewById(R.id.batt40);
        Batt35.setOnClickListener(v -> {
            String batt35 = ReadVoltage.getText().toString();
            Batt35.setText(batt35);
        });

        Batt36 = findViewById(R.id.batt41);
        Batt36.setOnClickListener(v -> {
            String batt36 = ReadVoltage.getText().toString();
            Batt36.setText(batt36);
        });

        Batt37 = findViewById(R.id.batt42);
        Batt37.setOnClickListener(v -> {
            String batt37 = ReadVoltage.getText().toString();
            Batt37.setText(batt37);
        });

        Batt38 = findViewById(R.id.batt43);
        Batt38.setOnClickListener(v -> {
            String batt38 = ReadVoltage.getText().toString();
            Batt38.setText(batt38);
        });

        Batt39 = findViewById(R.id.batt44);
        Batt39.setOnClickListener(v -> {
            String batt39 = ReadVoltage.getText().toString();
            Batt39.setText(batt39);
        });

        Batt40 = findViewById(R.id.batt45);
        Batt40.setOnClickListener(v -> {
            String batt40 = ReadVoltage.getText().toString();
            Batt40.setText(batt40);
        });
    }
    private void Measure_Battery5() {

        Batt41 = findViewById(R.id.batt46);
        Batt41.setOnClickListener(v -> {
            String batt41 = ReadVoltage.getText().toString();
            Batt41.setText(batt41);
        });

        Batt42 = findViewById(R.id.batt47);
        Batt42.setOnClickListener(v -> {
            String batt42 = ReadVoltage.getText().toString();
            Batt42.setText(batt42);
        });

        Batt43 = findViewById(R.id.batt48);
        Batt43.setOnClickListener(v -> {
            String batt43 = ReadVoltage.getText().toString();
            Batt43.setText(batt43);
        });

        Batt44 = findViewById(R.id.batt49);
        Batt44.setOnClickListener(v -> {
            String batt44 = ReadVoltage.getText().toString();
            Batt44.setText(batt44);
        });

        Batt45 = findViewById(R.id.batt50);
        Batt45.setOnClickListener(v -> {
            String batt45 = ReadVoltage.getText().toString();
            Batt45.setText(batt45);
        });

        Batt46 = findViewById(R.id.batt51);
        Batt46.setOnClickListener(v -> {
            String batt46 = ReadVoltage.getText().toString();
            Batt46.setText(batt46);
        });

        Batt47 = findViewById(R.id.batt52);
        Batt47.setOnClickListener(v -> {
            String batt47 = ReadVoltage.getText().toString();
            Batt47.setText(batt47);
        });

        Batt48 = findViewById(R.id.batt53);
        Batt48.setOnClickListener(v -> {
            String batt48 = ReadVoltage.getText().toString();
            Batt48.setText(batt48);
        });

        Batt49 = findViewById(R.id.batt54);
        Batt49.setOnClickListener(v -> {
            String batt49 = ReadVoltage.getText().toString();
            Batt49.setText(batt49);
        });

        Batt50 = findViewById(R.id.batt55);
        Batt50.setOnClickListener(v -> {
            String batt50 = ReadVoltage.getText().toString();
            Batt50.setText(batt50);
        });
    }
    private void Measure_Battery6() {

        Batt51 = findViewById(R.id.batt56);
        Batt51.setOnClickListener(v -> {
            String batt51 = ReadVoltage.getText().toString();
            Batt51.setText(batt51);
        });

        Batt52 = findViewById(R.id.batt57);
        Batt52.setOnClickListener(v -> {
            String batt52 = ReadVoltage.getText().toString();
            Batt52.setText(batt52);
        });

        Batt53 = findViewById(R.id.batt58);
        Batt53.setOnClickListener(v -> {
            String batt53 = ReadVoltage.getText().toString();
            Batt53.setText(batt53);
        });

        Batt54 = findViewById(R.id.batt59);
        Batt54.setOnClickListener(v -> {
            String batt54 = ReadVoltage.getText().toString();
            Batt54.setText(batt54);
        });

        Batt55 = findViewById(R.id.batt60);
        Batt55.setOnClickListener(v -> {
            String batt55 = ReadVoltage.getText().toString();
            Batt55.setText(batt55);
        });

        Batt56 = findViewById(R.id.batt61);
        Batt56.setOnClickListener(v -> {
            String batt56 = ReadVoltage.getText().toString();
            Batt56.setText(batt56);
        });

        Batt57 = findViewById(R.id.batt62);
        Batt57.setOnClickListener(v -> {
            String batt57 = ReadVoltage.getText().toString();
            Batt57.setText(batt57);
        });

        Batt58 = findViewById(R.id.batt63);
        Batt58.setOnClickListener(v -> {
            String batt58 = ReadVoltage.getText().toString();
            Batt58.setText(batt58);
        });

        Batt59 = findViewById(R.id.batt64);
        Batt59.setOnClickListener(v -> {
            String batt59 = ReadVoltage.getText().toString();
            Batt59.setText(batt59);
        });

        Batt60 = findViewById(R.id.batt65);
        Batt60.setOnClickListener(v -> {
            String batt60 = ReadVoltage.getText().toString();
            Batt60.setText(batt60);
        });
    }
    private void Measure_Battery7() {

        Batt61 = findViewById(R.id.batt66);
        Batt61.setOnClickListener(v -> {
            String batt61 = ReadVoltage.getText().toString();
            Batt61.setText(batt61);
        });

        Batt62 = findViewById(R.id.batt67);
        Batt62.setOnClickListener(v -> {
            String batt62 = ReadVoltage.getText().toString();
            Batt62.setText(batt62);
        });

        Batt63 = findViewById(R.id.batt68);
        Batt63.setOnClickListener(v -> {
            String batt63 = ReadVoltage.getText().toString();
            Batt63.setText(batt63);
        });

        Batt64 = findViewById(R.id.batt69);
        Batt64.setOnClickListener(v -> {
            String batt64 = ReadVoltage.getText().toString();
            Batt64.setText(batt64);
        });

        Batt65 = findViewById(R.id.batt70);
        Batt65.setOnClickListener(v -> {
            String batt65 = ReadVoltage.getText().toString();
            Batt65.setText(batt65);
        });

        Batt66 = findViewById(R.id.batt71);
        Batt66.setOnClickListener(v -> {
            String batt66 = ReadVoltage.getText().toString();
            Batt66.setText(batt66);
        });

        Batt67 = findViewById(R.id.batt72);
        Batt67.setOnClickListener(v -> {
            String batt67 = ReadVoltage.getText().toString();
            Batt67.setText(batt67);
        });

        Batt68 = findViewById(R.id.batt73);
        Batt68.setOnClickListener(v -> {
            String batt68 = ReadVoltage.getText().toString();
            Batt68.setText(batt68);
        });

        Batt69 = findViewById(R.id.batt74);
        Batt69.setOnClickListener(v -> {
            String batt69 = ReadVoltage.getText().toString();
            Batt69.setText(batt69);
        });

        Batt70 = findViewById(R.id.batt75);
        Batt70.setOnClickListener(v -> {
            String batt70 = ReadVoltage.getText().toString();
            Batt70.setText(batt70);
        });
    }
    private void Measure_Battery8() {

        Batt71 = findViewById(R.id.batt76);
        Batt71.setOnClickListener(v -> {
            String batt71 = ReadVoltage.getText().toString();
            Batt71.setText(batt71);
        });

        Batt72 = findViewById(R.id.batt77);
        Batt72.setOnClickListener(v -> {
            String batt72 = ReadVoltage.getText().toString();
            Batt72.setText(batt72);
        });

        Batt73 = findViewById(R.id.batt78);
        Batt73.setOnClickListener(v -> {
            String batt73 = ReadVoltage.getText().toString();
            Batt73.setText(batt73);
        });

        Batt74 = findViewById(R.id.batt79);
        Batt74.setOnClickListener(v -> {
            String batt74 = ReadVoltage.getText().toString();
            Batt74.setText(batt74);
        });

        Batt75 = findViewById(R.id.batt80);
        Batt75.setOnClickListener(v -> {
            String batt75 = ReadVoltage.getText().toString();
            Batt75.setText(batt75);
        });

        Batt76 = findViewById(R.id.batt81);
        Batt76.setOnClickListener(v -> {
            String batt76 = ReadVoltage.getText().toString();
            Batt76.setText(batt76);
        });

        Batt77 = findViewById(R.id.batt82);
        Batt77.setOnClickListener(v -> {
            String batt77 = ReadVoltage.getText().toString();
            Batt77.setText(batt77);
        });

        Batt78 = findViewById(R.id.batt83);
        Batt78.setOnClickListener(v -> {
            String batt78 = ReadVoltage.getText().toString();
            Batt78.setText(batt78);
        });

        Batt79 = findViewById(R.id.batt84);
        Batt79.setOnClickListener(v -> {
            String batt79 = ReadVoltage.getText().toString();
            Batt79.setText(batt79);
        });

        Batt80 = findViewById(R.id.batt85);
        Batt80.setOnClickListener(v -> {
            String batt80 = ReadVoltage.getText().toString();
            Batt80.setText(batt80);
        });
    }
    private void Measure_Battery9() {

        Batt81 = findViewById(R.id.batt86);
        Batt81.setOnClickListener(v -> {
            String batt81 = ReadVoltage.getText().toString();
            Batt81.setText(batt81);
        });

        Batt82 = findViewById(R.id.batt87);
        Batt82.setOnClickListener(v -> {
            String batt82 = ReadVoltage.getText().toString();
            Batt82.setText(batt82);
        });

        Batt83 = findViewById(R.id.batt88);
        Batt83.setOnClickListener(v -> {
            String batt83 = ReadVoltage.getText().toString();
            Batt83.setText(batt83);
        });

        Batt84 = findViewById(R.id.batt89);
        Batt84.setOnClickListener(v -> {
            String batt84 = ReadVoltage.getText().toString();
            Batt84.setText(batt84);
        });

        Batt85 = findViewById(R.id.batt90);
        Batt85.setOnClickListener(v -> {
            String batt85 = ReadVoltage.getText().toString();
            Batt85.setText(batt85);
        });

        Batt86 = findViewById(R.id.batt91);
        Batt86.setOnClickListener(v -> {
            String batt86 = ReadVoltage.getText().toString();
            Batt86.setText(batt86);
        });

        Batt87 = findViewById(R.id.batt92);
        Batt87.setOnClickListener(v -> {
            String batt87 = ReadVoltage.getText().toString();
            Batt87.setText(batt87);
        });

        Batt88 = findViewById(R.id.batt93);
        Batt88.setOnClickListener(v -> {
            String batt88 = ReadVoltage.getText().toString();
            Batt88.setText(batt88);
        });

        Batt89 = findViewById(R.id.batt94);
        Batt89.setOnClickListener(v -> {
            String batt89 = ReadVoltage.getText().toString();
            Batt89.setText(batt89);
        });

        Batt90 = findViewById(R.id.batt95);
        Batt90.setOnClickListener(v -> {
            String batt90 = ReadVoltage.getText().toString();
            Batt90.setText(batt90);
        });
    }
    private void Measure_Battery10() {

        Batt91 = findViewById(R.id.batt96);
        Batt91.setOnClickListener(v -> {
            String batt91 = ReadVoltage.getText().toString();
            Batt91.setText(batt91);
        });

        Batt92 = findViewById(R.id.batt97);
        Batt92.setOnClickListener(v -> {
            String batt92 = ReadVoltage.getText().toString();
            Batt92.setText(batt92);
        });

        Batt93 = findViewById(R.id.batt98);
        Batt93.setOnClickListener(v -> {
            String batt93 = ReadVoltage.getText().toString();
            Batt93.setText(batt93);
        });

        Batt94 = findViewById(R.id.batt99);
        Batt94.setOnClickListener(v -> {
            String batt94 = ReadVoltage.getText().toString();
            Batt94.setText(batt94);
        });

        Batt95 = findViewById(R.id.batt100);
        Batt95.setOnClickListener(v -> {
            String batt95 = ReadVoltage.getText().toString();
            Batt95.setText(batt95);
        });

        Batt96 = findViewById(R.id.batt101);
        Batt96.setOnClickListener(v -> {
            String batt96 = ReadVoltage.getText().toString();
            Batt96.setText(batt96);
        });

        Batt97 = findViewById(R.id.batt102);
        Batt97.setOnClickListener(v -> {
            String batt97 = ReadVoltage.getText().toString();
            Batt97.setText(batt97);
        });

        Batt98 = findViewById(R.id.batt103);
        Batt98.setOnClickListener(v -> {
            String batt98 = ReadVoltage.getText().toString();
            Batt98.setText(batt98);
        });

        Batt99 = findViewById(R.id.batt104);
        Batt99.setOnClickListener(v -> {
            String batt99 = ReadVoltage.getText().toString();
            Batt99.setText(batt99);
        });

        Batt100 = findViewById(R.id.batt105);
        Batt100.setOnClickListener(v -> {
            String batt100 = ReadVoltage.getText().toString();
            Batt100.setText(batt100);
        });
    }
    private void Measure_Battery11() {

        Batt101 = findViewById(R.id.batt106);
        Batt101.setOnClickListener(v -> {
            String batt101 = ReadVoltage.getText().toString();
            Batt101.setText(batt101);
        });

        Batt102 = findViewById(R.id.batt107);
        Batt102.setOnClickListener(v -> {
            String batt102 = ReadVoltage.getText().toString();
            Batt102.setText(batt102);
        });

        Batt103 = findViewById(R.id.batt108);
        Batt103.setOnClickListener(v -> {
            String batt103 = ReadVoltage.getText().toString();
            Batt103.setText(batt103);
        });

        Batt104 = findViewById(R.id.batt109);
        Batt104.setOnClickListener(v -> {
            String batt104 = ReadVoltage.getText().toString();
            Batt104.setText(batt104);
        });

        Batt105 = findViewById(R.id.batt110);
        Batt105.setOnClickListener(v -> {
            String batt105 = ReadVoltage.getText().toString();
            Batt105.setText(batt105);
        });

        Batt106 = findViewById(R.id.batt111);
        Batt106.setOnClickListener(v -> {
            String batt106 = ReadVoltage.getText().toString();
            Batt106.setText(batt106);
        });

        Batt107 = findViewById(R.id.batt112);
        Batt107.setOnClickListener(v -> {
            String batt107 = ReadVoltage.getText().toString();
            Batt107.setText(batt107);
        });

        Batt108 = findViewById(R.id.batt113);
        Batt108.setOnClickListener(v -> {
            String batt108 = ReadVoltage.getText().toString();
            Batt108.setText(batt108);
        });
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    private void Total_Clear() {
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
}
