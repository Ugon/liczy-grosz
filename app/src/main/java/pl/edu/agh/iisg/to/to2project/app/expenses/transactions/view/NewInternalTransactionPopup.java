package pl.edu.agh.iisg.to.to2project.app.expenses.transactions.view;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import pl.edu.agh.iisg.to.to2project.app.expenses.common.view.Popup;
import pl.edu.agh.iisg.to.to2project.app.expenses.transactions.controller.NewInternalTransactionPopupController;

/**
 * @author Bartłomiej Grochal
 */
@Component
@Scope("prototype")
public class NewInternalTransactionPopup extends Popup<NewInternalTransactionPopupController> {

    @Override
    public String getPopupTitle() {
        return "Transaction Between My Accounts";
    }

    @Override
    public String getFullyQualifiedResource() {
        return "/pl/edu/agh/iisg/to/to2project/app/expenses/transactions/view/NewInternalTransactionPopupView.fxml";
    }

}
