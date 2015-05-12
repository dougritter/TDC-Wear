package com.tdc.common.model;

/**
 * Created by douglas on 5/11/15.
 */
public class Temp {

    double day, min, max, night, eve, morn;

    public double getDay() {
        return day;
    }

    public void setDay(double day) {
        this.day = day;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public double getNight() {
        return night;
    }

    public void setNight(double night) {
        this.night = night;
    }

    public double getEve() {
        return eve;
    }

    public void setEve(double eve) {
        this.eve = eve;
    }

    public double getMorn() {
        return morn;
    }

    public void setMorn(double morn) {
        this.morn = morn;
    }

    /*
    "temp": {
                "day": 15.31,
                "min": 14.43,
                "max": 15.31,
                "night": 14.67,
                "eve": 15.31,
                "morn": 15.31
            },
     */

}
