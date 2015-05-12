package com.tdc.common.model;

/**
 * Created by douglas on 5/11/15.
 */
public class Weather {

    int id;
    String main;
    String description;
    String icon;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    /*
        "id": 502,
        "main": "Rain",
        "description": "heavy intensity rain",
        "icon": "10d"
     */

}
