package com.galaev.tsp.gui.controllers;

import com.galaev.tsp.gui.Prompt;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class PromptController {

    @FXML
    private TextField textField;
    @FXML
    private Button button;

    public void buttonClick(ActionEvent actionEvent) {
        Prompt.buttonPressed = true;
        Prompt.result = textField.getText();
        ((Stage) button.getScene().getWindow()).close();
    }
}