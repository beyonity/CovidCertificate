package com.bogarsoft.covidcertificate.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bogarsoft.covidcertificate.R;
import com.bogarsoft.covidcertificate.adapters.NewsAdapter;
import com.bogarsoft.covidcertificate.models.News;
import com.bogarsoft.covidcertificate.utils.Constants;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;


import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    Button refreshbutton;
    NativeAd nativeAd;
    SwipeRefreshLayout swipeRefreshLayout;
    List<NativeAd> ad = new ArrayList<>();
    int index = 5;
    private static final String TAG = "NewsFragment";
    RecyclerView rv;
    NewsAdapter newsAdapter;
    List<Object> newsList = new ArrayList<>();
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NewsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewsFragment newInstance(String param1, String param2) {
        NewsFragment fragment = new NewsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_news, container, false);
        rv = view.findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        newsAdapter = new NewsAdapter(newsList,getActivity());
        rv.setAdapter(newsAdapter);
        swipeRefreshLayout = view.findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });



        refreshAd(view);

        return view;
    }


    private void refreshAd(View v){

        AdLoader.Builder builder = new AdLoader.Builder(getContext(),"ca-app-pub-3940256099942544/2247696110");
        builder.forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
            @Override
            public void onNativeAdLoaded(@NonNull NativeAd nativeAdhere) {
                if (nativeAd==null){
                    nativeAd = nativeAdhere;
                    CardView cardView = v.findViewById(R.id.ad_container);
                    NativeAdView adView = (NativeAdView) getLayoutInflater().inflate(R.layout.native_ad_layout,null);
                    populateNativeAd(nativeAdhere,adView);
                    cardView.removeAllViews();
                    cardView.addView(adView);

                }
                if (nativeAd!=null){
                    nativeAd = nativeAdhere;
                    CardView cardView = v.findViewById(R.id.ad_container);
                    NativeAdView adView = (NativeAdView) getLayoutInflater().inflate(R.layout.native_ad_layout,null);
                    populateNativeAd(nativeAdhere,adView);
                    cardView.removeAllViews();
                    cardView.addView(adView);

                }
            }
        });

        AdLoader adLoader = builder.withAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                Toast.makeText(getContext(), loadAdError.toString(), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onAdFailedToLoad: "+loadAdError.getResponseInfo());
                super.onAdFailedToLoad(loadAdError);
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
            }

            @Override
            public void onAdClicked() {
                super.onAdClicked();
            }
        }).build();
        adLoader.loadAd(new AdRequest.Builder().build());
    }
    private void populateNativeAd(NativeAd nativeAd,NativeAdView nativeAdView){
        nativeAdView.setHeadlineView(nativeAdView.findViewById(R.id.ad_headline));
        nativeAdView.setAdvertiserView(nativeAdView.findViewById(R.id.ad_advertiser));
        nativeAdView.setBodyView(nativeAdView.findViewById(R.id.ad_body_text));
        nativeAdView.setStarRatingView(nativeAdView.findViewById(R.id.star_rating));
        nativeAdView.setMediaView(nativeAdView.findViewById(R.id.media_view));
        nativeAdView.setCallToActionView(nativeAdView.findViewById(R.id.add_call_to_action));
        nativeAdView.setIconView(nativeAdView.findViewById(R.id.adv_icon));


        nativeAdView.getMediaView().setMediaContent(nativeAd.getMediaContent());
        ((TextView)nativeAdView.getHeadlineView()).setText(nativeAd.getHeadline());

        if (nativeAd.getBody()==null){
            nativeAdView.getBodyView().setVisibility(View.INVISIBLE);
        }else {
            ((TextView)nativeAdView.getBodyView()).setText(nativeAd.getBody());
            nativeAdView.getBodyView().setVisibility(View.VISIBLE);

        }

        if (nativeAd.getAdvertiser() == null){
            nativeAdView.getAdvertiserView().setVisibility(View.INVISIBLE);
        }else {
            ((TextView)nativeAdView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            nativeAdView.getAdvertiserView().setVisibility(View.VISIBLE);
        }


        if (nativeAd.getStarRating() == null){
            nativeAdView.getStarRatingView().setVisibility(View.INVISIBLE);
        }else {
            ((RatingBar)nativeAdView.getStarRatingView()).setRating(nativeAd.getStarRating().floatValue());
            nativeAdView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getIcon() == null){
            nativeAdView.getIconView().setVisibility(View.INVISIBLE);
        }else {
            ((ImageView)nativeAdView.getIconView()).setImageDrawable(nativeAd.getIcon().getDrawable());
            nativeAdView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getCallToAction() == null){
            nativeAdView.getCallToActionView().setVisibility(View.INVISIBLE);
        }else {
            ((Button)nativeAdView.getCallToActionView()).setText(nativeAd.getCallToAction());
            nativeAdView.getCallToActionView().setVisibility(View.VISIBLE);
        }

        nativeAdView.setNativeAd(nativeAd);

    }

    private void getData(){
        AndroidNetworking.get(Constants.NEWS)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        swipeRefreshLayout.setRefreshing(false);
                        Log.d(TAG, "onResponse: "+response);
                        newsList.clear();
                        try {
                            JSONArray articles = response.getJSONArray("articles");
                            for (int a = 0;a<articles.length();a++){
                                JSONObject object = articles.getJSONObject(a);
                                News news = new News();
                                news.setSource(object.getJSONObject("source").getString("name"));
                                news.setDate(object.getString("publishedAt"));
                                news.setTitle(object.getString("title"));
                                news.setDescription("description");
                                news.setImage(object.getString("urlToImage"));
                                news.setUrl(object.getString("url"));
                                newsList.add(news);
                            }

                            newsAdapter.notifyDataSetChanged();

                            if (newsList.size()>0){
                                DateTime dateTime = new DateTime(((News)newsList.get(0)).getDate());
                                Log.d(TAG, "onResponse: "+dateTime.getMillis());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        swipeRefreshLayout.setRefreshing(false);
                        Log.d(TAG, "onError: "+anError.getErrorDetail());
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

    @Override
    public void onDestroy() {
        if (nativeAd!=null){
            nativeAd.destroy();
        }
        super.onDestroy();

    }


    private void insertAdsInMenuItems(List<NativeAd> adlist) {
        if (adlist.size() <= 0) {
            return;
        }
        int offset = 6;


        for (NativeAd ad : adlist) {
            Log.d(TAG, "insertAdsInMenuItems: " + index);
            if (index < newsList.size()) {
                newsList.add(index, ad);
                index = index + offset;
            }

        }
        newsAdapter.notifyDataSetChanged();

    }

}