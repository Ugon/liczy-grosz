package pl.edu.agh.iisg.to.to2project.app.expenses.accounts.view;

import pl.edu.agh.iisg.to.to2project.app.core.utils.SpringFXMLLoader;
import pl.edu.agh.iisg.to.to2project.app.expenses.common.view.Popup;

/**
 * @author Bartłomiej Grochal
 */
public class DeleteAccountPopup extends Popup {

    @Override
    public String getPopupTitle() {
        return "Delete Selected Account.";
    }

    @Override
    public String getFullyQualifiedResource() {
        return "DeleteAccountPopupView.fxml";
    }

}
