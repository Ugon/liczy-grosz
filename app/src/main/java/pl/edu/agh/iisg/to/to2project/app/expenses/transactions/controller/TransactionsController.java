package pl.edu.agh.iisg.to.to2project.app.expenses.transactions.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.fxmisc.easybind.EasyBind;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import pl.edu.agh.iisg.to.to2project.app.expenses.transactions.view.*;
import pl.edu.agh.iisg.to.to2project.domain.IExternalTransaction;
import pl.edu.agh.iisg.to.to2project.domain.IInternalTransaction;
import pl.edu.agh.iisg.to.to2project.domain.ITransaction;
import pl.edu.agh.iisg.to.to2project.domain.entity.*;
import pl.edu.agh.iisg.to.to2project.domain.utils.ObservableUtils;
import pl.edu.agh.iisg.to.to2project.service.AccountService;
import pl.edu.agh.iisg.to.to2project.service.ExternalTransactionService;
import pl.edu.agh.iisg.to.to2project.service.InternalTransactionService;

import java.math.BigDecimal;

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
    private TableColumn<ITransaction, String> destinationAccountColumn;

    @FXML
    private TableColumn<ITransaction, BigDecimal> transferColumn;

    @FXML
    private TableColumn<ITransaction, BigDecimal> balanceColumn;

    @FXML
    private TableColumn<ITransaction, LocalDate> dateColumn;

    @FXML
    private TableColumn<ITransaction, String> categoryColumn;

    @FXML
    private TableColumn<ITransaction, String> sourceAccountColumn;

    @FXML
    private TableColumn<ITransaction, String> commentColumn;

    @FXML
    private ComboBox<Account> accountsFilterCombo;

    private SortedList<ITransaction> sortedFilteredTransactions;

    private ObservableList<Account> accounts;

    private static final Account ALL_ACCOUNTS = new Account("All Accounts", new BigDecimal(0));

    @FXML
    public void initialize() {
        final ObservableList<? extends IInternalTransaction> internalTransactions = internalTransactionService.getList();
        final ObservableList<? extends IExternalTransaction> externalTransactions = externalTransactionService.getList();
        final ObservableList<? extends IInternalTransaction> internalTransactionInverses = EasyBind.map(internalTransactions, IInternalTransaction::getTransactionInverse);
        final ObservableList<ITransaction> allTransactions = ObservableUtils.transferElements(internalTransactions, externalTransactions, internalTransactionInverses);

        final FilteredList<ITransaction> filteredTransactions = allTransactions.filtered(p -> true);

        sortedFilteredTransactions = filteredTransactions.sorted();
        sortedFilteredTransactions.comparatorProperty().bind(transactionsTable.comparatorProperty());

        transactionsTable.setItems(sortedFilteredTransactions);
        transactionsTable.getSelectionModel().setSelectionMode(SINGLE);

        destinationAccountColumn.setCellValueFactory(dataValue -> EasyBind.monadic(dataValue.getValue().destinationAccountProperty()).flatMap(Account::nameProperty));
        transferColumn.setCellValueFactory(dataValue -> dataValue.getValue().deltaProperty());
        balanceColumn.setCellValueFactory(dataValue -> dataValue.getValue().accountBalanceAfterThisTransaction());
        dateColumn.setCellValueFactory(dataValue -> dataValue.getValue().dateProperty());
        categoryColumn.setCellValueFactory(dataValue -> dataValue.getValue().categoryMonadicProperty().flatMap(Category::nameProperty));
        sourceAccountColumn.setCellValueFactory(dataValue -> dataValue.getValue().sourcePropertyAsMonadicString());
        commentColumn.setCellValueFactory(dataValue -> dataValue.getValue().commentMonadicProperty());

        accounts = ObservableUtils.transferElements(accountService.getList(), FXCollections.observableArrayList(ALL_ACCOUNTS));
        accountsFilterCombo.setItems(accounts);
        accountsFilterCombo.setValue(ALL_ACCOUNTS);

        accountsFilterCombo.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(oldValue == null || !oldValue.equals(newValue)){
                filteredTransactions.setPredicate(transaction ->
                        newValue == null || newValue.equals(ALL_ACCOUNTS) || transaction.destinationAccountProperty().getValue().equals(newValue));
            }});
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
        ITransaction selectedTransaction = transactionsTable.getSelectionModel().getSelectedItem();

        if (selectedTransaction != null && selectedTransaction instanceof InternalTransaction){
            EditInternalTransactionPopup popup = context.getBean(EditInternalTransactionPopup.class);
            EditInternalTransactionPopupController controller = popup.getController();
            
            controller.editTransaction((InternalTransaction) selectedTransaction);
        }
        else if (selectedTransaction != null && selectedTransaction instanceof ExternalTransaction){
            EditExternalTransactionPopup popup = context.getBean(EditExternalTransactionPopup.class);
            EditExternalTransactionPopupController controller = popup.getController();
            
            controller.editTransaction((ExternalTransaction) selectedTransaction);
        }
        else if (selectedTransaction != null && selectedTransaction instanceof InternalTransactionInverse){
            EditInternalTransactionPopup popup = context.getBean(EditInternalTransactionPopup.class);
            EditInternalTransactionPopupController controller = popup.getController();

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
        transactionsTable.refresh();
    }
}
