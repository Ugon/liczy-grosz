package pl.edu.agh.iisg.to.to2project.app.expenses.accounts.view;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import pl.edu.agh.iisg.to.to2project.app.expenses.accounts.controller.EditAccountPopupController;
import pl.edu.agh.iisg.to.to2project.app.expenses.common.view.Popup;

/**
 * @author Bartłomiej Grochal
 * @author Wojciech Pachuta
 */
@Component
@Scope("prototype")
public class EditAccountPopup extends Popup<EditAccountPopupController> {

    @Override
    public String getPopupTitle() {
        return "Edit Selected Account.";
    }

    @Override
    public String getFullyQualifiedResource() {
        return "/pl/edu/agh/iisg/to/to2project/app/expenses/accounts/view/EditAccountPopupView.fxml";
    }

}
