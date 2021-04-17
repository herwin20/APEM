package com.herwinlab.apem.pmgasturbine.pmtrafo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.herwinlab.apem.R;

import static android.content.Context.MODE_PRIVATE;

public class MainTrafoFragment extends Fragment {

    public static final String MainTrafo = "maintrafo";

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

        checkBoxSharedPref();

        savedButtonGT11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPref();
                Toast.makeText(getActivity(), "Saved", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), MainPmTrafo.class);
                intent.putExtra("Test", EditOilTempGT11.getText().toString());
                startActivity(intent);
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

}
