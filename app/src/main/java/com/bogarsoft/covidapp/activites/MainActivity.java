package com.bogarsoft.covidapp.activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bogarsoft.covidapp.R;
import com.bogarsoft.covidapp.adapters.PageAdapter;
import com.bogarsoft.covidapp.fragments.AboutFragment;
import com.bogarsoft.covidapp.fragments.HomeFragment;
import com.bogarsoft.covidapp.fragments.NewsFragment;
import com.bogarsoft.covidapp.utils.Constants;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    int version_code = 0;
    String selectedCountry = "IND";
    BottomNavigationView bottomNavigationView;
    ViewPager viewPager;
    PageAdapter pageAdapter;
    HomeFragment homeFragment = new HomeFragment();
    NewsFragment newsFragment = new NewsFragment();
    AboutFragment aboutFragment = new AboutFragment();
    MaterialButton share;

    Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            version_code = pInfo.versionCode;
            Log.d(TAG, "onCreate: "+pInfo.versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        List<String> testDeviceIds = Arrays.asList("7D6FA30B926B8CC2F3CC9434DDB21731");
        RequestConfiguration configuration =
                new RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build();
        MobileAds.setRequestConfiguration(configuration);

        bottomNavigationView = findViewById(R.id.bottomnav);
        viewPager = findViewById(R.id.vg);
        pageAdapter = new PageAdapter(getSupportFragmentManager());
        pageAdapter.addFragment(homeFragment,"Home");
        pageAdapter.addFragment(newsFragment,"News");
        pageAdapter.addFragment(aboutFragment,"About");
        share = findViewById(R.id.share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String link = "";

                    link = "https://play.google.com/store/apps/details?id=" + getPackageName();


                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, "Covid App, All covid related data in one app");
                i.putExtra(Intent.EXTRA_TEXT, link);
                startActivity(Intent.createChooser(i, "Share app with friends"));
            }
        });



     /*   AndroidNetworking.download("https://cdn-api.co-vin.in/api/v2/registration/certificate/public/download", getFilesDir().getAbsolutePath(),"cert.pdf")
                .addHeaders("Accept","application/pdf")
                .addHeaders("Authorization","Bearer eyJraWQiOiJUcFhOUEFKZytpQWQ2Z0Jkc1dwYk9US1Z6d3FHUGdHWFZJYzVcL3MxZmp4UT0iLCJhbGciOiJSUzI1NiJ9.eyJjdXN0b206c2VjcmV0S2V5IjoiNzg2NGZkc2Y2NHhmMTZkMXhzNTQiLCJzdWIiOiIwZGJiM2M5ZC0xNGMzLTQ2MjAtOWYwNS1kYjIyNjU2MTQzNzMiLCJjb2duaXRvOmdyb3VwcyI6WyJkZXYtcG9ydGFsLVJlZ2lzdGVyZWRHcm91cCJdLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwiY29nbml0bzpwcmVmZXJyZWRfcm9sZSI6ImFybjphd3M6aWFtOjo3MjY5NDEzMDc4MDc6cm9sZVwvZGV2LXBvcnRhbC1Db2duaXRvUmVnaXN0ZXJlZFJvbGUtMTE0SFRHMElZT0JCTiIsImlzcyI6Imh0dHBzOlwvXC9jb2duaXRvLWlkcC5hcC1zb3V0aC0xLmFtYXpvbmF3cy5jb21cL2FwLXNvdXRoLTFfSmd6ZlRZVHRwIiwicGhvbmVfbnVtYmVyX3ZlcmlmaWVkIjp0cnVlLCJjb2duaXRvOnVzZXJuYW1lIjoiMGRiYjNjOWQtMTRjMy00NjIwLTlmMDUtZGIyMjY1NjE0MzczIiwiY3VzdG9tOkNhbGxiYWNrVVJMIjoiaHR0cHM6XC9cL2JvZ2Fyc29mdC5jb21cL2Fhcm9neWFzZXR1IiwiY3VzdG9tOnRuYyI6InRydWUiLCJjb2duaXRvOnJvbGVzIjpbImFybjphd3M6aWFtOjo3MjY5NDEzMDc4MDc6cm9sZVwvZGV2LXBvcnRhbC1Db2duaXRvUmVnaXN0ZXJlZFJvbGUtMTE0SFRHMElZT0JCTiJdLCJhdWQiOiI3cGg2aXRmZ3A4ZTdtc2V2NXJrM2VvNWs1byIsImV2ZW50X2lkIjoiZjViM2EyYjQtNjcyZi00ZDY5LTg3MDUtOTdhNmQ4YmRiM2Q0IiwiY3VzdG9tOmxvZ28iOiJodHRwczpcL1wvYm9nYXJzb2Z0LmNvbVwvaW1hZ2VzXC9sb2dvLnBuZyIsInRva2VuX3VzZSI6ImlkIiwiYXV0aF90aW1lIjoxNjIwOTg5NTQ0LCJwaG9uZV9udW1iZXIiOiIrOTE3MzU4MzM5OTA3IiwiY3VzdG9tOmNvbXBhbnlOYW1lIjoiQm9nYXJzb2Z0IiwiZXhwIjoxNjIwOTkzMTQ0LCJpYXQiOjE2MjA5ODk1NDQsImVtYWlsIjoiY29udGFjdHVzQGJvZ2Fyc29mdC5jb20ifQ.FApFaRm25Pr_yRr9YS75n_FSX3e0oHbm1fznYgySCaK4p3x7-aROpr0-NAncXN6_R9MRFLC46cks0QKcEbqW2e5C5SfvPx4n_hw1fsNzssnL5FGza8eJhCD-uFEHhXBJ38E9S1ezRNTxClMiCnn-M7krkGYdflmxpMCGG7rYNdovca7QK6QTJ_PAIwaNgMZlTE9m-fecaHq1pvmDzAqdG5HgCqRj_zpWQQhr7N4gJyH5WVyn55cdnsEFrqDfSEkgMk-Wz-LmFtfu67dFuVNaU-SJa8vHW5B3uprt4XGs-yr8iu45Z1WXBjhrJ8BlsbpsYRRl2S7y6wKJ4A63dYcvFw")
                .addQueryParameter("beneficiary_reference_id","1234567890123")
                .build()
                .setDownloadProgressListener(new DownloadProgressListener() {
                    @Override
                    public void onProgress(long bytesDownloaded, long totalBytes) {
                        Log.d(TAG, "onProgress: "+bytesDownloaded);
                    }
                })
                .startDownload(new DownloadListener() {
                    @Override
                    public void onDownloadComplete() {
                        Log.d(TAG, "onDownloadComplete: pdf downloaded");
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d(TAG, "onError: "+anError.getErrorDetail());
                    }
                });*/

        viewPager.setAdapter(pageAdapter);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:{
                        viewPager.setCurrentItem(0);
                        return true;
                    }
                    case R.id.news:{
                        viewPager.setCurrentItem(1);
                        return true;
                    }
                    case R.id.about:{
                        viewPager.setCurrentItem(2);
                        return true;
                    }
                }
                return true;
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position==0){
                    bottomNavigationView.setSelectedItemId(R.id.home);
                }else if (position == 1){
                    bottomNavigationView.setSelectedItemId(R.id.news);
                }else {
                    bottomNavigationView.setSelectedItemId(R.id.about);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        viewPager.setOffscreenPageLimit(3);



        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                checkAppUpdate();
            }
        },5000);
        //getCountries();

    }
/*

    private void getCountries(){
        AndroidNetworking.get(Constants.GET_ALL_COUNTRIES)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {

                        Constants.countryList.clear();
                        //Log.d(TAG, "onResponse: "+response);
                        Country allCountry = new Country("World","All");
                        Constants.countryList.add(allCountry);
                        for (int a = 0;a<response.length();a++){
                            try {
                                JSONObject object = response.getJSONObject(a);
                                Country country = new Country(object.getString("name"),object.getString("alpha3Code"));
                                Constants.countryList.add(country);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }


                        handler.post(refreshCovidData);


                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d(TAG, "onError: "+anError.getErrorDetail());
                    }
                });
    }

    private void getCovidCountryData(String Country) {

        AndroidNetworking.get(Constants.GET_COUNTRY_STATE+Country)
                .addQueryParameter("strict","true")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "onResponse: "+response);

                        try {
                            homeFragment.setValues(response.getString("cases"),response.getString("deaths"),response.getString("recovered"));
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
*/


    Runnable refreshCovidData = new Runnable() {
        @Override
        public void run() {
            //getCovidCountryData(selectedCountry);
            //handler.postDelayed(refreshCovidData,60000);
        }
    };


    private void checkAppUpdate(){
        Log.d(TAG, "checkAppUpdate: called");

        AndroidNetworking.get(Constants.GET_APP_UPDATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "onResponse: "+response);
                        try {
                            JSONObject data = response.getJSONObject("data");
                            String vn = data.getString("versionname");
                            int vc = data.getInt("versioncode");
                            if((vc - version_code)>3 ){


                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                                alertDialog.setCancelable(false);
                                View view = getLayoutInflater().inflate(R.layout.confirmation_dialog,null);
                                MaterialTextView t,m;
                                MaterialButton positivebtn,negativebtn;

                                t = view.findViewById(R.id.title);
                                m = view.findViewById(R.id.message);
                                t.setText("Update Available");
                                m.setText("A new Version "+data.getString("versionname")+" is available now, Your app version is outdated please udpate now");
                                positivebtn = view.findViewById(R.id.postiviebtn);
                                negativebtn = view.findViewById(R.id.negativebtn);
                                negativebtn.setVisibility(View.GONE);
                                positivebtn.setText("Update Now");
                                alertDialog.setView(view);
                                androidx.appcompat.app.AlertDialog dialog = alertDialog.show();
                                dialog.getWindow().setBackgroundDrawableResource(R.drawable.custom_dialog_bg);



                                positivebtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                        try {
                                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
                                        } catch (android.content.ActivityNotFoundException anfe) {
                                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
                                        }
                                    }
                                });





                            }else if(vc > version_code){

                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                                alertDialog.setCancelable(false);
                                View view = getLayoutInflater().inflate(R.layout.confirmation_dialog,null);
                                MaterialTextView t,m;
                                MaterialButton positivebtn,negativebtn;

                                t = view.findViewById(R.id.title);
                                m = view.findViewById(R.id.message);
                                t.setText("Update Available");
                                m.setText("A new Version " + data.getString("versionname") + " is available now, please udpate now");
                                positivebtn = view.findViewById(R.id.postiviebtn);
                                negativebtn = view.findViewById(R.id.negativebtn);
                                positivebtn.setText("Update Now");
                                negativebtn.setText("Close");
                                alertDialog.setView(view);
                                androidx.appcompat.app.AlertDialog dialog = alertDialog.show();
                                dialog.getWindow().setBackgroundDrawableResource(R.drawable.custom_dialog_bg);


                                negativebtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();

                                    }
                                });
                                positivebtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                        try {
                                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
                                        } catch (android.content.ActivityNotFoundException anfe) {
                                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
                                        }
                                    }
                                });


                            }
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




}