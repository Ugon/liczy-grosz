package pl.edu.agh.iisg.to.to2project.expenses.common.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * @author Bartłomiej Grochal
 */
public abstract class PopupController {

    @FXML
    protected Button cancelButton;

    @FXML
    protected Button OKButton;


    @FXML
    protected abstract void handleOKButtonClick(ActionEvent actionEvent);

    @FXML
    protected void handleCancelButtonClick(ActionEvent actionEvent) {
        ((Stage) cancelButton.getScene().getWindow()).close();
    }
}
