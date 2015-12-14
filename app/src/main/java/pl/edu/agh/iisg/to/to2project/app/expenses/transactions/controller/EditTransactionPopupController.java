package pl.edu.agh.iisg.to.to2project.app.expenses.transactions.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import pl.edu.agh.iisg.to.to2project.app.expenses.common.controller.PopupController;
import pl.edu.agh.iisg.to.to2project.domain.entity.ExternalTransaction;
import pl.edu.agh.iisg.to.to2project.domain.entity.InternalTransaction;

/**
 * @author Bartłomiej Grochal
 */
@Component
@Scope("prototype")
public class EditTransactionPopupController extends PopupController {

    @FXML
    private VBox conditionalBox;

    @FXML
    private Pane editInternalTransactionPopupContent;

    @FXML
    private Pane editExternalTransactionPopupContent;

    @FXML
    private EditInternalTransactionPopupController internalController;

    @FXML
    private EditExternalTransactionPopupController externalController;

    private InternalTransaction internalTransaction;
    private ExternalTransaction externalTransaction;



    @FXML
    @Override
    protected void handleOKButtonClick(ActionEvent actionEvent) {
        if(internalTransaction != null) {
            internalController.handleOKButtonClick(actionEvent, internalTransaction);
        }
        else if(externalTransaction != null) {
            externalController.handleOKButtonClick(actionEvent, externalTransaction);
        }

        dialogStage.close();
    }

    public void editTransaction(InternalTransaction transaction) {
        internalTransaction = transaction;
        prepareDialog(internalTransaction);
        showDialog();
    }

    public void editTransaction(ExternalTransaction transaction) {
        externalTransaction = transaction;
        prepareDialog(externalTransaction);
        showDialog();
    }

    private void prepareDialog(InternalTransaction transaction) {
        conditionalBox.getChildren().remove(editExternalTransactionPopupContent);
    }

    private void prepareDialog(ExternalTransaction transaction) {
        conditionalBox.getChildren().remove(editInternalTransactionPopupContent);
    }
}
