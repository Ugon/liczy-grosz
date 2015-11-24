package pl.edu.agh.iisg.to.to2project.expenses.common.view;

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
public abstract class Popup {

    public Popup() {
        initializePopup();
    }

    public abstract FXMLLoader getFXMLLoader();

    public abstract String getPopupTitle();

    private void initializePopup()
    {
        Stage stage = new Stage();
        stage.setTitle(getPopupTitle());

        try {
            stage.setScene(new Scene((BorderPane) getFXMLLoader().load()));
        } catch (IOException exc) {
            Logger.getLogger("GUI").log(ALL, "Cannot instantiate Popup Window Layout.");
            exc.printStackTrace();
        }

        stage.show();
    }
}
