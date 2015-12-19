package pl.edu.agh.iisg.to.to2project.app.expenses.transactions.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import pl.edu.agh.iisg.to.to2project.app.expenses.common.controller.PopupController;
import pl.edu.agh.iisg.to.to2project.domain.entity.Account;
import pl.edu.agh.iisg.to.to2project.domain.entity.Category;
import pl.edu.agh.iisg.to.to2project.domain.entity.InternalTransaction;
import pl.edu.agh.iisg.to.to2project.service.AccountService;
import pl.edu.agh.iisg.to.to2project.service.CategoryService;
import pl.edu.agh.iisg.to.to2project.service.InternalTransactionService;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.time.ZoneId;
import java.util.Date;

import static java.time.ZoneId.systemDefault;
import static java.util.Date.from;

/**
 * @author Bartłomiej Grochal
 */
@Controller
@Scope("prototype")
public class EditInternalTransactionPopupController extends PopupController {

    private static final Category NO_CATEGORY = new Category("None");

    @Autowired
    private InternalTransactionService internalTransactionService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private TransactionsController transactionsController;

    @FXML
    private ComboBox<Account> sourceAccountCombo;

    @FXML
    private ComboBox<Account> destinationAccountCombo;

    @FXML
    private ComboBox<Category> categoryCombo;

    @FXML
    private TextField transferTextField;

    @FXML
    private DatePicker transactionDatePicker;

    @FXML
    private TextArea commentTextArea;

    @FXML
    private Text errorLabel;

    private DecimalFormat decimalFormat;
    private InternalTransaction editedTransaction;

    @FXML
    public void initialize() {
        sourceAccountCombo.setItems(accountService.getList());
        destinationAccountCombo.setItems(accountService.getList());
        categoryCombo.getItems().addAll(categoryService.getList());
        categoryCombo.getItems().add(NO_CATEGORY);
        categoryCombo.setValue(NO_CATEGORY);

        sourceAccountCombo.valueProperty().addListener((observable, oldValue, newValue) -> errorLabel.setText(""));
        destinationAccountCombo.valueProperty().addListener((observable, oldValue, newValue) -> errorLabel.setText(""));

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

    public void editTransaction(InternalTransaction transaction){
        this.editedTransaction = transaction;
        fillDialog();
        showDialog();
    }

    private void fillDialog(){
        sourceAccountCombo.setValue(editedTransaction.sourceAccountProperty().getValue());
        destinationAccountCombo.setValue(editedTransaction.destinationAccountProperty().getValue());
        transferTextField.setText(editedTransaction.deltaProperty().getValue().toString());
        java.time.LocalDate localDate = editedTransaction.dateProperty().getValue().toDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        transactionDatePicker.setValue(localDate);
        categoryCombo.setValue(editedTransaction.categoryMonadicProperty().getOrElse(NO_CATEGORY));
        commentTextArea.setText(editedTransaction.commentMonadicProperty().getOrElse(""));
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

        return true;
    }

    private boolean isAccountValid() {
        return sourceAccountCombo.getSelectionModel().getSelectedItem() != null &&
                destinationAccountCombo.getSelectionModel().getSelectedItem() != null &&
                !sourceAccountCombo.getSelectionModel().getSelectedItem().equals(destinationAccountCombo.getSelectionModel().getSelectedItem());
    }

    private boolean isTransferValueValid() {
        return transferTextField.getText().matches("^\\d+(?:.\\d+)?$");
    }

    private void updateModel() {
        Account sourceAccount = sourceAccountCombo.getSelectionModel().getSelectedItem();
        Account destinationAccount = destinationAccountCombo.getSelectionModel().getSelectedItem();
        Category category = categoryCombo.getSelectionModel().getSelectedItem();

        BigDecimal transfer = null;
        try {
            transfer = (BigDecimal) decimalFormat.parse(transferTextField.getText());
        }
        catch (ParseException exc) {
            //todo: what next?
            exc.printStackTrace();
        }

        Date transferDateUtil =  from(transactionDatePicker.getValue().atStartOfDay().atZone(systemDefault()).toInstant());
        LocalDate transferDate = new LocalDate(transferDateUtil);

        String comment = commentTextArea.getText();

        editedTransaction.setSourceAccount(sourceAccount);
        editedTransaction.setDestinationAccount(destinationAccount);

        editedTransaction.removeCategoryIfPresent();
        if(!category.equals(NO_CATEGORY)) {
            editedTransaction.setCategory(category);
        }

        editedTransaction.setDelta(transfer);
        editedTransaction.setDate(transferDate);

        editedTransaction.removeCommentIfPresent();
        if(!comment.isEmpty()) {
            editedTransaction.setComment(comment);
        }

        internalTransactionService.save(editedTransaction);
    }

}
