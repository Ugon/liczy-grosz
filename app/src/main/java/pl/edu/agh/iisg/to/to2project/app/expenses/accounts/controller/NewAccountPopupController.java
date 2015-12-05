package pl.edu.agh.iisg.to.to2project.app.expenses.accounts.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import pl.edu.agh.iisg.to.to2project.app.expenses.common.controller.PopupController;
import pl.edu.agh.iisg.to.to2project.domain.Account;
import pl.edu.agh.iisg.to.to2project.service.AccountService;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;

/**
 * @author Bartłomiej Grochal
 * @author Wojciech Pachuta
 */
@Controller
@Scope("prototype")
public class NewAccountPopupController extends PopupController {

    @Autowired
    private AccountService accountService;

    private DecimalFormat decimalFormat;

    private Account newAccount;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField initialBalanceTextField;

    @FXML
    public void initialize(){
        decimalFormat = new DecimalFormat();
        decimalFormat.setParseBigDecimal(true);
    }

    @FXML
    @Override
    protected void handleOKButtonClick(ActionEvent actionEvent) {
        if(isInputValid()){
            updateModel();
        }
        dialogStage.close();
    }

    private boolean isInputValid(){
        return true;
    }

    private void updateModel(){
        String name = nameTextField.getText();
        BigDecimal initialBalance = null;
        try {
            initialBalance = (BigDecimal) decimalFormat.parse(initialBalanceTextField.getText());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        newAccount = new Account(name, initialBalance);
        accountService.save(newAccount);
    }

    public void addAccount() {
        showDialog();
    }
}