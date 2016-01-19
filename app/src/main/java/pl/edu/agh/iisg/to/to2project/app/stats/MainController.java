package pl.edu.agh.iisg.to.to2project.app.stats;

import javafx.fxml.FXML;
import javafx.scene.control.TabPane;
import org.springframework.stereotype.Controller;
import pl.edu.agh.iisg.to.to2project.app.stats.category_share_graph_panel.controller.CategoryShareWindowController;
import pl.edu.agh.iisg.to.to2project.app.stats.inout_graph_panel.controller.InOutWindowController;
import pl.edu.agh.iisg.to.to2project.app.stats.main_panel.controller.MainWindowController;
import pl.edu.agh.iisg.to.to2project.app.stats.planned_budget_graph_panel.controller.PlannedBudgetWindowController;


@Controller
public class MainController {

    @FXML
    private TabPane tabPane;

    @FXML
    private MainWindowController mainWindowController;

    @FXML
    private InOutWindowController inOutWindowController;

    @FXML
    private CategoryShareWindowController categoryShareWindowController;

    @FXML
    private PlannedBudgetWindowController plannedBudgetWindowController;

    @FXML
    public void initialize() {
        tabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.getId().equals("mainWindowTab")) {
//                mainWindowController.init();
                new MainWindowController();
            } else if (newValue.getId().equals("inOutTab")) {
//                inOutWindowController.init();
                new InOutWindowController();
            } else if (newValue.getId().equals("categoryShareTab")) {
//                categoryShareWindowController.init();
                new CategoryShareWindowController();
            } else if (newValue.getId().equals("plannedBudgetTab")) {
//                plannedBudgetWindowController.init();
                new PlannedBudgetWindowController();
            }
        });

    }
}