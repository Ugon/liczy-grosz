package pl.edu.agh.iisg.to.to2project.app.expenses.common.view;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import pl.edu.agh.iisg.to.to2project.app.core.utils.SpringFXMLLoader;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.logging.Logger;

import static java.util.logging.Level.ALL;

/**
 * @author Bartłomiej Grochal
 */
public abstract class Popup {

    @Autowired
    private SpringFXMLLoader loader;

    @PostConstruct
    private void initializePopup() {
        Parent parent = null;
        try {
            parent = (Parent) loader.load(getFullyQualifiedResource());
        } catch (IOException e) {
            Logger.getLogger("GUI").log(ALL, "Cannot instantiate Popup Window Layout.");
            e.printStackTrace();
        }
        Stage stage = new Stage();
        stage.setTitle("Home Budget Manager.");
        stage.setScene(new Scene(parent));
        stage.show();
    }

    public abstract String getPopupTitle();

    public abstract String getFullyQualifiedResource();

}
