package pl.edu.agh.iisg.to.to2project.app.expenses.accounts.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import pl.edu.agh.iisg.to.to2project.app.expenses.common.controller.PopupController;
import pl.edu.agh.iisg.to.to2project.domain.entity.Account;
import pl.edu.agh.iisg.to.to2project.service.AccountService;

/**
 * @author Bartłomiej Grochal
 * @author Wojciech Pachuta
 */
@Controller
@Scope("prototype")
public class DeleteAccountPopupController extends PopupController {

    @Autowired
    private AccountService accountService;
    private Account account;

    @FXML
    public Text errorLabel;

    @FXML
    @Override
    protected void handleOKButtonClick(ActionEvent actionEvent) {
        if(accountService.canDelete(account)) {
            accountService.remove(account);
            closeDialog();
        }
        else{
            errorLabel.setText("Sorry, you can't delete this account.");
        }
    }

    public void deleteAccount(Account account){
        this.account = account;
        showDialog();
    }
}
