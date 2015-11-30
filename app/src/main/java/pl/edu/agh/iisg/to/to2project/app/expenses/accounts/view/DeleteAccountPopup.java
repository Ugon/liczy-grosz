package pl.edu.agh.iisg.to.to2project.app.expenses.accounts.view;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import pl.edu.agh.iisg.to.to2project.app.expenses.accounts.controller.DeleteAccountPopupController;
import pl.edu.agh.iisg.to.to2project.app.expenses.common.view.Popup;

/**
 * @author Bartłomiej Grochal
 * @author Wojciech Pachuta
 */
@Component
@Scope("prototype")
public class DeleteAccountPopup extends Popup<DeleteAccountPopupController> {

    @Override
    public String getPopupTitle() {
        return "Delete Selected Account.";
    }

    @Override
    public String getFullyQualifiedResource() {
        return "/pl/edu/agh/iisg/to/to2project/app/expenses/accounts/view/DeleteAccountPopupView.fxml";
    }

}
