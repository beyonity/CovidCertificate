package com.bogarsoft.covidcertificate.utils;

import com.bogarsoft.covidcertificate.models.Country;

import java.util.ArrayList;
import java.util.List;

public class Constants {

    public static String GET_ALL_COUNTRIES = "https://restcountries.eu/rest/v2/all";
    public static String GET_COUNTRY_STATE = "https://disease.sh/v3/covid-19/countries/";

    public static List<Country> countryList = new ArrayList<>();
}
