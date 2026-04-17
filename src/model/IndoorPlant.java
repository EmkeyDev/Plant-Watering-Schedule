package model;

import java.time.LocalDate;

public class IndoorPlant extends Plant { // extends — значит наследует.
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
}
