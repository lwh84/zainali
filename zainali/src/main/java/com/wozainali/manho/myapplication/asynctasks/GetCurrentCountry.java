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
        try {
            final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            final String simCountry = tm.getSimCountryIso();
            if (simCountry != null && simCountry.length() == 2) { // SIM country code is available
                return simCountry.toLowerCase(Locale.getDefault());
            }
            else if (tm.getPhoneType() != TelephonyManager.PHONE_TYPE_CDMA) { // device is not 3G (would be unreliable)
                String networkCountry = tm.getNetworkCountryIso();
                if (networkCountry != null && networkCountry.length() == 2) { // network country code is available
                    return networkCountry.toLowerCase(Locale.getDefault());
                }
            }
        }
        catch (Exception e) { }
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
