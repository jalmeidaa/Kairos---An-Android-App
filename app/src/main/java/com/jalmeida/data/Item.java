package com.jalmeida.data;

import org.json.JSONObject;

/**
 * Created by jalmeida on 5/3/16.
 */
public class Item implements JSONPopulator {
    private Condition condition;

    @Override
    public void populate(JSONObject obj) {

        condition = new Condition();
        condition.populate(obj.optJSONObject("condition"));

    }

    public Condition getCondition() {
        return condition;
    }

    public Item setCondition(Condition condition) {
        this.condition = condition;
        return this;
    }
}
