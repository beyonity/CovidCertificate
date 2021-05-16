package com.bogarsoft.covidcertificate.activites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bogarsoft.covidcertificate.R;
import com.bogarsoft.covidcertificate.adapters.VaccineTrackerAdapter;
import com.bogarsoft.covidcertificate.models.District;
import com.bogarsoft.covidcertificate.models.State;
import com.bogarsoft.covidcertificate.models.VaccineTracker;
import com.bogarsoft.covidcertificate.utils.Constants;
import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class VaccineActivity extends AppCompatActivity {

    private static final String TAG = "VaccineACtivity";
    Spinner states,districts;
    ArrayAdapter<State> stateArrayAdapter;
    ArrayAdapter<District> districtArrayAdapter;
    List<State> stateList = new ArrayList<>();
    List<District> districtList = new ArrayList<>();
    EditText date;
    Calendar myCalendar = Calendar.getInstance();
    TextView noslotavailable;
    List<VaccineTracker> vaccineTrackerList = new ArrayList<>();
    VaccineTrackerAdapter vaccineTrackerAdapter;
    RecyclerView rv;

    DatePickerDialog.OnDateSetListener datepicker = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_vaccine_tracker);

        ((MaterialButton) findViewById(R.id.back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        noslotavailable = findViewById(R.id.emptyslot);

        date = findViewById(R.id.date);
        states = findViewById(R.id.states);
        districts = findViewById(R.id.district);

        stateArrayAdapter = new ArrayAdapter<>(VaccineActivity.this, android.R.layout.simple_spinner_dropdown_item,stateList);
        stateArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        states.setAdapter(stateArrayAdapter);

        districtArrayAdapter = new ArrayAdapter<>(VaccineActivity.this, android.R.layout.simple_spinner_dropdown_item,districtList);
        districtArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        districts.setAdapter(districtArrayAdapter);

        states.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getDistricts(stateList.get(position).getState_id());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        districts.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getVaccinesDetails(districtList.get(position).getDistrict_id());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(VaccineActivity.this, datepicker, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(new Date().getTime());
                datePickerDialog.show();
            }
        });

        rv = findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(VaccineActivity.this));
        vaccineTrackerAdapter = new VaccineTrackerAdapter(vaccineTrackerList,VaccineActivity.this);
        rv.setAdapter(vaccineTrackerAdapter);



        updateLabel();
    }


    private void getData(){
        AndroidNetworking.get(Constants.GET_SETU_STATES)
                .addHeaders("accept","application/json")
                .addHeaders("Accept-Language","en_US")
                .addHeaders("User-Agent","Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        stateList.clear();
                        Log.d(TAG, "onResponse: "+response);
                        try {
                            JSONArray states = response.getJSONArray("states");
                            for (int a = 0;a<states.length();a++){
                                JSONObject object = states.getJSONObject(a);
                                State state = new State();
                                state.setName(object.getString("state_name"));
                                state.setState_id(object.getString("state_id"));
                                stateList.add(state);

                            }

                            stateArrayAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d(TAG, "onError: "+anError.getErrorBody());
                    }
                });
    }

    private void getDistricts(String state_id){
        AndroidNetworking.get(Constants.GET_SETU_DISTRICTS+"/"+state_id)
                .addHeaders("accept","application/json")
                .addHeaders("Accept-Language","en_US")
                .addHeaders("User-Agent","Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        districtList.clear();
                        Log.d(TAG, "onResponse: "+response);
                        try {
                            JSONArray districts = response.getJSONArray("districts");
                            for (int a = 0;a<districts.length();a++){
                                JSONObject object = districts.getJSONObject(a);
                                District district = new District();
                                district.setName(object.getString("district_name"));
                                district.setDistrict_id(object.getString("district_id"));
                                district.setState_id(state_id);

                                districtList.add(district);

                            }

                            districtArrayAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d(TAG, "onError: "+anError.getErrorBody());
                    }
                });
    }

    private void updateLabel() {

        date.setText(Constants.getDate(myCalendar.getTimeInMillis(), Constants.D));
    }

    private void getVaccinesDetails(String district_id){

        String link = Constants.FIND_BY_DISTRICT+"district_id="+district_id+"&date="+Constants.getDate(myCalendar.getTimeInMillis(),"dd MM yyyy");
        Log.d(TAG, "getVaccinesDetails: "+link);
        AndroidNetworking.get(link)
                .addHeaders("accept","application/json")
                .addHeaders("Accept-Language","en_US")
                .addHeaders("User-Agent","Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "onResponse: "+response);
                        vaccineTrackerList.clear();
                        try {
                            JSONArray jsonArray = response.getJSONArray("sessions");
                            for (int a = 0;a<jsonArray.length();a++){
                                JSONObject object = jsonArray.getJSONObject(a);
                                VaccineTracker vaccineTracker = new VaccineTracker();
                                vaccineTracker.setCentername(object.getString("name"));
                                vaccineTracker.setAddress(object.getString("address"));
                                vaccineTracker.setPincode(object.getString("pincode"));
                                vaccineTracker.setOtime(object.getString("from"));
                                vaccineTracker.setCtime(object.getString("to"));
                                vaccineTracker.setDate(object.getString("date"));
                                vaccineTracker.setAvacapacity(object.getString("available_capacity"));
                                vaccineTracker.setMinage(object.getString("min_age_limit"));
                                vaccineTracker.setVaccinename(object.getString("vaccine"));
                                //vaccineTracker.setSlots(object.getJSONArray("slots").toString());
                                StringBuilder builder = new StringBuilder();
                                JSONArray slots = object.getJSONArray("slots");
                                for (int b = 0;b<slots.length();b++){

                                    if (builder.length()==0){
                                        builder.append(slots.getString(b));
                                    }else {
                                        builder.append(" , ");
                                        builder.append(slots.getString(b));
                                    }
                                }
                                vaccineTracker.setSlots(builder.toString());
                                vaccineTrackerList.add(vaccineTracker);

                            }

                            if (vaccineTrackerList.size()>0){
                                noslotavailable.setVisibility(View.GONE);
                            }else {
                                noslotavailable.setVisibility(View.VISIBLE);
                            }
                            vaccineTrackerAdapter.notifyDataSetChanged();
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