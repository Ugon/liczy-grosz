package pl.edu.agh.iisg.to.to2project.app.expenses.accounts.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import pl.edu.agh.iisg.to.to2project.app.expenses.common.controller.PopupController;
import pl.edu.agh.iisg.to.to2project.domain.Account;
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

    private boolean deleted = false;

    @FXML
    @Override
    protected void handleOKButtonClick(ActionEvent actionEvent) {
        accountService.remove(account);
        deleted = true;
        dialogStage.close();
    }

    public boolean deleteAccount(Account account){
        this.account = account;
        showDialogAndWait();
        return deleted;
    }
}
