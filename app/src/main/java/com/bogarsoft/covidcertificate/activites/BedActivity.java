package com.bogarsoft.covidcertificate.activites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bogarsoft.covidcertificate.R;
import com.bogarsoft.covidcertificate.adapters.BedAdapter;
import com.bogarsoft.covidcertificate.models.Bed;
import com.bogarsoft.covidcertificate.utils.Constants;
import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BedActivity extends AppCompatActivity {

    private static final String TAG = "BedActivity";
    RecyclerView rv;
    BedAdapter bedAdapter;
    List<Bed> bedList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bed);

        ((MaterialButton) findViewById(R.id.back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        rv = findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        bedAdapter = new BedAdapter(bedList,BedActivity.this);
        rv.setAdapter(bedAdapter);
    }


    private void getData(){
        AndroidNetworking.get(Constants.GET_BEDS)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "onResponse: "+response);
                        bedList.clear();
                        try {
                            JSONArray data = response.getJSONObject("data").getJSONArray("regional");
                            for(int a = 0;a<data.length();a++){
                                JSONObject object = data.getJSONObject(a);
                                Bed bed = new Bed();
                                bed.setName(object.getString("state"));
                                bed.setRuralhos(object.getString("ruralHospitals"));
                                bed.setRuralbeds(object.getString("ruralBeds"));
                                bed.setUrbanhos(object.getString("urbanHospitals"));
                                bed.setUrbanbeds(object.getString("urbanBeds"));
                                bedList.add(bed);

                            }

                            bedAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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