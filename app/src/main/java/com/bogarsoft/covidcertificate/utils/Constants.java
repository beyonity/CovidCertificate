package com.bogarsoft.covidcertificate.utils;

import com.bogarsoft.covidcertificate.models.Country;
import com.bogarsoft.covidcertificate.models.State;

import java.util.ArrayList;
import java.util.List;

public class Constants {

    public static String GET_ALL_COUNTRIES = "https://restcountries.eu/rest/v2/all";
    public static String GET_COUNTRY_STATE = "https://disease.sh/v3/covid-19/countries/";
    public static String GET_INDIAN_STATS = "https://api.covid19india.org/state_district_wise.json";
    public static String NEWS = "https://saurav.tech/NewsAPI/top-headlines/category/health/in.json";

    public static List<Country> countryList = new ArrayList<>();
    public static List<State> stateList = new ArrayList<>();


}
