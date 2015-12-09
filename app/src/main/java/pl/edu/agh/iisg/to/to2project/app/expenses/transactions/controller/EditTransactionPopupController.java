package pl.edu.agh.iisg.to.to2project.app.expenses.transactions.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import pl.edu.agh.iisg.to.to2project.app.expenses.common.controller.PopupController;
import pl.edu.agh.iisg.to.to2project.domain.AbstractTransaction;
import pl.edu.agh.iisg.to.to2project.domain.ExternalTransaction;
import pl.edu.agh.iisg.to.to2project.domain.InternalTransaction;

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

    private AbstractTransaction editedTransaction;



    @FXML
    @Override
    protected void handleOKButtonClick(ActionEvent actionEvent) {
        if(editedTransaction instanceof InternalTransaction) {
            internalController.handleOKButtonClick(actionEvent, editedTransaction);
        }
        else if(editedTransaction instanceof ExternalTransaction) {
            externalController.handleOKButtonClick(actionEvent, editedTransaction);
        }

        dialogStage.close();
    }

    public void editTransaction(AbstractTransaction transaction) {
        editedTransaction = transaction;
        prepareDialog();
        showDialog();
    }

    private void prepareDialog() {
        if(editedTransaction instanceof InternalTransaction) {
            conditionalBox.getChildren().remove(editExternalTransactionPopupContent);
        }
        else if (editedTransaction instanceof ExternalTransaction) {
            conditionalBox.getChildren().remove(editInternalTransactionPopupContent);
        }
    }
}
