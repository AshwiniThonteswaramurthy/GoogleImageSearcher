package com.android.ashwini.googleimagesearcher.helpers;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkHelper {
    public static Boolean isNetworkAvailable(ConnectivityManager connectivityManager) {
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
}
