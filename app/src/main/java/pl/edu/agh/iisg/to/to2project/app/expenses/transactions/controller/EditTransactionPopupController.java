package pl.edu.agh.iisg.to.to2project.app.expenses.transactions.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import pl.edu.agh.iisg.to.to2project.app.expenses.common.controller.PopupController;
import pl.edu.agh.iisg.to.to2project.domain.AbstractTransaction;

import java.io.IOException;
import java.util.logging.Logger;

import static java.util.logging.Level.ALL;

/**
 * @author Bartłomiej Grochal
 */
@Component
@Scope("prototype")
public class EditTransactionPopupController extends PopupController {

    @FXML
    private Pane EditTransactionPopupContent;



    @FXML
    @Override
    protected void handleOKButtonClick(ActionEvent actionEvent) {
    }

    @FXML
    private void externalPayeeTransactionChecked(ActionEvent actionEvent) {
        EditTransactionPopupContent.getChildren().clear();
        try {
            EditTransactionPopupContent.getChildren().add(FXMLLoader.load(getClass().getResource("../view/EditExternalTransactionPopupView.fxml")));
        } catch (IOException e) {
            Logger.getLogger("GUI").log(ALL, "Cannot instantiate Transaction Edit Popup Window view.");
            e.printStackTrace();
        }
    }

    @FXML
    private void selfAccountsTransactionChecked(ActionEvent actionEvent) {
        EditTransactionPopupContent.getChildren().clear();
        try {
            EditTransactionPopupContent.getChildren().add(FXMLLoader.load(getClass().getResource("../view/EditInternalTransactionPopupView.fxml")));
        } catch (IOException e) {
            Logger.getLogger("GUI").log(ALL, "Cannot instantiate Transaction Edit Popup Window view.");
            e.printStackTrace();
        }
    }

    public void editTransaction(AbstractTransaction transaction) {
        showDialog();
    }
}
