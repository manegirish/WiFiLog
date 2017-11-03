package com.example.girishm.wifilog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.text.TextUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

/**
 * Created by GirishM on 25-10-2017.
 */

class CheckWiFi {

    static void recordWifiStatus(Context context) throws ExecutionException, InterruptedException {
        MainDatabase mainDatabase = new MainDatabase(context);
        mainDatabase.insertLog(getWifiId(context), isConnected(context), new InternetTask().execute().get());
        mainDatabase.close();
    }

    private static int isConnected(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        boolean isConnect = mWifi.isConnected();
        getWifiId(context);
        if (isConnect) {
            return 1;
        } else {
            return 0;
        }
    }

    private static int isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        boolean isConnect = activeNetworkInfo != null && activeNetworkInfo.isConnected();
        if (isConnect) {
            return 1;
        } else {
            return 0;
        }
    }

    private static class InternetTask extends AsyncTask<Void, Void, Integer> {
        @Override
        protected Integer doInBackground(Void... params) {
            int statusCode = -1;
            try {
                URL url = new URL("http://www.google.com/humans.txt");
                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                statusCode = http.getResponseCode();
                http.disconnect();
            } catch (MalformedURLException ex) {
                return 0;
            } catch (IOException ex) {
                return 0;
            }
            if (statusCode == HttpURLConnection.HTTP_OK) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    @SuppressLint("WifiManagerPotentialLeak")
    private static String getWifiId(Context context) {
        String ss_id = "00";
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (networkInfo.isConnected()) {
            final WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            final WifiInfo connectionInfo = wifiManager.getConnectionInfo();
            if (connectionInfo != null && !TextUtils.isEmpty(connectionInfo.getSSID())) {
                ss_id = connectionInfo.getSSID();
            }
        }
        return ss_id;
    }
}
