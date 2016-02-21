package pl.edu.agh.iisg.to.to2project.app.core;

import javafx.fxml.FXML;
import javafx.scene.control.TabPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import pl.edu.agh.iisg.to.to2project.app.budget.controller.BudgetPlannerController;
import pl.edu.agh.iisg.to.to2project.app.stats.MainController;

/**
 * Created by mike on 15.01.16.
 */
@Controller
public class RootController {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private BudgetPlannerController budgetPlannerController;

    @Autowired
    private MainController mainController;

    @FXML
    private TabPane tabPane;

    @FXML
    public void initialize() {
        tabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.getId().equals("budgetTab")) {
                budgetPlannerController.refreshContent();
            } else if (newValue.getId().equals("statisticsTab")) {
//                TO DO
                mainController.refreshContent();
            }
        });
    }

    public boolean isBudgetTabActive() {
        return tabPane.getSelectionModel().getSelectedItem().getId().equals("budgetTab");
    }

}
