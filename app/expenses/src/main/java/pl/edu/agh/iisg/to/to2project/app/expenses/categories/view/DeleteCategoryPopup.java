package pl.edu.agh.iisg.to.to2project.app.expenses.categories.view;

import javafx.fxml.FXMLLoader;
import pl.edu.agh.iisg.to.to2project.app.expenses.common.view.Popup;

/**
 * @author Bartłomiej Grochal
 */
public class DeleteCategoryPopup extends Popup {

    @Override
    public FXMLLoader getFXMLLoader() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(DeleteCategoryPopup.class.getResource("DeleteCategoryPopupView.fxml"));

        return loader;
    }

    @Override
    public String getPopupTitle() {
        return "Delete Selected Category.";
    }
}
