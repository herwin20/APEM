package com.herwinlab.apem.pmgasturbine.batterybluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;

import com.herwinlab.apem.R;

import java.util.ArrayList;
import java.util.List;

import me.aflak.bluetooth.Bluetooth;

public class ScanBluetooth extends Activity implements Bluetooth.DiscoveryCallback, AdapterView.OnItemClickListener {

    Bluetooth btScan;
    ListView listViewScan;
    ArrayAdapter<String> adapterScan;
    TextView scanText;
    ProgressBar progressBarScan;
    Button btnScanDevices;
    List<BluetoothDevice> btDevices;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan_bluetooth_batt);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        transparentStatusAndNavigation();

        listViewScan = findViewById(R.id.scan_list);
        scanText = findViewById(R.id.scan_state);
        progressBarScan = findViewById(R.id.progressBarScan);
        btnScanDevices = findViewById(R.id.btnScan);

        adapterScan = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<String>());
        listViewScan.setAdapter(adapterScan);
        listViewScan.setOnItemClickListener(this);

        btScan = new Bluetooth(this);
        btScan.setDiscoveryCallback(this);
        btScan.scanDevices();

        progressBarScan.setVisibility(View.VISIBLE);
        scanText.setText("Scanning Bluetooth Devices ...");
        listViewScan.setEnabled(false);

        btnScanDevices.setEnabled(false);
        btDevices = new ArrayList<>();

        btnScanDevices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapterScan.clear();
                        btnScanDevices.setEnabled(false);
                    }
                });

                btDevices = new ArrayList<>();
                progressBarScan.setVisibility(View.VISIBLE);
                scanText.setText("Scanning Bluetooth Devices ...");
                btScan.scanDevices();
            }
        });
    }

    private void setText(final String txt){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                scanText.setText(txt);
            }
        });
    }

    private void setProgressVisibility(final int id){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBarScan.setVisibility(id);
            }
        });
    }

    @Override
    public void onFinish() {
        setProgressVisibility(View.INVISIBLE);
        setText("Scan finished!");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                btnScanDevices.setEnabled(true);
                listViewScan.setEnabled(true);
            }
        });
    }

    @Override
    public void onDevice(final BluetoothDevice device) {
        btDevices.add(device);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapterScan.add(device.getAddress()+" - "+ device.getName());
            }
        });
    }

    @Override
    public void onPair(BluetoothDevice device) {
        setProgressVisibility(View.INVISIBLE);
        setText("Paired!");
        Intent i = new Intent(ScanBluetooth.this, SelectBLEbatt.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onUnpair(BluetoothDevice device) {
        setProgressVisibility(View.INVISIBLE);
        setText("Paired!");
    }

    @Override
    public void onError(String message) {
        setProgressVisibility(View.INVISIBLE);
        setText("Error: "+message);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        setProgressVisibility(View.VISIBLE);
        setText("Pairing...");
        btScan.pair(btDevices.get(position));
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
