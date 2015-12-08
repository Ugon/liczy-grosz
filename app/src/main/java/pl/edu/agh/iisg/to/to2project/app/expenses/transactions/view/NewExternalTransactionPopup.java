package pl.edu.agh.iisg.to.to2project.app.expenses.transactions.view;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import pl.edu.agh.iisg.to.to2project.app.expenses.common.view.Popup;
import pl.edu.agh.iisg.to.to2project.app.expenses.transactions.controller.NewExternalTransactionPopupController;

/**
 * @author Bartłomiej Grochal
 */
@Component
@Scope("prototype")
public class NewExternalTransactionPopup extends Popup<NewExternalTransactionPopupController> {

    @Override
    public String getPopupTitle() {
        return "Transaction With External Payee";
    }

    @Override
    public String getFullyQualifiedResource() {
        return "/pl/edu/agh/iisg/to/to2project/app/expenses/transactions/view/NewExternalTransactionPopupView.fxml";
    }

}
