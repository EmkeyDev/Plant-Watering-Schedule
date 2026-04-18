package manager;

import model.IndoorPlant;
import model.OutdoorPlant;
import model.Plant;
import java.util.ArrayList;
import java.util.List;
import java.io.*;
import java.time.LocalDate;


public class PlantManager {
    private List<Plant> plants = new ArrayList<>();

    public void addPlant(Plant plant) {
        plants.add(plant);
    }

    public void deletePlant(String name) {
        plants.removeIf(plant -> plant.getName().equals(name));
    }

    public Plant findPlant(String name) {
        for (Plant plant : plants) {
            if (plant.getName().equals(name)) {
                return plant;
            }
        }
        return null;
    }

    public Plant updatePlant(String name, int newInterval){
        for(Plant plant : plants){
            if(plant.getName().equals(name)){
                plant.setWaterInterval(newInterval);
                return plant;
            }
        }
        return null;
    }

    public List<Plant> getAllPlants() {
        return plants;
    }

    public void saveToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("plants.csv"))) {
            writer.println("type,name,interval,lastWatered,extra");
            for (Plant plant : plants) {
                if (plant instanceof IndoorPlant ip) {
                    writer.println("INDOOR," + ip.getName() + "," + ip.getWaterInterval() + "," + ip.getLastWatered() + "," + ip.getLightRequirement());
                } else if (plant instanceof OutdoorPlant op) {
                    writer.println("OUTDOOR," + op.getName() + "," + op.getWaterInterval() + "," + op.getLastWatered() + "," + op.getSeason());
                }
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void loadFromFile() {
        File file = new File("plants.csv");
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String type = parts[0];
                String name = parts[1];
                int interval = Integer.parseInt(parts[2]);
                LocalDate lastWatered = LocalDate.parse(parts[3]);
                String extra = parts[4];

                if (type.equals("INDOOR")) {
                    plants.add(new IndoorPlant(name, interval, lastWatered, extra));
                } else if (type.equals("OUTDOOR")) {
                    plants.add(new OutdoorPlant(name, interval, lastWatered, extra));
                }
            }
        } catch (Exception e) {
            System.out.println("Error download: " + e.getMessage());
        }
    }
}
