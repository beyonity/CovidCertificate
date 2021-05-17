package com.bogarsoft.covidapp.fragments;

import android.content.Intent;
import android.location.Criteria;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bogarsoft.covidapp.R;
import com.bogarsoft.covidapp.activites.BedActivity;
import com.bogarsoft.covidapp.activites.DownloadVaccineCertificateActivity;
import com.bogarsoft.covidapp.activites.HelplineActivity;
import com.bogarsoft.covidapp.activites.VaccineActivity;
import com.bogarsoft.covidapp.models.District;
import com.bogarsoft.covidapp.models.State;
import com.bogarsoft.covidapp.utils.Constants;
import com.bogarsoft.covidapp.utils.StorageUtility;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.material.card.MaterialCardView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final String TAG = "HomeFragment";

    MaterialCardView materialCardView,bedcard,downloadcert,helpline;
    SwipeRefreshLayout swipeRefreshLayout;
    Spinner states;
    Spinner districts;
    List<District> districtList = new ArrayList<>();
    ArrayAdapter<State> stateArrayAdapter;
    ArrayAdapter<District> districtArrayAdapter;
    State selectedstate;
    TextView totalcases, totalrecoveries, totaldeaths;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public Criteria criteria;
    public String bestProvider;
    private FrameLayout adContainerView;

    StorageUtility storageUtility;
    LocationManager locationManager;
    public double latitude;
    public double longitude;
    private AdView adView;
    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        swipeRefreshLayout = view.findViewById(R.id.swiperefresh);
        storageUtility = new StorageUtility(getContext());

        totalcases = view.findViewById(R.id.totalcases);
        totaldeaths = view.findViewById(R.id.totaldeaths);
        totalrecoveries = view.findViewById(R.id.totalrecoveried);
        states = view.findViewById(R.id.states);
        districts = view.findViewById(R.id.district);

       materialCardView = view.findViewById(R.id.vaccineslot);
       bedcard = view.findViewById(R.id.bedcard);
       downloadcert = view.findViewById(R.id.downloadcard);
       helpline = view.findViewById(R.id.helpline);

       helpline.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(getActivity(), HelplineActivity.class);
               startActivity(intent);
           }
       });

       downloadcert.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(getActivity(), DownloadVaccineCertificateActivity.class);
               startActivity(intent);
           }
       });
       bedcard.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(getActivity(), BedActivity.class);
               startActivity(intent);
           }
       });
        materialCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), VaccineActivity.class);
                startActivity(intent);
            }
        });
        stateArrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, Constants.stateList);
        stateArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        states.setAdapter(stateArrayAdapter);

        districtArrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item,districtList);
        districtArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        districts.setAdapter(districtArrayAdapter);


        states.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                State state = Constants.stateList.get(position);
                storageUtility.setState(state.getName());
                selectedstate = state;
                if (selectedstate!=null){
                    totalcases.setText(selectedstate.getActive());
                    totaldeaths.setText(selectedstate.getDeath());
                    totalrecoveries.setText(selectedstate.getRecovered());
                }

                districtList.clear();
                districtList.addAll(state.getDistrictList());
                districtArrayAdapter.notifyDataSetChanged();
                boolean exist = false;
                for (District district : districtList){
                    if (district.getName().equals(storageUtility.getDistrict())){
                        districts.setSelection(districtList.indexOf(district));
                        exist = true;
                        break;
                    }
                }

                if (!exist){
                    districts.setSelection(0);
                    //getIndianData();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

      districts.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
          @Override
          public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

              District dis = districtList.get(position);
              storageUtility.setDistrict(dis.getName());
              if (!dis.getName().equals("All")){
                  totalcases.setText(dis.getActive());
                  totaldeaths.setText(dis.getDeath());
                  totalrecoveries.setText(dis.getRecovered());
              }else {
                  if (selectedstate!=null){
                      totalcases.setText(selectedstate.getActive());
                      totaldeaths.setText(selectedstate.getDeath());
                      totalrecoveries.setText(selectedstate.getRecovered());
                  }
              }

          }

          @Override
          public void onNothingSelected(AdapterView<?> parent) {

          }
      });



        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (swipeRefreshLayout.isRefreshing()){
                    getIndianData();
                }
            }
        });

        adContainerView = view.findViewById(R.id.ad_view_container);
        adContainerView.post(new Runnable() {
            @Override
            public void run() {
                loadBanner();
            }
        });


        return view;
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
        adView = new AdView(getActivity());
        adView.setAdUnitId("ca-app-pub-8669188519734324/8103166121");
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
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float density = outMetrics.density;

        float adWidthPixels = adContainerView.getWidth();

        // If the ad hasn't been laid out, default to the full screen width.
        if (adWidthPixels == 0) {
            adWidthPixels = outMetrics.widthPixels;
        }

        int adWidth = (int) (adWidthPixels / density);
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(getActivity(), adWidth);
    }

    private void getIndianData(){


        districtList.clear();
        Constants.stateList.clear();



        AndroidNetworking.get(Constants.GET_INDIAN_STATS)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        swipeRefreshLayout.setRefreshing(false);
                        Log.d(TAG, "onResponse: "+response);
                        Iterator<?> keys = response.keys();
                        while(keys.hasNext() ) {


                            String key = (String)keys.next();
                            if (!key.equals("State Unassigned")){
                                State state = new State();
                                state.setName(key);
                                District Alldistrict = new District();
                                Alldistrict.setName("All");
                                state.getDistrictList().add(Alldistrict);
                                //Log.d(TAG, "onResponse: "+key);

                                try {
                                    if ( response.get(key) instanceof JSONObject ) {
                                        //Log.d(TAG, "onResponse: inside if");
                                        JSONObject districtdata = response.getJSONObject(key).getJSONObject("districtData");
                                        Iterator<?> distKeys = districtdata.keys();
                                        int active = 0;
                                        int recovered = 0;
                                        int deceased = 0;
                                        int confirmed = 0;
                                        while (distKeys.hasNext()){

                                            String distkey = (String) distKeys.next();
                                           // Log.d(TAG, "onResponse: "+distkey);
                                            District district = new District();
                                            district.setName(distkey);
                                            district.setActive(districtdata.getJSONObject(distkey).getString("active"));
                                            district.setDeath(districtdata.getJSONObject(distkey).getString("deceased"));
                                            district.setRecovered(districtdata.getJSONObject(distkey).getString("recovered"));
                                            district.setConfirmed(districtdata.getJSONObject(distkey).getString("confirmed"));
                                            active = active + Integer.parseInt(district.getActive());
                                            recovered = recovered + Integer.parseInt(district.getRecovered());
                                            deceased = deceased + Integer.parseInt(district.getDeath());
                                            confirmed = confirmed + Integer.parseInt(district.getConfirmed());
                                            state.getDistrictList().add(district);
                                        }

                                        state.setActive(String.valueOf(active));
                                        state.setRecovered(String.valueOf(recovered));
                                        state.setDeath(String.valueOf(deceased));
                                        state.setConfirmed(String.valueOf(confirmed));

                                        Constants.stateList.add(state);

                                        //  Log.d("res1",xx.getString("something"));
                                        //Log.d("res2",xx.getString("something2"));
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                        }

                       // Log.d(TAG, "onResponse: "+Constants.stateList.size());


                        if (!storageUtility.getState().equals("None")){
                            for (State state : Constants.stateList){
                                if (state.getName().equals(storageUtility.getState())){
                                    states.setSelection(Constants.stateList.indexOf(state));
                                    if (!storageUtility.getDistrict().equals("None")){
                                        if (storageUtility.getDistrict().equals("All")){
                                            districts.setSelection(0);
                                            totalcases.setText(state.getActive());
                                            totaldeaths.setText(state.getDeath());
                                            totalrecoveries.setText(state.getRecovered());
                                            break;
                                        }else {
                                            for (District district : state.getDistrictList()){
                                                if (district.getName().equals(storageUtility.getDistrict())){
                                                    districts.setSelection(districtList.indexOf(state));
                                                    totalcases.setText(district.getActive());
                                                    totaldeaths.setText(district.getDeath());
                                                    totalrecoveries.setText(district.getRecovered());
                                                    break;
                                                }
                                            }
                                        }
                                    }

                                    break;

                                }
                            }
                        }
                        stateArrayAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(ANError anError) {
                        swipeRefreshLayout.setRefreshing(false);
                        Log.d(TAG, "onError: "+anError.getErrorDetail());
                        Toast.makeText(getContext(), anError.getErrorDetail(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    @Override
    public void onResume() {
        super.onResume();
        getIndianData();
    }
}