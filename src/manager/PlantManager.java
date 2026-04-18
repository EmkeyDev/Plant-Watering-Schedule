package manager;

import model.Plant;
import java.util.ArrayList;
import java.util.List;

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
}
