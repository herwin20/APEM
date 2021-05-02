package com.herwinlab.apem;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

public class SplashScreen extends AppCompatActivity {

    private final int waktu_loading = 3000;
    private Timer timer;
    private ProgressBar progressBar;
    private final int i = 0;
    TextView textView;

    private static final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 123;


    //4000=4 detik

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_bg);
        transparentStatusAndNavigation();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Build.VERSION.SDK_INT >= 23) {
                    // Marshmallow+
                    permissioncheck();

                } else {
                    // Pre-Marshmallow
                    LaunchApp();
                }
            }
        }, 1000);

    }

    //-************************ permission check ***********************************************************************

    private void permissioncheck() {
        List<String> permissionsNeeded = new ArrayList<String>();

        final List<String> permissionsList = new ArrayList<String>();
        if (!addPermission(permissionsList, Manifest.permission.READ_EXTERNAL_STORAGE))
            permissionsNeeded.add("READ");
        if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE))
            permissionsNeeded.add("WRITE");
        if (!addPermission(permissionsList, Manifest.permission.CAMERA))
            permissionsNeeded.add("CAMERA");
        if (!addPermission(permissionsList, Manifest.permission.ACCESS_FINE_LOCATION))
            permissionsNeeded.add("GPS");
        if (!addPermission(permissionsList, Manifest.permission.RECORD_AUDIO))
            permissionsNeeded.add("AUDIO");

        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                // Need Rationale
                String message = "You need to grant access to " + permissionsNeeded.get(0);
                for (int i = 1; i < permissionsNeeded.size(); i++)
                    message = message + ", " + permissionsNeeded.get(i);
                showMessageOKCancel(message,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if (Build.VERSION.SDK_INT >= 23) {
                                    // Marshmallow+
                                    requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                                            REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);


                                } else {
                                    // Pre-Marshmallow
                                }

                            }
                        });
                return;
            }

            if (Build.VERSION.SDK_INT >= 23) {
                // Marshmallow+
                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);


            } else {
                // Pre-Marshmallow

            }

            return;
        }else
        {
            // Toast.makeText(this,"Permission",Toast.LENGTH_LONG).show();
            LaunchApp();
        }

        //insertDummyContact();
    }


    private boolean addPermission(List<String> permissionsList, String permission) {

        Boolean cond;
        if (Build.VERSION.SDK_INT >= 23) {
            // Marshmallow+
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsList.add(permission);
                // Check for Rationale Option
                if (!shouldShowRequestPermissionRationale(permission))
                    //  return false;

                    cond = false;
            }
            //  return true;


        } else {
            // Pre-Marshmallow
        }
        cond = true;

        return cond;

    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(SplashScreen.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == 23) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                //Displaying a toast
                Toast.makeText(this, "Permission granted", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Permission Needed To Run The App", Toast.LENGTH_LONG).show();
            }
        }
        if (requestCode == REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS) {
            Map<String, Integer> perms = new HashMap<String, Integer>();
            // Initial
            perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
            perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
            perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
            perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
            perms.put(Manifest.permission.RECORD_AUDIO, PackageManager.PERMISSION_GRANTED);
            //Toast.makeText(SplashScreen.this, " Permissions are jddddd", Toast.LENGTH_SHORT).show();
            // Fill with results
            for (int i = 0; i < permissions.length; i++)
                perms.put(permissions[i], grantResults[i]);
            // Check for ACCESS_FINE_LOCATION
            if (perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                    perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    perms.get(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)
            {
                // All Permissions Granted
                // insertDummyContact();

                //Toast.makeText(SplashScreen.this, " Permissions are l", Toast.LENGTH_SHORT).show();
                LaunchApp();

            } else {
                // Permission Denied
                Toast.makeText(SplashScreen.this, "Some Permission is Denied", Toast.LENGTH_SHORT)
                        .show();

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Do something after 100
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                        finish();
                    }
                }, 3000);
            }

        }
    }

    public void LaunchApp()
    {
        Thread background = new Thread() {
            public void run() {

                try {
                    // Thread will sleep for 10 seconds
                    sleep(4*1000);

                    startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                    finish();

                } catch (Exception e) {

                }
            }
        };

        // start thread
        background.start();


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
