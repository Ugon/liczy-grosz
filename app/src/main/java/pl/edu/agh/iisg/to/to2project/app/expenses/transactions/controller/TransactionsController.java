package pl.edu.agh.iisg.to.to2project.app.expenses.transactions.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.fxmisc.easybind.EasyBind;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import pl.edu.agh.iisg.to.to2project.app.expenses.transactions.view.DeleteTransactionPopup;
import pl.edu.agh.iisg.to.to2project.app.expenses.transactions.view.EditTransactionPopup;
import pl.edu.agh.iisg.to.to2project.app.expenses.transactions.view.NewExternalTransactionPopup;
import pl.edu.agh.iisg.to.to2project.app.expenses.transactions.view.NewInternalTransactionPopup;
import pl.edu.agh.iisg.to.to2project.domain.IExternalTransaction;
import pl.edu.agh.iisg.to.to2project.domain.IInternalTransaction;
import pl.edu.agh.iisg.to.to2project.domain.ITransaction;
import pl.edu.agh.iisg.to.to2project.domain.entity.*;
import pl.edu.agh.iisg.to.to2project.domain.utils.ObservableUtils;
import pl.edu.agh.iisg.to.to2project.service.AccountService;
import pl.edu.agh.iisg.to.to2project.service.ExternalTransactionService;
import pl.edu.agh.iisg.to.to2project.service.InternalTransactionService;

import java.math.BigDecimal;
import java.util.ConcurrentModificationException;
import java.util.logging.Logger;

import static java.util.logging.Level.INFO;
import static javafx.scene.control.SelectionMode.SINGLE;

/**
 * @author Bartłomiej Grochal
 * @author Wojciech Pachuta
 */
@Controller
public class TransactionsController {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private ExternalTransactionService externalTransactionService;

    @Autowired
    private InternalTransactionService internalTransactionService;

    @Autowired
    private AccountService accountService;

    @FXML
    private TableView<ITransaction> transactionsTable;

    @FXML
    private TableColumn<ITransaction, String> fromAccountColumn;

    @FXML
    private TableColumn<ITransaction, BigDecimal> transferColumn;

    @FXML
    private TableColumn<ITransaction, BigDecimal> balanceColumn;

    @FXML
    private TableColumn<ITransaction, DateTime> dateColumn;

    @FXML
    private TableColumn<ITransaction, String> categoryColumn;

    @FXML
    private TableColumn<ITransaction, String> toAccountColumn;

    @FXML
    private TableColumn<ITransaction, String> commentColumn;

    @FXML
    private ComboBox<Account> accountsFilterCombo;

    private ObservableList<? extends IInternalTransaction> internalTransactions;
    private ObservableList<? extends IExternalTransaction> externalTransactions;
    private ObservableList<? extends IInternalTransaction> internalTransactionInverses;
    private ObservableList<ITransaction> allTransactions;

    private FilteredList<ITransaction> filteredTransactions;
    private static final Account ALL_ACCOUNTS = new Account("All Accounts", new BigDecimal(0));

    @FXML
    public void initialize() {
        internalTransactions = internalTransactionService.getList();
        externalTransactions = externalTransactionService.getList();
        internalTransactionInverses = EasyBind.map(internalTransactions, IInternalTransaction::getTransactionInverse);
        allTransactions = ObservableUtils.merge(internalTransactions, externalTransactions, internalTransactionInverses);

        transactionsTable.setItems(allTransactions);

        transactionsTable.getSelectionModel().setSelectionMode(SINGLE);
        fromAccountColumn.setCellValueFactory(dataValue -> EasyBind.monadic(dataValue.getValue().destinationAccountProperty()).flatMap(Account::nameProperty));
        transferColumn.setCellValueFactory(dataValue -> dataValue.getValue().deltaProperty());

        //todo:that aint gonna work. not bound properly, also should be current balance, not initial balance
        balanceColumn.setCellValueFactory(dataValue -> dataValue.getValue().destinationAccountProperty().getValue().initialBalanceProperty());

        dateColumn.setCellValueFactory(dataValue -> dataValue.getValue().dateTimeProperty());
        categoryColumn.setCellValueFactory(dataValue -> dataValue.getValue().categoryMonadicProperty().flatMap(Category::nameProperty));
        toAccountColumn.setCellValueFactory(dataValue -> dataValue.getValue().sourcePropertyAsMonadicString());
        commentColumn.setCellValueFactory(dataValue -> dataValue.getValue().commentMonadicProperty());

        filteredTransactions = new FilteredList<>(allTransactions, p -> true);
        SortedList<ITransaction> bindableFilteredList = new SortedList<>(filteredTransactions);
        bindableFilteredList.comparatorProperty().bind(transactionsTable.comparatorProperty());
        transactionsTable.setItems(bindableFilteredList);

        accountsFilterCombo.valueProperty().addListener(new AccountChangeListener<>());
        accountsFilterCombo.getItems().addAll(accountService.getList());
        accountsFilterCombo.getItems().set(0, ALL_ACCOUNTS);
        accountsFilterCombo.setValue(ALL_ACCOUNTS);
    }


    @FXML
    private void handleInternalTransactionClick(ActionEvent actionEvent) {
        NewInternalTransactionPopup popup = context.getBean(NewInternalTransactionPopup.class);
        NewInternalTransactionPopupController controller = popup.getController();

        controller.addInternalTransaction();
    }

    @FXML
    private void handleExternalTransactionClick(ActionEvent actionEvent) {
        NewExternalTransactionPopup popup = context.getBean(NewExternalTransactionPopup.class);
        NewExternalTransactionPopupController controller = popup.getController();

        controller.addExternalTransaction();
    }

    @FXML
    private void handleEditTransactionClick(ActionEvent actionEvent) {
        EditTransactionPopup popup = context.getBean(EditTransactionPopup.class);
        EditTransactionPopupController controller = popup.getController();

        ITransaction selectedTransaction = transactionsTable.getSelectionModel().getSelectedItem();

        if (selectedTransaction != null && selectedTransaction instanceof InternalTransaction){
            controller.editTransaction((InternalTransaction) selectedTransaction);
        }
        else if (selectedTransaction != null && selectedTransaction instanceof ExternalTransaction){
            controller.editTransaction((ExternalTransaction) selectedTransaction);
        }
        else if (selectedTransaction != null && selectedTransaction instanceof InternalTransactionInverse){
            InternalTransactionInverse internalTransactionInverse = (InternalTransactionInverse)selectedTransaction;
            InternalTransaction internalTransaction = (InternalTransaction) internalTransactionInverse.getTransactionInverse();
            controller.editTransaction(internalTransaction);
        }
    }

    @FXML
    private void handleDeleteTransactionClick(ActionEvent actionEvent) {
        DeleteTransactionPopup popup = context.getBean(DeleteTransactionPopup.class);
        DeleteTransactionPopupController controller = popup.getController();

        ITransaction selectedTransaction = transactionsTable.getSelectionModel().getSelectedItem();

        if (selectedTransaction != null && selectedTransaction instanceof InternalTransaction){
            controller.deleteTransaction((InternalTransaction) selectedTransaction);
        }
        else if (selectedTransaction != null && selectedTransaction instanceof ExternalTransaction){
            controller.deleteTransaction((ExternalTransaction) selectedTransaction);
        }
        else if (selectedTransaction != null && selectedTransaction instanceof InternalTransactionInverse){
            InternalTransactionInverse internalTransactionInverse = (InternalTransactionInverse)selectedTransaction;
            InternalTransaction internalTransaction = (InternalTransaction) internalTransactionInverse.getTransactionInverse();
            controller.deleteTransaction(internalTransaction);
        }
    }

    public void refreshContent() {
        externalTransactionService.refreshCache();
        internalTransactionService.refreshCache();
        accountService.refreshCache();
        updateAccountsComboItems();
    }

    private void updateAccountsComboItems() {
        for(Account account : accountService.getList()) {
            if(!accountsFilterCombo.getItems().contains(account)) {
                accountsFilterCombo.getItems().add(account);
            }
        }

        try {
            for (Account account : accountsFilterCombo.getItems()) {
                if (!accountService.getList().contains(account) && !account.equals(ALL_ACCOUNTS)) {
                    accountsFilterCombo.getItems().remove(account);
                }
            }
        }
        catch(ConcurrentModificationException exc) {
            Logger.getLogger("GUI").log(INFO, "Removed account used as a filter.");
            accountsFilterCombo.setValue(ALL_ACCOUNTS);
        }
    }



    private class AccountChangeListener<T> implements ChangeListener<T> {
        @Override
        public void changed(ObservableValue<? extends T> observable, T oldValue, T newValue) {
            if(oldValue != null && oldValue.equals(newValue)) {
                return;
            }

            filteredTransactions.setPredicate(transaction ->
                    newValue.equals(ALL_ACCOUNTS) || transaction.destinationAccountProperty().getValue().equals(newValue)
            );
        }
    }
}
