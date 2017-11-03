package com.example.girishm.wifilog;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void startService(int interval) {
        Intent intent = new Intent(getApplicationContext(), RecordDataService.class);
        intent.putExtra("time", TimeUnit.MINUTES.toMillis(interval));
        startService(intent);
        Toast.makeText(getApplicationContext(), "Started....", Toast.LENGTH_LONG).show();
    }

    private void stopService() {
        Intent intent = new Intent(getApplicationContext(), RecordDataService.class);
        stopService(intent);
        Toast.makeText(getApplicationContext(), "Stopped", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    private int getInterval() {
        EditText intervalBox = (EditText) findViewById(R.id.content_main_interval);
        String minutes = intervalBox.getText().toString().trim();
        if (minutes.length() <= 0) {
            return 5;
        } else {
            return Integer.parseInt(minutes);
        }
    }

    private void exportDB() {
        MainDatabase mainDatabase = new MainDatabase(getApplicationContext());
        File exportDir = new File(Environment.getExternalStorageDirectory(), "");
        if (!exportDir.exists()) {
            exportDir.mkdirs();
        }

        File file = new File(exportDir, "logs_" + System.currentTimeMillis() + ".csv");
        try {
            file.createNewFile();
            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
            SQLiteDatabase db = mainDatabase.getReadableDatabase();
            Cursor curCSV = db.rawQuery("SELECT * FROM logs", null);
            csvWrite.writeNext(curCSV.getColumnNames());
            while (curCSV.moveToNext()) {
                String arrStr[] = {curCSV.getString(0), curCSV.getString(1), curCSV.getString(2)
                        , curCSV.getString(3), curCSV.getString(4)};
                csvWrite.writeNext(arrStr);
            }
            csvWrite.close();
            curCSV.close();
            Toast.makeText(getApplicationContext(), "File exported", Toast.LENGTH_LONG).show();
        } catch (Exception sqlEx) {
            Toast.makeText(getApplicationContext(), ""+sqlEx.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.content_main_set:
                stopService();
                startService(getInterval());
                break;
            case R.id.content_main_start:
                startService(getInterval());
                break;
            case R.id.content_main_stop:
                stopService();
                break;
            case R.id.content_main_export:
                exportDB();
                break;
        }
    }
}
