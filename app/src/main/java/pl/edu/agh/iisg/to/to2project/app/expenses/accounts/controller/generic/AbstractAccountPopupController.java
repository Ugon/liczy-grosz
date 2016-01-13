package pl.edu.agh.iisg.to.to2project.app.expenses.accounts.controller.generic;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import pl.edu.agh.iisg.to.to2project.app.expenses.common.controller.PopupController;
import pl.edu.agh.iisg.to.to2project.app.expenses.common.nodes.ColorfulValidatingTextField;
import pl.edu.agh.iisg.to.to2project.domain.entity.Account;
import pl.edu.agh.iisg.to.to2project.service.AccountService;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;

/**
 * @author Wojciech Pachuta.
 */
@Controller
@Scope("prototype")
public abstract class AbstractAccountPopupController extends PopupController {

    @Autowired
    private AccountService accountService;

    @FXML
    protected ColorfulValidatingTextField nameTextField;

    @FXML
    protected ColorfulValidatingTextField initialBalanceTextField;

    private DecimalFormat decimalFormat;

    private ObservableList<Account> currentAccounts;

    @FXML
    public void initialize(){
        decimalFormat = new DecimalFormat();
        decimalFormat.setParseBigDecimal(true);

        currentAccounts = accountService.getList();

        nameTextField.setValidationSupplier(this::isNameValid);

        initialBalanceTextField.setValidationSupplier(this::isInitialBalanceValid);
    }

    @FXML
    @Override
    protected void handleOKButtonClick(ActionEvent actionEvent) {
        if(isInputValid()) {
            updateModel();
            closeDialog();
        }
    }

    protected boolean isInputValid() {
        nameTextField.triggerValidation();
        initialBalanceTextField.triggerValidation();

        return isNameValid() && isInitialBalanceValid();
    }

    protected boolean isNameValid() {
        return !nameTextField.getText().isEmpty() &&
                currentAccounts.filtered(acc -> acc.nameProperty().get().equals(nameTextField.getText())).isEmpty();
    }

    private boolean isInitialBalanceValid() {
        return initialBalanceTextField.getText().matches("^\\-?\\d+(?:.\\d+)?$");
    }

    private void updateModel() {
        String name = nameTextField.getText();

        BigDecimal initialBalance = null;
        try {
            initialBalance = (BigDecimal) decimalFormat.parse(initialBalanceTextField.getText());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Account account = produceAccount(name, initialBalance);

        accountService.save(account);
    }

    protected abstract Account produceAccount(String name, BigDecimal initialBalance);
}
