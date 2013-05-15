package com.galaev.tsp.gui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * A controller class for popup messages.
 *
 * @author Anton Galaev
 */
public class MessageController {

    /* OK Button */
    @FXML private Button button;

    /**
     * Closes window on a click.
     *
     * @param actionEvent click event
     */
    public void buttonClick(ActionEvent actionEvent) {
        ((Stage) button.getScene().getWindow()).close();
    }
}