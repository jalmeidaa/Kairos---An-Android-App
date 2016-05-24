package com.jalmeida.main;

import android.net.Uri;
import android.os.AsyncTask;

import com.jalmeida.data.Temperature;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by jalmeida on 5/15/16.
 */
public class WeatherActivity {
    private WeatherService mainCallback;
    private Exception error;

    public WeatherActivity(WeatherService mainCallback) {
        this.mainCallback = mainCallback;
    }

    public void getWeather(String location) {
        new AsyncTask<String, Void, Temperature>() {

            @Override
            protected Temperature doInBackground(String... locations) {

                String location = locations[0];

                Temperature temperature = new Temperature();

                String YQL = String.format("select * from weather.forecast where woeid in (select woeid from geo.places(1) where text=\"%s\")", location);

                String endpoint = String.format("https://query.yahooapis.com/v1/public/yql?q=%s&format=json", Uri.encode(YQL));

                try {
                    URL url = new URL(endpoint);

                    URLConnection connection = url.openConnection();
                    connection.setUseCaches(false);

                    InputStream inputStream = connection.getInputStream();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    JSONObject data = new JSONObject(result.toString());

                    JSONObject queryResults = data.optJSONObject("query");

                    int count = queryResults.optInt("count");

                    if (count == 0) {
                        error = new LocationWeatherException("No weather information found for " + location);
                        return null;
                    }

                    JSONObject TemperatureJSON = queryResults.optJSONObject("results").optJSONObject("channel");
                    temperature.populate(TemperatureJSON);

                    return temperature;

                } catch (Exception e) {
                    error = e;
                }

                return null;
            }

            @Override
            protected void onPostExecute(Temperature temperature) {

                if (temperature == null && error != null) {
                    mainCallback.serviceFailure(error);
                } else {
                    mainCallback.serviceSuccess(temperature);
                }

            }

        }.execute(location);
    }

    private class LocationWeatherException extends Exception {
        public LocationWeatherException(String s) {
            super(s);
        }
    }

}