package com.galaev.tsp.gui.controllers;

import com.galaev.tsp.gui.Prompt;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * A controller class for prompt messages.
 *
 * @author Anton Galaev
 */
public class PromptController {

    /* Text field */
    @FXML private TextField textField;
    /* OK Button */
    @FXML private Button button;

    /**
     * Reads value from text field.
     *
     * @param actionEvent
     */
    public void buttonClick(ActionEvent actionEvent) {
        Prompt.buttonPressed = true;
        Prompt.result = textField.getText();
        ((Stage) button.getScene().getWindow()).close();
    }
}