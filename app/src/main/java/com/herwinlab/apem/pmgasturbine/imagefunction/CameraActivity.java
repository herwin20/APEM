package com.herwinlab.apem.pmgasturbine.imagefunction;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.google.android.material.textfield.TextInputEditText;
import com.herwinlab.apem.BuildConfig;
import com.herwinlab.apem.R;
import com.herwinlab.apem.pmgasturbine.PmGenerator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.Random;

public class CameraActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST_CODE_BEFORE = 1;
    private static final int CAMERA_REQUEST_CODE_PROCESS = 2;
    private static final int CAMERA_REQUEST_CODE_AFTER = 3;
    String DIRECTORY_PHOTO = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Preventive/GT/";
    Bitmap bitmap, PJBscale, IPJBway, turbineImg;
    Date dateTime;
    DateFormat dateFormat;
    LinearLayout buttonPhotoBefore, buttonPhotoProcess, buttonPhotoAfter, buttonCreatedPhotoPDF, dialogCreatePDF;
    ImageView cameraViewBefore, cameraViewProcess, cameraViewAfter;
    TextInputEditText namaFolder, namaPhotoBefore, namaPhotoProcess, namaPhotoAfter, namaGTdialog, namaPDF;
    private final File output=null;
    public Uri photoUri;
    String fileName;

    AlertDialog.Builder dialogCustomPDF;
    LayoutInflater inflaterPDF;
    View dialogViewPDF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_activity);
        // PDF Bitmap
        //Logo PJB
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.logo_pjb);
        PJBscale = Bitmap.createScaledBitmap(bitmap,200, 100, false);

        //Logo PJB
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.logo_ipjb);
        IPJBway = Bitmap.createScaledBitmap(bitmap,260, 100, false);

        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.turbine);
        turbineImg = Bitmap.createScaledBitmap(bitmap, 514, 500, false);

        //Button Untuk Take Photo
        buttonPhotoBefore = findViewById(R.id.photo_activityBefore);
        buttonPhotoProcess = findViewById(R.id.photo_activityProcess);
        buttonPhotoAfter = findViewById(R.id.photo_activityAfter);

        //Button Created PDF
        buttonCreatedPhotoPDF = findViewById(R.id.dialogPhotoCreatepdf);
        //ImageView Camera
        cameraViewBefore = findViewById(R.id.previewPhotoBefore);
        cameraViewProcess = findViewById(R.id.previewPhotoProcess);
        cameraViewAfter = findViewById(R.id.previewPhotoAfter);

        //namaFolder
        namaFolder = findViewById(R.id.namaFolder);

        namaPhotoBefore = findViewById(R.id.namafoto_before);
        namaPhotoProcess = findViewById(R.id.namafoto_process);
        namaPhotoAfter = findViewById(R.id.namafoto_after);

        buttonCreatedPhotoPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(CameraActivity.this, R.style.CustomAlertDialog);
                ViewGroup viewGroup = findViewById(R.id.content);
                final View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.dialog_camerapdf, viewGroup, false);
                builder.setView(dialogView);
                final AlertDialog alertDialog = builder.create();

                //namaGTdialog = dialogView.findViewById(R.id.nama_gt);
                namaPDF = dialogView.findViewById(R.id.nama_pdf);
                dialogCreatePDF = dialogView.findViewById(R.id.dialogCreatepdf);

                dialogCreatePDF.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String nama_PDF = namaPDF.getText().toString();
                        if (nama_PDF.isEmpty())
                        {
                            namaPDF.setError("Isi dahulu, nama GT sesuai dengan saat Take Photo ");
                            namaPDF.requestFocus();
                        }
                        else {
                            CreatedPDFPhoto();
                        }
                    }
                });

                alertDialog.show();

            }
        });

        buttonPhotoBefore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String namaImageBefore = namaPhotoBefore.getText().toString();
                final String namaFolderStorage = namaFolder.getText().toString();
                if(namaImageBefore.isEmpty())
                {
                    namaPhotoBefore.setError("Please write Tag Picture");
                    namaPhotoBefore.requestFocus();
                }
                else if (namaFolderStorage.isEmpty()) {
                    namaFolder.setError("Write the folder on storage");
                    namaFolder.requestFocus();
                }

                else {
                    File dir = new File(Environment.getExternalStorageDirectory(), "Preventive/GT/"+namaFolderStorage+"/");
                    dir.mkdirs();
                    File cameraPhoto = new File(dir, namaImageBefore+".jpeg"); // Folder Biar tidak sama
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    photoUri = FileProvider.getUriForFile(Objects.requireNonNull(getApplicationContext()),
                            BuildConfig.APPLICATION_ID + ".provider", cameraPhoto);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                    startActivityForResult(intent, CAMERA_REQUEST_CODE_BEFORE);
                }
            }
        });

        buttonPhotoProcess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String namaImageProcess = namaPhotoProcess.getText().toString();
                final String namaFolderStorage = namaFolder.getText().toString();
                if(namaImageProcess.isEmpty())
                {
                    namaPhotoProcess.setError("Please write Tag Picture");
                    namaPhotoProcess.requestFocus();
                }
                else if (namaFolderStorage.isEmpty()) {
                    namaFolder.setError("Write the folder on storage");
                    namaFolder.requestFocus();
                }

                else {
                    File dir = new File(Environment.getExternalStorageDirectory(), "Preventive/GT/"+namaFolderStorage+"/");
                    dir.mkdirs();
                    File cameraPhoto = new File(dir, namaImageProcess+".jpeg"); // Folder Biar tidak sama
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    photoUri = FileProvider.getUriForFile(Objects.requireNonNull(getApplicationContext()),
                            BuildConfig.APPLICATION_ID + ".provider", cameraPhoto);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                    startActivityForResult(intent, CAMERA_REQUEST_CODE_PROCESS);
                }
            }
        });

        buttonPhotoAfter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String namaImageAfter = namaPhotoAfter.getText().toString();
                final String namaFolderStorage = namaFolder.getText().toString();
                if(namaImageAfter.isEmpty())
                {
                    namaPhotoAfter.setError("Please write Tag Picture");
                    namaPhotoAfter.requestFocus();
                }
                else if (namaFolderStorage.isEmpty()) {
                    namaFolder.setError("Write the folder on storage");
                    namaFolder.requestFocus();
                }

                else {
                    File dir = new File(Environment.getExternalStorageDirectory(), "Preventive/GT/"+namaFolderStorage+"/");
                    dir.mkdirs();
                    File cameraPhoto = new File(dir, namaImageAfter+".jpeg"); // Folder Biar tidak sama
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    photoUri = FileProvider.getUriForFile(Objects.requireNonNull(getApplicationContext()),
                            BuildConfig.APPLICATION_ID + ".provider", cameraPhoto);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                    startActivityForResult(intent, CAMERA_REQUEST_CODE_AFTER);
                }
            }
        });

        initPhotoError();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE_BEFORE)
        {
            if(resultCode == Activity.RESULT_OK){
                // result code sama, save gambar ke bitmap
                //Bitmap bitmap;
                //bitmap = (Bitmap) data.getExtras().get("data");
                //cameraView.setImageBitmap(bitmap);
                Log.d("TAG", "outputFileUri RESULT_OK" + photoUri);
                if (photoUri != null) {

                    Bitmap bitmapBefore;
                    bitmapBefore = decodeSampledBitmapFromUri(photoUri,
                            cameraViewBefore.getWidth(), cameraViewBefore.getHeight());

                    if (bitmapBefore == null) {
                        Toast.makeText(getApplicationContext(),
                                "the image data could not be decoded",
                                Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(
                                getApplicationContext(),
                                "Decoded Bitmap: " + bitmapBefore.getWidth() + " x "
                                        + bitmapBefore.getHeight(), Toast.LENGTH_LONG)
                                .show();
                        cameraViewBefore.setImageBitmap(bitmapBefore);
                    }
                }
            }
        }
        if (requestCode == CAMERA_REQUEST_CODE_PROCESS)
        {
            if (resultCode == Activity.RESULT_OK){
                // result code sama, save gambar ke bitmap
                //Bitmap bitmap;
                //bitmap = (Bitmap) data.getExtras().get("data");
                //cameraView.setImageBitmap(bitmap);
                Log.d("TAG", "outputFileUri RESULT_OK" + photoUri);
                if (photoUri != null) {

                    Bitmap bitmapProcess;
                    bitmapProcess = decodeSampledBitmapFromUri(photoUri,
                            cameraViewProcess.getWidth(), cameraViewProcess.getHeight());

                    if (bitmapProcess == null) {
                        Toast.makeText(getApplicationContext(),
                                "the image data could not be decoded",
                                Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(
                                getApplicationContext(),
                                "Decoded Bitmap: " + bitmapProcess.getWidth() + " x "
                                        + bitmapProcess.getHeight(), Toast.LENGTH_LONG)
                                .show();
                        cameraViewProcess.setImageBitmap(bitmapProcess);
                    }
                }
            }
        }
        if (requestCode == CAMERA_REQUEST_CODE_AFTER)
        {
            if (resultCode == Activity.RESULT_OK) {
                // result code sama, save gambar ke bitmap
                //Bitmap bitmap;
                //bitmap = (Bitmap) data.getExtras().get("data");
                //cameraView.setImageBitmap(bitmap);
                Log.d("TAG", "outputFileUri RESULT_OK" + photoUri);
                if (photoUri != null) {

                    Bitmap bitmapAfter;
                    bitmapAfter = decodeSampledBitmapFromUri(photoUri,
                            cameraViewAfter.getWidth(), cameraViewAfter.getHeight());

                    if (bitmapAfter == null) {
                        Toast.makeText(getApplicationContext(),
                                "the image data could not be decoded",
                                Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(
                                getApplicationContext(),
                                "Decoded Bitmap: " + bitmapAfter.getWidth() + " x "
                                        + bitmapAfter.getHeight(), Toast.LENGTH_LONG)
                                .show();
                        cameraViewAfter.setImageBitmap(bitmapAfter);
                    }
                }
            }
        }
    }

    //yg bener Asli
/*    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CAMERA_REQUEST_CODE_BEFORE :
                if(resultCode == Activity.RESULT_OK)
                {
                    // result code sama, save gambar ke bitmap
                    //Bitmap bitmap;
                    //bitmap = (Bitmap) data.getExtras().get("data");
                    //cameraView.setImageBitmap(bitmap);
                    Log.d("TAG", "outputFileUri RESULT_OK" + photoUri);
                    if (photoUri != null) {

                        Bitmap bitmap;
                        bitmap = decodeSampledBitmapFromUri(photoUri,
                                cameraViewBefore.getWidth(), cameraViewBefore.getHeight());

                        if (bitmap == null) {
                            Toast.makeText(getApplicationContext(),
                                    "the image data could not be decoded",
                                    Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(
                                    getApplicationContext(),
                                    "Decoded Bitmap: " + bitmap.getWidth() + " x "
                                            + bitmap.getHeight(), Toast.LENGTH_LONG)
                                    .show();
                            cameraViewBefore.setImageBitmap(bitmap);
                        }
                    }
                }
                break;
        }
    } */

 /*   public void saveImage(Bitmap finalBitmap)
    {
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/Preventive/");
        myDir.mkdirs();
        //Random generator = new Random();
        //int n = 10000;
        //n = generator.nextInt(n);
        String fname = namaPhoto.getText().toString()+".jpg";
        File file = new File (myDir, fname);
        if (file.exists ()) file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            //finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG,1000, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static File getOutputMediaFile(){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "CameraDemo");

        if (!mediaStorageDir.exists()){
            if (!mediaStorageDir.mkdirs()){
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator +
                "IMG_"+ timeStamp + ".jpg");
    } */

    private void initPhotoError(){
        // android 7.0 system to solve the problem of taking pictures
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
    }

    public Bitmap decodeSampledBitmapFromUri(Uri uri, int reqWidth,
                                             int reqHeight) {

        Bitmap bm = null;

        try {
            // First decode with inJustDecodeBounds=true to check dimensions
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(getContentResolver()
                    .openInputStream(uri), null, options);

            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, reqWidth,
                    reqHeight);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            bm = BitmapFactory.decodeStream(getContentResolver()
                    .openInputStream(uri), null, options);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.toString(),
                    Toast.LENGTH_LONG).show();
        }

        return bm;
    }

    public int calculateInSampleSize(BitmapFactory.Options options,
                                     int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }
        }
        return inSampleSize;
    }

    public void CreatedPDFPhoto()
    {

        String namaFolderPDF = namaFolder.getText().toString();
        String namaFotoBeforePDF = namaPhotoBefore.getText().toString();
        String namaFotoProcessPDF = namaPhotoProcess.getText().toString();
        String namaFotoAfterPDF = namaPhotoAfter.getText().toString();

        //Photo Before, Process, After
        String ImageBefore  = DIRECTORY_PHOTO+namaFolderPDF+"/"+namaFotoBeforePDF+".jpeg";
        Bitmap bitmapImageBfr = BitmapFactory.decodeFile(ImageBefore);
        String ImageProcess = DIRECTORY_PHOTO+namaFolderPDF+"/"+namaFotoProcessPDF+".jpeg";
        Bitmap bitmapImagePro = BitmapFactory.decodeFile(ImageProcess);
        String ImageAfter   = DIRECTORY_PHOTO+namaFolderPDF+"/"+namaFotoAfterPDF+".jpeg";
        Bitmap bitmapImageAft = BitmapFactory.decodeFile(ImageAfter);

        dateTime = new Date();
        PdfDocument pdfDocument = new PdfDocument();
        Paint paint = new Paint();
        Paint titlePaint = new Paint(); // Untuk Judul Header

        PdfDocument.PageInfo pageInfo2
                = new PdfDocument.PageInfo.Builder(1200, 2010, 2).create();
        PdfDocument.Page page2 = pdfDocument.startPage(pageInfo2);

        Canvas canvas2 = page2.getCanvas();

        canvas2.drawBitmap(PJBscale,50,75, paint); // Logo PJB
        //canvas2.drawBitmap(IPJBway,905,75, paint); // Logo IPJB
        canvas2.drawBitmap(turbineImg,655, 1300, paint );
        // Garis tepi
        canvas2.drawLine(30, 30, 1170, 30, paint); // Garis Atas
        canvas2.drawLine(30, 30, 30, 1980, paint); // Garis Kiri
        canvas2.drawLine(1170, 30, 1170, 1980, paint); // Garis Kanan
        canvas2.drawLine(30, 1980, 1170, 1980, paint); // Garis Bawah

        //Garis Header
        canvas2.drawLine(30, 220, 1170, 220, paint);
        canvas2.drawLine(30, 230, 1170, 230, paint);
        canvas2.drawLine(265, 30, 265, 220, paint); // Kiri
        canvas2.drawLine(905, 30, 905, 220, paint); // Kanan

        //Bawah tanggal
        canvas2.drawLine(30, 290, 1170, 290, paint);

        // Garis NO Document
        canvas2.drawLine(905, 90, 1170, 90, paint);
        canvas2.drawLine(905, 160, 1170, 160, paint);
        canvas2.drawLine(905, 230, 1170, 230, paint);

        // Judul Header
        titlePaint.setTextAlign(Paint.Align.CENTER);
        titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        titlePaint.setColor(Color.BLACK);
        titlePaint.setTextSize(30);
        canvas2.drawText("FOTO-FOTO PREVENTIVE MAINTENANCE", 1170 / 2, 75, titlePaint);
        canvas2.drawText("PEMELIHARAAN LISTRIK BLOK 1-2", 1170 / 2, 115, titlePaint);
        canvas2.drawText("PT PEMBANGKITAN JAWA-BALI", 1170 / 2, 155, titlePaint);
        canvas2.drawText("UP MUARA TAWAR", 1170 / 2, 195, titlePaint);

        titlePaint.setTextSize(18f);
        titlePaint.setTextAlign(Paint.Align.LEFT);
        canvas2.drawText("Nomor Dokumen : FMT-17.2.1", 912,65, titlePaint);
        canvas2.drawText("Revisi : 00", 912,135, titlePaint);
        canvas2.drawText("Tanggal Terbit : 20-08-2013", 912,195, titlePaint);

        // Nama Preventive GT dan Tanggal
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setColor(Color.BLACK);
        paint.setTextSize(30f);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setColor(Color.BLACK);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        paint.setTextSize(30);
        //canvas2.drawText(namaGTdialog.getText().toString(), 1170/2, 270, paint); // Nama GT
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        paint.setColor(Color.BLACK);
        paint.setTextSize(30);
        dateFormat = new SimpleDateFormat("dd/MM/yy");
        canvas2.drawText(dateFormat.format(dateTime), 50, 270, paint);
        dateFormat = new SimpleDateFormat("HH:mm:ss");
        canvas2.drawText(dateFormat.format(dateTime), 1170 - 145, 270, paint);

        //Garis Footer
        canvas2.drawLine(30, 1800, 1170, 1800, paint);
        canvas2.drawLine(390, 1800, 390, 1980, paint);
        canvas2.drawLine(780, 1800, 780, 1980, paint);

        //Content Page 2

        paint.setTextAlign(Paint.Align.CENTER);
        paint.setColor(Color.BLACK);
        paint.setTextSize(35f);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas2.drawText("Information", 1170/2, 350, paint);

        canvas2.drawText("Before", 250, 400, paint);
        canvas2.drawText("After", 950, 400, paint);
        canvas2.drawText("Proses", 1170/2, 1050, paint);

        Matrix matrix1 = new Matrix();
        Matrix matrix2 = new Matrix();
        Matrix matrix3 = new Matrix();
        matrix1.setRotate(90,10,450);
        matrix2.setRotate(90,-150,950);
        matrix3.setRotate(90,355,800); //After

        //Image Before, Process, After
        canvas2.drawBitmap(Bitmap.createScaledBitmap(bitmapImageBfr,640,420,false), matrix1, paint);
        canvas2.drawBitmap(Bitmap.createScaledBitmap(bitmapImagePro,640,420,false), matrix2, paint);
        canvas2.drawBitmap(Bitmap.createScaledBitmap(bitmapImageAft,640,420,false), matrix3, paint);
        //canvas.drawBitmap(bitmapImagePro.createScaledBitmap(bitmapImageSPV,250,200,false),800,1980-180, paint);
        //canvas.drawBitmap(bitmapImageAft.createScaledBitmap(bitmapImageOPS,250,200,false),400,1980-180,paint );
        dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        pdfDocument.finishPage(page2); // Page 2

        File file = new File(Environment.getExternalStorageDirectory(), "/"+namaPDF.getText().toString()+" "+dateFormat.format(dateTime)+".pdf");
        try {
            pdfDocument.writeTo(new FileOutputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }

        pdfDocument.close();
        Toast.makeText(CameraActivity.this, "PDF has Created", Toast.LENGTH_LONG).show();

        dateTime = new Date();
    }
}
