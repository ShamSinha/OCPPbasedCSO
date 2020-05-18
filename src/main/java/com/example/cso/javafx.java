package com.example.cso;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class javafx extends Application {


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Fxml1controller controller = new Fxml1controller();

        FXMLLoader loader = new FXMLLoader();
        loader.setController(controller);
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/new.fxml"));

        primaryStage.setScene(new Scene(root));
        primaryStage.show();

    }
}
