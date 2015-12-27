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
public class NewAccountPopupController extends AbstractAccountPopupController {

    @Override
    protected Account produceAccount(String name, BigDecimal initialBalance) {
        return new Account(name, initialBalance);
    }

    public void addAccount() {
        showDialog();
    }
}
