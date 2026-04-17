package model;

import java.time.LocalDate;

public class OutdoorPlant extends Plant {
    private String season;

    public OutdoorPlant(String name, int waterInterval, LocalDate lastWatered, String season) {
        super(name, waterInterval, lastWatered);
        this.season = season;
    }

    public String getSeason(){
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }
}
