package pl.edu.agh.iisg.to.to2project.app.expenses.transactions.view;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import pl.edu.agh.iisg.to.to2project.app.expenses.common.view.Popup;
import pl.edu.agh.iisg.to.to2project.app.expenses.transactions.controller.EditExternalTransactionPopupController;

/**
 * @author Wojciech Pachuta.
 */
@Component
@Scope("prototype")
public class EditExternalTransactionPopup extends Popup<EditExternalTransactionPopupController>{

    @Override
    public String getPopupTitle() {
        return "Edit External Transaction.";
    }

    @Override
    public String getFullyQualifiedResource() {
        return "/pl/edu/agh/iisg/to/to2project/app/expenses/transactions/view/EditExternalTransactionPopupView.fxml";
    }
}
