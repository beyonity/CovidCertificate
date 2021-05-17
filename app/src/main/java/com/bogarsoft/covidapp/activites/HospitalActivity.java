package com.bogarsoft.covidapp.activites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Spinner;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bogarsoft.covidapp.R;
import com.bogarsoft.covidapp.adapters.HospitalAdapter;

import com.bogarsoft.covidapp.models.Hospital;
import com.bogarsoft.covidapp.utils.Constants;
import com.faltenreich.skeletonlayout.Skeleton;
import com.faltenreich.skeletonlayout.SkeletonLayoutUtils;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class HospitalActivity extends AppCompatActivity {

    HashMap<String,Set<String>> maplist = new HashMap<>();
    List<String> cities = new ArrayList<>();
    List<String> statelist = new ArrayList<>();
    private static final String TAG = "HospitalActivity";
    RecyclerView rv;
    HospitalAdapter hospitalAdapter;
    List<Hospital> hospitalList = new ArrayList<>();
    Skeleton skeleton;
    private FrameLayout adContainerView;
    Spinner states,citys;
    ArrayAdapter stateArrayAdapter,cityArrayAdapter;
    private AdView adView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital);

        ((MaterialButton) findViewById(R.id.back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        states = findViewById(R.id.states);
        citys = findViewById(R.id.districts);


        stateArrayAdapter = new ArrayAdapter<>(HospitalActivity.this, android.R.layout.simple_spinner_dropdown_item,statelist);
        stateArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        states.setAdapter(stateArrayAdapter);

        cityArrayAdapter = new ArrayAdapter<>(HospitalActivity.this, android.R.layout.simple_spinner_dropdown_item,cities);
        cityArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citys.setAdapter(cityArrayAdapter);

        rv = findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        hospitalAdapter = new HospitalAdapter(hospitalList, HospitalActivity.this);
        rv.setAdapter(hospitalAdapter);

        skeleton = SkeletonLayoutUtils.applySkeleton(rv,R.layout.addresslayout);

        adContainerView = findViewById(R.id.ad_view_container);
        adContainerView.post(new Runnable() {
            @Override
            public void run() {
                loadBanner();
            }
        });


        states.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cities.clear();
                cities.addAll(maplist.get(statelist.get(position)));
                cityArrayAdapter.notifyDataSetChanged();
                getValues();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        citys.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getValues();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        getData();

    }


    private void getData(){
        skeleton.showSkeleton();
       /* AndroidNetworking.get(Constants.GET_BEDS)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "onResponse: "+response);
                        bedList.clear();
                        skeleton.showOriginal();
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
                        skeleton.showOriginal();
                        Log.d(TAG, "onError: "+anError.getErrorDetail());
                    }
                });*/


      /*  AndroidNetworking.get(Constants.GET_HOSPITALDETAILS)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        skeleton.showOriginal();
                        hospitalList.clear();
                        Log.d(TAG, "onResponse: "+response);
                        try {
                            JSONArray data = response.getJSONArray("data");
                            for (int a = 0 ;a<data.length();a++){
                                JSONObject object = data.getJSONObject(a);
                                Hospital hospital = new Hospital();
                                hospital.setName(object.getString("Hospital Name"));
                                hospital.setState(object.getString("State"));
                                hospital.setCity(object.getString("City"));
                                hospital.setEmail(object.getString("Email"));
                                hospital.setLatitude(object.getString("LATITUDE"));
                                hospital.setLongitude(object.getString("LONGITUDE"));
                                hospital.setPhone(object.getString("Phone No."));
                                hospital.setPincode(object.getString("Pin Code"));

                                hospital.setAddress(object.getString("Address"));

                                hospitalList.add(hospital);
                            }

                            hospitalAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        skeleton.showOriginal();
                        Log.d(TAG, "onError: "+anError.getErrorDetail());
                    }
                });
*/

        AndroidNetworking.get(Constants.GET_STATES_FROM_HOSPITALS)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        skeleton.showOriginal();
                        maplist.clear();
                        Log.d(TAG, "onResponse: "+response);
                        try {
                            JSONArray data = response.getJSONArray("data");
                            for (int a = 0;a<data.length();a++){
                                JSONObject object = data.getJSONObject(a);

                                if (maplist.containsKey(object.getString("state"))){
                                    maplist.get(object.getString("state")).add(object.getString("city"));
                                }else {
                                    Set<String> cites = new TreeSet<>();
                                    cites.add(object.getString("city"));
                                    maplist.put(object.getString("state"),cites);
                                }

                            }



                            statelist.clear();
                            Set<String> s = maplist.keySet();
                            for (String name : maplist.keySet()){
                                statelist.add(name);
                            }

                            Collections.sort(statelist);

                            cities.clear();
                            cities.addAll(maplist.get(statelist.get(0)));
                            stateArrayAdapter.notifyDataSetChanged();
                            cityArrayAdapter.notifyDataSetChanged();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        skeleton.showOriginal();
                        Log.d(TAG, "onError: "+anError.getErrorDetail());
                    }
                });

    }


    @Override
    public void onPause() {
        if (adView != null) {
            adView.pause();
        }
        super.onPause();
    }


    private void getValues(){
        skeleton.showSkeleton();
        AndroidNetworking.get(Constants.GET_HOSPITALDETAILS)
                .addQueryParameter("state",states.getSelectedItem().toString())
                .addQueryParameter("city",citys.getSelectedItem().toString())
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        skeleton.showOriginal();
                        hospitalList.clear();
                        Log.d(TAG, "onResponse: "+response);
                        try {
                            JSONArray data = response.getJSONArray("data");
                            for (int a = 0 ;a<data.length();a++){
                                JSONObject object = data.getJSONObject(a);
                                Hospital hospital = new Hospital();
                                hospital.setName(object.getString("Hospital Name"));
                                hospital.setState(object.getString("State"));
                                hospital.setCity(object.getString("City"));
                                hospital.setEmail(object.getString("Email"));
                                hospital.setLatitude(object.getString("LATITUDE"));
                                hospital.setLongitude(object.getString("LONGITUDE"));
                                hospital.setPhone(object.getString("Phone No."));
                                hospital.setPincode(object.getString("Pin Code"));

                                hospital.setAddress(object.getString("Address"));

                                hospitalList.add(hospital);
                            }

                            hospitalAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        skeleton.showOriginal();
                        Log.d(TAG, "onError: "+anError.getErrorDetail());
                    }
                });
    }

    @Override
    public void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }


    private void loadBanner() {
        // Create an ad request.
        adView = new AdView(HospitalActivity.this);
        adView.setAdUnitId("ca-app-pub-8669188519734324/8000128309");
        adContainerView.removeAllViews();
        adContainerView.addView(adView);

        AdSize adSize = getAdSize();
        adView.setAdSize(adSize);

        AdRequest adRequest = new AdRequest.Builder().build();

        // Start loading the ad in the background.
        adView.loadAd(adRequest);
    }

    private AdSize getAdSize() {
        // Determine the screen width (less decorations) to use for the ad width.
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float density = outMetrics.density;

        float adWidthPixels = adContainerView.getWidth();

        // If the ad hasn't been laid out, default to the full screen width.
        if (adWidthPixels == 0) {
            adWidthPixels = outMetrics.widthPixels;
        }

        int adWidth = (int) (adWidthPixels / density);
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(HospitalActivity.this, adWidth);
    }


    @Override
    protected void onResume() {
        super.onResume();
        //getData();
    }
}