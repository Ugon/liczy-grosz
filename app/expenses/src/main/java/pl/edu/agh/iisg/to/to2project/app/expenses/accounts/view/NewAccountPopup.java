package pl.edu.agh.iisg.to.to2project.app.expenses.accounts.view;

import javafx.fxml.FXMLLoader;
import pl.edu.agh.iisg.to.to2project.app.expenses.common.view.Popup;

/**
 * @author Bartłomiej Grochal
 */
public class NewAccountPopup extends Popup {

    @Override
    public FXMLLoader getFXMLLoader() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(NewAccountPopup.class.getResource("NewAccountPopupView.fxml"));

        return loader;
    }

    @Override
    public String getPopupTitle() {
        return "Add New Account.";
    }
}
