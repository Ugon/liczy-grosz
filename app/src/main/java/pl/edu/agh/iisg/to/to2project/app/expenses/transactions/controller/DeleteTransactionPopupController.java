package pl.edu.agh.iisg.to.to2project.app.expenses.transactions.controller;

import com.google.common.base.Preconditions;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import pl.edu.agh.iisg.to.to2project.app.expenses.common.controller.PopupController;
import pl.edu.agh.iisg.to.to2project.domain.entity.ExternalTransaction;
import pl.edu.agh.iisg.to.to2project.domain.entity.InternalTransaction;
import pl.edu.agh.iisg.to.to2project.service.ExternalTransactionService;
import pl.edu.agh.iisg.to.to2project.service.InternalTransactionService;

/**
 * @author Bartłomiej Grochal
 */
@Component
@Scope("prototype")
public class DeleteTransactionPopupController extends PopupController {

    @Autowired
    private InternalTransactionService internalTransactionService;

    @Autowired
    private ExternalTransactionService externalTransactionService;

    @Autowired
    private TransactionsController transactionsController;

    private InternalTransaction internalTransaction;
    private ExternalTransaction externalTransaction;



    @FXML
    @Override
    protected void handleOKButtonClick(ActionEvent actionEvent) {
        if(externalTransaction != null) {
            externalTransactionService.remove(externalTransaction);
        }
        else if (internalTransaction != null) {
            internalTransactionService.remove(internalTransaction);
        }

        transactionsController.refreshContent();

        dialogStage.close();
    }

    public void deleteTransaction(InternalTransaction transaction) {
        Preconditions.checkNotNull(transaction);
        internalTransaction = transaction;
        showDialog();
    }

    public void deleteTransaction(ExternalTransaction transaction) {
        Preconditions.checkNotNull(transaction);
        externalTransaction = transaction;
        showDialog();
    }
}
