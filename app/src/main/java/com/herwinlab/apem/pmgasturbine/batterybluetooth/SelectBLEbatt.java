package com.herwinlab.apem.pmgasturbine.batterybluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.herwinlab.apem.R;

import java.util.ArrayList;
import java.util.List;

import me.aflak.bluetooth.Bluetooth;
import me.aflak.pulltorefresh.PullToRefresh;

public class SelectBLEbatt extends Activity implements PullToRefresh.OnRefreshListener {

    Bluetooth btSelect;
    ListView listViewSelect;
    Button btnScan;
    List<BluetoothDevice> paired;
    PullToRefresh pullRefresh;
    boolean registered=false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_bt_battery);

        transparentStatusAndNavigation();

        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(mReceiver, filter);
        registered=true;

        btSelect = new Bluetooth(SelectBLEbatt.this);
        btSelect.enableBluetooth();

        pullRefresh = findViewById(R.id.pull_to_refresh);
        listViewSelect = findViewById(R.id.list);
        btnScan = findViewById(R.id.buttonScan);

        pullRefresh.setListView(listViewSelect);
        pullRefresh.setOnRefreshListener(this);
        pullRefresh.setSlide(1000);

        listViewSelect.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(SelectBLEbatt.this, BluetoothBattery.class);
                i.putExtra("pos", position);
                if (registered) {
                    unregisterReceiver(mReceiver);
                    registered=false;
                }
                startActivity(i);
                finish();
            }
        });

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectBLEbatt.this, ScanBluetooth.class);
                startActivity(intent);
            }
        });

        addDeviceToList();
    }

    @Override
    public void onRefresh() {
        List<String> names = new ArrayList<>();
        for (BluetoothDevice d : btSelect.getPairedDevices()) {
            names.add(d.getName());
        }

        String[] array = names.toArray(new String[names.size()]);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1, array);

        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                listViewSelect.removeViews(0, listViewSelect.getCount());
                listViewSelect.setAdapter(adapter);
                paired = btSelect.getPairedDevices();
            }
        });
        pullRefresh.refreshComplete();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(registered) {
            unregisterReceiver(mReceiver);
            registered=false;
        }
    }

    private void addDeviceToList(){
        paired = btSelect.getPairedDevices();

        List<String> names = new ArrayList<>();
        for (BluetoothDevice d : paired){
            names.add(d.getName());
        }

        String[] array = names.toArray(new String[names.size()]);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1, array);

        listViewSelect.setAdapter(adapter);

        btnScan.setEnabled(true);
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);

                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                listViewSelect.setEnabled(false);
                            }
                        });
                        Toast.makeText(SelectBLEbatt.this, "Turn on bluetooth", Toast.LENGTH_LONG).show();
                        break;
                    case BluetoothAdapter.STATE_ON:
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                addDeviceToList();
                                listViewSelect.setEnabled(true);
                            }
                        });
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
}
