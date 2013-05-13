package com.galaev.tsp.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class Message {

    public Message(Stage primaryStage, String title, String message) {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("view/MessageView.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        Stage stage = new Stage();
        //Initialize the Stage with type of modal
        stage.initModality(Modality.APPLICATION_MODAL);
        //Set the owner of the Stage
        stage.initOwner(primaryStage);
        stage.setTitle(title);
        Label label = (Label) root.lookup("#label");
        label.setText(message);
        Scene scene = new Scene(root, 250, 100);
        scene.getStylesheets().add("com/galaev/tsp/gui/resources/popup.css");
        stage.setScene(scene);
        stage.showAndWait();
    }
}
