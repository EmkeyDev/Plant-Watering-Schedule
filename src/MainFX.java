import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import manager.PlantManager;
import model.Plant;

public class MainFX extends Application {
    PlantManager manager = new PlantManager();
    ListView<Plant> listView = new ListView<>();

    @Override
    public void start(Stage stage) {
        manager.loadFromFile();

        listView.setCellFactory(lv -> new ListCell<Plant>() {
            Button waterBtn = new Button();
            HBox row = new HBox(10);
            {
                waterBtn.setOnAction(e -> {
                    Plant plant = getItem();
                    if (plant != null) {
                        plant.setLastWatered(java.time.LocalDate.now());
                        manager.saveToFile();
                        refreshList();
                    }
                });
                setOnMouseClicked(e -> {
                    if (getItem() != null) {
                        listView.getSelectionModel().select(getItem());
                    }
                });
            }

            @Override
            protected void updateItem(Plant plant, boolean empty) {
                super.updateItem(plant, empty);
                if (empty || plant == null) {
                    setGraphic(null);
                    setText(null);
                    return;
                }
                boolean needs = plant.needsWatering();
                String info = plant.getInfo() + " | every " + plant.getWaterInterval()
                        + " days | next: " + plant.getNextWateringDate();

                Label label = new Label(info);
                label.setStyle(needs
                        ? "-fx-text-fill: #e53935; -fx-font-size: 12px;"
                        : "-fx-text-fill: #2d6a2d; -fx-font-size: 12px;");

                waterBtn.setText(needs ? "💧 Water" : "✓ OK");
                waterBtn.setStyle(needs
                        ? "-fx-background-color: #e53935; -fx-text-fill: white; -fx-background-radius: 4; -fx-font-size: 11px;"
                        : "-fx-background-color: #00c500; -fx-text-fill: white; -fx-background-radius: 4; -fx-font-size: 11px; -fx-font-weight: bold;");                row.getChildren().setAll(waterBtn, label);
                row.setAlignment(Pos.CENTER_LEFT);
                setGraphic(row);
                setText(null);
            }
        });
        listView.setStyle("-fx-background-color: #f4f9f4; -fx-border-color: #81c784; "
                + "-fx-border-radius: 8; -fx-background-radius: 8; "
                + "-fx-selection-bar: #e8f5e9; -fx-selection-bar-non-focused: #e8f5e9;");

        refreshList();

        Label title = new Label("🌿 Plant Watering Schedule");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2d6a2d;");

        listView.setStyle("-fx-background-color: #f4f9f4; -fx-border-color: #81c784; -fx-border-radius: 8; -fx-background-radius: 8;");
        listView.setPrefHeight(300);

        Button addBtn = new Button("+ Add");
        Button deleteBtn = new Button("🗑 Delete");
        Button updateBtn = new Button("✎ Update");

        addBtn.setStyle("-fx-background-color: #00c500; -fx-text-fill: white; -fx-font-size: 13px; -fx-padding: 8 20; -fx-background-radius: 6;");
        deleteBtn.setStyle("-fx-background-color: #e53935; -fx-text-fill: white; -fx-font-size: 13px; -fx-padding: 8 20; -fx-background-radius: 6;");
        updateBtn.setStyle("-fx-background-color: #1565c0; -fx-text-fill: white; -fx-font-size: 13px; -fx-padding: 8 20; -fx-background-radius: 6;");

        addBtn.setOnAction(e -> {
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Add Plant");
            TextField nameField = new TextField();
            nameField.setPromptText("Plant name");
            TextField intervalField = new TextField();
            intervalField.setPromptText("Watering interval (days)");
            ComboBox<String> typeBox = new ComboBox<>();
            typeBox.getItems().addAll("Indoor", "Outdoor");
            typeBox.setValue("Indoor");
            TextField extraField = new TextField();
            extraField.setPromptText("Light (low/medium/high) or Season");
            VBox content = new VBox(10, nameField, intervalField, typeBox, extraField);
            content.setPadding(new Insets(10));
            dialog.getDialogPane().setContent(content);
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
            dialog.showAndWait().ifPresent(btn -> {
                if (btn == ButtonType.OK) {
                    try {
                        String name = nameField.getText();
                        int interval = Integer.parseInt(intervalField.getText());
                        String extra = extraField.getText();
                        if (typeBox.getValue().equals("Indoor")) {
                            manager.addPlant(new model.IndoorPlant(name, interval, java.time.LocalDate.now(), extra));
                        } else {
                            manager.addPlant(new model.OutdoorPlant(name, interval, java.time.LocalDate.now(), extra));
                        }
                        manager.saveToFile();
                        refreshList();
                    } catch (NumberFormatException ex) {
                        new Alert(Alert.AlertType.ERROR, "Enter a valid number!").show();
                    }
                }
            });
        });

        deleteBtn.setOnAction(e -> {
            Plant selected = listView.getSelectionModel().getSelectedItem();
            if (selected == null) {
                new Alert(Alert.AlertType.WARNING, "Select a plant first!").show();
                return;
            }
            manager.deletePlant(selected.getName());
            manager.saveToFile();
            refreshList();
        });

        updateBtn.setOnAction(e -> {
            Plant selected = listView.getSelectionModel().getSelectedItem();
            if (selected == null) {
                new Alert(Alert.AlertType.WARNING, "Select a plant first!").show();
                return;
            }
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Update Plant");
            dialog.setHeaderText("Update: " + selected.getName());
            dialog.setContentText("New interval (days):");
            dialog.showAndWait().ifPresent(value -> {
                try {
                    int interval = Integer.parseInt(value);
                    manager.updatePlant(selected.getName(), interval);
                    manager.saveToFile();
                    refreshList();
                } catch (NumberFormatException ex) {
                    new Alert(Alert.AlertType.ERROR, "Enter a valid number!").show();
                }
            });
        });

        HBox buttons = new HBox(10, addBtn, deleteBtn, updateBtn);
        buttons.setPadding(new Insets(5, 0, 0, 0));

        VBox root = new VBox(12, title, listView, buttons);
        root.setOnMouseClicked(e -> {
            listView.getSelectionModel().clearSelection();
        });
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #ffffff;");

        stage.setTitle("Plant Watering Schedule");
        stage.setScene(new Scene(root, 580, 450));
        stage.show();
    }

    void refreshList() {
        ObservableList<Plant> items = FXCollections.observableArrayList(manager.getAllPlants());
        listView.setItems(items);
    }

    public static void main(String[] args) {
        launch(args);
    }
}