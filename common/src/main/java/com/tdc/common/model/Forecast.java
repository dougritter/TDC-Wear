package com.tdc.common.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by douglas on 5/11/15.
 */

public class Forecast implements Parcelable {

    int cod;
    String message;
    City city;
    int cnt;
    List<LotOfThings> list;

    public Forecast() {
    }

    public int getCod() {
        return cod;
    }

    public void setCod(int cod) {
        this.cod = cod;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    public List<LotOfThings> getList() {
        return list;
    }

    public void setList(List<LotOfThings> list) {
        this.list = list;
    }


    protected Forecast(Parcel in) {
        cod = in.readInt();
        message = in.readString();
        city = (City) in.readValue(City.class.getClassLoader());
        cnt = in.readInt();
        if (in.readByte() == 0x01) {
            list = new ArrayList<LotOfThings>();
            in.readList(list, LotOfThings.class.getClassLoader());
        } else {
            list = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(cod);
        dest.writeString(message);
        dest.writeValue(city);
        dest.writeInt(cnt);
        if (list == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(list);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Forecast> CREATOR = new Parcelable.Creator<Forecast>() {
        @Override
        public Forecast createFromParcel(Parcel in) {
            return new Forecast(in);
        }

        @Override
        public Forecast[] newArray(int size) {
            return new Forecast[size];
        }
    };
}