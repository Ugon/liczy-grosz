package pl.edu.agh.iisg.to.to2project.app.expenses;

import javafx.fxml.FXML;
import javafx.scene.control.TabPane;
import org.springframework.stereotype.Controller;
import pl.edu.agh.iisg.to.to2project.app.expenses.accounts.controller.AccountsController;
import pl.edu.agh.iisg.to.to2project.app.expenses.categories.controller.CategoriesController;
import pl.edu.agh.iisg.to.to2project.app.expenses.transactions.controller.TransactionsController;

/**
 * @author Wojciech Pachuta.
 */
@Controller
public class ExpensesController {

    @FXML
    private TabPane tabPane;

    @FXML
    private AccountsController accountsController;

    @FXML
    private TransactionsController transactionsController;

    @FXML
    private CategoriesController categoriesController;

    @FXML
    public void initialize() {
        tabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.getId().equals("accountsTab")) {
                accountsController.refreshContent();
            } else if (newValue.getId().equals("transactionsTab")) {
                transactionsController.refreshContent();
            } else if (newValue.getId().equals("categoriesTab")) {
                categoriesController.refreshContent();
            }
        });

    }
}
