package pl.edu.agh.iisg.to.to2project.expenses.accounts.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import pl.edu.agh.iisg.to.to2project.expenses.accounts.view.DeleteAccountPopup;
import pl.edu.agh.iisg.to.to2project.expenses.accounts.view.EditAccountPopup;
import pl.edu.agh.iisg.to.to2project.expenses.accounts.view.NewAccountPopup;

/**
 * @author Bartłomiej Grochal
 */
public class AccountButtonsHandlers {

    @FXML
    private void handleAddAccountClick(ActionEvent actionEvent) {
        new NewAccountPopup();
    }

    @FXML
    private void handleEditAccountClick(ActionEvent actionEvent) {
        new EditAccountPopup();
    }

    @FXML
    private void handleDeleteAccountClick(ActionEvent actionEvent) {
        new DeleteAccountPopup();
    }
}
