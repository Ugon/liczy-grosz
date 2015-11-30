package pl.edu.agh.iisg.to.to2project.app.expenses.common.view;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import pl.edu.agh.iisg.to.to2project.app.core.utils.SpringFXMLLoader;
import pl.edu.agh.iisg.to.to2project.app.expenses.common.controller.PopupController;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.logging.Logger;

import static java.util.logging.Level.ALL;

/**
 * @author Bartłomiej Grochal
 * @author Wojciech Pachuta
 */
public abstract class Popup<T extends PopupController> {

    @Autowired
    private SpringFXMLLoader loader;

    private T controller;

    @PostConstruct
    private void initializePopup() {
        try {
            Parent parent = (Parent) loader.load(getFullyQualifiedResource());
            Stage stage = new Stage();
            stage.setTitle(getPopupTitle());
            stage.setScene(new Scene(parent));
            controller = (T) loader.getController();
            controller.setDialogStage(stage);
        } catch (IOException e) {
            Logger.getLogger("GUI").log(ALL, "Cannot instantiate Popup Window Layout.");
            e.printStackTrace();
        }
    }

    public abstract String getPopupTitle();

    public abstract String getFullyQualifiedResource();

    public T getController(){
        return controller;
    }
}
