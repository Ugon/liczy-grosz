package pl.edu.agh.iisg.to.to2project.app.expenses.transactions.controller;

import org.joda.time.LocalDate;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import pl.edu.agh.iisg.to.to2project.app.expenses.transactions.controller.generic.AbstractExternalTransactionPopupController;
import pl.edu.agh.iisg.to.to2project.domain.entity.Account;
import pl.edu.agh.iisg.to.to2project.domain.entity.ExternalTransaction;

import java.math.BigDecimal;
import java.time.ZoneId;

/**
 * @author Bartłomiej Grochal
 * @author Wojciech Pachuta
 */
@Controller
@Scope("prototype")
public class EditExternalTransactionPopupController extends AbstractExternalTransactionPopupController {

    private ExternalTransaction editedTransaction;

    @Override
    protected ExternalTransaction produceInternalTransaction(String sourceAccount, Account destinationAccount, BigDecimal transfer, LocalDate date) {
        editedTransaction.setSource(sourceAccount);
        editedTransaction.setDestinationAccount(destinationAccount);
        editedTransaction.setDelta(transfer);
        editedTransaction.setDate(date);
        return editedTransaction;
    }

    private void fillDialog() {
        payeeTextField.setText(editedTransaction.sourcePayeeProperty().getValue());
        destinationAccountCombo.setValue(editedTransaction.destinationAccountProperty().getValue());
        java.time.LocalDate localDate = editedTransaction.dateProperty().getValue().toDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        datePicker.setValue(localDate);
        categoryCombo.setValue(editedTransaction.categoryMonadicProperty().getOrElse(NO_CATEGORY));
        commentTextArea.setText(editedTransaction.commentMonadicProperty().getOrElse(""));

        BigDecimal transfer = editedTransaction.deltaProperty().getValue();
        if(transfer.compareTo(BigDecimal.ZERO) >= 0){
            incomeRadioButton.fire();
        }
        else {
            expenditureRadioButton.fire();
            transfer = transfer.negate();
        }
        transferTextField.setText(transfer.toString());
    }

    public void editTransaction(ExternalTransaction transaction){
        this.editedTransaction = transaction;
        fillDialog();
        showDialog();
    }
}
