package pl.edu.agh.iisg.to.to2project.app.expenses.transactions.controller;

import javafx.beans.property.ReadOnlyListWrapper;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import pl.edu.agh.iisg.to.to2project.app.expenses.transactions.view.DeleteTransactionPopup;
import pl.edu.agh.iisg.to.to2project.app.expenses.transactions.view.EditTransactionPopup;
import pl.edu.agh.iisg.to.to2project.app.expenses.transactions.view.ExternalTransactionPopup;
import pl.edu.agh.iisg.to.to2project.app.expenses.transactions.view.SelfTransactionPopup;
import pl.edu.agh.iisg.to.to2project.domain.*;
import pl.edu.agh.iisg.to.to2project.service.ExternalTransactionService;
import pl.edu.agh.iisg.to.to2project.service.InternalTransactionService;

import java.math.BigDecimal;
import java.util.Optional;

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
    private TableColumn<AbstractTransaction, Optional<String>> commentColumn;

    private ObservableList<AbstractTransaction> data;


    @FXML
    public void initialize() {
        data = new ReadOnlyListWrapper<>();
        data.addAll(externalTransactionService.getList());
        data.addAll(internalTransactionService.getList());
        transactionsTable.setItems(data);

        transactionsTable.getSelectionModel().setSelectionMode(SINGLE);

        nameColumn.setCellValueFactory(dataValue -> dataValue.getValue().destinationAccountProperty().getValue().nameProperty());
        transferColumn.setCellValueFactory(dataValue -> dataValue.getValue().deltaProperty());
        balanceColumn.setCellValueFactory(dataValue -> dataValue.getValue().destinationAccountProperty().getValue().initialBalanceProperty());
        dateColumn.setCellValueFactory(dataValue -> dataValue.getValue().dateTimeProperty());
        categoryColumn.setCellValueFactory(dataValue -> dataValue.getValue().categoryProperty().getValue().get().nameProperty());
        payeeColumn.setCellValueFactory(
                dataValue -> dataValue.getValue() instanceof ExternalTransaction ?
                        ((ExternalTransaction) dataValue.getValue()).sourceProperty() :
                        ((InternalTransaction) dataValue.getValue()).sourceAccountProperty().getValue().nameProperty()
        );
        commentColumn.setCellValueFactory(dataValue -> dataValue.getValue().commentProperty());
    }


    @FXML
    private void handleSelfTransactionClick(ActionEvent actionEvent) {
        SelfTransactionPopup popup = context.getBean(SelfTransactionPopup.class);
        SelfTransactionPopupController controller = popup.getController();

        controller.addSelfTransaction();
    }

    @FXML
    private void handleExternalTransactionClick(ActionEvent actionEvent) {
        ExternalTransactionPopup popup = context.getBean(ExternalTransactionPopup.class);
        ExternalTransactionPopupController controller = popup.getController();

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
}
