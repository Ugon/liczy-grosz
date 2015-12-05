package pl.edu.agh.iisg.to.to2project.app.expenses.transactions.view;

import pl.edu.agh.iisg.to.to2project.app.core.utils.SpringFXMLLoader;
import pl.edu.agh.iisg.to.to2project.app.expenses.common.view.Popup;

/**
 * @author Bartłomiej Grochal
 */
public class SelfTransactionPopup extends Popup {

    @Override
    public String getPopupTitle() {
        return "Transaction Between Your Accounts";
    }

    @Override
    public String getFullyQualifiedResource() {
        return "SelfTransactionPopupView.fxml";
    }

}
