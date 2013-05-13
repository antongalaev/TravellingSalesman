package com.galaev.tsp.gui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class MessageController {

    @FXML
    private Button button;

    public void buttonClick(ActionEvent actionEvent) {
        ((Stage) button.getScene().getWindow()).close();
    }
}