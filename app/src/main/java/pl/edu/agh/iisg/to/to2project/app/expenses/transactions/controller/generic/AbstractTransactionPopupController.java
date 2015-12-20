package pl.edu.agh.iisg.to.to2project.app.expenses.transactions.controller.generic;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import pl.edu.agh.iisg.to.to2project.app.expenses.common.controller.PopupController;
import pl.edu.agh.iisg.to.to2project.app.expenses.common.nodes.ColorfulValidatingComboBox;
import pl.edu.agh.iisg.to.to2project.app.expenses.common.nodes.ColorfulValidatingDatePicker;
import pl.edu.agh.iisg.to.to2project.app.expenses.common.nodes.ColorfulValidatingTextField;
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
    protected ColorfulValidatingComboBox<Account> destinationAccountCombo;

    @FXML
    protected ComboBox<Category> categoryCombo;

    @FXML
    protected ColorfulValidatingTextField transferTextField;

    @FXML
    protected ColorfulValidatingDatePicker datePicker;

    @FXML
    protected TextArea commentTextArea;

    private DecimalFormat decimalFormat;

    @FXML
    public void initialize() {
        decimalFormat = new DecimalFormat();
        decimalFormat.setParseBigDecimal(true);

        destinationAccountCombo.getItems().addAll(accountService.getList());
        destinationAccountCombo.setValidationSupplier(this::isDestinationAccountValid);

        categoryCombo.getItems().addAll(categoryService.getList());
        categoryCombo.getItems().add(NO_CATEGORY);
        categoryCombo.setValue(NO_CATEGORY);

        datePicker.setValidationSupplier(this::isDateValid);

        transferTextField.setValidationSupplier(this::isTransferValueValid);
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
        destinationAccountCombo.triggerValidation();
        transferTextField.triggerValidation();
        datePicker.triggerValidation();

        return isDateValid() && isDestinationAccountValid() && isTransferValueValid();
    }

    protected boolean isDestinationAccountValid() {
        return destinationAccountCombo.getValue() != null;
    }

    private boolean isTransferValueValid() {
        return transferTextField.getText().matches("^\\d+(?:.\\d+)?$");
    }

    private boolean isDateValid(){
        return datePicker.getValue() != null;
    }

    private void updateModel() {
        Account destinationAccount = destinationAccountCombo.getValue();

        BigDecimal transfer = null;
        try {
            transfer = (BigDecimal) decimalFormat.parse(transferTextField.getText());
        }
        catch (ParseException exc) {
            exc.printStackTrace();
        }

        Date dateUtil = from(datePicker.getValue().atStartOfDay().atZone(systemDefault()).toInstant());
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
