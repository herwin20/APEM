package com.herwinlab.apem.pmgasturbine.pmtrafo;

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

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.textfield.TextInputEditText;
import com.herwinlab.apem.R;

import static android.content.Context.MODE_PRIVATE;

public class UATFragment extends Fragment {

    //CheckBox
    public CheckBox ChkBoxSilicaUat11, ChkOilUat11, ChkFanUat11, ChkGroundUat11, ChkBucholzUat11, ChkSumppumpUat11; // GT 11
    public CheckBox ChkBoxSilicaUat12, ChkOilUat12, ChkFanUat12, ChkGroundUat12, ChkBucholzUat12, ChkSumppumpUat12; // GT 12
    public CheckBox ChkBoxSilicaUat13, ChkOilUat13, ChkFanUat13, ChkGroundUat13, ChkBucholzUat13, ChkSumppumpUat13; // GT 13
    public CheckBox ChkBoxSilicaUat21, ChkOilUat21, ChkFanUat21, ChkGroundUat21, ChkBucholzUat21, ChkSumppumpUat21; // GT 21
    public CheckBox ChkBoxSilicaUat22, ChkOilUat22, ChkFanUat22, ChkGroundUat22, ChkBucholzUat22, ChkSumppumpUat22; // GT 22
    //EditText
    public TextInputEditText EditOilTempUat11, EditWindingUat11, EditOillevelUat11, EditKeteranganUat11;
    public TextInputEditText EditOilTempUat12, EditWindingUat12, EditOillevelUat12, EditKeteranganUat12;
    public TextInputEditText EditOilTempUat13, EditWindingUat13, EditOillevelUat13, EditKeteranganUat13;
    public TextInputEditText EditOilTempUat21, EditWindingUat21, EditOillevelUat21, EditKeteranganUat21;
    public TextInputEditText EditOilTempUat22, EditWindingUat22, EditOillevelUat22, EditKeteranganUat22;
    //TextView
    public TextView ketSilicaUat11, ketOilUat11, ketFanUat11, ketGroundUat11, ketBucholzUat11, ketSumppumpUat11; // GT 11
    public TextView ketSilicaUat12, ketOilUat12, ketFanUat12, ketGroundUat12, ketBucholzUat12, ketSumppumpUat12; // GT 12
    public TextView ketSilicaUat13, ketOilUat13, ketFanUat13, ketGroundUat13, ketBucholzUat13, ketSumppumpUat13; // GT 13
    public TextView ketSilicaUat21, ketOilUat21, ketFanUat21, ketGroundUat21, ketBucholzUat21, ketSumppumpUat21; // GT 21
    public TextView ketSilicaUat22, ketOilUat22, ketFanUat22, ketGroundUat22, ketBucholzUat22, ketSumppumpUat22; // GT 22
    //LinearButton
    public LinearLayout savedButtonGT11, savedButtonGT12, savedButtonGT13, savedButtonGT21, savedButtonGT22;

    public SharedPreferences pref;
    //GT 11
    private final String OilTempUAT11_KEY = "OilTempUAT11";
    private final String WindingTempUAT11_KEY = "WindingTempUAT11";
    private final String OilLevelUAT11_KEY = "OilLevelUAT11";
    private final String KeteranganUAT11_KEY = "KeteranganUAT11";
    private final String SilicaGelUAT11_KEY = "SilicaGelUAT11";
    private final String OilUAT11_KEY = "OilUAT11";
    private final String FanUAT11_KEY = "FanUAT11";
    private final String GroundUAT11_KEY = "GroundUAT11";
    private final String BucholzUAT11_KEY = "BucholzUAT11";
    private final String SumppumpUAT11_KEY = "SumppumpUAT11";
    //GT 12
    private final String OilTempUAT12_KEY = "OilTempUAT12";
    private final String WindingTempUAT12_KEY = "WindingTempUAT12";
    private final String OilLevelUAT12_KEY = "OilLevelUAT12";
    private final String KeteranganUAT12_KEY = "KeteranganUAT12";
    private final String SilicaGelUAT12_KEY = "SilicaGelUAT12";
    private final String OilUAT12_KEY = "OilUAT12";
    private final String FanUAT12_KEY = "FanUAT12";
    private final String GroundUAT12_KEY = "GroundUAT12";
    private final String BucholzUAT12_KEY = "BucholzUAT12";
    private final String SumppumpUAT12_KEY = "SumppumpUAT12";
    //GT 13
    private final String OilTempUAT13_KEY = "OilTempUAT13";
    private final String WindingTempUAT13_KEY = "WindingTempUAT13";
    private final String OilLevelUAT13_KEY = "OilLevelUAT13";
    private final String KeteranganUAT13_KEY = "KeteranganUAT13";
    private final String SilicaGelUAT13_KEY = "SilicaGelUAT13";
    private final String OilUAT13_KEY = "OilUAT13";
    private final String FanUAT13_KEY = "FanUAT13";
    private final String GroundUAT13_KEY = "GroundUAT13";
    private final String BucholzUAT13_KEY = "BucholzUAT13";
    private final String SumppumpUAT13_KEY = "SumppumpUAT13";
    //GT 21
    private final String OilTempUAT21_KEY = "OilTempUAT21";
    private final String WindingTempUAT21_KEY = "WindingTempUAT21";
    private final String OilLevelUAT21_KEY = "OilLevelUAT21";
    private final String KeteranganUAT21_KEY = "KeteranganUAT21";
    private final String SilicaGelUAT21_KEY = "SilicaGelUAT21";
    private final String OilUAT21_KEY = "OilUAT21";
    private final String FanUAT21_KEY = "FanUAT21";
    private final String GroundUAT21_KEY = "GroundUAT21";
    private final String BucholzUAT21_KEY = "BucholzUAT21";
    private final String SumppumpUAT21_KEY = "SumppumpUAT21";
    //GT 22
    private final String OilTempUAT22_KEY = "OilTempUAT22";
    private final String WindingTempUAT22_KEY = "WindingTempUAT122";
    private final String OilLevelUAT22_KEY = "OilLevelUAT22";
    private final String KeteranganUAT22_KEY = "KeteranganUAT22";
    private final String SilicaGelUAT22_KEY = "SilicaGelUAT22";
    private final String OilUAT22_KEY = "OilUAT22";
    private final String FanUAT22_KEY = "FanUAT22";
    private final String GroundUAT22_KEY = "GroundUAT22";
    private final String BucholzUAT22_KEY = "BucholzUAT22";
    private final String SumppumpUAT22_KEY = "SumppumpUAT22";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_uat, container, false);

        //GT11
        ketSilicaUat11 = v.findViewById(R.id.silicaGelUat11);
        ketOilUat11 = v.findViewById(R.id.OilGlassUat11);
        ketGroundUat11 = v.findViewById(R.id.groundUat11);
        ketBucholzUat11 = v.findViewById(R.id.bucholzUat11);
        ketSumppumpUat11 = v.findViewById(R.id.sumpPumpUat11);
        //==========================================================
        EditOillevelUat11 = v.findViewById(R.id.indikatorlevelUat11);
        EditWindingUat11 = v.findViewById(R.id.windingUat11);
        EditOilTempUat11 = v.findViewById(R.id.oiltempUat11);
        EditKeteranganUat11 = v.findViewById(R.id.keteranganUat11);
        //==========================================================
        ChkBoxSilicaUat11 = v.findViewById(R.id.checkSilicaUat11);
        ChkOilUat11 = v.findViewById(R.id.checkOilUat11);
        ChkGroundUat11 = v.findViewById(R.id.checkGroundUat11);
        ChkBucholzUat11 = v.findViewById(R.id.checkBucholzUat11);
        ChkSumppumpUat11 = v.findViewById(R.id.checkSumppumpUat11);
        //GT12
        ketSilicaUat12 = v.findViewById(R.id.silicaGelUat12);
        ketOilUat12 = v.findViewById(R.id.OilGlassUat12);
        ketGroundUat12 = v.findViewById(R.id.groundUat12);
        ketBucholzUat12 = v.findViewById(R.id.bucholzUat12);
        ketSumppumpUat12 = v.findViewById(R.id.sumpPumpUat12);
        //==========================================================
        EditOillevelUat12 = v.findViewById(R.id.indikatorlevelUat12);
        EditWindingUat12 = v.findViewById(R.id.windingUat12);
        EditOilTempUat12 = v.findViewById(R.id.oiltempUat12);
        EditKeteranganUat12 = v.findViewById(R.id.keteranganUat12);
        //==========================================================
        ChkBoxSilicaUat12 = v.findViewById(R.id.checkSilicaUat12);
        ChkOilUat12 = v.findViewById(R.id.checkOilUat12);
        ChkGroundUat12 = v.findViewById(R.id.checkGroundUat12);
        ChkBucholzUat12 = v.findViewById(R.id.checkBucholzUat12);
        ChkSumppumpUat12 = v.findViewById(R.id.checkSumppumpUat12);
        //GT13
        ketSilicaUat13 = v.findViewById(R.id.silicaGelUat13);
        ketOilUat13 = v.findViewById(R.id.OilGlassUat13);
        ketGroundUat13 = v.findViewById(R.id.groundUat13);
        ketBucholzUat13 = v.findViewById(R.id.bucholzUat13);
        ketSumppumpUat13 = v.findViewById(R.id.sumpPumpUat13);
        //==========================================================
        EditOillevelUat13 = v.findViewById(R.id.indikatorlevelUat13);
        EditWindingUat13 = v.findViewById(R.id.windingUat13);
        EditOilTempUat13 = v.findViewById(R.id.oiltempUat13);
        EditKeteranganUat13 = v.findViewById(R.id.keteranganUat13);
        //==========================================================
        ChkBoxSilicaUat13 = v.findViewById(R.id.checkSilicaUat13);
        ChkOilUat13 = v.findViewById(R.id.checkOilUat13);
        ChkGroundUat13 = v.findViewById(R.id.checkGroundUat13);
        ChkBucholzUat13 = v.findViewById(R.id.checkBucholzUat13);
        ChkSumppumpUat13 = v.findViewById(R.id.checkSumppumpUat13);
        //GT21
        ketSilicaUat21 = v.findViewById(R.id.silicaGelUat21);
        ketOilUat21 = v.findViewById(R.id.OilGlassUat21);
        ketGroundUat21 = v.findViewById(R.id.groundUat21);
        ketBucholzUat21 = v.findViewById(R.id.bucholzUat21);
        ketSumppumpUat21 = v.findViewById(R.id.sumpPumpUat21);
        //==========================================================
        EditOillevelUat21 = v.findViewById(R.id.indikatorlevelUat21);
        EditWindingUat21 = v.findViewById(R.id.windingUat21);
        EditOilTempUat21 = v.findViewById(R.id.oiltempUat21);
        EditKeteranganUat21 = v.findViewById(R.id.keteranganUat21);
        //==========================================================
        ChkBoxSilicaUat21 = v.findViewById(R.id.checkSilicaUat21);
        ChkOilUat21 = v.findViewById(R.id.checkOilUat21);
        ChkGroundUat21 = v.findViewById(R.id.checkGroundUat21);
        ChkBucholzUat21 = v.findViewById(R.id.checkBucholzUat21);
        ChkSumppumpUat21 = v.findViewById(R.id.checkSumppumpUat21);
        //GT22
        ketSilicaUat22 = v.findViewById(R.id.silicaGelUat22);
        ketOilUat22 = v.findViewById(R.id.OilGlassUat22);
        ketGroundUat22 = v.findViewById(R.id.groundUat22);
        ketBucholzUat22 = v.findViewById(R.id.bucholzUat22);
        ketSumppumpUat22 = v.findViewById(R.id.sumpPumpUat22);
        //==========================================================
        EditOillevelUat22 = v.findViewById(R.id.indikatorlevelUat22);
        EditOilTempUat22 = v.findViewById(R.id.oiltempUat22);
        EditWindingUat22 = v.findViewById(R.id.windingUat22);
        EditKeteranganUat22 = v.findViewById(R.id.keteranganUat22);
        //==========================================================
        ChkBoxSilicaUat22 = v.findViewById(R.id.checkSilicaUat22);
        ChkOilUat22 = v.findViewById(R.id.checkOilUat22);
        ChkGroundUat22 = v.findViewById(R.id.checkGroundUat22);
        ChkBucholzUat22 = v.findViewById(R.id.checkBucholzUat22);
        ChkSumppumpUat22 = v.findViewById(R.id.checkSumppumpUat22);

        savedButtonGT11 = v.findViewById(R.id.buttonSavedGT11);
        savedButtonGT12 = v.findViewById(R.id.buttonSavedGT12);
        savedButtonGT13 = v.findViewById(R.id.buttonSavedGT13);
        savedButtonGT21 = v.findViewById(R.id.buttonSavedGT21);
        savedButtonGT22 = v.findViewById(R.id.buttonSavedGT22);

        //SharePreference TextVIew
        pref = this.getActivity().getPreferences(MODE_PRIVATE);
        ketSilicaUat11.setText(pref.getString(SilicaGelUAT11_KEY, ""));
        ketOilUat11.setText(pref.getString(OilUAT11_KEY, ""));
        ketGroundUat11.setText(pref.getString(GroundUAT11_KEY, ""));
        ketBucholzUat11.setText(pref.getString(BucholzUAT11_KEY, ""));
        ketSumppumpUat11.setText(pref.getString(SumppumpUAT11_KEY, ""));
        EditOilTempUat11.setText(pref.getString(OilTempUAT11_KEY, ""));
        EditWindingUat11.setText(pref.getString(WindingTempUAT11_KEY, ""));
        EditOillevelUat11.setText(pref.getString(OilLevelUAT11_KEY, ""));
        EditKeteranganUat11.setText(pref.getString(KeteranganUAT11_KEY, ""));

        ketSilicaUat12.setText(pref.getString(SilicaGelUAT12_KEY, ""));
        ketOilUat12.setText(pref.getString(OilUAT12_KEY, ""));
        ketGroundUat12.setText(pref.getString(GroundUAT12_KEY, ""));
        ketBucholzUat12.setText(pref.getString(BucholzUAT12_KEY, ""));
        ketSumppumpUat12.setText(pref.getString(SumppumpUAT12_KEY, ""));
        EditOilTempUat12.setText(pref.getString(OilTempUAT12_KEY, ""));
        EditWindingUat12.setText(pref.getString(WindingTempUAT12_KEY, ""));
        EditOillevelUat12.setText(pref.getString(OilLevelUAT12_KEY, ""));
        EditKeteranganUat12.setText(pref.getString(KeteranganUAT12_KEY, ""));

        ketSilicaUat13.setText(pref.getString(SilicaGelUAT13_KEY, ""));
        ketOilUat13.setText(pref.getString(OilUAT13_KEY, ""));
        ketGroundUat13.setText(pref.getString(GroundUAT13_KEY, ""));
        ketBucholzUat13.setText(pref.getString(BucholzUAT13_KEY, ""));
        ketSumppumpUat13.setText(pref.getString(SumppumpUAT13_KEY, ""));
        EditOilTempUat13.setText(pref.getString(OilTempUAT13_KEY, ""));
        EditWindingUat13.setText(pref.getString(WindingTempUAT13_KEY, ""));
        EditOillevelUat13.setText(pref.getString(OilLevelUAT13_KEY, ""));
        EditKeteranganUat13.setText(pref.getString(KeteranganUAT13_KEY, ""));

        ketSilicaUat21.setText(pref.getString(SilicaGelUAT21_KEY, ""));
        ketOilUat21.setText(pref.getString(OilUAT21_KEY, ""));
        ketGroundUat21.setText(pref.getString(GroundUAT21_KEY, ""));
        ketBucholzUat21.setText(pref.getString(BucholzUAT21_KEY, ""));
        ketSumppumpUat21.setText(pref.getString(SumppumpUAT21_KEY, ""));
        EditOilTempUat21.setText(pref.getString(OilTempUAT21_KEY, ""));
        EditWindingUat21.setText(pref.getString(WindingTempUAT21_KEY, ""));
        EditOillevelUat21.setText(pref.getString(OilLevelUAT21_KEY, ""));
        EditKeteranganUat21.setText(pref.getString(KeteranganUAT21_KEY, ""));

        ketSilicaUat22.setText(pref.getString(SilicaGelUAT22_KEY, ""));
        ketOilUat22.setText(pref.getString(OilUAT22_KEY, ""));
        ketGroundUat22.setText(pref.getString(GroundUAT22_KEY, ""));
        ketBucholzUat22.setText(pref.getString(BucholzUAT22_KEY, ""));
        ketSumppumpUat22.setText(pref.getString(SumppumpUAT22_KEY, ""));
        EditOilTempUat22.setText(pref.getString(OilTempUAT22_KEY, ""));
        EditWindingUat22.setText(pref.getString(WindingTempUAT22_KEY, ""));
        EditOillevelUat22.setText(pref.getString(OilLevelUAT22_KEY, ""));
        EditKeteranganUat22.setText(pref.getString(KeteranganUAT22_KEY, ""));

        checkBoxSharedPrefUAT();

        savedButtonGT11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPref();
                Toast.makeText(getActivity(), "Saved", Toast.LENGTH_SHORT).show();
            }
        });

        savedButtonGT12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPref();
                Toast.makeText(getActivity(), "Saved", Toast.LENGTH_SHORT).show();
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

        savedButtonGT22.setOnClickListener(new View.OnClickListener() {
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
        pref = this.getActivity().getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(SilicaGelUAT11_KEY, ketSilicaUat11.getText().toString());
        editor.putString(OilUAT11_KEY, ketOilUat11.getText().toString());
        editor.putString(GroundUAT11_KEY, ketGroundUat11.getText().toString());
        editor.putString(BucholzUAT11_KEY, ketBucholzUat11.getText().toString());
        editor.putString(SumppumpUAT11_KEY, ketSumppumpUat11.getText().toString());
        editor.putString(OilTempUAT11_KEY, EditOilTempUat11.getText().toString());
        editor.putString(WindingTempUAT11_KEY, EditWindingUat11.getText().toString());
        editor.putString(OilLevelUAT11_KEY, EditOillevelUat11.getText().toString());
        editor.putString(KeteranganUAT11_KEY, EditKeteranganUat11.getText().toString());

        editor.putString(SilicaGelUAT12_KEY, ketSilicaUat12.getText().toString());
        editor.putString(OilUAT12_KEY, ketOilUat12.getText().toString());
        editor.putString(GroundUAT12_KEY, ketGroundUat12.getText().toString());
        editor.putString(BucholzUAT12_KEY, ketBucholzUat12.getText().toString());
        editor.putString(SumppumpUAT12_KEY, ketSumppumpUat12.getText().toString());
        editor.putString(OilTempUAT12_KEY, EditOilTempUat12.getText().toString());
        editor.putString(WindingTempUAT12_KEY, EditWindingUat12.getText().toString());
        editor.putString(OilLevelUAT12_KEY, EditOillevelUat12.getText().toString());
        editor.putString(KeteranganUAT12_KEY, EditKeteranganUat12.getText().toString());

        editor.putString(SilicaGelUAT13_KEY, ketSilicaUat13.getText().toString());
        editor.putString(OilUAT13_KEY, ketOilUat13.getText().toString());
        editor.putString(GroundUAT13_KEY, ketGroundUat13.getText().toString());
        editor.putString(BucholzUAT13_KEY, ketBucholzUat13.getText().toString());
        editor.putString(SumppumpUAT13_KEY, ketSumppumpUat13.getText().toString());
        editor.putString(OilTempUAT13_KEY, EditOilTempUat13.getText().toString());
        editor.putString(WindingTempUAT13_KEY, EditWindingUat13.getText().toString());
        editor.putString(OilLevelUAT13_KEY, EditOillevelUat13.getText().toString());
        editor.putString(KeteranganUAT13_KEY, EditKeteranganUat13.getText().toString());

        editor.putString(SilicaGelUAT21_KEY, ketSilicaUat21.getText().toString());
        editor.putString(OilUAT21_KEY, ketOilUat21.getText().toString());
        editor.putString(GroundUAT21_KEY, ketGroundUat21.getText().toString());
        editor.putString(BucholzUAT21_KEY, ketBucholzUat21.getText().toString());
        editor.putString(SumppumpUAT21_KEY, ketSumppumpUat21.getText().toString());
        editor.putString(OilTempUAT21_KEY, EditOilTempUat21.getText().toString());
        editor.putString(WindingTempUAT21_KEY, EditWindingUat21.getText().toString());
        editor.putString(OilLevelUAT21_KEY, EditOillevelUat21.getText().toString());
        editor.putString(KeteranganUAT21_KEY, EditKeteranganUat21.getText().toString());

        editor.putString(SilicaGelUAT22_KEY, ketSilicaUat22.getText().toString());
        editor.putString(OilUAT22_KEY, ketOilUat22.getText().toString());
        editor.putString(GroundUAT22_KEY, ketGroundUat22.getText().toString());
        editor.putString(BucholzUAT22_KEY, ketBucholzUat22.getText().toString());
        editor.putString(SumppumpUAT22_KEY, ketSumppumpUat22.getText().toString());
        editor.putString(OilTempUAT22_KEY, EditOilTempUat22.getText().toString());
        editor.putString(WindingTempUAT22_KEY, EditWindingUat22.getText().toString());
        editor.putString(OilLevelUAT22_KEY, EditOillevelUat22.getText().toString());
        editor.putString(KeteranganUAT22_KEY, EditKeteranganUat22.getText().toString());
        editor.apply();
    }

    public void checkBoxSharedPrefUAT() {
        pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        final SharedPreferences.Editor editor = pref.edit();
        ChkBoxSilicaUat11.setChecked(pref.contains("checkedSilicaUat11") && pref.getBoolean("checkedSilicaUat11", false) == true);

        ChkOilUat11.setChecked(pref.contains("checkedOilUat11") && pref.getBoolean("checkedOilUat11", false) == true);

        ChkGroundUat11.setChecked(pref.contains("checkedGroundUat11") && pref.getBoolean("checkedGroundUat11", false) == true);

        ChkBucholzUat11.setChecked(pref.contains("checkedBucholzUat11") && pref.getBoolean("checkedBucholzUat11", false) == true);

        ChkSumppumpUat11.setChecked(pref.contains("checkedSumpUat11") && pref.getBoolean("checkedSumpUat11", false) == true);

        ChkBoxSilicaUat11.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (ChkBoxSilicaUat11.isChecked()) {
                    ketSilicaUat11.setText("OK");
                    editor.putBoolean("checkedSilicaUat11", true);
                    editor.apply();
                    sharedPref();
                } else {
                    ketSilicaUat11.setText("Not OK");
                    editor.putBoolean("checkedSilicaUat11", false);
                    editor.apply();
                    sharedPref();
                }
            }
        });

        ChkOilUat11.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (ChkOilUat11.isChecked()) {
                    ketOilUat11.setText("OK");
                    editor.putBoolean("checkedOilUat11", true);
                    editor.apply();
                    sharedPref();
                } else {
                    ketOilUat11.setText("Not OK");
                    editor.putBoolean("checkedOilUat11", false);
                    editor.apply();
                    sharedPref();
                }
            }
        });

        ChkGroundUat11.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (ChkGroundUat11.isChecked()) {
                    ketGroundUat11.setText("OK");
                    editor.putBoolean("checkedGroundUat11", true);
                    editor.apply();
                    sharedPref();
                } else {
                    ketGroundUat11.setText("Not OK");
                    editor.putBoolean("checkedGroundUat11", false);
                    editor.apply();
                    sharedPref();
                }
            }
        });

        ChkBucholzUat11.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (ChkBucholzUat11.isChecked()) {
                    ketBucholzUat11.setText("OK");
                    editor.putBoolean("checkedBushingUat11", true);
                    editor.apply();
                    sharedPref();
                } else {
                    ketBucholzUat11.setText("Not OK");
                    editor.putBoolean("checkedBucholzUat11", false);
                    editor.apply();
                    sharedPref();
                }
            }
        });

        ChkSumppumpUat11.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (ChkSumppumpUat11.isChecked()) {
                    ketSumppumpUat11.setText("OK");
                    editor.putBoolean("checkedSumpUat11", true);
                    editor.apply();
                    sharedPref();
                } else {
                    ketSumppumpUat11.setText("Not OK");
                    editor.putBoolean("checkedSumpUat11", false);
                    editor.apply();
                    sharedPref();
                }
            }
        });

        ChkBoxSilicaUat12.setChecked(pref.contains("checkedSilicaUat12") && pref.getBoolean("checkedSilicaUat12", false) == true);

        ChkOilUat12.setChecked(pref.contains("checkedOilUat12") && pref.getBoolean("checkedOilUat12", false) == true);

        ChkGroundUat12.setChecked(pref.contains("checkedGroundUat12") && pref.getBoolean("checkedGroundUat12", false) == true);

        ChkBucholzUat12.setChecked(pref.contains("checkedBucholzUat12") && pref.getBoolean("checkedBucholzUat12", false) == true);

        ChkSumppumpUat12.setChecked(pref.contains("checkedSumpUat12") && pref.getBoolean("checkedSumpUat12", false) == true);

        ChkBoxSilicaUat12.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (ChkBoxSilicaUat12.isChecked()) {
                    ketSilicaUat12.setText("OK");
                    editor.putBoolean("checkedSilicaUat12", true);
                    editor.apply();
                    sharedPref();
                } else {
                    ketSilicaUat12.setText("Not OK");
                    editor.putBoolean("checkedSilicaUat12", false);
                    editor.apply();
                    sharedPref();
                }
            }
        });

        ChkOilUat12.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (ChkOilUat12.isChecked()) {
                    ketOilUat12.setText("OK");
                    editor.putBoolean("checkedOilUat12", true);
                    editor.apply();
                    sharedPref();
                } else {
                    ketOilUat12.setText("Not OK");
                    editor.putBoolean("checkedOilUat12", false);
                    editor.apply();
                    sharedPref();
                }
            }
        });

        ChkGroundUat12.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (ChkGroundUat12.isChecked()) {
                    ketGroundUat12.setText("OK");
                    editor.putBoolean("checkedGroundUat12", true);
                    editor.apply();
                    sharedPref();
                } else {
                    ketGroundUat12.setText("Not OK");
                    editor.putBoolean("checkedGroundUat12", false);
                    editor.apply();
                    sharedPref();
                }
            }
        });

        ChkBucholzUat12.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (ChkBucholzUat12.isChecked()) {
                    ketBucholzUat12.setText("OK");
                    editor.putBoolean("checkedBushingUat12", true);
                    editor.apply();
                    sharedPref();
                } else {
                    ketBucholzUat12.setText("Not OK");
                    editor.putBoolean("checkedBucholzUat12", false);
                    editor.apply();
                    sharedPref();
                }
            }
        });

        ChkSumppumpUat12.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (ChkSumppumpUat12.isChecked()) {
                    ketSumppumpUat12.setText("OK");
                    editor.putBoolean("checkedSumpUat12", true);
                    editor.apply();
                    sharedPref();
                } else {
                    ketSumppumpUat12.setText("Not OK");
                    editor.putBoolean("checkedSumpUat12", false);
                    editor.apply();
                    sharedPref();
                }
            }
        });

        ChkBoxSilicaUat13.setChecked(pref.contains("checkedSilicaUat13") && pref.getBoolean("checkedSilicaUat13", false) == true);

        ChkOilUat13.setChecked(pref.contains("checkedOilUat13") && pref.getBoolean("checkedOilUat13", false) == true);

        ChkGroundUat13.setChecked(pref.contains("checkedGroundUat13") && pref.getBoolean("checkedGroundUat13", false) == true);

        ChkBucholzUat13.setChecked(pref.contains("checkedBucholzUat13") && pref.getBoolean("checkedBucholzUat13", false) == true);

        ChkSumppumpUat13.setChecked(pref.contains("checkedSumpUat13") && pref.getBoolean("checkedSumpUat13", false) == true);

        ChkBoxSilicaUat13.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (ChkBoxSilicaUat13.isChecked()) {
                    ketSilicaUat13.setText("OK");
                    editor.putBoolean("checkedSilicaUat13", true);
                    editor.apply();
                    sharedPref();
                } else {
                    ketSilicaUat13.setText("Not OK");
                    editor.putBoolean("checkedSilicaUat13", false);
                    editor.apply();
                    sharedPref();
                }
            }
        });

        ChkOilUat13.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (ChkOilUat13.isChecked()) {
                    ketOilUat13.setText("OK");
                    editor.putBoolean("checkedOilUat13", true);
                    editor.apply();
                    sharedPref();
                } else {
                    ketOilUat13.setText("Not OK");
                    editor.putBoolean("checkedOilUat13", false);
                    editor.apply();
                    sharedPref();
                }
            }
        });

        ChkGroundUat13.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (ChkGroundUat13.isChecked()) {
                    ketGroundUat13.setText("OK");
                    editor.putBoolean("checkedGroundUat13", true);
                    editor.apply();
                    sharedPref();
                } else {
                    ketGroundUat13.setText("Not OK");
                    editor.putBoolean("checkedGroundUat13", false);
                    editor.apply();
                    sharedPref();
                }
            }
        });

        ChkBucholzUat13.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (ChkBucholzUat13.isChecked()) {
                    ketBucholzUat13.setText("OK");
                    editor.putBoolean("checkedBushingUat13", true);
                    editor.apply();
                    sharedPref();
                } else {
                    ketBucholzUat13.setText("Not OK");
                    editor.putBoolean("checkedBucholzUat13", false);
                    editor.apply();
                    sharedPref();
                }
            }
        });

        ChkSumppumpUat13.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (ChkSumppumpUat13.isChecked()) {
                    ketSumppumpUat13.setText("OK");
                    editor.putBoolean("checkedSumpUat13", true);
                    editor.apply();
                    sharedPref();
                } else {
                    ketSumppumpUat13.setText("Not OK");
                    editor.putBoolean("checkedSumpUat13", false);
                    editor.apply();
                    sharedPref();
                }
            }
        });

        ChkBoxSilicaUat21.setChecked(pref.contains("checkedSilicaUat21") && pref.getBoolean("checkedSilicaUat21", false) == true);

        ChkOilUat21.setChecked(pref.contains("checkedOilUat21") && pref.getBoolean("checkedOilUat21", false) == true);

        ChkGroundUat21.setChecked(pref.contains("checkedGroundUat21") && pref.getBoolean("checkedGroundUat21", false) == true);

        ChkBucholzUat21.setChecked(pref.contains("checkedBucholzUat21") && pref.getBoolean("checkedBucholzUat21", false) == true);

        ChkSumppumpUat21.setChecked(pref.contains("checkedSumpUat21") && pref.getBoolean("checkedSumpUat21", false) == true);

        ChkBoxSilicaUat21.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (ChkBoxSilicaUat21.isChecked()) {
                    ketSilicaUat21.setText("OK");
                    editor.putBoolean("checkedSilicaUat21", true);
                    editor.apply();
                    sharedPref();
                } else {
                    ketSilicaUat21.setText("Not OK");
                    editor.putBoolean("checkedSilicaUat21", false);
                    editor.apply();
                    sharedPref();
                }
            }
        });

        ChkOilUat21.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (ChkOilUat21.isChecked()) {
                    ketOilUat21.setText("OK");
                    editor.putBoolean("checkedOilUat21", true);
                    editor.apply();
                    sharedPref();
                } else {
                    ketOilUat21.setText("Not OK");
                    editor.putBoolean("checkedOilUat21", false);
                    editor.apply();
                    sharedPref();
                }
            }
        });

        ChkGroundUat21.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (ChkGroundUat21.isChecked()) {
                    ketGroundUat21.setText("OK");
                    editor.putBoolean("checkedGroundUat21", true);
                    editor.apply();
                    sharedPref();
                } else {
                    ketGroundUat21.setText("Not OK");
                    editor.putBoolean("checkedGroundUat21", false);
                    editor.apply();
                    sharedPref();
                }
            }
        });

        ChkBucholzUat21.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (ChkBucholzUat21.isChecked()) {
                    ketBucholzUat21.setText("OK");
                    editor.putBoolean("checkedBushingUat21", true);
                    editor.apply();
                    sharedPref();
                } else {
                    ketBucholzUat21.setText("Not OK");
                    editor.putBoolean("checkedBucholzUat21", false);
                    editor.apply();
                    sharedPref();
                }
            }
        });

        ChkSumppumpUat21.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (ChkSumppumpUat21.isChecked()) {
                    ketSumppumpUat21.setText("OK");
                    editor.putBoolean("checkedSumpUat21", true);
                    editor.apply();
                    sharedPref();
                } else {
                    ketSumppumpUat21.setText("Not OK");
                    editor.putBoolean("checkedSumpUat21", false);
                    editor.apply();
                    sharedPref();
                }
            }
        });

        ChkBoxSilicaUat22.setChecked(pref.contains("checkedSilicaUat22") && pref.getBoolean("checkedSilicaUat22", false) == true);

        ChkOilUat22.setChecked(pref.contains("checkedOilUat22") && pref.getBoolean("checkedOilUat22", false) == true);

        ChkGroundUat22.setChecked(pref.contains("checkedGroundUat22") && pref.getBoolean("checkedGroundUat22", false) == true);

        ChkBucholzUat22.setChecked(pref.contains("checkedBucholzUat22") && pref.getBoolean("checkedBucholzUat22", false) == true);

        ChkSumppumpUat22.setChecked(pref.contains("checkedSumpUat22") && pref.getBoolean("checkedSumpUat22", false) == true);

        ChkBoxSilicaUat22.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (ChkBoxSilicaUat22.isChecked()) {
                    ketSilicaUat22.setText("OK");
                    editor.putBoolean("checkedSilicaUat22", true);
                    editor.apply();
                    sharedPref();
                } else {
                    ketSilicaUat22.setText("Not OK");
                    editor.putBoolean("checkedSilicaUat22", false);
                    editor.apply();
                    sharedPref();
                }
            }
        });

        ChkOilUat22.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (ChkOilUat22.isChecked()) {
                    ketOilUat22.setText("OK");
                    editor.putBoolean("checkedOilUat22", true);
                    editor.apply();
                    sharedPref();
                } else {
                    ketOilUat22.setText("Not OK");
                    editor.putBoolean("checkedOilUat22", false);
                    editor.apply();
                    sharedPref();
                }
            }
        });

        ChkGroundUat22.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (ChkGroundUat22.isChecked()) {
                    ketGroundUat22.setText("OK");
                    editor.putBoolean("checkedGroundUat22", true);
                    editor.apply();
                    sharedPref();
                } else {
                    ketGroundUat22.setText("Not OK");
                    editor.putBoolean("checkedGroundUat22", false);
                    editor.apply();
                    sharedPref();
                }
            }
        });

        ChkBucholzUat22.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (ChkBucholzUat22.isChecked()) {
                    ketBucholzUat22.setText("OK");
                    editor.putBoolean("checkedBushingUat22", true);
                    editor.apply();
                    sharedPref();
                } else {
                    ketBucholzUat22.setText("Not OK");
                    editor.putBoolean("checkedBucholzUat22", false);
                    editor.apply();
                    sharedPref();
                }
            }
        });

        ChkSumppumpUat22.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (ChkSumppumpUat22.isChecked()) {
                    ketSumppumpUat22.setText("OK");
                    editor.putBoolean("checkedSumpUat22", true);
                    editor.apply();
                    sharedPref();
                } else {
                    ketSumppumpUat22.setText("Not OK");
                    editor.putBoolean("checkedSumpUat22", false);
                    editor.apply();
                    sharedPref();
                }
            }
        });
    }
}
