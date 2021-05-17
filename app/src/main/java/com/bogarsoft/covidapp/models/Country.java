package com.bogarsoft.covidapp.models;

public class Country {
    String name;
    String iso3;

    public Country(String name, String iso3) {
        this.name = name;
        this.iso3 = iso3;
    }

    public Country(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIso3() {
        return iso3;
    }

    public void setIso3(String iso3) {
        this.iso3 = iso3;
    }

    @Override
    public String toString() {
        return name;
    }
}
