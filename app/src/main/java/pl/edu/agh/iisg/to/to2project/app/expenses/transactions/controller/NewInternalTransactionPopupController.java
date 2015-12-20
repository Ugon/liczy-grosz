package pl.edu.agh.iisg.to.to2project.app.expenses.transactions.controller;

import org.joda.time.LocalDate;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import pl.edu.agh.iisg.to.to2project.app.expenses.transactions.controller.generic.AbstractInternalTransactionPopupController;
import pl.edu.agh.iisg.to.to2project.domain.entity.Account;
import pl.edu.agh.iisg.to.to2project.domain.entity.InternalTransaction;

import java.math.BigDecimal;

/**
 * @author Bartłomiej Grochal
 * @author Wojciech Pachuta
 */
@Controller
@Scope("prototype")
public class NewInternalTransactionPopupController extends AbstractInternalTransactionPopupController {

    @Override
    protected InternalTransaction produceInternalTransaction(Account sourceAccount, Account destinationAccount, BigDecimal transfer, LocalDate date) {
        return new InternalTransaction(sourceAccount, destinationAccount, transfer, date);
    }

    public void addInternalTransaction() {
        showDialog();
    }

}
