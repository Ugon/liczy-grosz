package pl.edu.agh.iisg.to.to2project.app.expenses.transactions.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import pl.edu.agh.iisg.to.to2project.app.expenses.common.controller.PopupController;

/**
 * @author Bartłomiej Grochal
 */
@Component
@Scope("prototype")
public class EditInternalTransactionPopupController extends PopupController {

    @FXML
    @Override
    protected void handleOKButtonClick(ActionEvent actionEvent) {

    }
}
