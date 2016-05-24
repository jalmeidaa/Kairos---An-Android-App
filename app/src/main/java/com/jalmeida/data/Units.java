package com.jalmeida.data;

import org.json.JSONObject;

/**
 * Created by jalmeida on 4/26/16.
 */
public class Units implements JSONPopulator {
    private String temperature;

    @Override
    public void populate(JSONObject obj) {

        temperature = obj.optString("temperature");
    }

    public String getTemperature() {
        return temperature;
    }

    public Units setTemperature(String temperature) {
        this.temperature = temperature;
        return this;
    }
}
