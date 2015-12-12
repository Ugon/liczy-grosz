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
import pl.edu.agh.iisg.to.to2project.app.core.utils.ObservableMerge;
import pl.edu.agh.iisg.to.to2project.app.expenses.transactions.view.DeleteTransactionPopup;
import pl.edu.agh.iisg.to.to2project.app.expenses.transactions.view.EditTransactionPopup;
import pl.edu.agh.iisg.to.to2project.app.expenses.transactions.view.NewExternalTransactionPopup;
import pl.edu.agh.iisg.to.to2project.app.expenses.transactions.view.NewInternalTransactionPopup;
import pl.edu.agh.iisg.to.to2project.domain.*;
import pl.edu.agh.iisg.to.to2project.service.ExternalTransactionService;
import pl.edu.agh.iisg.to.to2project.service.InternalTransactionService;

import java.math.BigDecimal;

import static javafx.scene.control.SelectionMode.SINGLE;

/**
 * @author Bartłomiej Grochal
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
    private TableView<AbstractTransaction> transactionsTable;

    @FXML
    private TableColumn<AbstractTransaction, String> nameColumn;

    @FXML
    private TableColumn<AbstractTransaction, BigDecimal> transferColumn;

    @FXML
    private TableColumn<AbstractTransaction, BigDecimal> balanceColumn;

    @FXML
    private TableColumn<AbstractTransaction, DateTime> dateColumn;

    @FXML
    private TableColumn<AbstractTransaction, String> categoryColumn;

    @FXML
    private TableColumn<AbstractTransaction, String> payeeColumn;

    @FXML
    private TableColumn<AbstractTransaction, String> commentColumn;

    private ObservableList<InternalTransaction> internalTransactions;
    private ObservableList<ExternalTransaction> externalTransactions;
    private ObservableList<AbstractTransaction> allTransactions;

    @FXML
    public void initialize() {
        internalTransactions = internalTransactionService.getList();
        externalTransactions = externalTransactionService.getList();
        allTransactions = ObservableMerge.merge(internalTransactions, externalTransactions);

        transactionsTable.setItems(allTransactions);

        transactionsTable.getSelectionModel().setSelectionMode(SINGLE);
        nameColumn.setCellValueFactory(dataValue -> EasyBind.monadic(dataValue.getValue().destinationAccountProperty()).flatMap(Account::nameProperty));
        transferColumn.setCellValueFactory(dataValue -> dataValue.getValue().deltaProperty());

        //todo:that aint gonna work. not bound properly, also should be current balance, not initial balance
        balanceColumn.setCellValueFactory(dataValue -> dataValue.getValue().destinationAccountProperty().getValue().initialBalanceProperty());

        dateColumn.setCellValueFactory(dataValue -> dataValue.getValue().dateTimeProperty());
        categoryColumn.setCellValueFactory(dataValue -> dataValue.getValue().categoryMonadicProperty().flatMap(Category::nameProperty));
        payeeColumn.setCellValueFactory(dataValue -> dataValue.getValue().sourcePropertyAsMonadicString());
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

        AbstractTransaction selectedTransaction = transactionsTable.getSelectionModel().getSelectedItem();
        if(selectedTransaction != null) {
            controller.editTransaction(selectedTransaction);
        }
    }

    @FXML
    private void handleDeleteTransactionClick(ActionEvent actionEvent) {
        DeleteTransactionPopup popup = context.getBean(DeleteTransactionPopup.class);
        DeleteTransactionPopupController controller = popup.getController();

        AbstractTransaction selectedTransaction = transactionsTable.getSelectionModel().getSelectedItem();
        if(selectedTransaction != null) {
            controller.deleteTransaction(selectedTransaction);
        }
    }

    public void refreshContent() {
        externalTransactionService.refreshCache();
        internalTransactionService.refreshCache();
    }
}
