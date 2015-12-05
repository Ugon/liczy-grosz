package pl.edu.agh.iisg.to.to2project.app.expenses.transactions.view;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import pl.edu.agh.iisg.to.to2project.app.core.utils.SpringFXMLLoader;
import pl.edu.agh.iisg.to.to2project.app.expenses.common.view.Popup;
import pl.edu.agh.iisg.to.to2project.app.expenses.transactions.controller.DeleteTransactionPopupController;

/**
 * @author Bartłomiej Grochal
 */
@Component
@Scope("prototype")
public class DeleteTransactionPopup extends Popup<DeleteTransactionPopupController> {

    @Override
    public String getPopupTitle() {
        return "Delete Selected Transaction.";
    }

    @Override
    public String getFullyQualifiedResource() {
        return "/pl/edu/agh/iisg/to/to2project/app/expenses/transactions/view/DeleteTransactionPopupView.fxml";
    }

}
