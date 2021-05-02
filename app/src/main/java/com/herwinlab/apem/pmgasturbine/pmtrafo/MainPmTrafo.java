package com.herwinlab.apem.pmgasturbine.pmtrafo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.herwinlab.apem.R;
import com.herwinlab.apem.pmgasturbine.PmGenerator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.herwinlab.apem.pmgasturbine.pmtrafo.MainTrafoFragment.MainTrafo;

public class MainPmTrafo extends AppCompatActivity {

    public FloatingActionButton FabTrafoDelete, FabTrafoPDF;
    //PDF
    Bitmap bitmap, scaleBitmap, PJBscale, IPJBway, TTDpakdam, bitmapTTD, turbineImg;
    int pageWidth = 1200;
    Date dateTime;
    DateFormat dateFormat;

    //Title PDF
    public TextView judulPmTrafo;

    public String Test;

    public TextInputEditText EditOilTempGT11;

    //Call Frament
    public FragmentManager mainTrafoFrag;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tabpm_transformer);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final TabLayout tabLayout = findViewById(R.id.tab_layout);
        final ViewPager viewPager = findViewById(R.id.pager);

        //Memanggil dan Memasukan Value pada Class PagerAdapter(FragmentManager dan JumlahTab)
        TabsPagerAdapter pagerAdapter = new TabsPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());

        //Memasang Adapter pada ViewPager
        viewPager.setAdapter(pagerAdapter);

        //Logo PJB
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.logo_pjb);
        PJBscale = Bitmap.createScaledBitmap(bitmap,200, 100, false);
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.turbine);
        turbineImg = Bitmap.createScaledBitmap(bitmap, 514, 500, false);

        /*SharedPreferences sharedPreferences = getSharedPreferences(MainTrafo, Context.MODE_PRIVATE);
        Test = sharedPreferences.getString("OilTempGT11_KEY", ""); */

        Intent intent = getIntent();
        Test = intent.getStringExtra("Test");

       /* FabTrafoPDF = findViewById(R.id.fab_pdf);
        FabTrafoPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainPmTrafo.this, "Woy "+Test, Toast.LENGTH_SHORT).show();
                //createPDFTrafo();
            }
        });

        FabTrafoDelete = findViewById(R.id.fab_delete);
        FabTrafoDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainPmTrafo.this, "Delete", Toast.LENGTH_SHORT).show();
            }
        }); */

        /**
         Menambahkan Listener yang akan dipanggil kapan pun halaman berubah atau
         bergulir secara bertahap, sehingga posisi tab tetap singkron
         */
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        //Callback Interface dipanggil saat status pilihan tab berubah.
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //Dipanggil ketika tab memasuki state/keadaan yang dipilih.
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                //Dipanggil saat tab keluar dari keadaan yang dipilih.
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                //Dipanggil ketika tab yang sudah dipilih, dipilih lagi oleh user.
            }
        });

        //EditOilTempGT11 = findViewById(R.id.oiltemp);
    }

    public void createPDFTrafo()
    {
        dateTime = new Date();
        PdfDocument pdfDocument = new PdfDocument();
        Paint paint = new Paint();
        Paint titlePaint = new Paint(); // Untuk Judul Header

        PdfDocument.PageInfo pageInfo
                = new PdfDocument.PageInfo.Builder(1200, 2010, 1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);

        Canvas canvas = page.getCanvas();
        //canvas.drawBitmap(scaleBitmap, 0, 0, paint);
        canvas.drawBitmap(PJBscale,50,75, paint); // Logo PJB
        //canvas.drawBitmap(IPJBway,905,75, paint); // Logo IPJB
        canvas.drawBitmap(turbineImg,655, 1300, paint );

        //TTD Digital
        //canvas.drawBitmap(Bitmap.createScaledBitmap(bitmapImageOPS,250,200,false),400,1980-180,paint );
        //canvas.drawBitmap(Bitmap.createScaledBitmap(bitmapImageSPV,250,200,false),800,1980-180, paint);

        // Garis tepi
        canvas.drawLine(30, 30, 1170, 30, paint); // Garis Atas
        canvas.drawLine(30, 30, 30, 1980, paint); // Garis Kiri
        canvas.drawLine(1170, 30, 1170, 1980, paint); // Garis Kanan
        canvas.drawLine(30, 1980, 1170, 1980, paint); // Garis Bawah

        //Garis Header
        canvas.drawLine(30, 220, 1170, 220, paint);
        canvas.drawLine(30, 230, 1170, 230, paint);
        canvas.drawLine(265, 30, 265, 220, paint); // Kiri
        canvas.drawLine(905, 30, 905, 220, paint); // Kanan
        //Bawah tanggal
        canvas.drawLine(30, 310, 1170, 310, paint);

        // Garis NO Document
        canvas.drawLine(905, 90, 1170, 90, paint);
        canvas.drawLine(905, 160, 1170, 160, paint);
        canvas.drawLine(905, 230, 1170, 230, paint);

        // Judul Header
        titlePaint.setTextAlign(Paint.Align.CENTER);
        titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        titlePaint.setColor(Color.BLACK);
        titlePaint.setTextSize(25f);
        canvas.drawText("PT PEMBANGKITAN JAWA BALI UP MUARA TAWAR", 1170 / 2, 70, titlePaint);
        titlePaint.setTextSize(20f);
        canvas.drawText("PJB INTEGRATED MANAGEMENT SYSTEM", 1170 / 2, 110, titlePaint);
        titlePaint.setTextSize(25f);
        canvas.drawText("CEK LIST PREVENTIVE PEMELIHARAAN RUTIN ", 1170 / 2, 155, titlePaint);
        canvas.drawText("PEMELIHARAAN TRANSFORMER GT", 1170 / 2, 195, titlePaint);

        titlePaint.setTextSize(18f);
        titlePaint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("Nomor Dokumen : FMT-17.2.1", 912,65, titlePaint);
        canvas.drawText("Revisi : 00", 912,135, titlePaint);
        canvas.drawText("Tanggal Terbit : 20-08-2013", 912,195, titlePaint);

        // Nama Preventive GT dan Tanggal
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setColor(Color.BLACK);
        paint.setTextSize(30f);
        canvas.drawText("Main Transformer GT ", 40, 270, paint ); // Judul PM
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        paint.setColor(Color.BLACK);
        paint.setTextSize(30);
        dateFormat = new SimpleDateFormat("dd/MM/yy");
        canvas.drawText(dateFormat.format(dateTime), 1170 - 145, 270, paint);
        dateFormat = new SimpleDateFormat("HH:mm:ss");
        canvas.drawText(dateFormat.format(dateTime), 1170 - 145, 300, paint);

        /** Isi dari PDF */
        paint.setColor(Color.BLACK);
        paint.setTextSize(30f);
        canvas.drawText(Test,180, 450, paint);

        //Garis Footer
        canvas.drawLine(30, 1800, 1170, 1800, paint);
        canvas.drawLine(390, 1800, 390, 1980, paint);
        canvas.drawLine(780, 1800, 780, 1980, paint);

        // Tanda Tangan dan Nama Pelaksana
        paint.setTextSize(25);
        canvas.drawText("Nama Pelaksana :", 50, 1980-150, paint);
        canvas.drawText("1. "/*+orangPm1.getText().toString()*/, 70, 1980-110, paint);
        canvas.drawText("2. "/*+orangPm2.getText().toString()*/, 70, 1980-70, paint);
        canvas.drawText("3. "/*+orangPm3.getText().toString()*/, 70, 1980-30, paint);

        canvas.drawText("Regu Operasi", 420, 1980-150, paint );
        //canvas.drawText(Operator.getText().toString(), 420, 1980-30, paint);

        canvas.drawText("SPV Listrik 1-2", 800, 1980-150, paint );
        canvas.drawText("Dammora W", 800, 1980-30, paint);

        dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        pdfDocument.finishPage(page);
        File file = new File(Environment.getExternalStorageDirectory(), "/"+judulPmTrafo.getText().toString()+" "+dateFormat.format(dateTime)+".pdf");
        try {
            pdfDocument.writeTo(new FileOutputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }

        pdfDocument.close();
        Toast.makeText(MainPmTrafo.this, "PDF has Created", Toast.LENGTH_LONG).show();


        dateTime = new Date();
    }
}
