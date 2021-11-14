package com.herwinlab.apem.pmgasturbine.batterybluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;

import com.herwinlab.apem.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import me.aflak.bluetooth.Bluetooth;

public class BluetoothBattery extends AppCompatActivity implements Bluetooth.CommunicationCallback {

    public String name;
    public Bluetooth btActivity;
    TextView ReadVoltage, ConditionBattery;
    boolean registered=false;
    CardView cardCondition;

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
    public TextView TanggalNow;

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

        btActivity = new Bluetooth(this);
        btActivity.enableBluetooth();

        btActivity.setCommunicationCallback(this);
        int pos = getIntent().getExtras().getInt("pos");
        name = btActivity.getPairedDevices().get(pos).getName();

        btActivity.connectToDevice(btActivity.getPairedDevices().get(pos));

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
                ReadVoltage.setText(data+" V");

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

    //Fungsi Untuk Jam dan Tanggal
    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Calendar c1 = Calendar.getInstance();
            SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/YYYY h:m:s a");
            String strdate1 = sdf1.format(c1.getTime());
            TextView txtdate1 = findViewById(R.id.tanggalble2);
            txtdate1.setText(strdate1);

            handler.postDelayed(this, 1000);
        }
    };

}
