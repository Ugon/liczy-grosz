package pl.edu.agh.iisg.to.to2project.app.expenses.common.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * @author Bartłomiej Grochal
 * @author Wojciech Pachuta
 */
public abstract class PopupController {

    protected Stage dialogStage;

    @FXML
    protected abstract void handleOKButtonClick(ActionEvent actionEvent);

    @FXML
    protected void handleCancelButtonClick(ActionEvent actionEvent) {
        dialogStage.close();
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    protected void showDialog(){
        dialogStage.show();
    }
}
