package pl.edu.agh.iisg.to.to2project.app.expenses.accounts.controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import pl.edu.agh.iisg.to.to2project.app.expenses.accounts.view.DeleteAccountPopup;
import pl.edu.agh.iisg.to.to2project.app.expenses.accounts.view.EditAccountPopup;
import pl.edu.agh.iisg.to.to2project.app.expenses.accounts.view.NewAccountPopup;
import pl.edu.agh.iisg.to.to2project.domain.entity.Account;
import pl.edu.agh.iisg.to.to2project.service.AccountService;

import java.math.BigDecimal;

import static javafx.scene.control.SelectionMode.SINGLE;

/**
 * @author Bartłomiej Grochal
 * @author Wojciech Pachuta
 */
@Controller
public class AccountsController {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private AccountService accountService;

    @FXML
    private TableView<Account> accountsTable;

    @FXML
    private TableColumn<Account, String> nameColumn;

    @FXML
    private TableColumn<Account, BigDecimal> initialBalanceColumn;

    @FXML
    private TableColumn<Account, BigDecimal> currentBalanceColumn;

    private ObservableList<Account> data;

    @FXML
    public void initialize() {
        data = accountService.getList();
        accountsTable.setItems(data);

        accountsTable.getSelectionModel().setSelectionMode(SINGLE);

        nameColumn.setCellValueFactory(dataValue -> dataValue.getValue().nameProperty());
        initialBalanceColumn.setCellValueFactory(dataValue -> dataValue.getValue().initialBalanceProperty());
        currentBalanceColumn.setCellValueFactory(dataValue -> dataValue.getValue().currentBalance());
    }

    @FXML
    private void handleAddAccountClick(ActionEvent actionEvent) {
        NewAccountPopup popup = context.getBean(NewAccountPopup.class);
        NewAccountPopupController controller = popup.getController();

        controller.addAccount();
    }

    @FXML
    private void handleEditAccountClick(ActionEvent actionEvent) {
        EditAccountPopup popup = context.getBean(EditAccountPopup.class);
        EditAccountPopupController controller = popup.getController();

        Account selectedAccount = accountsTable.getSelectionModel().getSelectedItem();
        if(selectedAccount != null) {
            controller.editAccount(selectedAccount);
        }
    }

    @FXML
    private void handleDeleteAccountClick(ActionEvent actionEvent) {
        DeleteAccountPopup popup = context.getBean(DeleteAccountPopup.class);
        DeleteAccountPopupController controller = popup.getController();

        Account selectedAccount = accountsTable.getSelectionModel().getSelectedItem();
        if(selectedAccount != null){
            controller.deleteAccount(selectedAccount);
        }
    }

    public void refreshContent() {
        accountService.refreshCache();
        accountsTable.refresh();
    }
}
