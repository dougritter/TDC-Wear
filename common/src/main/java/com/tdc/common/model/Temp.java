package com.tdc.common.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by douglas on 5/11/15.
 */
public class Temp implements Parcelable {

    double day, min, max, night, eve, morn;

    public Temp() {
    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.day);
        dest.writeDouble(this.min);
        dest.writeDouble(this.max);
        dest.writeDouble(this.night);
        dest.writeDouble(this.eve);
        dest.writeDouble(this.morn);
    }

    private Temp(Parcel in) {
        this.day = in.readDouble();
        this.min = in.readDouble();
        this.max = in.readDouble();
        this.night = in.readDouble();
        this.eve = in.readDouble();
        this.morn = in.readDouble();
    }

    public static final Parcelable.Creator<Temp> CREATOR = new Parcelable.Creator<Temp>() {
        public Temp createFromParcel(Parcel source) {
            return new Temp(source);
        }

        public Temp[] newArray(int size) {
            return new Temp[size];
        }
    };
}
