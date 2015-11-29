package pl.edu.agh.iisg.to.to2project.app.core;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Logger;

import static java.util.logging.Level.ALL;

/**
 * @author Bartłomiej Grochal
 */
public class ApplicationMainGUITest extends Application {

    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;

        initStage();
        initRootContent();
    }

    private void initStage() {
        primaryStage.setTitle("Home Budget Manager.");
    }

    private void initRootContent() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(ApplicationMain.class.getResource("view/RootContent.fxml"));

        try {
            primaryStage.setScene(new Scene((BorderPane) loader.load()));
        } catch (IOException exc) {
            Logger.getLogger("GUI").log(ALL, "Cannot instantiate Application Root Content Layout.");
            exc.printStackTrace();
        }

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
