package pl.edu.agh.iisg.to.to2project.app.expenses.transactions.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import pl.edu.agh.iisg.to.to2project.app.expenses.common.controller.PopupController;
import pl.edu.agh.iisg.to.to2project.domain.AbstractTransaction;
import pl.edu.agh.iisg.to.to2project.domain.ExternalTransaction;
import pl.edu.agh.iisg.to.to2project.domain.InternalTransaction;
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

    private AbstractTransaction deletedTransaction;



    @FXML
    @Override
    protected void handleOKButtonClick(ActionEvent actionEvent) {
        if(deletedTransaction instanceof ExternalTransaction) {
            externalTransactionService.remove((ExternalTransaction) deletedTransaction);
        }
        else if (deletedTransaction instanceof InternalTransaction) {
            internalTransactionService.remove((InternalTransaction) deletedTransaction);
        }

        dialogStage.close();
    }

    public void deleteTransaction(AbstractTransaction transaction) {
        deletedTransaction = transaction;
        showDialog();
    }
}
