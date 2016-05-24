package com.jalmeida.main;

import com.jalmeida.data.Temperature;

/**
 * Created by jalmeida on 5/15/16.
 */
public interface WeatherService {

    void serviceSuccess(Temperature temperature);

    void serviceFailure(Exception exception);
}
