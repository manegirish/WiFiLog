package com.example.girishm.wifilog;

/**
 * Created by GirishM on 25-10-2017.
 */

class DatabaseQuery_ {

    static final String CREATE_TABLE_LOG = "CREATE TABLE `logs` (" +
            " `_id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
            " `wifi_id` TEXT NOT NULL," +
            " `wifi_status` INTEGER NOT NULL, " +
            " `internet_status` INTEGER NOT NULL, " +
            " `timestamp` TEXT NOT NULL " +
            ");";
}
