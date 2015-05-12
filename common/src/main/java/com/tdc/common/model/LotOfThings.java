package com.tdc.common.model;

import java.util.List;

/**
 * Created by douglas on 5/11/15.
 */
public class LotOfThings {

    long dt;
    Temp temp;
    double pressure;
    List<Weather> weather;

    public long getDt() {
        return dt;
    }

    public void setDt(long dt) {
        this.dt = dt;
    }

    public Temp getTemp() {
        return temp;
    }

    public void setTemp(Temp temp) {
        this.temp = temp;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public List<Weather> getWeather() {
        return weather;
    }

    public void setWeather(List<Weather> weather) {
        this.weather = weather;
    }

    /*
    "dt": 1431356400,
            "temp": {
                "day": 15.31,
                "min": 14.43,
                "max": 15.31,
                "night": 14.67,
                "eve": 15.31,
                "morn": 15.31
            },
            "pressure": 964.68,
            "humidity": 100,
            "weather": [
                {
                    "id": 502,
                    "main": "Rain",
                    "description": "heavy intensity rain",
                    "icon": "10d"
                }
            ],
            "speed": 10.37,
            "deg": 193,
            "clouds": 92,
            "rain": 19.01
     */
}
