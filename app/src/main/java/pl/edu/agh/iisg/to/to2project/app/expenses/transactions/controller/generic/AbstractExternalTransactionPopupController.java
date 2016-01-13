package pl.edu.agh.iisg.to.to2project.app.expenses.transactions.controller.generic;

import javafx.fxml.FXML;
import javafx.scene.control.ToggleGroup;
import org.joda.time.LocalDate;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import pl.edu.agh.iisg.to.to2project.app.expenses.common.nodes.ColorfulValidatingRadioButton;
import pl.edu.agh.iisg.to.to2project.app.expenses.common.nodes.ColorfulValidatingTextField;
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
    protected ColorfulValidatingTextField payeeTextField;

    @FXML
    protected ToggleGroup transferTypeGroup;

    @FXML
    protected ColorfulValidatingRadioButton incomeRadioButton;

    @FXML
    protected ColorfulValidatingRadioButton expenditureRadioButton;

    @FXML
    @Override
    public void initialize() {
        super.initialize();

        payeeTextField.setValidationSupplier(this::isPayeeValid);

        incomeRadioButton.setValidationSupplier(this::isTransferTypeValid);
        incomeRadioButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            expenditureRadioButton.triggerValidation();
        });

        expenditureRadioButton.setValidationSupplier(this::isTransferTypeValid);
        expenditureRadioButton.selectedProperty().addListener((observable, oldValue, newValue) -> {
            incomeRadioButton.triggerValidation();
        });
    }

    @Override
    protected boolean isInputValid() {
        payeeTextField.triggerValidation();
        incomeRadioButton.triggerValidation();
        expenditureRadioButton.triggerValidation();

        return super.isInputValid() && isTransferTypeValid();

    }

    private boolean isPayeeValid(){
        return !payeeTextField.getText().isEmpty();
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