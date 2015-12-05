package pl.edu.agh.iisg.to.to2project.app.expenses.accounts.view;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import pl.edu.agh.iisg.to.to2project.app.expenses.accounts.controller.NewAccountPopupController;
import pl.edu.agh.iisg.to.to2project.app.expenses.common.view.Popup;

/**
 * @author Bartłomiej Grochal
 * @author Wojciech Pachuta
 */
@Component
@Scope("prototype")
public class NewAccountPopup extends Popup<NewAccountPopupController> {

    @Override
    public String getPopupTitle() {
        return "Add New Account.";
    }

    @Override
    public String getFullyQualifiedResource() {
        return "/pl/edu/agh/iisg/to/to2project/app/expenses/accounts/view/NewAccountPopupView.fxml";
    }

}
