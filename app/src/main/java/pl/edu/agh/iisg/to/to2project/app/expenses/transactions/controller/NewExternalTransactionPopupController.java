package pl.edu.agh.iisg.to.to2project.app.expenses.transactions.controller;

import org.joda.time.LocalDate;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import pl.edu.agh.iisg.to.to2project.app.expenses.transactions.controller.generic.AbstractExternalTransactionPopupController;
import pl.edu.agh.iisg.to.to2project.domain.entity.Account;
import pl.edu.agh.iisg.to.to2project.domain.entity.ExternalTransaction;

import java.math.BigDecimal;

/**
 * @author Bartłomiej Grochal
 * @author Wojciech Pachuta
 */
@Controller
@Scope("prototype")
public class NewExternalTransactionPopupController extends AbstractExternalTransactionPopupController {

    @Override
    protected ExternalTransaction produceInternalTransaction(String sourceAccount, Account destinationAccount, BigDecimal transfer, LocalDate date) {
        return new ExternalTransaction(sourceAccount, destinationAccount, transfer, date);
    }

    public void addExternalTransaction() {
        showDialog();
    }
}
