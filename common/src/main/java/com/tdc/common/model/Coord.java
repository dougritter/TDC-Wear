package com.tdc.common.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by douglas on 5/11/15.
 */
public class Coord implements Parcelable {

    double lon;
    double lat;

    public Coord() {
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }



    /*
    "coord": {
            "lon": -48.549171,
            "lat": -27.59667
        },
     */

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.lon);
        dest.writeDouble(this.lat);
    }

    private Coord(Parcel in) {
        this.lon = in.readDouble();
        this.lat = in.readDouble();
    }

    public static final Parcelable.Creator<Coord> CREATOR = new Parcelable.Creator<Coord>() {
        public Coord createFromParcel(Parcel source) {
            return new Coord(source);
        }

        public Coord[] newArray(int size) {
            return new Coord[size];
        }
    };
}
