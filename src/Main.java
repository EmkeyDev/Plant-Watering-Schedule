import manager.AuthManager;
import manager.PlantManager;
import model.IndoorPlant;
import model.OutdoorPlant;
import model.Plant;

import model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class Main {
    static PlantManager manager = new PlantManager();
    static Scanner scanner = new Scanner(System.in);
    static AuthManager authManager = new AuthManager();

    public static void main(String[] args) {
        manager.loadFromFile();

        if (!login()) {
            System.out.println("Too many failed attempts. Goodbye!");
            return;
        }
        while (true) {
            System.out.println("\n--- Plant Watering Schedule ---");
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
        String name = getValidName("Enter name plant: ");
        System.out.println("Which type is plant \n Outdoor - 1\n Indoor - 2");
        System.out.print("Enter number: ");
        int type = Integer.parseInt(scanner.nextLine());
        int interval = getValidNumber("How much watering interval for your plant: ");
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
        String name = getValidName("Enter name plant: ");
        manager.deletePlant(name);
        System.out.println("The " + name + " deleted");
    }

    static void updatePlant() {
        String name = getValidName("Enter name plant: ");
        int interval = getValidNumber("How much watering interval for your plant: ");
        manager.updatePlant(name, interval);
        System.out.println("The " + name + " successful update");
    }

    static int getValidNumber(String prompt) {
        while (true) {
            System.out.println(prompt);
            try {
                int number = Integer.parseInt(scanner.nextLine());
                if (number <= 0) {
                    System.out.println("Number must be greater than 0!");
                } else {
                    return number;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Enter a number.");
            }
        }
    }

    static String getValidName(String prompt) {
        while (true) {
            System.out.println(prompt);
            String name = scanner.nextLine().trim();
            if (name.isEmpty()) {
                System.out.println("Name cannot be empty!");
            } else if (!name.matches("[a-zA-Z]+")) {
                System.out.println("Name must contain only letters!");
            } else {
                return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
            }
        }
    }

    static String getValidPassword(String prompt) {
        while (true) {
            System.out.print(prompt);
            String password = scanner.nextLine();
            if (password.isEmpty()) {
                System.out.println("Password cannot be empty!");
            } else if (password.length() < 4) {
                System.out.println("Password must be at least 4 characters!");
            } else {
                return password;
            }
        }
    }

    static boolean login() {
        int attempts = 3;
        while (attempts > 0) {
            String username = getValidName("Username: ");
            String password = getValidPassword("Password: ");

            User user = authManager.login(username, password);
            if (user != null) {
                System.out.println("Welcome, " + user.getUsername() + "! Role: " + user.getRole());
                return true;
            }
            attempts--;
            System.out.println("Wrong username or password! Attempts left: " + attempts);
        }
        return false;
    }
}
