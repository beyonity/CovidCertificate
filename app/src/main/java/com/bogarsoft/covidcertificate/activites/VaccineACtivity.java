package com.bogarsoft.covidcertificate.activites;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bogarsoft.covidcertificate.R;

import org.json.JSONObject;

public class VaccineACtivity extends AppCompatActivity {

    private static final String TAG = "VaccineACtivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vaccine);
    }


    private void getData(){
        AndroidNetworking.get("https://covid-19india-api.herokuapp.com/v2.0/helpline_numbers")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "onResponse: "+response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d(TAG, "onError: "+anError.getErrorDetail());
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }
}