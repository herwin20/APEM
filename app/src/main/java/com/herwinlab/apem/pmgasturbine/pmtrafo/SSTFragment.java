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

public class SSTFragment extends Fragment {

    //CheckBox
    public CheckBox ChkBoxSilicaSst12, ChkOilSst12, ChkFanSst12, ChkGroundSst12, ChkBucholzSst12, ChkSumppumpSst12; // GT 12
    public CheckBox ChkBoxSilicaSst13, ChkOilSst13, ChkFanSst13, ChkGroundSst13, ChkBucholzSst13, ChkSumppumpSst13; // GT 13
    //EditText
    public TextInputEditText EditOilTempSst12, EditWindingSst12, EditOillevelSst12, EditKeteranganSst12;
    public TextInputEditText EditOilTempSst13, EditWindingSst13, EditOillevelSst13, EditKeteranganSst13;
    //TextView
    public TextView ketSilicaSst12, ketOilSst12, ketFanSst12, ketGroundSst12, ketBucholzSst12, ketSumppumpSst12; // GT 12
    public TextView ketSilicaSst13, ketOilSst13, ketFanSst13, ketGroundSst13, ketBucholzSst13, ketSumppumpSst13; // GT 13
    //LinearButton
    public LinearLayout savedButtonGT12, savedButtonGT13;

    public SharedPreferences pref;
    //GT 12
    private final String OilTempSST12_KEY = "OilTempSST12";
    private final String WindingTempSST12_KEY = "WindingTempSST12";
    private final String OilLevelSST12_KEY = "OilLevelSST12";
    private final String KeteranganSST12_KEY = "KeteranganSST12";
    private final String SilicaGelSST12_KEY = "SilicaGelSST12";
    private final String OilSST12_KEY = "OilSST12";
    private final String FanSST12_KEY = "FanSST12";
    private final String GroundSST12_KEY = "GroundSST12";
    private final String BucholzSST12_KEY = "BucholzSST12";
    private final String SumppumpSST12_KEY = "SumppumpSST12";
    //GT 13
    private final String OilTempSST13_KEY = "OilTempSST13";
    private final String WindingTempSST13_KEY = "WindingTempSST13";
    private final String OilLevelSST13_KEY = "OilLevelSST13";
    private final String KeteranganSST13_KEY = "KeteranganSST13";
    private final String SilicaGelSST13_KEY = "SilicaGelSST13";
    private final String OilSST13_KEY = "OilSST13";
    private final String FanSST13_KEY = "FanSST13";
    private final String GroundSST13_KEY = "GroundSST13";
    private final String BucholzSST13_KEY = "BucholzSST13";
    private final String SumppumpSST13_KEY = "SumppumpSST13";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sst, container, false);

        //GT12
        ketSilicaSst12 = v.findViewById(R.id.silicaGelSst12);
        ketOilSst12 = v.findViewById(R.id.OilGlassSst12);
        ketGroundSst12 = v.findViewById(R.id.groundSst12);
        ketFanSst12 = v.findViewById(R.id.fanSst12);
        ketBucholzSst12 = v.findViewById(R.id.bucholzSst12);
        ketSumppumpSst12 = v.findViewById(R.id.sumpPumpSst12);
        //==========================================================
        EditOillevelSst12 = v.findViewById(R.id.indikatorlevelSst12);
        EditWindingSst12 = v.findViewById(R.id.windingSst12);
        EditOilTempSst12 = v.findViewById(R.id.oiltempSst12);
        EditKeteranganSst12 = v.findViewById(R.id.keteranganSst12);
        //==========================================================
        ChkBoxSilicaSst12 = v.findViewById(R.id.checkSilicaSst12);
        ChkOilSst12 = v.findViewById(R.id.checkOilSst12);
        ChkFanSst12 = v.findViewById(R.id.checkFanSst12);
        ChkGroundSst12 = v.findViewById(R.id.checkGroundSst12);
        ChkBucholzSst12 = v.findViewById(R.id.checkBucholzSst12);
        ChkSumppumpSst12 = v.findViewById(R.id.checkSumppumpSst12);
        //GT13
        ketSilicaSst13 = v.findViewById(R.id.silicaGelSst13);
        ketOilSst13 = v.findViewById(R.id.OilGlassSst13);
        ketFanSst13 = v.findViewById(R.id.fanSst13);
        ketGroundSst13 = v.findViewById(R.id.groundSst13);
        ketBucholzSst13 = v.findViewById(R.id.bucholzSst13);
        ketSumppumpSst13 = v.findViewById(R.id.sumpPumpSst13);
        //==========================================================
        EditOillevelSst13 = v.findViewById(R.id.indikatorlevelSst13);
        EditWindingSst13 = v.findViewById(R.id.windingSst13);
        EditOilTempSst13 = v.findViewById(R.id.oiltempSst13);
        EditKeteranganSst13 = v.findViewById(R.id.keteranganSst13);
        //==========================================================
        ChkBoxSilicaSst13 = v.findViewById(R.id.checkSilicaSst13);
        ChkOilSst13 = v.findViewById(R.id.checkOilSst13);
        ChkFanSst13= v.findViewById(R.id.checkFanSst13);
        ChkGroundSst13 = v.findViewById(R.id.checkGroundSst13);
        ChkBucholzSst13 = v.findViewById(R.id.checkBucholzSst13);
        ChkSumppumpSst13 = v.findViewById(R.id.checkSumppumpSst13);

        savedButtonGT12 = v.findViewById(R.id.buttonSavedSst12);
        savedButtonGT13 = v.findViewById(R.id.buttonSavedSst13);

        //SharePreference TextVIew
        pref = this.getActivity().getPreferences(MODE_PRIVATE);
        ketSilicaSst12.setText(pref.getString(SilicaGelSST12_KEY, ""));
        ketOilSst12.setText(pref.getString(OilSST12_KEY, ""));
        ketFanSst12.setText(pref.getString(FanSST12_KEY, ""));
        ketGroundSst12.setText(pref.getString(GroundSST12_KEY, ""));
        ketBucholzSst12.setText(pref.getString(BucholzSST12_KEY, ""));
        ketSumppumpSst12.setText(pref.getString(SumppumpSST12_KEY, ""));
        EditOilTempSst12.setText(pref.getString(OilTempSST12_KEY, ""));
        EditWindingSst12.setText(pref.getString(WindingTempSST12_KEY, ""));
        EditOillevelSst12.setText(pref.getString(OilLevelSST12_KEY, ""));
        EditKeteranganSst12.setText(pref.getString(KeteranganSST12_KEY, ""));

        ketSilicaSst13.setText(pref.getString(SilicaGelSST13_KEY, ""));
        ketOilSst13.setText(pref.getString(OilSST13_KEY, ""));
        ketFanSst13.setText(pref.getString(FanSST13_KEY, ""));
        ketGroundSst13.setText(pref.getString(GroundSST13_KEY, ""));
        ketBucholzSst13.setText(pref.getString(BucholzSST13_KEY, ""));
        ketSumppumpSst13.setText(pref.getString(SumppumpSST13_KEY, ""));
        EditOilTempSst13.setText(pref.getString(OilTempSST13_KEY, ""));
        EditWindingSst13.setText(pref.getString(WindingTempSST13_KEY, ""));
        EditOillevelSst13.setText(pref.getString(OilLevelSST13_KEY, ""));
        EditKeteranganSst13.setText(pref.getString(KeteranganSST13_KEY, ""));

        checkBoxSharedPrefSST();

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

        return v;
    }

    public void sharedPref() {
        pref = this.getActivity().getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(SilicaGelSST12_KEY, ketSilicaSst12.getText().toString());
        editor.putString(OilSST12_KEY, ketOilSst12.getText().toString());
        editor.putString(FanSST12_KEY, ketFanSst12.getText().toString());
        editor.putString(GroundSST12_KEY, ketGroundSst12.getText().toString());
        editor.putString(BucholzSST12_KEY, ketBucholzSst12.getText().toString());
        editor.putString(SumppumpSST12_KEY, ketSumppumpSst12.getText().toString());
        editor.putString(OilTempSST12_KEY, EditOilTempSst12.getText().toString());
        editor.putString(WindingTempSST12_KEY, EditWindingSst12.getText().toString());
        editor.putString(OilLevelSST12_KEY, EditOillevelSst12.getText().toString());
        editor.putString(KeteranganSST12_KEY, EditKeteranganSst12.getText().toString());

        editor.putString(SilicaGelSST13_KEY, ketSilicaSst13.getText().toString());
        editor.putString(OilSST13_KEY, ketOilSst13.getText().toString());
        editor.putString(FanSST13_KEY, ketFanSst13.getText().toString());
        editor.putString(GroundSST13_KEY, ketGroundSst13.getText().toString());
        editor.putString(BucholzSST13_KEY, ketBucholzSst13.getText().toString());
        editor.putString(SumppumpSST13_KEY, ketSumppumpSst13.getText().toString());
        editor.putString(OilTempSST13_KEY, EditOilTempSst13.getText().toString());
        editor.putString(WindingTempSST13_KEY, EditWindingSst13.getText().toString());
        editor.putString(OilLevelSST13_KEY, EditOillevelSst13.getText().toString());
        editor.putString(KeteranganSST13_KEY, EditKeteranganSst13.getText().toString());
        editor.apply();
    }

    public void checkBoxSharedPrefSST() {
        pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        final SharedPreferences.Editor editor = pref.edit();
        ChkBoxSilicaSst12.setChecked(pref.contains("checkedSilicaSst12") && pref.getBoolean("checkedSilicaSst12", false) == true);

        ChkOilSst12.setChecked(pref.contains("checkedOilSst12") && pref.getBoolean("checkedOilSst12", false) == true);

        ChkFanSst12.setChecked(pref.contains("checkedFanSst12") && pref.getBoolean("checkedFanSst12", false) == true);

        ChkGroundSst12.setChecked(pref.contains("checkedGroundSst12") && pref.getBoolean("checkedGroundSst12", false) == true);

        ChkBucholzSst12.setChecked(pref.contains("checkedBucholzSst12") && pref.getBoolean("checkedBucholzSst12", false) == true);

        ChkSumppumpSst12.setChecked(pref.contains("checkedSumpSst12") && pref.getBoolean("checkedSumpSst12", false) == true);

        ChkBoxSilicaSst12.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (ChkBoxSilicaSst12.isChecked()) {
                    ketSilicaSst12.setText("OK");
                    editor.putBoolean("checkedSilicaSst12", true);
                    editor.apply();
                    sharedPref();
                } else {
                    ketSilicaSst12.setText("Not OK");
                    editor.putBoolean("checkedSilicaSst12", false);
                    editor.apply();
                    sharedPref();
                }
            }
        });

        ChkFanSst12.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (ChkFanSst12.isChecked()) {
                    ketFanSst12.setText("OK");
                    editor.putBoolean("checkedFanSst12", true);
                    editor.apply();
                    sharedPref();
                } else {
                    ketFanSst12.setText("Not OK");
                    editor.putBoolean("checkedFanSst12", false);
                    editor.apply();
                    sharedPref();
                }
            }
        });

        ChkOilSst12.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (ChkOilSst12.isChecked()) {
                    ketOilSst12.setText("OK");
                    editor.putBoolean("checkedOilSst12", true);
                    editor.apply();
                    sharedPref();
                } else {
                    ketOilSst12.setText("Not OK");
                    editor.putBoolean("checkedOilSst12", false);
                    editor.apply();
                    sharedPref();
                }
            }
        });

        ChkGroundSst12.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (ChkGroundSst12.isChecked()) {
                    ketGroundSst12.setText("OK");
                    editor.putBoolean("checkedGroundSst12", true);
                    editor.apply();
                    sharedPref();
                } else {
                    ketGroundSst12.setText("Not OK");
                    editor.putBoolean("checkedGroundSst12", false);
                    editor.apply();
                    sharedPref();
                }
            }
        });

        ChkBucholzSst12.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (ChkBucholzSst12.isChecked()) {
                    ketBucholzSst12.setText("OK");
                    editor.putBoolean("checkedBushingSst12", true);
                    editor.apply();
                    sharedPref();
                } else {
                    ketBucholzSst12.setText("Not OK");
                    editor.putBoolean("checkedBucholzSst12", false);
                    editor.apply();
                    sharedPref();
                }
            }
        });

        ChkSumppumpSst12.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (ChkSumppumpSst12.isChecked()) {
                    ketSumppumpSst12.setText("OK");
                    editor.putBoolean("checkedSumpSst12", true);
                    editor.apply();
                    sharedPref();
                } else {
                    ketSumppumpSst12.setText("Not OK");
                    editor.putBoolean("checkedSumpSst12", false);
                    editor.apply();
                    sharedPref();
                }
            }
        });

        ChkBoxSilicaSst13.setChecked(pref.contains("checkedSilicaSst13") && pref.getBoolean("checkedSilicaSst13", false) == true);

        ChkOilSst13.setChecked(pref.contains("checkedOilSst13") && pref.getBoolean("checkedOilSst13", false) == true);

        ChkFanSst13.setChecked(pref.contains("checkedFanSst13") && pref.getBoolean("checkedFanSst13", false) == true);

        ChkGroundSst13.setChecked(pref.contains("checkedGroundSst13") && pref.getBoolean("checkedGroundSst13", false) == true);

        ChkBucholzSst13.setChecked(pref.contains("checkedBucholzSst13") && pref.getBoolean("checkedBucholzSst13", false) == true);

        ChkSumppumpSst13.setChecked(pref.contains("checkedSumpSst13") && pref.getBoolean("checkedSumpSst13", false) == true);

        ChkBoxSilicaSst13.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (ChkBoxSilicaSst13.isChecked()) {
                    ketSilicaSst13.setText("OK");
                    editor.putBoolean("checkedSilicaSst13", true);
                    editor.apply();
                    sharedPref();
                } else {
                    ketSilicaSst13.setText("Not OK");
                    editor.putBoolean("checkedSilicaSst13", false);
                    editor.apply();
                    sharedPref();
                }
            }
        });

        ChkOilSst13.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (ChkOilSst13.isChecked()) {
                    ketOilSst13.setText("OK");
                    editor.putBoolean("checkedOilSst13", true);
                    editor.apply();
                    sharedPref();
                } else {
                    ketOilSst13.setText("Not OK");
                    editor.putBoolean("checkedOilSst13", false);
                    editor.apply();
                    sharedPref();
                }
            }
        });

        ChkFanSst13.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (ChkFanSst13.isChecked()) {
                    ketFanSst13.setText("OK");
                    editor.putBoolean("checkedFanSst13", true);
                    editor.apply();
                    sharedPref();
                } else {
                    ketFanSst13.setText("Not OK");
                    editor.putBoolean("checkedFanSst13", false);
                    editor.apply();
                    sharedPref();
                }
            }
        });

        ChkGroundSst13.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (ChkGroundSst13.isChecked()) {
                    ketGroundSst13.setText("OK");
                    editor.putBoolean("checkedGroundSst13", true);
                    editor.apply();
                    sharedPref();
                } else {
                    ketGroundSst13.setText("Not OK");
                    editor.putBoolean("checkedGroundSst13", false);
                    editor.apply();
                    sharedPref();
                }
            }
        });

        ChkBucholzSst13.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (ChkBucholzSst13.isChecked()) {
                    ketBucholzSst13.setText("OK");
                    editor.putBoolean("checkedBushingSst13", true);
                    editor.apply();
                    sharedPref();
                } else {
                    ketBucholzSst13.setText("Not OK");
                    editor.putBoolean("checkedBucholzSst13", false);
                    editor.apply();
                    sharedPref();
                }
            }
        });

        ChkSumppumpSst13.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (ChkSumppumpSst13.isChecked()) {
                    ketSumppumpSst13.setText("OK");
                    editor.putBoolean("checkedSumpSst13", true);
                    editor.apply();
                    sharedPref();
                } else {
                    ketSumppumpSst13.setText("Not OK");
                    editor.putBoolean("checkedSumpUat13", false);
                    editor.apply();
                    sharedPref();
                }
            }
        });

    }
}
