package pl.edu.agh.iisg.to.to2project.app.expenses.accounts.controller;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import pl.edu.agh.iisg.to.to2project.app.expenses.accounts.controller.generic.AbstractAccountPopupController;
import pl.edu.agh.iisg.to.to2project.domain.entity.Account;

import java.math.BigDecimal;

/**
 * @author Bartłomiej Grochal
 * @author Wojciech Pachuta
 */
@Controller
@Scope("prototype")
public class EditAccountPopupController extends AbstractAccountPopupController {

    private Account account;

    @Override
    protected Account produceAccount(String name, BigDecimal initialBalance) {
        account.setName(name);
        account.setInitialBalance(initialBalance);
        return account;
    }

    @Override
    protected boolean isNameValid() {
        return nameTextField.getText().equals(account.nameProperty().get()) || super.isNameValid();
    }

    private void fillDialog(){
        nameTextField.setText(account.nameProperty().getValue());
        initialBalanceTextField.setText(account.initialBalanceProperty().getValue().toString());
    }

    public void editAccount(Account account) {
        this.account = account;
        fillDialog();
        showDialog();
    }
}
