package com.tdc.common.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by douglas on 5/11/15.
 */
public class City implements Parcelable {

    int id;
    String name;
    Coord coord;
    String country;
    int population;

    public City() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Coord getCoord() {
        return coord;
    }

    public void setCoord(Coord coord) {
        this.coord = coord;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }



    /*
    "city": {
        "id": 3463237,
        "name": "Florianopolis",
        "coord": {
            "lon": -48.549171,
            "lat": -27.59667
        },
        "country": "BR",
        "population": 0,
        "sys": {
            "population": 0
        }
    },
     */

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeParcelable(this.coord, flags);
        dest.writeString(this.country);
        dest.writeInt(this.population);
    }

    private City(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.coord = in.readParcelable(Coord.class.getClassLoader());
        this.country = in.readString();
        this.population = in.readInt();
    }

    public static final Parcelable.Creator<City> CREATOR = new Parcelable.Creator<City>() {
        public City createFromParcel(Parcel source) {
            return new City(source);
        }

        public City[] newArray(int size) {
            return new City[size];
        }
    };
}
