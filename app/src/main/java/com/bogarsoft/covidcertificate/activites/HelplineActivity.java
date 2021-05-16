package com.bogarsoft.covidcertificate.activites;

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
import com.bogarsoft.covidcertificate.R;
import com.bogarsoft.covidcertificate.adapters.HelplineAdapter;
import com.bogarsoft.covidcertificate.models.Helpline;
import com.bogarsoft.covidcertificate.utils.Constants;
import com.faltenreich.skeletonlayout.Skeleton;
import com.faltenreich.skeletonlayout.SkeletonLayoutUtils;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HelplineActivity extends AppCompatActivity {

    private FrameLayout adContainerView;
    private AdView adView;
    RecyclerView recyclerView;
    HelplineAdapter helplineAdapter;
    List<Helpline> helplineList = new ArrayList<>();
    Skeleton skeleton;
    private static final String TAG = "HelplineActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_helpline_numbers);

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        recyclerView = findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        helplineAdapter = new HelplineAdapter(helplineList,HelplineActivity.this);
        recyclerView.setAdapter(helplineAdapter);

        skeleton = SkeletonLayoutUtils.applySkeleton(recyclerView,R.layout.phonenumber);
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
        AndroidNetworking.get(Constants.GET_HELPLINE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        skeleton.showOriginal();
                        Log.d(TAG, "onResponse: "+response);
                        helplineList.clear();
                        try {
                            JSONArray data = response.getJSONObject("data").getJSONArray("PDFTables.com");
                            for(int a = 0;a<data.length();a++){
                                JSONObject object = data.getJSONObject(a);
                                Helpline helpline = new Helpline();
                                helpline.setPhone(object.getString("phone"));
                                helpline.setState(object.getString("state"));
                                helpline.setRegional(object.getString("regional"));
                                helplineList.add(helpline);
                            }

                            helplineAdapter.notifyDataSetChanged();
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
        adView = new AdView(HelplineActivity.this);
        adView.setAdUnitId("ca-app-pub-8669188519734324/3071235254");
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
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(HelplineActivity.this, adWidth);
    }



    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }
}