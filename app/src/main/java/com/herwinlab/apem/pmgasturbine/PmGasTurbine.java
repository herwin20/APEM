package com.herwinlab.apem.pmgasturbine;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;

import com.herwinlab.apem.R;
import com.herwinlab.apem.TemplatePDF;
import com.herwinlab.apem.mergepdf.MergePdfWeb;
import com.herwinlab.apem.pmgasturbine.batterybluetooth.SelectBLEbatt;
import com.herwinlab.apem.pmgasturbine.imagefunction.CameraActivity;
import com.herwinlab.apem.pmgasturbine.pmtrafo.MainPmTrafo;

public class PmGasTurbine extends AppCompatActivity {

    //Button NewPMgen, ViewPMgen;

    LinearLayout btnBattery12, btnBattery2, btnBLE12, btnBLE2;

    public CardView cardToPmGen, cardToPmBatt, cardToPmTrafo, cardMCCAC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_pm_gas_activity);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        transparentStatusAndNavigation();

        //LinearLayout NewPMgen = findViewById(R.id.create_pmgen);
        cardToPmGen = findViewById(R.id.cardGen);
        cardToPmGen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PmGasTurbine.this, PmGenerator.class);
                startActivity(intent);
            }
        });

       /** LinearLayout MergeBtn = findViewById(R.id.merge_btn);
        MergeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PmGasTurbine.this, MergePdfWeb.class);
                startActivity(intent);
            }
        }); */

        //LinearLayout NewPMbatt = findViewById(R.id.create_pmeexc);
        cardToPmBatt = findViewById(R.id.cardBatt);
        cardToPmBatt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(PmGasTurbine.this, R.style.CustomAlertDialog);
                ViewGroup viewGroup = findViewById(R.id.content);
                final View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.dialog_battery, viewGroup, false);
                builder.setView(dialogView);
                final AlertDialog alertDialog = builder.create();

                btnBattery2 = dialogView.findViewById(R.id.battery2);
                btnBattery12 = dialogView.findViewById(R.id.battery12);
                btnBLE2 = dialogView.findViewById(R.id.battery2BLE);
                btnBLE12 = dialogView.findViewById(R.id.battery12BLE);

                btnBattery2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(PmGasTurbine.this, PmBatteryBTM.class);
                        startActivity(intent);
                    }
                });

                btnBattery12.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(PmGasTurbine.this, PmBattery12V.class);
                        startActivity(intent);
                    }
                });

                btnBLE2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(PmGasTurbine.this, SelectBLEbatt.class);
                        startActivity(intent);
                    }
                });

                btnBLE12.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(PmGasTurbine.this, SelectBLEbatt.class);
                        startActivity(intent);
                    }
                });

                alertDialog.show();
            }
        });

        //LinearLayout NewPMTrans = findViewById(R.id.create_pmtrans);
        cardToPmTrafo = findViewById(R.id.cardTrafo);
        cardToPmTrafo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PmGasTurbine.this, MainPmTrafo.class);
                startActivity(intent);
            }
        });
        cardMCCAC =  findViewById(R.id.cardMCCAC);
        cardMCCAC.setOnClickListener(v -> {
            //Intent intent = new Intent(PmGasTurbine.this, MCCAC.class);
            //startActivity(intent);
        });




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
