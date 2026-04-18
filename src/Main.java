import manager.PlantManager;
import model.IndoorPlant;
import model.OutdoorPlant;
import model.Plant;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class Main {
    static PlantManager manager = new PlantManager();
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        manager.loadFromFile();

        while (true) {
            System.out.println("\n=== Plant Watering Schedule ===");
            System.out.println("1. Add plant");
            System.out.println("2. List plants");
            System.out.println("3. Delete plant");
            System.out.println("4. Update watering interval");
            System.out.println("0. Exit");
            System.out.print("Enter number: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> addPlant();
                case "2" -> listPlants();
                case "3" -> deletePlant();
                case "4" -> updatePlant();
                case "0" -> {
                    manager.saveToFile();
                    return;
                }
                default -> System.out.println("Null");
            }
        }
    }

    static void listPlants() {
        List<Plant> list = manager.getAllPlants();
        if (list.isEmpty()) {
            System.out.println("List is empty");
            return;
        }
        for (Plant plant : list) {
            System.out.println(plant.getInfo());
        }
    }

    static void addPlant() {
        System.out.print("Enter name plant: ");
        String name = scanner.nextLine();
        System.out.print("Which type is plant \n Outdoor - 1\n Indoor - 2");
        System.out.print("Enter number: ");
        int type = Integer.parseInt(scanner.nextLine());
        System.out.print("How much watering interval for your plant: ");
        int interval = Integer.parseInt(scanner.nextLine());
        if (type == 1) {
            System.out.print("Season (spring/summer/all-year): ");
            String season = scanner.nextLine();
            manager.addPlant(new OutdoorPlant(name, interval, LocalDate.now(), season));
        } else {
            System.out.print("Light (low/medium/high): ");
            String light = scanner.nextLine();
            manager.addPlant(new IndoorPlant(name, interval, LocalDate.now(), light));
        }
    }

    static void deletePlant() {
        System.out.print("Enter name plant: ");
        String name = scanner.nextLine();
        manager.deletePlant(name);
        System.out.println("The " + name + " deleted");
    }

    static void updatePlant() {
        System.out.print("Enter name plant: ");
        String name = scanner.nextLine();
        System.out.print("Enter new watering interval: ");
        int interval = Integer.parseInt(scanner.nextLine());
        manager.updatePlant(name, interval);
        System.out.println("The " + name + " successful update");
    }
}
