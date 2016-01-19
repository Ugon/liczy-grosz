package pl.edu.agh.iisg.to.to2project.app.stats.planned_budget_graph_panel.view;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import pl.edu.agh.iisg.to.to2project.app.core.utils.SpringFXMLLoader;
import pl.edu.agh.iisg.to.to2project.app.stats.util.PropertiesUtil;
import pl.edu.agh.iisg.to.to2project.domain.entity.Account;
import pl.edu.agh.iisg.to.to2project.domain.entity.Category;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

/**
 * Created by Nesbite on 2015-11-26.
 */
public class PlannedBudgetWindow extends Application {
    protected Stage primaryStage;

    private List<Account> accounts;
    private List<Category> categories;
    private LocalDate dateFrom;
    private LocalDate dateTo;

    public PlannedBudgetWindow(List<Account> acc, List<Category> cat, LocalDate dateFrom, LocalDate dateTo) {
        this.accounts = acc;
        this.categories = cat;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }

    public void start(Stage primaryStage) throws Exception {

        this.primaryStage = primaryStage;
        this.primaryStage.setTitle(PropertiesUtil.MAIN_WINDOW_TITLE);

        initRootLayout();
    }

    private void initRootLayout() {
        try {

            AnnotationConfigApplicationContext context =
                    new AnnotationConfigApplicationContext(PlannedBudgetWindow.class);

            String resource = PropertiesUtil.GRAPHWINDOW_WINDOW_RESOURCE_PLANED;
            SpringFXMLLoader loader = context.getBean(SpringFXMLLoader.class);

            Parent root = (Parent) loader.load(resource);


            Scene scene = new Scene(root, PropertiesUtil.WINDOW_WIDTH, PropertiesUtil.WINDOW_HEIGHT);
            scene.getStylesheets().add(PropertiesUtil.CALENDAR_STYLE_PATH);
            primaryStage.setResizable(true);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
