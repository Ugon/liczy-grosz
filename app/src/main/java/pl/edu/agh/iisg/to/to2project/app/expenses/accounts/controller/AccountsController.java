package pl.edu.agh.iisg.to.to2project.app.expenses.accounts.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import pl.edu.agh.iisg.to.to2project.app.expenses.accounts.view.DeleteAccountPopup;
import pl.edu.agh.iisg.to.to2project.app.expenses.accounts.view.EditAccountPopup;
import pl.edu.agh.iisg.to.to2project.app.expenses.accounts.view.NewAccountPopup;
import pl.edu.agh.iisg.to.to2project.domain.Account;
import pl.edu.agh.iisg.to.to2project.service.AccountService;

import java.math.BigDecimal;
import java.util.Optional;

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
    private TableColumn<Account, BigDecimal> balanceColumn;

    private ObservableList<Account> data;

    @FXML
    public void initialize() {
        data = FXCollections.observableArrayList(accountService.getList());
        accountsTable.setItems(data);

        accountsTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        nameColumn.setCellValueFactory(dataValue -> dataValue.getValue().nameProperty());
        balanceColumn.setCellValueFactory(dataValue -> dataValue.getValue().initialBalanceProperty());
    }

    @FXML
    private void handleAddAccountClick(ActionEvent actionEvent) {
        NewAccountPopup popup = context.getBean(NewAccountPopup.class);
        NewAccountPopupController controller = popup.getController();

        Optional<Account> newAccount = controller.addAccount();
        if(newAccount.isPresent()){
            data.add(newAccount.get());
        }
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
            if(controller.deleteAccount(selectedAccount)){
                data.remove(selectedAccount);
            }
        }
    }
}
