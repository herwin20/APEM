package com.herwinlab.apem;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.herwinlab.apem.pmgasturbine.PmGasTurbine;
import com.herwinlab.apem.testprogram.SignatureCoba;
import com.herwinlab.apem.testprogram.TesNotifikasi;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LocationListener {

    TextView textView, locationText;
    LocationManager locationManager;
    String userName;
    TextView txt_id;
    SharedPreferences sharedPreferences;
    CoordinatorLayout coor;
    private static final int MY_PERMISSIONS_REQUEST_GET_CAMERA = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_main_activity);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        //getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        //FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        //fab.setOnClickListener(new View.OnClickListener() {
        //   @Override
        //   public void onClick(View view) {
        //       Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
        //               .setAction("Action", null).show();
        //  }
        //});

        transparentStatusAndNavigation();
        locationText = (TextView)findViewById(R.id.latitude);

        //Get GPS
        getLocation();

        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        }


        //Function IP Address
       /* textView = (TextView) findViewById(R.id.ip_address);
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        String ipAddress = Formatter.formatIpAddress(wifiManager.getConnectionInfo().getIpAddress());
        textView.setText("IP Address : " + ipAddress); */

        //card view click
        CardView card1 = (CardView) findViewById(R.id.cardView);
        card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PmGasTurbine.class);
                startActivity(intent);
            }
        });

        CardView card2 = (CardView) findViewById(R.id.cardView2);
        card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent2 = new Intent(MainActivity.this, PmSteamTurbine.class);
                //startActivity(intent2);
            }
        });

        //Contoh Notifikasi
        CardView card3 = (CardView) findViewById(R.id.cardView3);
        card3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(MainActivity.this, TesNotifikasi.class);
                startActivity(intent3);
            }
        });

        final FloatingActionButton fab_share = (FloatingActionButton) findViewById(R.id.fab_share);
        fab_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionShare();
            }
        });

        final FloatingActionButton fab_contact = (FloatingActionButton) findViewById(R.id.fab_contact);
        fab_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionFeedback();
            }
        });

        final FloatingActionButton fab_logout = (FloatingActionButton) findViewById(R.id.fab_logout);
        fab_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("LOGOUT");
                builder.setMessage("Do you want to Log Out ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        SharedPreferences sharedpreferences = getSharedPreferences(LoginActivity.MyPREFERENCES, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.clear();
                        editor.commit();
                        Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                        startActivity(intent);
                        finish();

                    }
                });

                builder.setNegativeButton("Not now", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        //retrive data
        txt_id = findViewById(R.id.share_user);
        sharedPreferences = getSharedPreferences(LoginActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        String userName = sharedPreferences.getString("userName", null);
        txt_id.setText(userName);

        coor = findViewById(R.id.main_coordinator);
    }


    void getLocation()
    {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,5000,5,this);
        }
        catch(SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        //locationText.setText("Latitude: " + location.getLatitude() + "\nLongitude: " + location.getLongitude());

        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            locationText.setText(locationText.getText() + "\n"+addresses.get(0).getAddressLine(0));/*+", "+
                    addresses.get(0).getAddressLine(1)+", "+addresses.get(0).getAddressLine(2));*/
        }
        catch(Exception e)
        {

        }

    }

    private void onOptionShare() {
        String[] TO = {"herwinjanuardi20@gmail.com"};
        String[] CC = {""};
        Intent shareIntent = new Intent(Intent.ACTION_SEND);

        shareIntent.setData(Uri.parse("mailto:"));
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "https://drive.google.com/drive/u/0/folders/1MWqBv9RDebNQSbd3trBWqqU96nrthX4Z");

        try {
            startActivity(Intent.createChooser(shareIntent,"Share To "));
        }catch (android.content.ActivityNotFoundException ex){
            Toast.makeText(MainActivity.this,"Bla",Toast.LENGTH_SHORT).show();
        }
    }

    public void onOptionFeedback() {
        final Uri contactUri = null;
        String[] TO = {"herwinjanuardi20@gmail.com"};
        String[] CC = {""};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("message/rfc822");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT,"Feedback");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Email Here");

        try {
            startActivity(Intent.createChooser(emailIntent,"Send Email"));
        }catch (android.content.ActivityNotFoundException ex){
            Toast.makeText(MainActivity.this,"Bla",Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(MainActivity.this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onBackPressed()
    {
        Snackbar snackbar = Snackbar.make(coor,"Do You Want to Exit ??", Snackbar.LENGTH_LONG)
                .setAction("EXIT", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
        snackbar.show();
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
