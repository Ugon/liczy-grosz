package pl.edu.agh.iisg.to.to2project.app.expenses.transactions.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import pl.edu.agh.iisg.to.to2project.app.expenses.common.controller.PopupController;
import pl.edu.agh.iisg.to.to2project.domain.entity.Account;
import pl.edu.agh.iisg.to.to2project.domain.entity.Category;
import pl.edu.agh.iisg.to.to2project.domain.entity.ExternalTransaction;
import pl.edu.agh.iisg.to.to2project.service.AccountService;
import pl.edu.agh.iisg.to.to2project.service.CategoryService;
import pl.edu.agh.iisg.to.to2project.service.ExternalTransactionService;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.time.ZoneId;
import java.util.Date;

import static java.math.BigDecimal.ZERO;
import static java.time.ZoneId.systemDefault;
import static java.util.Date.from;

/**
 * @author Bartłomiej Grochal
 */
@Controller
@Scope("prototype")
public class EditExternalTransactionPopupController extends PopupController {

    private static final Category NO_CATEGORY = new Category("None");

    @Autowired
    private ExternalTransactionService externalTransactionService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private TransactionsController transactionsController;

    @FXML
    private ComboBox<Account> destinationAccountCombo;

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
        destinationAccountCombo.setItems(accountService.getList());
        categoryCombo.getItems().addAll(categoryService.getList());
        categoryCombo.getItems().add(NO_CATEGORY);
        categoryCombo.setValue(NO_CATEGORY);

        destinationAccountCombo.valueProperty().addListener((observable, oldValue, newValue) -> errorLabel.setText(""));
        payeeTextField.textProperty().addListener((observable, oldValue, newValue) -> errorLabel.setText(""));

        errorLabel.setText("");

        decimalFormat = new DecimalFormat();
        decimalFormat.setParseBigDecimal(true);
    }

    @FXML
    @Override
    protected void handleOKButtonClick(ActionEvent actionEvent) {
        if(isInputValid()) {
            updateModel();
            closeDialog();
            transactionsController.refreshContent();
        }
    }

    public void editTransaction(ExternalTransaction transaction){
        this.editedTransaction = transaction;
        fillDialog();
        showDialog();
    }

    private void fillDialog() {
        destinationAccountCombo.setValue(editedTransaction.destinationAccountProperty().getValue());
        payeeTextField.setText(editedTransaction.sourcePayeeProperty().getValue());
        java.time.LocalDate localDate = editedTransaction.dateProperty().getValue().toDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        transactionDatePicker.setValue(localDate);
        categoryCombo.setValue(editedTransaction.categoryMonadicProperty().getOrElse(NO_CATEGORY));

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
        return destinationAccountCombo.getSelectionModel().getSelectedItem() != null &&
                !payeeTextField.getText().isEmpty() &&
                destinationAccountCombo.getSelectionModel().getSelectedItem().nameProperty().getValue() != payeeTextField.getText();
    }

    private boolean isTransferValueValid() {
        return transferTextField.getText().matches("^\\d+(?:.\\d+)?$");
    }

    private boolean isTransferTypeValid() {
        return transferTypeGroup.getSelectedToggle() != null;
    }

    private void updateModel() {
        Account sourceAccount = destinationAccountCombo.getSelectionModel().getSelectedItem();
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

        Date transferDateUtil =  from(transactionDatePicker.getValue().atStartOfDay().atZone(systemDefault()).toInstant());
        LocalDate transferDate = new LocalDate(transferDateUtil);

        String comment = commentTextArea.getText();

        editedTransaction.setDestinationAccount(sourceAccount);
        editedTransaction.setSource(payee);

        editedTransaction.removeCategoryIfPresent();
        if(!category.equals(NO_CATEGORY)){
            editedTransaction.setCategory(category);
        }
        editedTransaction.setDelta(transfer);
        editedTransaction.setDate(transferDate);

        editedTransaction.removeCommentIfPresent();
        if(!comment.isEmpty()) {
            editedTransaction.setComment(comment);
        }

        externalTransactionService.save(editedTransaction);
    }

}
