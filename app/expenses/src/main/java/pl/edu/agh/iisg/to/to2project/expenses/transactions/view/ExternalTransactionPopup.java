package pl.edu.agh.iisg.to.to2project.expenses.transactions.view;

import javafx.fxml.FXMLLoader;
import pl.edu.agh.iisg.to.to2project.expenses.common.view.Popup;

/**
 * @author Bartłomiej Grochal
 */
public class ExternalTransactionPopup extends Popup {

    @Override
    public FXMLLoader getFXMLLoader() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(ExternalTransactionPopup.class.getResource("ExternalTransactionPopupView.fxml"));

        return loader;
    }

    @Override
    public String getPopupTitle() {
        return "Transaction With External Payee";
    }
}
