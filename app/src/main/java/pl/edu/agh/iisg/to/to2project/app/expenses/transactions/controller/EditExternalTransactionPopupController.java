package pl.edu.agh.iisg.to.to2project.app.expenses.transactions.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import pl.edu.agh.iisg.to.to2project.app.expenses.common.controller.PopupController;
import pl.edu.agh.iisg.to.to2project.domain.AbstractTransaction;
import pl.edu.agh.iisg.to.to2project.domain.Account;
import pl.edu.agh.iisg.to.to2project.domain.Category;
import pl.edu.agh.iisg.to.to2project.domain.ExternalTransaction;
import pl.edu.agh.iisg.to.to2project.service.AccountService;
import pl.edu.agh.iisg.to.to2project.service.CategoryService;
import pl.edu.agh.iisg.to.to2project.service.ExternalTransactionService;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Date;

import static java.math.BigDecimal.ZERO;
import static java.time.LocalTime.now;
import static java.time.ZoneId.systemDefault;
import static java.util.Date.from;

/**
 * @author Bartłomiej Grochal
 */
@Component
@Scope("prototype")
public class EditExternalTransactionPopupController extends PopupController {

    private static final Category NO_CATEGORY = new Category("None");

    @Autowired
    private ExternalTransactionService externalTransactionService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private CategoryService categoryService;

    @FXML
    private ComboBox<Account> accountNameCombo;

    @FXML
    private TextField payeeTextField;

    @FXML
    private ComboBox<Category> categoryCombo;

    @FXML
    private TextField transferTextField;

    @FXML
    private ToggleGroup transferTypeGroup;

    @FXML
    private RadioButton incomeRadioButton;

    @FXML
    private RadioButton expenditureRadioButton;

    @FXML
    private DatePicker transactionDatePicker;

    @FXML
    private TextArea commentTextArea;

    @FXML
    private Text errorLabel;
    private DecimalFormat decimalFormat;
    private ExternalTransaction editedTransaction;



    @FXML
    public void initialize() {
        accountNameCombo.setItems(accountService.getList());
        categoryCombo.getItems().addAll(categoryService.getList());
        categoryCombo.getItems().add(NO_CATEGORY);

        accountNameCombo.valueProperty().addListener(new AccountChangeListener<>());
        payeeTextField.textProperty().addListener(new AccountChangeListener<>());
        errorLabel.setText("");

        decimalFormat = new DecimalFormat();
        decimalFormat.setParseBigDecimal(true);
    }


    @FXML
    @Override
    protected void handleOKButtonClick(ActionEvent actionEvent) {
    }

    public void handleOKButtonClick(ActionEvent actionEvent, AbstractTransaction editedTransaction) {
        this.editedTransaction = (ExternalTransaction) editedTransaction;

        if(isInputValid()) {
            updateModel();
        }
    }

    private boolean isInputValid() {
        if(!isAccountValid()) {
            errorLabel.setText("You are not able to create transaction between these accounts.");
            return false;
        }
        if(!isTransferValueValid()) {
            errorLabel.setText("You are not able to create transaction with given amount of money.");
            return false;
        }
        if(!isTransferTypeValid()) {
            errorLabel.setText("You are not able to create transaction with this type of transaction.");
            return false;
        }

        return true;
    }

    private boolean isAccountValid() {
        return accountNameCombo.getSelectionModel().getSelectedItem() != null &&
                !payeeTextField.getText().isEmpty() &&
                accountNameCombo.getSelectionModel().getSelectedItem().nameProperty().getValue() != payeeTextField.getText();
    }

    private boolean isTransferValueValid() {
        return transferTextField.getText().matches("^\\-?\\d+(?:.\\d+)?$");
    }

    private boolean isTransferTypeValid() {
        return transferTypeGroup.getSelectedToggle() != null;
    }

    private void updateModel() {
        Account sourceAccount = accountNameCombo.getSelectionModel().getSelectedItem();
        String payee = payeeTextField.getText();
        Category category = categoryCombo.getSelectionModel().getSelectedItem();

        BigDecimal transfer = null;
        try {
            transfer = (BigDecimal) decimalFormat.parse(transferTextField.getText());
        }
        catch (ParseException exc) {
            //todo: what next?
            exc.printStackTrace();
        }

        if(transferTypeGroup.getSelectedToggle().equals(incomeRadioButton)) {
            transfer = transfer.compareTo(ZERO) >= 0 ? transfer : transfer.multiply(new BigDecimal(-1));
        }
        else if(transferTypeGroup.getSelectedToggle().equals(expenditureRadioButton)) {
            transfer = transfer.compareTo(ZERO) <= 0 ? transfer : transfer.multiply(new BigDecimal(-1));
        }

        Date transferDateUtil =  from(transactionDatePicker.getValue().atTime(now()).atZone(systemDefault()).toInstant());
        DateTime transferDateTime = new DateTime(transferDateUtil);

        String comment = commentTextArea.getText();

        editedTransaction.setDestinationAccount(sourceAccount);
        editedTransaction.setSource(payee);

        editedTransaction.removeCategoryIfPresent();
        if(!category.equals(NO_CATEGORY)){
            editedTransaction.setCategory(category);
        }
        editedTransaction.setDelta(transfer);
        editedTransaction.setDateTime(transferDateTime);

        editedTransaction.removeCommentIfPresent();
        if(!comment.isEmpty()) {
            editedTransaction.setComment(comment);
        }

        externalTransactionService.save(editedTransaction);
    }



    private class AccountChangeListener<T> implements ChangeListener<T> {
        @Override
        public void changed(ObservableValue<? extends T> observable, T oldValue, T newValue) {
            errorLabel.setText("");
        }
    }
}
