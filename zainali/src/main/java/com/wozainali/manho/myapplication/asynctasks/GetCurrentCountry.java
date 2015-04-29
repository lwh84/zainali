package com.wozainali.manho.myapplication.asynctasks;

import android.content.Context;
import android.os.AsyncTask;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.wozainali.manho.myapplication.kml.Placemarks;

import java.util.Locale;

public class GetCurrentCountry extends AsyncTask<Void, Void, String> {

    Context context;

    public GetCurrentCountry(Context context) {
        this.context = context;
    }

    public String getUserCountry() {
//
        return null;
    }


    @Override
    protected String doInBackground(Void... params) {
        return getUserCountry();
    }

    @Override
    protected void onPostExecute(String country) {
        Log.i(this.getClass().toString(), "" + country);
        context = null;
    }
}
