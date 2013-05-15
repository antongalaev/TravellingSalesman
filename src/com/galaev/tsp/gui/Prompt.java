package com.galaev.tsp.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Main class for a Prompt Window.
 *
 * @author Anton Galaev
 */
public class Prompt {

    /** Result of prompt */
    public static String result;
    /** If button was pressed */
    public static boolean buttonPressed;
    /** The stage  */
    private Stage stage;

    /**
     * Public constructor for a Prompt.
     * Creates a modal window with label,
     * text field and message,
     * provided as parameters.
     *
     * @param primaryStage primary stage
     * @param title title of message
     * @param message message itself
     */
    public Prompt(Stage primaryStage, String title, String message) {
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("view/PromptView.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        stage = new Stage();
        //Initialize the Stage with type of modal
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        //Set the owner of the Stage
        stage.initOwner(primaryStage);
        stage.setTitle(title);
        Label label = (Label) root.lookup("#label");
        label.setText(message);
        Scene scene = new Scene(root, 300, 100);
        scene.getStylesheets().add("com/galaev/tsp/gui/resources/styles/popup.css");
        stage.setScene(scene);
    }

    /**
     * Shows the prompt window.
     * @return
     */
    public boolean show() {
        buttonPressed = false;
        stage.showAndWait();
        return buttonPressed;
    }
}
