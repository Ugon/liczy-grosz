package pl.edu.agh.iisg.to.to2project.app.expenses.transactions.controller.generic;

import javafx.fxml.FXML;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import pl.edu.agh.iisg.to.to2project.app.expenses.common.nodes.ColorfulValidatingComboBox;
import pl.edu.agh.iisg.to.to2project.domain.entity.Account;
import pl.edu.agh.iisg.to.to2project.domain.entity.InternalTransaction;
import pl.edu.agh.iisg.to.to2project.service.AccountService;

import java.math.BigDecimal;

/**
 * @author Wojciech Pachuta.
 */
@Controller
@Scope("prototype")
public abstract class AbstractInternalTransactionPopupController extends AbstractTransactionPopupController<InternalTransaction> {

    @Autowired
    private AccountService accountService;

    @FXML
    protected ColorfulValidatingComboBox<Account> sourceAccountCombo;

    @FXML
    @Override
    public void initialize() {
        super.initialize();

        sourceAccountCombo.getItems().addAll(accountService.getList());

        destinationAccountCombo.setValidationSupplier(this::isDestinationAccountValid);
        destinationAccountCombo.valueProperty().addListener((observable, oldValue, newValue) -> {
            sourceAccountCombo.triggerValidation();
        });

        sourceAccountCombo.setValidationSupplier(this::isSourceAccountValid);
        sourceAccountCombo.valueProperty().addListener((observable, oldValue, newValue) -> {
            destinationAccountCombo.triggerValidation();
        });
    }

    @Override
    protected boolean isInputValid() {
        sourceAccountCombo.triggerValidation();

        return super.isInputValid() && isSourceAccountValid();
    }

    @Override
    protected boolean isDestinationAccountValid() {
        return destinationAccountCombo.getValue() != null &&
                !sourceAccountCombo.getValue().equals(destinationAccountCombo.getValue());
    }

    private boolean isSourceAccountValid() {
        return sourceAccountCombo.getValue() != null &&
                !sourceAccountCombo.getValue().equals(destinationAccountCombo.getValue());
    }

    @Override
    protected InternalTransaction produceTransaction(Account destinationAccount, BigDecimal transfer, LocalDate date) {
        return produceInternalTransaction(sourceAccountCombo.getValue(), destinationAccount, transfer, date);
    }

    protected abstract InternalTransaction produceInternalTransaction(Account sourceAccount, Account destinationAccount, BigDecimal transfer, LocalDate date);

}
