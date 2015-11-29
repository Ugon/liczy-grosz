package pl.edu.agh.iisg.to.to2project.app.expenses.transactions.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import pl.edu.agh.iisg.to.to2project.app.expenses.transactions.view.DeleteTransactionPopup;
import pl.edu.agh.iisg.to.to2project.app.expenses.transactions.view.EditTransactionPopup;
import pl.edu.agh.iisg.to.to2project.app.expenses.transactions.view.ExternalTransactionPopup;
import pl.edu.agh.iisg.to.to2project.app.expenses.transactions.view.SelfTransactionPopup;

/**
 * @author Bartłomiej Grochal
 */
public class TransactionButtonsHandlers {

    @FXML
    private void handleSelfTransactionClick(ActionEvent actionEvent) {
        new SelfTransactionPopup();
    }

    @FXML
    private void handleExternalTransactionClick(ActionEvent actionEvent) {
        new ExternalTransactionPopup();
    }

    @FXML
    private void handleEditTransactionClick(ActionEvent actionEvent) {
        new EditTransactionPopup();
    }

    @FXML
    private void handleDeleteTransactionClick(ActionEvent actionEvent) {
        new DeleteTransactionPopup();
    }
}
