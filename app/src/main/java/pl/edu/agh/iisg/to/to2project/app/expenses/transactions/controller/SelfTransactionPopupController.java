package pl.edu.agh.iisg.to.to2project.app.expenses.transactions.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import pl.edu.agh.iisg.to.to2project.app.expenses.common.controller.PopupController;
import pl.edu.agh.iisg.to.to2project.domain.Account;
import pl.edu.agh.iisg.to.to2project.domain.Category;
import pl.edu.agh.iisg.to.to2project.domain.InternalTransaction;
import pl.edu.agh.iisg.to.to2project.service.AccountService;
import pl.edu.agh.iisg.to.to2project.service.CategoryService;
import pl.edu.agh.iisg.to.to2project.service.InternalTransactionService;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Date;

import static java.time.LocalTime.now;
import static java.time.ZoneId.systemDefault;
import static java.util.Date.from;

/**
 * @author Bartłomiej Grochal
 */
@Controller
@Scope("prototype")
public class SelfTransactionPopupController extends PopupController {

    @Autowired
    private InternalTransactionService internalTransactionService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private AccountService accountService;

    @FXML
    private ComboBox<Account> sourceAccountCombo;

    @FXML
    private ComboBox<Account> targetAccountCombo;

    @FXML
    private ComboBox<Category> categoryCombo;

    @FXML
    private TextField transferTextField;

    @FXML
    private DatePicker transferDatePicker;

    @FXML
    private TextArea commentTextArea;

    private DecimalFormat decimalFormat;

    private InternalTransaction newTransaction;

    private static final Category NO_CATEGORY = new Category("None");


    @FXML
    public void initialize() {
        sourceAccountCombo.getItems().addAll(accountService.getList());
        targetAccountCombo.getItems().addAll(accountService.getList());
        categoryCombo.getItems().addAll(categoryService.getList());
        categoryCombo.getItems().add(NO_CATEGORY);
        categoryCombo.setValue(NO_CATEGORY);

        decimalFormat = new DecimalFormat();
        decimalFormat.setParseBigDecimal(true);
    }

    @FXML
    @Override
    protected void handleOKButtonClick(ActionEvent actionEvent) {
        if(isInputValid()) {
            updateModel();
            dialogStage.close();
        }
    }

    private boolean isInputValid() {
        return sourceAccountCombo.getSelectionModel().getSelectedItem() != null &&
                targetAccountCombo.getSelectionModel().getSelectedItem() != null &&
                !sourceAccountCombo.getSelectionModel().getSelectedItem().equals(targetAccountCombo.getSelectionModel().getSelectedItem());
    }

    private void updateModel() {
        Account sourceAccount = sourceAccountCombo.getSelectionModel().getSelectedItem();
        Account destinationAccount = targetAccountCombo.getSelectionModel().getSelectedItem();
        Category category = categoryCombo.getSelectionModel().getSelectedItem();

        BigDecimal transfer = null;
        try {
            transfer = (BigDecimal) decimalFormat.parse(transferTextField.getText());
        }
        catch (ParseException exc) {
            exc.printStackTrace();
        }

        Date transferDateUtil =  from(transferDatePicker.getValue().atTime(now()).atZone(systemDefault()).toInstant());
        DateTime transferDateTime = new DateTime(transferDateUtil);

        String comment = commentTextArea.getText();

        newTransaction = new InternalTransaction(sourceAccount, destinationAccount, transfer, transferDateTime);
        if (!category.equals(NO_CATEGORY)) {
            newTransaction.setCategory(category);
        }
        if (!comment.isEmpty()) {
            newTransaction.setComment(comment);
        }

        internalTransactionService.save(newTransaction);
    }

    public void addSelfTransaction() {
        showDialog();
    }
}
