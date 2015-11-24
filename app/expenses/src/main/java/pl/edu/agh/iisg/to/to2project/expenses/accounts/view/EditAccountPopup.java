package pl.edu.agh.iisg.to.to2project.expenses.accounts.view;

import javafx.fxml.FXMLLoader;
import pl.edu.agh.iisg.to.to2project.expenses.common.view.Popup;

/**
 * @author Bartłomiej Grochal
 */
public class EditAccountPopup extends Popup {

    @Override
    public FXMLLoader getFXMLLoader() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(EditAccountPopup.class.getResource("EditAccountPopupView.fxml"));

        return loader;
    }

    @Override
    public String getPopupTitle() {
        return "Edit Selected Account.";
    }
}
