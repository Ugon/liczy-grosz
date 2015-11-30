package pl.edu.agh.iisg.to.to2project.app.expenses.accounts.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import pl.edu.agh.iisg.to.to2project.app.expenses.accounts.view.DeleteAccountPopup;
import pl.edu.agh.iisg.to.to2project.app.expenses.accounts.view.EditAccountPopup;
import pl.edu.agh.iisg.to.to2project.domain.Account;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Bartłomiej Grochal
 * @author Wojciech Pachuta
 */
@Controller
public class AccountsController implements Initializable {
    @Autowired
    private ApplicationContext context;

    @FXML
    private TableView<Account> accountsTable;

    @FXML
    private TableColumn<Account, String> nameColumn;

    @FXML
    private TableColumn<Account, BigDecimal> balanceColumn;

    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        accountsTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        nameColumn.setCellValueFactory(dataValue -> dataValue.getValue().nameProperty());
        balanceColumn.setCellValueFactory(dataValue -> dataValue.getValue().initialBalanceProperty());
    }

    @FXML
    private void handleAddAccountClick(ActionEvent actionEvent) {
        context.getBean("newAccountPopup");
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
