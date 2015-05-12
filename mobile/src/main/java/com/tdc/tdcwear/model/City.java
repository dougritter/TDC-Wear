package com.tdc.tdcwear.model;

/**
 * Created by douglas on 5/11/15.
 */
public class City {

    int id;
    String name;
    Coord coord;
    String country;
    int population;

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
}
