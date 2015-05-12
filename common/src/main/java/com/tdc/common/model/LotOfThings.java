package com.tdc.common.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by douglas on 5/11/15.
 */
public class LotOfThings implements Parcelable {

    long dt;
    Temp temp;
    double pressure;
    List<Weather> weather;

    public LotOfThings() {
    }

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


    protected LotOfThings(Parcel in) {
        dt = in.readLong();
        temp = (Temp) in.readValue(Temp.class.getClassLoader());
        pressure = in.readDouble();
        if (in.readByte() == 0x01) {
            weather = new ArrayList<Weather>();
            in.readList(weather, Weather.class.getClassLoader());
        } else {
            weather = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(dt);
        dest.writeValue(temp);
        dest.writeDouble(pressure);
        if (weather == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(weather);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<LotOfThings> CREATOR = new Parcelable.Creator<LotOfThings>() {
        @Override
        public LotOfThings createFromParcel(Parcel in) {
            return new LotOfThings(in);
        }

        @Override
        public LotOfThings[] newArray(int size) {
            return new LotOfThings[size];
        }
    };
}
