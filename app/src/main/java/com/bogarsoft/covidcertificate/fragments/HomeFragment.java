package com.bogarsoft.covidcertificate.fragments;

import android.location.Criteria;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.agrawalsuneet.dotsloader.loaders.TrailingCircularDotsLoader;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bogarsoft.covidcertificate.R;
import com.bogarsoft.covidcertificate.models.District;
import com.bogarsoft.covidcertificate.models.State;
import com.bogarsoft.covidcertificate.utils.Constants;
import com.bogarsoft.covidcertificate.utils.StorageUtility;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;

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

    SwipeRefreshLayout swipeRefreshLayout;
    MaterialAutoCompleteTextView states;
    MaterialAutoCompleteTextView districts;
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
    TrailingCircularDotsLoader progressbar;

    StorageUtility storageUtility;
    LocationManager locationManager;
    public double latitude;
    public double longitude;
    private FusedLocationProviderClient fusedLocationClient;
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
        progressbar = view.findViewById(R.id.covidcasesprogess);
        states = view.findViewById(R.id.states);
        districts = view.findViewById(R.id.district);





        states.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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
                districts.setSelection(0);
            }
        });

        districts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

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
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (swipeRefreshLayout.isRefreshing()){
                    getIndianData();
                }
            }
        });





        return view;
    }




    private void getIndianData(){


        districtList.clear();
        Constants.stateList.clear();
        states.setText(storageUtility.getState());
        districts.setText(storageUtility.getDistrict());

        stateArrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line, Constants.stateList);
        stateArrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        states.setAdapter(stateArrayAdapter);

        districtArrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line,districtList);
        districtArrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        districts.setAdapter(districtArrayAdapter);
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

                                    if (!storageUtility.getDistrict().equals("None")){
                                        if (storageUtility.getDistrict().equals("All")){

                                            totalcases.setText(state.getActive());
                                            totaldeaths.setText(state.getDeath());
                                            totalrecoveries.setText(state.getRecovered());
                                            break;
                                        }else {
                                            for (District district : state.getDistrictList()){
                                                if (district.getName().equals(storageUtility.getDistrict())){

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