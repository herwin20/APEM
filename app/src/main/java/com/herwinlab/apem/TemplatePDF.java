package com.herwinlab.apem;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.os.Environment;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TemplatePDF extends AppCompatActivity {

    public EditText A1_NDE, A2_NDE, A3_NDE;
    public EditText A1_DE, A2_DE, A3_DE;
    public Button createdPDF;
    Bitmap bitmap, scaleBitmap, PJBscale, IPJBway, TTDpakdam;
    int pageWidth = 1200;
    Date dateTime;
    DateFormat dateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.template_pdf);

        A1_NDE   = findViewById(R.id.a1_nde);
        A2_NDE   = findViewById(R.id.a2_nde);
        A3_NDE   = findViewById(R.id.a3_nde);
        A1_DE   = findViewById(R.id.a1_de);
        A2_DE   = findViewById(R.id.a2_de);
        A3_DE   = findViewById(R.id.a3_de);

        createdPDF = findViewById(R.id.prediction_A);

        //cover header
        //bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bg_cover);
        //scaleBitmap = Bitmap.createScaledBitmap(bitmap,1200, 518, false);

        //Logo PJB
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.logo_pjb);
        PJBscale = Bitmap.createScaledBitmap(bitmap,200, 100, false);

        //Logo PJB
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.logo_ipjb);
        IPJBway = Bitmap.createScaledBitmap(bitmap,260, 100, false);

        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ttd);
        TTDpakdam = Bitmap.createScaledBitmap(bitmap,390, 180, false);

        //permission
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        createPDF();

    }

    private void createPDF()
    {
        createdPDF.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SimpleDateFormat")
            @Override
            public void onClick(View v) {

                dateTime = new Date();

                //get input
           /*     if     (A1_DE.getText().toString().length() == 0 ||
                        A2_DE.getText().toString().length() == 0 ||
                        A3_DE.getText().toString().length() == 0 ||
                        A1_NDE.getText().toString().length() == 0 ||
                        A2_NDE.getText().toString().length() == 0 ||
                        A3_NDE.getText().toString().length() == 0) {
                    Toast.makeText(MainActivity.this, "Data tidak boleh kosong!", Toast.LENGTH_LONG).show();
                } else { */

                    PdfDocument pdfDocument = new PdfDocument();
                    Paint paint = new Paint();
                    Paint titlePaint = new Paint(); // Untuk Judul Header

                    PdfDocument.PageInfo pageInfo
                            = new PdfDocument.PageInfo.Builder(1200, 2010, 1).create();
                    PdfDocument.Page page = pdfDocument.startPage(pageInfo);

                    Canvas canvas = page.getCanvas();
                    //canvas.drawBitmap(scaleBitmap, 0, 0, paint);
                    canvas.drawBitmap(PJBscale,50,75, paint); // Logo PJB
                    canvas.drawBitmap(IPJBway,905,75, paint); // Logo IPJB
                    canvas.drawBitmap(TTDpakdam, 780, 1980-180, paint);

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

                    // Judul Header
                    titlePaint.setTextAlign(Paint.Align.CENTER);
                    titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                    titlePaint.setColor(Color.BLACK);
                    titlePaint.setTextSize(30);
                    canvas.drawText("CHECKLIST PREVENTIVE MAINTENANCE", 1170 / 2, 70, titlePaint);
                    canvas.drawText("PEMELIHARAAN LISTRIK BLOK 1-2", 1170 / 2, 115, titlePaint);
                    canvas.drawText("PT PEMBANGKITAN JAWA-BALI", 1170 / 2, 155, titlePaint);
                    canvas.drawText("UP MUARA TAWAR", 1170 / 2, 195, titlePaint);

                    // Nama Preventive GT dan Tanggal
                    paint.setTextAlign(Paint.Align.LEFT);
                    paint.setColor(Color.BLACK);
                    paint.setTextSize(30f);
                    canvas.drawText(" Pemeliharan Motor Listrik", 30, 270, paint );
                    paint.setTextAlign(Paint.Align.CENTER);
                    paint.setColor(Color.BLACK);
                    paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                    paint.setTextSize(30);
                    canvas.drawText("GT 1.1", 1170/2, 270, paint);
                    paint.setTextAlign(Paint.Align.LEFT);
                    paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
                    paint.setColor(Color.BLACK);
                    paint.setTextSize(30);
                    dateFormat = new SimpleDateFormat("dd/MM/yy");
                    canvas.drawText(dateFormat.format(dateTime), 1170 - 145, 270, paint);
                    dateFormat = new SimpleDateFormat("HH:mm:ss");
                    canvas.drawText(dateFormat.format(dateTime), 1170 - 145, 300, paint);

                    //Garis Footer
                    canvas.drawLine(30, 1800, 1170, 1800, paint);
                    canvas.drawLine(390, 1800, 390, 1980, paint);
                    canvas.drawLine(780, 1800, 780, 1980, paint);

                    // Tanda Tangan dan Nama Pelaksana
                    paint.setTextSize(25);
                    canvas.drawText("Nama Pelaksana :", 50, 1980-150, paint);
                    canvas.drawText("1. Herwin Januardi", 70, 1980-110, paint);
                    canvas.drawText("2. Herwin Januardi", 70, 1980-70, paint);
                    canvas.drawText("3. Herwin Januardi", 70, 1980-30, paint);

                    canvas.drawText("Regu Operasi", 420, 1980-150, paint );
                    canvas.drawText("Herwin Januardi", 420, 1980-30, paint);

                    canvas.drawText("SPV Listrik 1-2", 800, 1980-150, paint );
                    canvas.drawText("Dammora W", 800, 1980-30, paint);

                    /*paint.setTextAlign(Paint.Align.LEFT);
                    paint.setColor(Color.BLACK);
                    paint.setTextSize(35f);
                    canvas.drawText("A1 NDE: " + A1_NDE.getText(), 20, 590, paint);
                    canvas.drawText("A2 NDE: " + A2_NDE.getText(), 20, 640, paint); */

                    pdfDocument.finishPage(page);

                    File file = new File(Environment.getExternalStorageDirectory(), "/ChkBoxSilicaGT11.pdf");
                    try {
                        pdfDocument.writeTo(new FileOutputStream(file));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    pdfDocument.close();
                    Toast.makeText(TemplatePDF.this, "PDF sudah dibuat", Toast.LENGTH_LONG).show();

              //  }
            }
        });

        dateTime = new Date();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
