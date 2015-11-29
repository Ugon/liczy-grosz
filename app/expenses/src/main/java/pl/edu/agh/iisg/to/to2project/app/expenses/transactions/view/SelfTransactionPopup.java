package pl.edu.agh.iisg.to.to2project.app.expenses.transactions.view;

import javafx.fxml.FXMLLoader;
import pl.edu.agh.iisg.to.to2project.app.expenses.common.view.Popup;

/**
 * @author Bartłomiej Grochal
 */
public class SelfTransactionPopup extends Popup {

    @Override
    public FXMLLoader getFXMLLoader() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(SelfTransactionPopup.class.getResource("SelfTransactionPopupView.fxml"));

        return loader;
    }

    @Override
    public String getPopupTitle() {
        return "Transaction Between Your Accounts";
    }
}
