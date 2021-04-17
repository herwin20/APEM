package com.herwinlab.apem.testprogram;

import android.app.Activity;
import android.app.Notification;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.herwinlab.apem.notification.NotificationUtils;
import com.herwinlab.apem.R;

public class TesNotifikasi extends AppCompatActivity {

    private Button tesNotif;
    private NotificationUtils mNotificationUtils;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notifikasi_activity);

        mNotificationUtils = new NotificationUtils(this);
        tesNotif = findViewById(R.id.btnShowNotif);

        tesNotif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Notification.Builder nb = mNotificationUtils.getAndroidChannelNotification("Tes Notifikasi","Herwin Ganteng");
                mNotificationUtils.getManager().notify(101, nb.build());
            }
        });
    }

}