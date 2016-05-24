package com.jalmeida.data;

import org.json.JSONObject;

/**
 * Created by jalmeida on 5/3/16.
 */
public class Location implements JSONPopulator {
    private String city;
    private String country;
    private String region;

    @Override
    public void populate(JSONObject obj) {

        city = obj.optString("city");
        country = obj.optString("country");
        region = obj.optString("region");

    }

    public String getCity() {
        return city;
    }

    public Location setCity(String city) {
        this.city = city;
        return this;
    }

    public String getCountry() {
        return country;
    }

    public Location setCountry(String country) {
        this.country = country;
        return this;
    }

    public String getRegion() {
        return region;
    }

    public Location setRegion(String region) {
        this.region = region;
        return this;
    }
}
