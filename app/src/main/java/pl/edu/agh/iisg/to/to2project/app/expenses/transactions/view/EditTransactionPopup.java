package pl.edu.agh.iisg.to.to2project.app.expenses.transactions.view;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import pl.edu.agh.iisg.to.to2project.app.core.utils.SpringFXMLLoader;
import pl.edu.agh.iisg.to.to2project.app.expenses.common.view.Popup;
import pl.edu.agh.iisg.to.to2project.app.expenses.transactions.controller.EditTransactionPopupController;

/**
 * @author Bartłomiej Grochal
 */
@Component
@Scope("prototype")
public class EditTransactionPopup extends Popup<EditTransactionPopupController> {

    @Override
    public String getPopupTitle() {
        return "Edit Selected Transaction.";
    }

    @Override
    public String getFullyQualifiedResource() {
        return "/pl/edu/agh/iisg/to/to2project/app/expenses/transactions/view/EditTransactionPopupView.fxml";
    }

}
