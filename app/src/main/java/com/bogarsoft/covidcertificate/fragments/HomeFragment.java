package com.bogarsoft.covidcertificate.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.agrawalsuneet.dotsloader.loaders.TrailingCircularDotsLoader;
import com.bogarsoft.covidcertificate.R;
import com.bogarsoft.covidcertificate.models.Country;
import com.bogarsoft.covidcertificate.utils.Constants;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;

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

    MaterialAutoCompleteTextView countries;
    ArrayAdapter<Country> countryArrayAdapter;

    TextView totalcases, totalrecoveries, totaldeaths;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TrailingCircularDotsLoader progressbar;
    OnChangeCountryChange onChangeCountryChange;

    public void setOnChangeCountryChange(OnChangeCountryChange onChangeCountryChange) {
        this.onChangeCountryChange = onChangeCountryChange;
    }

    public interface OnChangeCountryChange {
        void OnChange(Country country);
    }

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
        totalcases = view.findViewById(R.id.totalcases);
        totaldeaths = view.findViewById(R.id.totaldeaths);
        totalrecoveries = view.findViewById(R.id.totalrecoveried);
        progressbar = view.findViewById(R.id.covidcasesprogess);
        countries = view.findViewById(R.id.countries);
        countries.setText("India");
        countryArrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line, Constants.countryList);
        countryArrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        countries.setAdapter(countryArrayAdapter);

        countries.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (onChangeCountryChange!=null){
                    onChangeCountryChange.OnChange(Constants.countryList.get(position));
                }
            }
        });
        return view;
    }

    public void setValues(String totalcasesdata, String totaldeathsdata, String totalrecoveriesdata) {
        totalcases.setText(totalcasesdata);
        totaldeaths.setText(totaldeathsdata);
        totalrecoveries.setText(totalrecoveriesdata);
        countryArrayAdapter.notifyDataSetChanged();
    }


}