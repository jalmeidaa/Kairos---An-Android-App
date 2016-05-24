package com.jalmeida.data;

import org.json.JSONObject;

/**
 * Created by jalmeida on 5/3/16.
 */
public class Condition implements JSONPopulator {
    private int code;
    private int temp;
    private String text;

    @Override
    public void populate(JSONObject obj) {
        code = obj.optInt("code");
        temp = obj.optInt("temp");
        text = obj.optString("text");
    }

    public int getCode() {
        return code;
    }

    public Condition setCode(int code) {
        this.code = code;
        return this;
    }

    public int getTemp() {
        return temp;
    }

    public Condition setTemp(int temp) {
        this.temp = temp;
        return this;
    }

    public String getText() {
        return text;
    }

    public Condition setText(String text) {
        this.text = text;
        return this;
    }
}
