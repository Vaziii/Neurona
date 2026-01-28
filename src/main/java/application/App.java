package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utilities.Paths;

import java.net.URL;

public class App extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        // 1. Cargar el FXML inicial
        URL fxmlLocation = getClass().getResource(Paths.MainMenu);
        if (fxmlLocation == null) {

            throw new RuntimeException("No se pudo encontrar el archivo FXML en: " + Paths.MainMenu);
        }
        Parent root = FXMLLoader.load(fxmlLocation);
        Scene scene = new Scene(root);

        stage.setResizable(false);
        stage.setTitle("Menu Memory Core");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}