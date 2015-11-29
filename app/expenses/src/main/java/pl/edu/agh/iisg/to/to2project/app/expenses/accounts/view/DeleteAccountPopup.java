package pl.edu.agh.iisg.to.to2project.app.expenses.accounts.view;

import javafx.fxml.FXMLLoader;
import pl.edu.agh.iisg.to.to2project.app.expenses.common.view.Popup;

/**
 * @author Bartłomiej Grochal
 */
public class DeleteAccountPopup extends Popup {

    @Override
    public FXMLLoader getFXMLLoader() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(DeleteAccountPopup.class.getResource("DeleteAccountPopupView.fxml"));

        return loader;
    }

    @Override
    public String getPopupTitle() {
        return "Delete Selected Account.";
    }
}
