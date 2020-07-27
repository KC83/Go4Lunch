package com.kcapp.go4lunch.api.services;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class InternetManagerImpl implements InternetManager {

    private Context mContext;

    public InternetManagerImpl(Context context) {
        mContext = context;
    }

    /**
     * Check if the phone is connected
     * @return true if the phone is connected
     */
    @Override
    public Boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}
