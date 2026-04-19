import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import manager.PlantManager;
import model.Plant;

public class MainFX extends Application {
    PlantManager manager = new PlantManager();
    ListView<String> listView = new ListView<>();

    @Override
    public void start(Stage stage) {
        manager.loadFromFile();
        refreshList();

        Button addBtn = new Button("Add");
        Button deleteBtn = new Button("Delete");
        Button updateBtn = new Button("Update");

        HBox buttons = new HBox(10, addBtn, deleteBtn, updateBtn);
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
                }
            });
        });
        deleteBtn.setOnAction(e -> {
            String selected = listView.getSelectionModel().getSelectedItem();
            if (selected == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Select a plant first!");
                alert.show();
                return;
            }
            String name = selected.split("\\|")[0].replace("Indoor:", "").replace("Outdoor:", "").trim();
            manager.deletePlant(name);
            manager.saveToFile();
            refreshList();
        });
        updateBtn.setOnAction(e -> {
            String selected = listView.getSelectionModel().getSelectedItem();
            if (selected == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Select a plant first!");
                alert.show();
                return;
            }
            String name = selected.split("\\|")[0].replace("Indoor:", "").replace("Outdoor:", "").trim();

            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Update Plant");
            dialog.setHeaderText("Update watering interval for: " + name);
            dialog.setContentText("New interval (days):");

            dialog.showAndWait().ifPresent(value -> {
                try {
                    int interval = Integer.parseInt(value);
                    manager.updatePlant(name, interval);
                    manager.saveToFile();
                    refreshList();
                } catch (NumberFormatException ex) {
                    new Alert(Alert.AlertType.ERROR, "Enter a valid number!").show();
                }
            });
        });
        VBox root = new VBox(10, listView, buttons);
        root.setPadding(new Insets(15));

        stage.setTitle("Plant Watering Schedule");
        stage.setScene(new Scene(root, 500, 400));
        stage.show();
    }

    void refreshList() {
        ObservableList<String> items = FXCollections.observableArrayList();
        for (Plant p : manager.getAllPlants()) {
            items.add(p.getInfo());
        }
        listView.setItems(items);

    }


    public static void main(String[] args) {
        launch(args);
    }
}