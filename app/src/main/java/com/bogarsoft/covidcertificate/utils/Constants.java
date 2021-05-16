package com.bogarsoft.covidcertificate.utils;

import com.bogarsoft.covidcertificate.models.Country;
import com.bogarsoft.covidcertificate.models.State;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Constants {

    public static String LINK = "https://bogarsoft.com";
    public static String GET_ALL_COUNTRIES = "https://restcountries.eu/rest/v2/all";
    public static String GET_COUNTRY_STATE = "https://disease.sh/v3/covid-19/countries/";
    public static String GET_INDIAN_STATS = "https://api.covid19india.org/state_district_wise.json";
    public static String NEWS = "https://saurav.tech/NewsAPI/top-headlines/category/health/in.json";
    public static String GET_SETU_STATES = "https://cdn-api.co-vin.in/api/v2/admin/location/states";
    public static String GET_SETU_DISTRICTS = "https://cdn-api.co-vin.in/api/v2/admin/location/districts";
    public static String GET_HELPLINE = LINK+"/gethelpline";
    public static String GET_APP_UPDATE = LINK+"/getappupdate";
    public static String FIND_BY_DISTRICT = "https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/findByDistrict?";
    public static String FIND_BY_PINCODE = "https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/findByPin?";
    public static String GET_BEDS = "https://api.rootnet.in/covid19-in/hospitals/beds";
    public static String GENERATE_OTP = "https://cdn-api.co-vin.in/api/v2/auth/public/generateOTP";
    public static String CONFIRM_OTP = "https://cdn-api.co-vin.in/api/v2/auth/public/confirmOTP";
    public static String DOWNLOAD_CERTIFICATE = "https://cdn-api.co-vin.in/api/v2/registration/certificate/public/download";

    public static List<Country> countryList = new ArrayList<>();
    public static List<State> stateList = new ArrayList<>();


    public static String DT = "MMM dd yyyy hh:mm 1.webp";
    public static String D = "MMM dd yyyy";
    public static String T = "hh:mm 1.webp";
    public static String disT = "h 1.webp";
    public static String RUPEE = "₹";

    public static String getDate(long milliSeconds, String dateFormat)
    {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

}
