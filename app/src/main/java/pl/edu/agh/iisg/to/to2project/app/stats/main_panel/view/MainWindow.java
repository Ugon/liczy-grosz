package pl.edu.agh.iisg.to.to2project.app.stats.main_panel.view;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import pl.edu.agh.iisg.to.to2project.app.core.utils.SpringFXMLLoader;
import pl.edu.agh.iisg.to.to2project.app.stats.util.PropertiesUtil;

import java.io.IOException;

/**
 * Created by Wojciech Dymek on 20.11.2015.
 */
public class MainWindow extends Application{
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle(PropertiesUtil.MAIN_WINDOW_TITLE);

        initRootLayout();
    }

    private void initRootLayout() {
        try {
            AnnotationConfigApplicationContext context =
                    new AnnotationConfigApplicationContext(MainWindow.class);

            String resource = PropertiesUtil.MAIN_WINDOW_RESOURCE;
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
