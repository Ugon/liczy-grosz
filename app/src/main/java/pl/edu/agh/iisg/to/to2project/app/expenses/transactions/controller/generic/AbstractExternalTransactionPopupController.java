package pl.edu.agh.iisg.to.to2project.app.expenses.transactions.controller.generic;

import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import org.joda.time.LocalDate;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import pl.edu.agh.iisg.to.to2project.domain.entity.Account;
import pl.edu.agh.iisg.to.to2project.domain.entity.ExternalTransaction;

import java.math.BigDecimal;

/**
 * @author Wojciech Pachuta.
 */
@Controller
@Scope("prototype")
public abstract class AbstractExternalTransactionPopupController extends AbstractTransactionPopupController<ExternalTransaction>{

    @FXML
    protected TextField payeeTextField;

    @FXML
    protected ToggleGroup transferTypeGroup;

    @FXML
    protected RadioButton incomeRadioButton;

    @FXML
    protected RadioButton expenditureRadioButton;

    @Override
    protected boolean isInputValid() {
        if(!isTransferTypeValid()) {
            errorLabel.setText("You are not able to create transaction with this type of transaction.");
            return false;
        }

        return super.isInputValid();
    }

    private boolean isTransferTypeValid() {
        return transferTypeGroup.getSelectedToggle() != null;
    }

    @Override
    protected ExternalTransaction produceTransaction(Account destinationAccount, BigDecimal transfer, LocalDate date) {
        BigDecimal trans = transfer;
        if(expenditureRadioButton.isSelected()){
            trans = transfer.negate();
        }
        return produceInternalTransaction(payeeTextField.getText(), destinationAccount, trans, date);
    }

    protected abstract ExternalTransaction produceInternalTransaction(String sourceAccount, Account destinationAccount, BigDecimal transfer, LocalDate date);

}