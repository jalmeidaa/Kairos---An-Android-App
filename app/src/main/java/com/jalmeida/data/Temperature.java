package com.jalmeida.data;

import org.json.JSONObject;

/**
 * Created by jalmeida on 4/26/16.
 */
public class Temperature implements JSONPopulator {
    private Units units;
    private String title;
    private Item item;
    private Location location;

    @Override
    public void populate(JSONObject obj) {

        units = new Units();
        units.populate(obj.optJSONObject("units"));

        title = obj.optString("title");

        item = new Item();
        item.populate(obj.optJSONObject("item"));

        location = new Location();
        location.populate(obj.optJSONObject("location"));
    }


    public Units getUnits() {
        return units;
    }

    public Temperature setUnits(Units units) {
        this.units = units;
        return this;
    }

    public Location getLocation() {
        return location;
    }

    public Temperature setLocation(Location location) {
        this.location = location;
        return this;
    }

    public Item getItem() {
        return item;
    }

    public Temperature setItem(Item item) {
        this.item = item;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Temperature setTitle(String title) {
        this.title = title;
        return this;
    }

    public boolean equals(Object obj){
        if (obj instanceof Temperature) {
            Temperature pp = (Temperature) obj;
            return (pp.title.equals(this.title));
        } else {
            return false;
        }
    }
}
