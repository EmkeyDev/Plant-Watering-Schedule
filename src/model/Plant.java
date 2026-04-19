package model;

import java.time.LocalDate;

public abstract class Plant {
    private String name;
    private int waterInterval;
    private LocalDate lastWatered;

    public Plant(String name, int waterInterval, LocalDate lastWatered) {
        this.name = name;
        this.waterInterval = waterInterval;
        this.lastWatered = lastWatered;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWaterInterval() {
        return waterInterval;
    }

    public void setLastWatered(LocalDate lastWatered) {
        this.lastWatered = lastWatered;
    }

    public LocalDate getLastWatered() {
        return lastWatered;
    }

    public void setWaterInterval(int waterInterval) {
        this.waterInterval = waterInterval;
    }

    public LocalDate getNextWateringDate() {
        return lastWatered.plusDays(waterInterval);
    }

    // Второй метод который говорит нужно ли поливать сегодня.
    public boolean needsWatering() {
        return !LocalDate.now().isBefore(getNextWateringDate());
    }

    public abstract String getInfo();
}