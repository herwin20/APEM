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
import androidx.cardview.widget.CardView;

import com.herwinlab.apem.R;

import me.aflak.bluetooth.Bluetooth;

public class BluetoothBattery extends AppCompatActivity implements Bluetooth.CommunicationCallback {

    public String name;
    public Bluetooth btActivity;
    TextView ReadVoltage, ConditionBattery;
    boolean registered=false;
    CardView cardCondition;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bluetooth_activity_battery);

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
                ReadVoltage.setText(data+" VDC");

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

}
