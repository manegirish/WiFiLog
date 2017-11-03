package com.example.girishm.wifilog;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by GirishM on 25-10-2017.
 */

class DateTime {

    @SuppressLint("SimpleDateFormat")
    static String getCurrentTimestamp() {
        SimpleDateFormat formatFrom = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
        Date date = new Date(System.currentTimeMillis());
        return formatFrom.format(date);
    }
}
