package pl.edu.agh.iisg.to.to2project.app.expenses.transactions.controller.generic;

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
import pl.edu.agh.iisg.to.to2project.app.expenses.transactions.controller.TransactionsController;
import pl.edu.agh.iisg.to.to2project.domain.entity.AbstractTransaction;
import pl.edu.agh.iisg.to.to2project.domain.entity.Account;
import pl.edu.agh.iisg.to.to2project.domain.entity.Category;
import pl.edu.agh.iisg.to.to2project.service.AccountService;
import pl.edu.agh.iisg.to.to2project.service.CategoryService;
import pl.edu.agh.iisg.to.to2project.service.generic.CRUDService;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Date;

import static java.time.ZoneId.systemDefault;
import static java.util.Date.from;

/**
 * @author Wojciech Pachuta.
 */
@Controller
@Scope("prototype")
public abstract class AbstractTransactionPopupController<T extends AbstractTransaction> extends PopupController{

    protected static final Category NO_CATEGORY = new Category("None");

    @Autowired
    private TransactionsController transactionsController;

    @Autowired
    private CRUDService<T, Long> transactionService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private AccountService accountService;

    @FXML
    protected ComboBox<Account> destinationAccountCombo;

    @FXML
    protected ComboBox<Category> categoryCombo;

    @FXML
    protected TextField transferTextField;

    @FXML
    protected DatePicker transactionDatePicker;

    @FXML
    protected TextArea commentTextArea;

    @FXML
    protected Text errorLabel;

    private DecimalFormat decimalFormat;

    @FXML
    public void initialize() {
        decimalFormat = new DecimalFormat();
        decimalFormat.setParseBigDecimal(true);

        destinationAccountCombo.getItems().addAll(accountService.getList());
        destinationAccountCombo.valueProperty().addListener((observable, oldValue, newValue) -> errorLabel.setText(""));

        categoryCombo.getItems().addAll(categoryService.getList());
        categoryCombo.getItems().add(NO_CATEGORY);
        categoryCombo.setValue(NO_CATEGORY);

        errorLabel.setText("");
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

    protected boolean isInputValid() {
        if(!isDestinationAccountValid()) {
            errorLabel.setText("You are not able to create transaction between these accounts.");
            return false;
        }
        if(!isTransferValueValid()) {
            errorLabel.setText("You are not able to create transaction with given amount of money.");
            return false;
        }

        return true;
    }

    protected boolean isDestinationAccountValid() {
        return destinationAccountCombo.getSelectionModel().getSelectedItem() != null;
    }

    private boolean isTransferValueValid() {
        return transferTextField.getText().matches("^\\d+(?:.\\d+)?$") &&
                decimalFormat.isParseBigDecimal();
    }

    private void updateModel() {
        Account destinationAccount = destinationAccountCombo.getSelectionModel().getSelectedItem();

        BigDecimal transfer = null;
        try {
            transfer = (BigDecimal) decimalFormat.parse(transferTextField.getText());
        }
        catch (ParseException exc) {
            exc.printStackTrace();
        }

        Date dateUtil = from(transactionDatePicker.getValue().atStartOfDay().atZone(systemDefault()).toInstant());
        LocalDate date = new LocalDate(dateUtil);

        T transaction = produceTransaction(destinationAccount, transfer, date);

        Category category = categoryCombo.getSelectionModel().getSelectedItem();
        if (!category.equals(NO_CATEGORY)) {
            transaction.setCategory(category);
        }

        String comment = commentTextArea.getText();
        if (!comment.isEmpty()) {
            transaction.setComment(comment);
        }

        transactionService.save(transaction);
    }

    protected abstract T produceTransaction(Account destinationAccount, BigDecimal transfer, LocalDate date);

}
