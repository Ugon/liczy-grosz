package pl.edu.agh.iisg.to.to2project.app.expenses.transactions.controller;

import org.joda.time.LocalDate;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import pl.edu.agh.iisg.to.to2project.app.expenses.transactions.controller.generic.AbstractInternalTransactionPopupController;
import pl.edu.agh.iisg.to.to2project.domain.entity.Account;
import pl.edu.agh.iisg.to.to2project.domain.entity.InternalTransaction;

import java.math.BigDecimal;
import java.time.ZoneId;

/**
 * @author Bartłomiej Grochal
 * @author Wojciech Pachuta
 */
@Controller
@Scope("prototype")
public class EditInternalTransactionPopupController extends AbstractInternalTransactionPopupController {

    private InternalTransaction editedTransaction;

    @Override
    protected InternalTransaction produceInternalTransaction(Account sourceAccount, Account destinationAccount, BigDecimal transfer, LocalDate date) {
        editedTransaction.setSourceAccount(sourceAccount);
        editedTransaction.setDestinationAccount(destinationAccount);
        editedTransaction.setDelta(transfer);
        editedTransaction.setDate(date);
        return editedTransaction;
    }

    private void fillDialog(){
        sourceAccountCombo.setValue(editedTransaction.sourceAccountProperty().getValue());
        destinationAccountCombo.setValue(editedTransaction.destinationAccountProperty().getValue());
        transferTextField.setText(editedTransaction.deltaProperty().getValue().toString());
        java.time.LocalDate localDate = editedTransaction.dateProperty().getValue().toDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        datePicker.setValue(localDate);
        categoryCombo.setValue(editedTransaction.categoryMonadicProperty().getOrElse(NO_CATEGORY));
        commentTextArea.setText(editedTransaction.commentMonadicProperty().getOrElse(""));
    }

    public void editTransaction(InternalTransaction transaction){
        this.editedTransaction = transaction;
        fillDialog();
        showDialog();
    }

}