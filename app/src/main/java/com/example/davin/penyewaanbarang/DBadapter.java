package com.example.davin.penyewaanbarang;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by davin on 09/11/15.
 */
public class DBadapter {
    private static final String DB_NAME = "uts";
    private static final String TABLE_NAME = "d_pembooking";
    private static final String COL_ID = "id";
    private static final String COL_NAME = "nama";
    private static final String COL_KET = "keterangan";
    private static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
    private SQLiteDatabase sqLiteDatabase = null;

    public DBAdapter(Context context) { super(context, DB_NAME, null, 1); }

    @Override
    public void onCreate(SQLiteDatabase db) { createTable(db); }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE);
    }

    public void createTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + COL_NAME + " TEXT,"
                + COL_KET + " TEXT);");
    }

    public void save(Pembooking pembooking) {
        sqLiteDatabase = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_NAME, pembooking.getNama());
        contentValues.put(COL_KET, pembooking.getKeterangan());

        sqLiteDatabase.insertWithOnConflict(TABLE_NAME, null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);

        sqLiteDatabase.close();
    }

    public void updatebooking(Pembooking pembooking) {
        sqLiteDatabase = getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(COL_NAME, pembooking.getNama());
        cv.put(COL_KET, pembooking.getKeterangan());
        String whereClause = COL_ID + "==?";
        String whereArgs[] = new String[]{pembooking.getId()};
        sqLiteDatabase.update(TABLE_NAME, cv, whereClause, whereArgs);
        sqLiteDatabase.close();

    }

    public void openDB(){
        if (sqLiteDatabase == null){
            sqLiteDatabase = getWritableDatabase();
        }
    }

    public void closeDB(){
        if(sqLiteDatabase != null){
            if (sqLiteDatabase.isOpen()){
                sqLiteDatabase.close();
            }
        }
    }

    public void deleteAll() {
        sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.delete(TABLE_NAME, null, null);
        sqLiteDatabase.close();
    }

    public void delete(Pembooking pembooking) {
        sqLiteDatabase = getWritableDatabase();
        String whereClause = COL_ID + "==?";
        String whereArgs[] = new String[]{String.valueOf(pembooking.getId())};
        sqLiteDatabase.delete(TABLE_NAME, whereClause, whereArgs);
        sqLiteDatabase.close();
    }

    public List<Pembooking> getAllData() {
        sqLiteDatabase = getWritableDatabase();

        Cursor cursor = this.sqLiteDatabase.query(TABLE_NAME, new String[]{
                COL_ID, COL_NAME, COL_KET}, null, null, null, null, null);
        List<Pembooking> absensis = new ArrayList<Pembooking>();

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Pembooking pembooking = new Pembooking();
                pembooking.setId(cursor.getString(cursor.getColumnIndex(COL_ID)));
                pembooking.setNama(cursor.getString(cursor.getColumnIndex(COL_NAME)));
                pembooking.setKeterangan(cursor.getString(cursor.getColumnIndex(COL_KET)));
                absensis.add(pembooking);
            }
            sqLiteDatabase.close();
            return absensis;
        } else {
            sqLiteDatabase.close();
            return new ArrayList<Pembooking>();

        }

    }
}
