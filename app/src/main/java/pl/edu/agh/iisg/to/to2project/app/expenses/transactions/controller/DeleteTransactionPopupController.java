package pl.edu.agh.iisg.to.to2project.app.expenses.transactions.controller;

import com.google.common.base.Preconditions;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import pl.edu.agh.iisg.to.to2project.app.expenses.common.controller.PopupController;
import pl.edu.agh.iisg.to.to2project.domain.entity.ExternalTransaction;
import pl.edu.agh.iisg.to.to2project.domain.entity.InternalTransaction;
import pl.edu.agh.iisg.to.to2project.service.ExternalTransactionService;
import pl.edu.agh.iisg.to.to2project.service.InternalTransactionService;

/**
 * @author Bartłomiej Grochal
 * @author Wojciech Pachuta
 */
@Controller
@Scope("prototype")
public class DeleteTransactionPopupController extends PopupController {

    @Autowired
    private InternalTransactionService internalTransactionService;

    @Autowired
    private ExternalTransactionService externalTransactionService;

    @Autowired
    private TransactionsController transactionsController;

    @FXML
    public Text errorLabel;

    private InternalTransaction internalTransaction;

    private ExternalTransaction externalTransaction;

    @FXML
    @Override
    protected void handleOKButtonClick(ActionEvent actionEvent) {
        boolean removed = false;
        if(externalTransaction != null) {
            if(externalTransactionService.canDelete(externalTransaction)) {
                externalTransactionService.remove(externalTransaction);
                removed = true;
            }
        }
        else if (internalTransaction != null) {
            if(internalTransactionService.canDelete(internalTransaction)) {
                internalTransactionService.remove(internalTransaction);
                removed = true;
            }
        }

        if(removed) {
            transactionsController.refreshContent();
            closeDialog();
        }
        else{
            errorLabel.setText("Sorry, you can't delete this transaction.");
        }
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
