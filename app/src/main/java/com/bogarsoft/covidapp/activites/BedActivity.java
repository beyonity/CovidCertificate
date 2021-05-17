package com.bogarsoft.covidapp.activites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.FrameLayout;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bogarsoft.covidapp.R;
import com.bogarsoft.covidapp.adapters.BedAdapter;
import com.bogarsoft.covidapp.models.Bed;
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
import java.util.List;

public class BedActivity extends AppCompatActivity {

    private static final String TAG = "BedActivity";
    RecyclerView rv;
    BedAdapter bedAdapter;
    List<Bed> bedList = new ArrayList<>();
    Skeleton skeleton;
    private FrameLayout adContainerView;


    private AdView adView;
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

        skeleton = SkeletonLayoutUtils.applySkeleton(rv,R.layout.bed_list);

        adContainerView = findViewById(R.id.ad_view_container);
        adContainerView.post(new Runnable() {
            @Override
            public void run() {
                loadBanner();
            }
        });

    }


    private void getData(){
        skeleton.showSkeleton();
        AndroidNetworking.get(Constants.GET_BEDS)
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
                });


    }


    @Override
    public void onPause() {
        if (adView != null) {
            adView.pause();
        }
        super.onPause();
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
        adView = new AdView(BedActivity.this);
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
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(BedActivity.this, adWidth);
    }


    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }
}