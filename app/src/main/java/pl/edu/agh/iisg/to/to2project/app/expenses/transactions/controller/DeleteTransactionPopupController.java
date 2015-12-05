package pl.edu.agh.iisg.to.to2project.app.expenses.transactions.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import pl.edu.agh.iisg.to.to2project.app.expenses.common.controller.PopupController;
import pl.edu.agh.iisg.to.to2project.domain.AbstractTransaction;

/**
 * @author Bartłomiej Grochal
 */
@Component
@Scope("prototype")
public class DeleteTransactionPopupController extends PopupController {

    @FXML
    @Override
    protected void handleOKButtonClick(ActionEvent actionEvent) {

    }

    public void deleteTransaction(AbstractTransaction transaction) {
        showDialog();
    }
}
