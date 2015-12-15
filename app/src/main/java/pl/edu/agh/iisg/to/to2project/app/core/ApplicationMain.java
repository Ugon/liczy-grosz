package pl.edu.agh.iisg.to.to2project.app.core;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import pl.edu.agh.iisg.to.to2project.app.core.utils.SpringFXMLLoader;

/**
 * @author Bartłomiej Grochal
 * @author Wojciech Pachuta
 */
@SpringBootApplication
@ImportResource({"classpath:pl/agh/edu/iisg/to/to2project/persistence/persistenceContext.xml"})
@ComponentScan({"pl.edu.agh.iisg.to.to2project"})
public class ApplicationMain extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(ApplicationMain.class);

        SpringFXMLLoader loader = context.getBean(SpringFXMLLoader.class);

        Parent root = (Parent) loader.load("/pl/edu/agh/iisg/to/to2project/app/core/view/RootContent.fxml");
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
