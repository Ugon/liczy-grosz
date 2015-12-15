package pl.edu.agh.iisg.to.to2project.app.expenses.transactions.controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.fxmisc.easybind.EasyBind;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import pl.edu.agh.iisg.to.to2project.domain.utils.ObservableUtils;
import pl.edu.agh.iisg.to.to2project.app.expenses.transactions.view.DeleteTransactionPopup;
import pl.edu.agh.iisg.to.to2project.app.expenses.transactions.view.EditTransactionPopup;
import pl.edu.agh.iisg.to.to2project.app.expenses.transactions.view.NewExternalTransactionPopup;
import pl.edu.agh.iisg.to.to2project.app.expenses.transactions.view.NewInternalTransactionPopup;
import pl.edu.agh.iisg.to.to2project.domain.*;
import pl.edu.agh.iisg.to.to2project.domain.entity.*;
import pl.edu.agh.iisg.to.to2project.domain.entity.InternalTransactionInverse;
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

    private ObservableList<? extends IInternalTransaction> internalTransactions;
    private ObservableList<? extends IExternalTransaction> externalTransactions;
    private ObservableList<? extends IInternalTransaction> internalTransactionInverses;
    private ObservableList<ITransaction> allTransactions;

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
    }
}
