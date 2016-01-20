package pl.edu.agh.iisg.to.to2project.app.stats;

import javafx.fxml.FXML;
import javafx.scene.control.TabPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import pl.edu.agh.iisg.to.to2project.app.stats.category_share_graph_panel.controller.CategoryShareWindowController;
import pl.edu.agh.iisg.to.to2project.app.stats.inout_graph_panel.controller.InOutWindowController;
import pl.edu.agh.iisg.to.to2project.app.stats.main_panel.controller.MainWindowController;
import pl.edu.agh.iisg.to.to2project.app.stats.planned_budget_graph_panel.controller.PlannedBudgetWindowController;


@Controller
public class MainController {

    @FXML
    private TabPane tabPane;
    @Autowired
    @FXML
    private MainWindowController mainWindowController;
    @Autowired
    @FXML
    private InOutWindowController inOutWindowController;
    @Autowired
    @FXML
    private CategoryShareWindowController categoryShareWindowController;
    @Autowired
    @FXML
    private PlannedBudgetWindowController plannedBudgetWindowController;

    @FXML
    public void initialize() {
        tabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.getId().equals("mainWindowTab")) {
//                mainWindowController.refreshContent();
                new MainWindowController();
            } else if (newValue.getId().equals("inOutTab")) {
//                inOutWindowController.refreshContent();
                new InOutWindowController();
            } else if (newValue.getId().equals("categoryShareTab")) {
//                categoryShareWindowController.refreshContent();
                new CategoryShareWindowController();
            } else if (newValue.getId().equals("plannedBudgetTab")) {
//                plannedBudgetWindowController.refreshContent();
               new PlannedBudgetWindowController();
            }
        });
    }
    public void refreshContent(){
        mainWindowController.refreshContent();
        inOutWindowController.refreshContent();
        categoryShareWindowController.refreshContent();
        plannedBudgetWindowController.refreshContent();

    }
}