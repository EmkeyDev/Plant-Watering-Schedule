package model;

import java.time.LocalDate;

public class IndoorPlant extends Plant {
    private String lightRequirement;

    public IndoorPlant(String name, int waterInterval, LocalDate lastWatered, String lightRequirement) {
        super(name, waterInterval, lastWatered);
        this.lightRequirement = lightRequirement;
    }

    public String getLightRequirement(){
        return lightRequirement;
    }

    public void setLightRequirement(String lightRequirement) {
        this.lightRequirement = lightRequirement;
    }

    @Override
    public String getInfo() {
        return "Indoor: " + getName() + " | light: " + lightRequirement;
    }
}
