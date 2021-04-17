package com.herwinlab.apem.sqliteData;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.TextView;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME="Btm_battery.db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        //SQLiteDatabase db = this.getWritableDatabase();

    }

    @Override
    public void onCreate(SQLiteDatabase DB) {
        DB.execSQL("create Table Batterybtm(id INTEGER PRIMARY KEY AUTOINCREMENT, tanggal TEXT, btm TEXT, namagt TEXT, tegangan TEXT, beratjenis TEXT, airbattery TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int oldVersion, int newVersion) {
        DB.execSQL("drop table if exists Batterybtm");
        onCreate(DB);

    }

    //metode untuk tambah data
    public boolean insertData(String tanggal, String btm, String namagt, String tegangan, String beratjenis, String airbattery) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("tanggal",tanggal);
        contentValues.put("btm",btm);
        contentValues.put("namagt",namagt);
        contentValues.put("tegangan",tegangan);
        contentValues.put("beratjenis",beratjenis);
        contentValues.put("airbattery",airbattery);
        long result = DB.insert("Batterybtm", null, contentValues);
        return result != -1;
    }



    //metode untuk mengambil data
    public Cursor getAllData() {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("select * from Batterybtm", null);
        return cursor;
    }



    //metode untuk merubah data
    public boolean updateData(String id, String tanggal, String btm, String namagt, String tegangan, String beratjenis, String airbattery) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id",id);
        contentValues.put("tanggal",tanggal);
        contentValues.put("btm",btm);
        contentValues.put("namagt",namagt);
        contentValues.put("tegangan",tegangan);
        contentValues.put("beratjenis",beratjenis);
        contentValues.put("airbattery",airbattery);
        DB.update("Batterybtm",contentValues,"id = ?",new String[] {id});
        return true;
    }



    //metode untuk menghapus data
    public int deleteData (String id) {
        SQLiteDatabase DB = this.getWritableDatabase();
        return DB.delete("Batterybtm", "id = ?", new String[] {id});
    }
}
