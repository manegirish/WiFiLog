package com.example.girishm.wifilog;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


class MainDatabase extends SQLiteOpenHelper {

    private static final String TAG = MainDatabase.class.getSimpleName();
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "wifi_logs";
    private SQLiteDatabase database;

    MainDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DatabaseQuery_.CREATE_TABLE_LOG);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private void open() {
        database = this.getReadableDatabase();
    }

    public void close() {
        if (database != null && database.isOpen())
            database.close();
    }

    void insertLog(String wifi_id, int wifi_status, int internet_status) {

        open();
        ContentValues logValues = new ContentValues();
        logValues.put("wifi_id", wifi_id);
        logValues.put("wifi_status", wifi_status);
        logValues.put("internet_status", internet_status);
        logValues.put("timestamp", DateTime.getCurrentTimestamp());
        database.insert("logs", null, logValues);
    }
}
