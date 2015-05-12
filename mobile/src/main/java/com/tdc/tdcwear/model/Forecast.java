package com.tdc.tdcwear.model;

import java.util.List;

/**
 * Created by douglas on 5/11/15.
 */
public class Forecast {

    int cod;
    String message;
    City city;
    int cnt;
    List<LotOfThings> list;

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

    /*
    {
    "cod": "200",
    "message": 0.0203,
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
    "cnt": 7,
    "list": [
        {
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
        },
     */
}
